import SessionService from "./SessionService";

const RSVP_UI_API = import.meta.env.VITE_RSVP_API_URI;

function login(wordSet) {
  return fetch(`${RSVP_UI_API}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ wordSet }),
  }).then((response) => {
    if (!response.ok) {
      throw new Error(`Login request failed: ${response.status}`);
    } else {
      return response.json();
    }
  });
}

function refresh(token) {
  return fetch(`${RSVP_UI_API}/api/auth/refresh`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error(`Refresh failed: ${response.status}`);
    } else {
      return response.json();
    }
  });
}

function logout(token) {
  SessionService.clear();

  const options = {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };

  fetch(`${RSVP_UI_API}/api/auth/logout`, options).catch((error) =>
    console.error(error)
  );
}

export default { login, refresh, logout };
