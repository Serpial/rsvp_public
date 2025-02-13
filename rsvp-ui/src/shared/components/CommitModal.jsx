import PrimaryButton from "./PrimaryButton";
import WarningButton from "./WarningButton";
import PropTypes from "prop-types";

import "./CommitModal.css";

function CommitModal({
  actionWord,
  onAction,
  setShowModal,
  children,
}) {
  function onInitiateAction() {
    if (!onAction) {
      return;
    }

    if (typeof onAction.then !== "function") {
      onAction();
      closeWindow();
      return;
    }

    onAction(false).resolve();
    closeWindow();
  }

  function closeWindow() {
    if (!setShowModal) {
      return;
    }

    setShowModal(false);
  }

  return (
    <>
      <section className="commit-modal__background" />
      <div className="commit-modal__container">
        <dialog className="commit-modal">
          <h1>{actionWord}</h1>
          <div className="commit-modal__content">{children}</div>
          <div className="commit-modal__button-array">
            <PrimaryButton value={actionWord} onClick={onInitiateAction} />
            <WarningButton value="Close" onClick={closeWindow} />
          </div>
        </dialog>
      </div>
    </>
  );
}

CommitModal.propTypes = {
  actionWord: PropTypes.string.isRequired,
  onAction: PropTypes.func,
  setShowModal: PropTypes.func,
  children: PropTypes.node.isRequired,
};

export default CommitModal;
