const RSVP_UI_API = import.meta.env.VITE_RSVP_API_URI;

function getResponses(accessToken) {
  if (!accessToken) {
    return;
  }

  const options = {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
  };

  return fetch(RSVP_UI_API + "/api/guest/responses", options)
    .then((response) => {
      if (!response.ok) {
        throw new Error(
          `Guest response request failed: ${response.status}`
        );
      }
      return response.json();
    }).then(response => Array(...response))
    .catch((error) => {
      console.error(error);
      return [];
    });
}

function getWordSet(accessToken) {
  if (!accessToken) {
    return;
  }

  const options = {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
  };

  return fetch(RSVP_UI_API + "/api/guest/word-set", options)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`Failed retrieving word set: ${response.status}`);
      }

      return response.json();
    })
    .catch((error) => {
      console.error(error);
      return "";
    });
}

function getNames(accessToken) {
  if (!accessToken) {
    return;
  }

  const options = {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
  };

  return fetch(RSVP_UI_API + "/api/guest/names", options)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`Failed retrieving names: ${response.status}`);
      }

      return response.json();
    })
    .then((response) => response.names)
    .catch((error) => {
      console.error(error);
      return [];
    });
}

function pushResponses(accessToken, responses) {
  if (!accessToken) {
    return;
  }

  const options = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
    body: JSON.stringify(responses),
  };

  return fetch(RSVP_UI_API + "/api/guest/responses", options)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`Failed posting responses: ${response.status}`);
      }
    })
    .catch((error) => console.error(error));
}

const GuestService = {
  getResponses,
  getWordSet,
  getNames,
  pushResponses,
};

export default GuestService;
