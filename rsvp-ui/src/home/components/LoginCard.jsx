import { useEffect, useRef, useContext, useState, useCallback } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import AuthContext from "../../shared/contexts/AuthContext";
import WordInput from "./WordInput";
import PrimaryButton from "../../shared/components/PrimaryButton";
import SecondaryButton from "../../shared/components/SecondaryButton";
import ErrorBanner from "../../shared/components/ErrorBanner";
import GuestService from "../../shared/GuestService";
import CheckService from "../../shared/CheckService";
import LoadingWheel from "../../shared/components/LoadingWheel";

import "./LoginCard.css";

function LoginCard() {
  const navigate = useNavigate();
  const wordSetRef = useRef();
  const [searchParams] = useSearchParams();
  const { tryLogin, tryLogout, isLoggedIn, getAccessToken } =
    useContext(AuthContext);
  const [hasGuest, setHasGuest] = useState(false);
  const [authenticatedWordSet, setAuthenticatedWordSet] = useState("");
  const [showUnrecognisedLogin, setShowUnrecognisedLogin] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  async function loginGuest(event) {
    event.preventDefault();
    const words = wordSetRef.current;
    const success = await tryLogin(words);
    if (success) {
      navigate("/response");

      // Reset error
      setShowUnrecognisedLogin(false);
    } else {
      setShowUnrecognisedLogin(true);
    }
  }

  async function logoutGuest() {
    await tryLogout();

    setHasGuest(undefined);
    setAuthenticatedWordSet(undefined);
  }

  const tryFetchWordSet = useCallback(() => {
    const token = getAccessToken();
    if (!token) {
      return;
    }

    return GuestService.getWordSet(token)
      .then((data) => setAuthenticatedWordSet(data["word-set"]))
      .catch((error) => console.error(error));
  }, [getAccessToken]);

  useEffect(() => {
    CheckService.passesHealthCheck()
      .then((checkState) => {
        if (!checkState) {
          throw new Error("Could not pass health check");
        }

        const hasGuest = isLoggedIn();
        if (!hasGuest) {
          setIsLoading(false);
          return;
        }

        setHasGuest(hasGuest);
      })
      .then(tryFetchWordSet)
      .then(() => setIsLoading(false))
      .catch((error) => console.error(error));
  }, [isLoggedIn, tryFetchWordSet]);

  return isLoading ? (
    <div className="login-card__loading">
      <p>We will find out</p>
      <LoadingWheel />
    </div>
  ) : (
    <div className="login-card">
      {hasGuest ? (
        <article>
          <p>You are currently reviewing the page as:</p>
          <WordInput preset={authenticatedWordSet} disabled />
          <div className="login-card__actions">
            <PrimaryButton
              value="Open my options"
              onClick={() => navigate("/response")}
            />
            <SecondaryButton value="Log out" onClick={logoutGuest} />
          </div>
        </article>
      ) : (
        <form onSubmit={loginGuest}>
          <ErrorBanner
            isDisplayed={showUnrecognisedLogin}
            level="Warning"
            caption="The phrase you entered wasn't recognised. Please retry!"
          />
          <label>Yes, please! Please enter your three word phrase:</label>
          <WordInput ref={wordSetRef} preset={searchParams.get("phrase")} />
          <p className="login-card__info">This input is case insensitive</p>
          <PrimaryButton value="Respond" />
        </form>
      )}
    </div>
  );
}

export default LoginCard;
