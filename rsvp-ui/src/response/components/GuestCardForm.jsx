import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import PropTypes from "prop-types";
import Constants from "../../shared/res/Constants";

import "./GuestCardForm.css";

function GuestCardForm({ id, guestName, onChange }) {
  const [dataObject, setDataObject] = useState({});
  const [isConfirmed, setIsConfirmed] = useState(false);
  const [menu, setMenu] = useState();
  const confirmedYes = useRef();
  const confirmedNo = useRef();
  const menuType = useRef();
  const isDefaultState = useMemo(
    () => !("confirmed" in dataObject) && !("menu-type" in dataObject),
    [dataObject]
  );

  function updateDataObject() {
    const confirmed = confirmedYes.current?.checked ?? false;
    const newData = {
      "individual-id": id,
      confirmed: confirmed,
    };

    if (confirmed) {
      newData["menu-type"] = menuType.current?.value ?? "STANDARD";
    }

    setIsConfirmed(confirmed);
    setDataObject((prev) => {
      if (
        prev.confirmed !== newData.confirmed ||
        (Object.keys(prev).includes("menu-type") &&
          prev["menu-type"] !== newData["menu-type"])
      ) {
        return newData;
      }

      return prev;
    });
  }

  function resetState(event) {
    event?.preventDefault();
    setDataObject({
      "individual-id": id,
    });

    setIsConfirmed(false);
    confirmedYes.current.checked = false;
    confirmedNo.current.checked = false;
    menuType.current.value = "STANDARD";
  }

  const onStateChanged = useCallback(() => {
    if (!onChange) {
      return;
    }

    const modified = !isDefaultState;
    onChange(modified, dataObject);
  }, [dataObject, isDefaultState, onChange]);

  useEffect(() => {
    if (!dataObject || !dataObject.confirmed) {
      return;
    }

    switch (dataObject["menu-type"]) {
      case "STANDARD":
        setMenu({
          starter: Constants.standardStarter,
          main: Constants.standardMain,
          dessert: Constants.standardDessert,
        });
        break;
      case "VEGAN":
        setMenu({
          starter: Constants.veganStarter,
          main: Constants.veganMain,
          dessert: Constants.veganDessert,
        });
        break;
      case "CHILDREN":
        setMenu({
          starter: Constants.childrenStarter,
          main: Constants.childrenMain,
          dessert: Constants.childrenDessert,
        });
        break;
      default:
        setMenu();
        break;
    }
  }, [dataObject]);

  useEffect(() => {
    onStateChanged();
  }, [onStateChanged]);

  return (
    <fieldset className="guest-card-form__card">
      <div  className="guest-card-form__container">
      <h4>
        Responding for{" "}
        <span className="guest-card-form__name">{guestName}</span>
      </h4>
      <span>Would you like to join us?</span>
      <div className="guest-card-form__confirmation-buttons">
        <input
          className="guest-card-form_confirmed-yes-radio"
          id={`confirmedYes-${id}`}
          type="radio"
          name={`confirmed-${id}`}
          ref={confirmedYes}
          onChange={updateDataObject}
        />
        <label htmlFor={`confirmedYes-${id}`}>
          <img src="/response_form/check.svg" alt="confirm" />
        </label>
        <input
          className="guest-card-form_confirmed-no-radio"
          id={`confirmedNo-${id}`}
          type="radio"
          name={`confirmed-${id}`}
          ref={confirmedNo}
          onChange={updateDataObject}
        />
        <label htmlFor={`confirmedNo-${id}`}>
          <img src="/response_form/cross.svg" alt="reject" />
        </label>
      </div>
      <span>Which menu would you like to select?</span>
      <select
        className="guest-card-form__menu-selection"
        name="menu-type"
        ref={menuType}
        disabled={!isConfirmed}
        onChange={updateDataObject}
      >
        <option value="STANDARD">Standard</option>
        <option value="VEGAN">Vegan</option>
        <option value="CHILDREN">Children</option>
      </select>
      </div>
      <button
        className="guest-card-form__reset-button"
        onClick={resetState}
        disabled={isDefaultState}
        title="Reset"
      >
        <img src="/response_form/reset.svg" alt="reset" />
      </button>
      {isConfirmed && (
        <div className="guest-card-form__menu">
          <h4>This means that you would be eating:</h4>
          <h5>Starter</h5>
          <span>{menu?.starter}</span>
          <h5>Main course</h5>
          <span>{menu?.main}</span>
          <h5>Desert</h5>
          <span>{menu?.dessert}</span>
        </div>
      )}
    </fieldset>
  );
}

GuestCardForm.propTypes = {
  id: PropTypes.number,
  guestName: PropTypes.string,
  onChange: PropTypes.func,
};

export default GuestCardForm;
