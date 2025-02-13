import { forwardRef, useRef } from "react";
import PropTypes from "prop-types";

import "./WordInput.css";

const SET_SIZE = 6;

const InputBox = forwardRef(function (props, ref) {
  const { disabled, size, onChange, onKeyDown, onPaste } = props;

  let defaultValue = props.defaultValue;
  if (defaultValue?.length > size) {
    defaultValue = defaultValue.substr(0, size);
  }

  return (
    <input
      ref={ref}
      required
      disabled={disabled}
      minLength={size}
      maxLength={size}
      defaultValue={defaultValue}
      type="text"
      onChange={onChange}
      onPaste={onPaste}
      onKeyDown={onKeyDown}
    />
  );
});

InputBox.displayName = "InputBox";
InputBox.propTypes = {
  disabled: PropTypes.bool,
  defaultValue: PropTypes.string,
  size: PropTypes.number,
  onChange: PropTypes.func,
  onKeyDown: PropTypes.func,
  onPaste: PropTypes.func,
};

const WordInput = forwardRef(function ({ preset, disabled }, ref) {
  const classes = ["word-entry", disabled && "word-entry--disabled"]
    .filter(Boolean) // Removes falsy values like `false` or `undefined`
    .join(" ");

  const presets = preset?.toLowerCase().split("-");
  setRef(presets);

  const firstInput = useRef();
  const secondInput = useRef();
  const thirdInput = useRef();

  function handleInputChange(event, nextRef) {
    const newValue = event.target.value;
    if (newValue.length === SET_SIZE && nextRef) {
      nextRef.current.focus();
    }

    setRefToCurrent();
  }

  function handlePaste(event, currentRef, followUpRefs) {
    if (!followUpRefs) {
      return;
    }

    const currentContent = currentRef?.current?.value ?? "";
    const pastedText = event.clipboardData
      .getData("Text")
      .replaceAll("-", "")
      .trim();

    const availableSpace = SET_SIZE - currentContent.length;
    const totalFollowUpSpace = (followUpRefs?.length ?? 0) * SET_SIZE;

    currentRef.current.value =
      currentContent + pastedText.slice(0, availableSpace);

    const remainingText = pastedText.slice(
      availableSpace,
      availableSpace + totalFollowUpSpace
    );
    const chunks =
      remainingText.match(new RegExp(`.{1,${SET_SIZE}}`, "g")) || [];

    followUpRefs[followUpRefs.length - 1]?.current?.focus();
    followUpRefs.forEach((ref, index) => {
      if (ref?.current && chunks[index]) {
        ref.current.value = chunks[index];
      }
    });

    setRefToCurrent();
  }

  function handleKeyDown(event, previousRef) {
    if (
      event.key === "Backspace" &&
      event.target.value.length === 0 &&
      previousRef
    ) {
      previousRef.current.focus();
    }
  }

  function setRefToCurrent() {
    setRef([
      firstInput.current.value,
      secondInput.current.value,
      thirdInput.current.value,
    ]);
  }

  function setRef(values) {
    if (!values || !ref) {
      return;
    }

    const value1 = values[0] ?? "";
    const value2 = values[1] ?? "";
    const value3 = values[2] ?? "";

    ref.current = (value1 + "-" + value2 + "-" + value3).toLowerCase();
  }

  return (
    <div className={classes}>
      <InputBox
        ref={firstInput}
        size={SET_SIZE}
        defaultValue={presets && presets[0]}
        disabled={disabled}
        onChange={(e) => handleInputChange(e, secondInput)}
        onPaste={(e) => handlePaste(e, firstInput, [secondInput, thirdInput])}
        onKeyDown={(e) => handleKeyDown(e, firstInput, null)}
      />
      <span>-</span>
      <InputBox
        ref={secondInput}
        size={SET_SIZE}
        defaultValue={presets && presets[1]}
        disabled={disabled}
        onChange={(e) => handleInputChange(e, thirdInput)}
        onPaste={(e) => handlePaste(e, secondInput, [thirdInput])}
        onKeyDown={(e) => handleKeyDown(e, firstInput)}
      />
      <span>-</span>
      <InputBox
        ref={thirdInput}
        size={SET_SIZE}
        defaultValue={presets && presets[2]}
        disabled={disabled}
        onChange={(e) => handleInputChange(e, null)}
        onKeyDown={(e) => handleKeyDown(e, secondInput)}
      />
    </div>
  );
});

WordInput.displayName = "WordInput";
WordInput.propTypes = {
  preset: PropTypes.string,
  disabled: PropTypes.bool,
};

export default WordInput;
