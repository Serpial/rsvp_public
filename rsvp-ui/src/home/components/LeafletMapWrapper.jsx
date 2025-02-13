import PropTypes from "prop-types";
import { MapContainer, TileLayer, ZoomControl } from "react-leaflet";

import "leaflet/dist/leaflet.css";
import "./LeafletMapWrapper.css";

function LeafletMapWrapper({ longitude, latitude, children }) {
  const position = [longitude, latitude];

  return (
    <MapContainer
      center={position}
      zoom={12}
      zoomControl={false}
      scrollWheelZoom={false}
      doubleClickZoom={false}
      dragging={false}
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <ZoomControl position="bottomleft" />
      {children}
    </MapContainer>
  );
}

LeafletMapWrapper.propTypes = {
  latitude: PropTypes.number,
  longitude: PropTypes.number,
  children: PropTypes.arrayOf(PropTypes.element),
};

export default LeafletMapWrapper;
