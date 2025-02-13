import PropTypes from "prop-types";
import { useMapEvent } from "react-leaflet";

function TransferTo({ uri }) {
  useMapEvent({
    click: () => window.open(uri, "_blank").focus(),
  });
  return null;
}

TransferTo.propTypes = {
  uri: PropTypes.string,
};

export default TransferTo;
