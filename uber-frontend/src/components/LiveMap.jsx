import { MapContainer, TileLayer, Marker } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

delete L.Icon.Default.prototype._getIconUrl;

const LiveMap = ({ pickup, drop, driver }) => {
  return (
    <MapContainer
      center={[pickup.lat, pickup.lng]}
      zoom={13}
      style={{ height: "400px", width: "100%" }}
    >
      <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
      {/* Pickup */}
      <Marker position={[pickup.lat, pickup.lng]} />
      {/* Drop */}
      <Marker position={[drop.lat, drop.lng]} />
      {/* Driver */}
      {driver && <Marker position={[driver.latitude, driver.longitude]} />}
    </MapContainer>
  );
};

export default LiveMap;
