import PropTypes from "prop-types";

import "./BaseButton.css";
import "./PrimaryButton.css";

function PrimaryButton({ value, disabled, onClick }) {
  function buttonClicked() {
    if (onClick) {
      onClick();
    }
  }

  return (
    <button
      className="button button__primary"
      type="submit"
      disabled={disabled}
      onClick={buttonClicked}
    >
      {value}
    </button>
  );
}

PrimaryButton.propTypes = {
  value: PropTypes.string,
  disabled: PropTypes.bool,
  onClick: PropTypes.func,
};

export default PrimaryButton;
