import PropTypes from "prop-types";
import MenuHelper from "../MenuHelper";

import "./GuestCardDisplay.css";

function GuestCardDisplay({ name, confirmed, menuType, updated }) {
  const parsedUpdated = new Date(Date.parse(updated));

  return (
    <section className="guest-card-display__container">
      <div className="guest-card-display">
        <div className="guest-card-display__image">
          <img
            src={
              confirmed
                ? "/response_form/check_large.svg"
                : "/response_form/cross_large.svg"
            }
            alt={confirmed ? "check" : "cross"}
          />
        </div>
        <div className="guest-card-display__content">
          <span className="guest-card-display__content-category">Name</span>
          <span className="guest-card-display__content-response">{name}</span>
          <span className="guest-card-display__content-category">
            Last updated
          </span>
          <span className="guest-card-display__content-response">{`${parsedUpdated.toLocaleDateString()} at ${parsedUpdated.toLocaleTimeString()}`}</span>
          <span className="guest-card-display__content-category">
            Attending
          </span>
          <span className="guest-card-display__content-response">
            {confirmed ? "Yes" : "No"}
          </span>
          {confirmed && (
            <>
              <span className="guest-card-display__content-category">
                Menu chosen
              </span>
              <span className="guest-card-display__content-response">
                {MenuHelper.getMenuType(menuType)}
              </span>
            </>
          )}
        </div>
      </div>
    </section>
  );
}

GuestCardDisplay.propTypes = {
  name: PropTypes.string,
  confirmed: PropTypes.bool,
  menuType: PropTypes.string,
  updated: PropTypes.string,
};

export default GuestCardDisplay;
