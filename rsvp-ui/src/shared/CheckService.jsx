const RSVP_UI_API = import.meta.env.VITE_RSVP_API_URI;

function passesHealthCheck() {
  return fetch(RSVP_UI_API + "/actuator/health", { method: "GET" })
    .then((response) => Boolean(response?.ok))
    .catch(() => false);
}

export default { passesHealthCheck };
