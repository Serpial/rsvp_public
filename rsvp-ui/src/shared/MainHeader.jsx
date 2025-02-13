import "./MainHeader.css";

function MainHeader() {
  return (
    <header className="main-header main-header__container">
      <h3 className="main-header main-header__text">
        Please join us for the wedding of
      </h3>
      <h1 className="main-header main-header__text">
        <span>Lois</span>
        <span>&nbsp;&&nbsp;</span>
        <span>Paul</span>
      </h1>
      <h2 className="main-header main-header__text">8th of February, 2025</h2>
    </header>
  );
}

export default MainHeader;
