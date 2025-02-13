import { useCallback, useContext, useEffect, useMemo, useState } from "react";
import AuthContext from "../shared/contexts/AuthContext";
import { useNavigate } from "react-router-dom";
import SecondaryButton from "../shared/components/SecondaryButton";
import PrimaryButton from "../shared/components/PrimaryButton";
import GuestCardDisplay from "./components/GuestCardDisplay";
import GuestService from "../shared/GuestService";
import GuestCardForm from "./components/GuestCardForm";
import CommitModal from "../shared/components/CommitModal";
import MenuHelper from "./MenuHelper";
import CheckService from "../shared/CheckService";

import "./Response.css";

function Response() {
  const navigate = useNavigate();
  const { isLoggedIn, getAccessToken } = useContext(AuthContext);
  const [isLoading, setIsLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [existingResponsesData, setExistingResponsesData] = useState();
  const [responseData, setResponseData] = useState({});
  const [responseFormData, setResponseFormData] = useState();
  const [respondingNames, setRespondingNames] = useState(null);
  const isSubmitDisabled = useMemo(
    () => Object.keys(responseData).length <= 0,
    [responseData]
  );

  async function onSubmit(event) {
    event.preventDefault();
    setShowModal(true);
  }

  function createListItems({ name, index }) {
    const relatedData = responseData[index];
    if (!relatedData) {
      return <></>;
    }

    return (
      <li key={index}>
        {name}, is confirmed {!relatedData.confirmed && "not"} to attend
        {!relatedData.confirmed
          ? "."
          : ", eating the " +
            MenuHelper.getMenuType(relatedData["menu-type"]) +
            " menu."}
      </li>
    );
  }

  const handleUpdatedCard = useCallback((modified, dataObject) => {
    const id = dataObject["individual-id"];
    setResponseData((prevData) => {
      const updatedData = prevData ? { ...prevData } : [];
      if (modified && Object.keys(dataObject).length > 0) {
        updatedData[id] = dataObject;
      } else {
        delete updatedData[id];
      }

      return updatedData;
    });
  }, []);

  const constructExistingResponses = useCallback(() => {
    const token = getAccessToken();
    if (!token) {
      return;
    }

    return GuestService.getResponses(token).then(setExistingResponsesData);
  }, [getAccessToken]);

  const constructResponseForms = useCallback(() => {
    const token = getAccessToken();
    if (!token) {
      return;
    }

    return GuestService.getNames(token).then((names) => {
      if (
        !existingResponsesData ||
        existingResponsesData.length === names.length
      ) {
        setResponseFormData([]);
        return;
      }

      const existingIds = existingResponsesData.map(
        (response) => response["individual-id"]
      );

      const formData = names
        .filter((_, index) => !existingIds.includes(index))
        .map((name) => ({
          index: names.indexOf(name),
          name,
        }));

      setResponseFormData(formData);
    });
  }, [existingResponsesData, getAccessToken]);

  const respondAction = useCallback(async () => {
    const token = getAccessToken();
    if (!token) {
      return;
    }

    try {
      setIsLoading(true);
      const responseValues = Object.values(responseData);
      await GuestService.pushResponses(token, responseValues);

      await constructExistingResponses();

      setResponseData({});
      setTimeout(() => setIsLoading(false), 500);
    } catch (error) {
      console.error("There was an error pushing response: ", error);
    }
  }, [constructExistingResponses, getAccessToken, responseData]);

  useEffect(() => {
    constructResponseForms()?.then(() =>
      setTimeout(() => setIsLoading(false), 300)
    );
  }, [constructResponseForms]);

  useEffect(() => {
    if (!responseFormData) {
      return;
    }

    let names = [];
    Object.keys(responseData).map((id) => {
      const name = responseFormData
        .filter((data) => data.index === Number(id))
        .map((data) => data.name);
      names = name?.concat(names) ?? [];
    });

    if (names.length === 0) {
      setRespondingNames(null);
      return;
    }

    setRespondingNames(names);
  }, [responseFormData, responseData]);

  useEffect(() => {
    CheckService.passesHealthCheck()
      .then((passes) => {
        if (!passes) {
          throw new Error("Could not pass health check");
        }

        const loggedIn = isLoggedIn();
        if (!loggedIn) {
          throw new Error("Is not logged in.");
        }
      })
      .then(constructExistingResponses)
      .catch((error) => {
        console.error(error);
        navigate("/page-not-found");
      });
  }, [isLoggedIn, getAccessToken, constructExistingResponses, navigate]);

  return (
    !isLoading && (
      <>
        {showModal && (
          <CommitModal
            actionWord="Respond"
            onAction={respondAction}
            setShowModal={setShowModal}
          >
            You are sending your RSVP with this information:
            <ul style={{ paddingLeft: "2rem" }}>
              {Object.values(responseFormData)?.map(createListItems)}
            </ul>
          </CommitModal>
        )}
        <article className="responses">
          {existingResponsesData && existingResponsesData.length > 0 && (
            <section className="responses__section responses__existing-container">
              <h3>Your Responses</h3>
              <p>
                If you notice any issues or if anything changes between now and
                the wedding, please don&apos;t hesitate to reach out to us.
                Additionally, if you can only attend part of the day, let us
                know.
              </p>
              <div className="responses__existing">
                {existingResponsesData.map((guestResponse) => (
                  <GuestCardDisplay
                    key={guestResponse["individual-id"]}
                    id={guestResponse["individual-id"]}
                    name={guestResponse["name"]}
                    confirmed={guestResponse["confirmed"]}
                    menuType={guestResponse["menu-type"]}
                    updated={guestResponse["updated"]}
                  />
                ))}
              </div>
            </section>
          )}
          <section className="responses__section responses__forms-container">
            <form onSubmit={onSubmit}>
              {responseData && responseFormData.length > 0 && (
                <div className="responses__forms">
                  {responseFormData.map(({ index, name }) => (
                    <GuestCardForm
                      key={index}
                      id={index}
                      guestName={name}
                      onChange={handleUpdatedCard}
                    />
                  ))}
                </div>
              )}

              <div className="responses__action-container">
                <SecondaryButton
                  value="Return to main"
                  onClick={() => navigate("/")}
                />
                {responseData && responseFormData.length > 0 && (
                  <PrimaryButton
                    value={`Respond for ${respondingNames?.join(", ") ?? "guests"}`}
                    disabled={isSubmitDisabled}
                  />
                )}
              </div>
            </form>
          </section>
        </article>
      </>
    )
  );
}

export default Response;
