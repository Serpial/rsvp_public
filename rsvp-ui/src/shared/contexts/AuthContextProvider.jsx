import { useEffect, useState, useCallback } from "react";
import AuthContext from "./AuthContext";
import PropTypes from "prop-types";
import { jwtDecode } from "jwt-decode";
import LoadingPage from "../components/LoadingPage";
import AuthService from "../AuthService";
import SessionService from "../SessionService";

function AuthContextProvider({ children }) {
  const [isLoading, setIsLoading] = useState(true);
  const [accessToken, setAccessToken] = useState(undefined);
  const [refreshToken, setRefreshToken] = useState(
    () => SessionService.getRefreshToken() || undefined
  );

  const hasTokenExpired = useCallback((token) => {
    try {
      if (!token) return true;

      const { exp } = jwtDecode(token);
      return !exp || exp * 1000 < Date.now();
    } catch (error) {
      console.error("Error decoding token:", error);
      return true;
    }
  }, []);

  const tryRestoreToken = useCallback(() => {
    const storedRefreshToken = SessionService.getRefreshToken();

    return {
      isPresent: Boolean(storedRefreshToken),
      refreshToken: storedRefreshToken,
    };
  }, []);

  const tryRefresh = useCallback(
    (token) =>
      AuthService.refresh(token)
        .then(({ access_token, refresh_token }) => {
          setAccessToken(access_token);
          setRefreshToken(refresh_token);
          return true;
        })
        .catch((error) => {
          console.error(error);
          return false;
        }),
    []
  );

  const tryLogin = useCallback(async (wordSet) => {
    try {
      if (!/^\w{6}-\w{6}-\w{6}$/.test(wordSet)) {
        throw new Error("Invalid login pattern.");
      }

      const { access_token, refresh_token } = await AuthService.login(wordSet);
      setAccessToken(access_token);
      setRefreshToken(refresh_token);
      return true;
    } catch (error) {
      console.error(error);
      return false;
    }
  }, []);

  useEffect(() => {
    const { isPresent, refreshToken: storedRefreshToken } = tryRestoreToken();
    if (!isPresent) {
      console.log("No tokens to restore.");
      setIsLoading(false);
      return;
    }

    const refreshExpired = hasTokenExpired(storedRefreshToken);
    if (refreshExpired) {
      console.log("Cannot refresh; logging out");
      setRefreshToken(undefined);
      setIsLoading(false);
      return;
    }

    tryRefresh(storedRefreshToken).then((success) => {
      if (success) {
        console.log("Token refreshed");
      } else {
        console.error("Token refresh failed");
      }

      setIsLoading(false);
    });
  }, [tryRestoreToken, hasTokenExpired, tryRefresh]);

  useEffect(() => SessionService.setRefreshToken(refreshToken), [refreshToken]);

  const contextValue = {
    tryLogin,
    tryLogout: useCallback(() => {
      setAccessToken(undefined);
      setRefreshToken(undefined);
      AuthService.logout(accessToken);
    }, [accessToken]),
    getAccessToken: useCallback(() => accessToken, [accessToken]),
    isLoggedIn: useCallback(
      () => "string" === typeof accessToken,
      [accessToken]
    ),
  };

  return (
    <AuthContext.Provider value={contextValue}>
      {isLoading ? <LoadingPage /> : children}
    </AuthContext.Provider>
  );
}

AuthContextProvider.propTypes = {
  children: PropTypes.node.isRequired,
};

export default AuthContextProvider;
