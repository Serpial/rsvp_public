import PropTypes from "prop-types";

import "./ErrorBanner.css";

function ErrorBanner({ isDisplayed, level, caption }) {
  return (
    isDisplayed && (
      <article className="error-banner">
        <h2>{level}</h2>
        <p>{caption}</p>
      </article>
    )
  );
}

ErrorBanner.displayName = "ErrorBanner";
ErrorBanner.propTypes = {
  isDisplayed: PropTypes.bool,
  level: PropTypes.string,
  caption: PropTypes.string,
};

export default ErrorBanner;
