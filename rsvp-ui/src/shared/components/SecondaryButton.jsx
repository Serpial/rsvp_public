import PropTypes from "prop-types";

import "./BaseButton.css";
import "./SecondaryButton.css";

function SecondaryButton({ value, onClick }) {
  function buttonClicked() {
    if (onClick) {
      onClick();
    }
  }

  return (
    <button
      className="button button__secondary"
      type="submit"
      onClick={buttonClicked}
    >
      {value}
    </button>
  );
}

SecondaryButton.propTypes = {
  value: PropTypes.string,
  onClick: PropTypes.func,
};

export default SecondaryButton;
