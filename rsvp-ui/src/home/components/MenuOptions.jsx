import { useState } from "react";

import "./MenuOptions.css";
import Constants from "../../shared/res/Constants";

const MenuOptions = () => {
  const [activeTab, setActiveTab] = useState("nonVegan");

  const renderMenu = () => {
    switch (activeTab) {
      case "vegan":
        return (
          <div className="menu-options__options">
            <h4>Starter</h4>
            <p>{Constants.veganStarter}</p>

            <h4>Main</h4>
            <p>{Constants.veganMain}</p>

            <h4>Dessert</h4>
            <p>{Constants.veganDessert}</p>
          </div>
        );
      case "nonVegan":
        return (
          <div className="menu-options__options">
            <h4>Starter</h4>
            <p>{Constants.standardStarter}</p>

            <h4>Main</h4>
            <p>{Constants.standardMain}</p>

            <h4>Dessert</h4>
            <p>{Constants.standardDessert}</p>
          </div>
        );
      case "children":
        return (
          <div className="menu-options__options">
            <h4>Starter</h4>
            <p>{Constants.childrenStarter}</p>

            <h4>Main</h4>
            <p>{Constants.childrenMain}</p>

            <h4>Dessert</h4>
            <p>{Constants.childrenDessert}</p>
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <div className="menu-options">
      <div className="menu-options__tab-array-container">
        <div className="menu-options__tab-array">
          <input
            id="non-vegan"
            type="radio"
            name="tab"
            onClick={() => setActiveTab("nonVegan")}
          />
          <label
            className={
              activeTab === "nonVegan" ? "menu-options__current-tab" : ""
            }
            htmlFor="non-vegan"
          >
            <span id="set-tab" className="menu-option__tab-icon" />
            <span className="menu-option__tab-content">Standard</span>
          </label>
          <input
            id="vegan"
            type="radio"
            name="tab"
            onClick={() => setActiveTab("vegan")}
          />
          <label
            className={activeTab === "vegan" ? "menu-options__current-tab" : ""}
            htmlFor="vegan"
          >
            <span id="vegan-tab" className="menu-option__tab-icon" />
            <span className="menu-option__tab-content">Vegan</span>
          </label>
          <input
            id="children"
            name="tab"
            type="radio"
            onClick={() => setActiveTab("children")}
          />
          <label
            className={
              activeTab === "children" ? "menu-options__current-tab" : ""
            }
            htmlFor="children"
          >
            <span id="children-tab" className="menu-option__tab-icon" />
            <span className="menu-option__tab-content">Children</span>
          </label>
        </div>
      </div>

      {renderMenu()}
    </div>
  );
};

export default MenuOptions;
