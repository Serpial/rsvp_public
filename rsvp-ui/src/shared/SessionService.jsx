const REFRESH_TOKEN_ID = "RefreshToken";

function setRefreshToken(token) {
  if (!token) {
    removeRefreshToken();
    return;
  }

  sessionStorage.setItem(REFRESH_TOKEN_ID, token);
}

function removeRefreshToken() {
  sessionStorage.removeItem(REFRESH_TOKEN_ID);
}

function getRefreshToken() {
  return sessionStorage.getItem(REFRESH_TOKEN_ID);
}

function clear() {
  sessionStorage.clear();
}

export default { setRefreshToken, removeRefreshToken, getRefreshToken, clear };
