import PropTypes from "prop-types";

import "./BaseButton.css";
import "./WarningButton.css";

function WarningButton({ value, disabled, onClick }) {
  function buttonClicked() {
    if (onClick) {
      onClick();
    }
  }

  return (
    <button
      className="button button__warning"
      type="submit"
      disabled={disabled}
      onClick={buttonClicked}
    >
      {value}
    </button>
  );
}

WarningButton.propTypes = {
  value: PropTypes.string,
  disabled: PropTypes.bool,
  onClick: PropTypes.func,
};

export default WarningButton;
