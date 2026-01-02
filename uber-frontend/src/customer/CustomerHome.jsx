import { useState } from "react";
import { requestRide } from "../api/rideApi";

const CustomerHome = () => {
  const [pickupLat, setPickupLat] = useState("");
  const [pickupLng, setPickupLng] = useState("");
  const [dropLat, setDropLat] = useState("");
  const [dropLng, setDropLng] = useState("");

  const [ride, setRide] = useState(null);
  const [error, setError] = useState("");

  const handleRequestRide = async () => {
    setError("");
    try {
      const rideRequest = {
        pickupLat: Number(pickupLat),
        pickupLng: Number(pickupLng),
        dropLat: Number(dropLat),
        dropLng: Number(dropLng),
      };
      const data = await requestRide(rideRequest);
      setRide(data);
    } catch (error) {
      setError("No drivers available or request failed");
    }
  };

  return (
    <div>
      <h2>Customer Home</h2>

      <h4>Pickup Location</h4>
      <input
        placeholder="Pickup Latitude"
        value={pickupLat}
        onChange={(e) => setPickupLat(e.target.value)}
      />
      <input
        placeholder="Pickup Longitude"
        value={pickupLng}
        onChange={(e) => setPickupLng(e.target.value)}
      />
      <h4>Drop Location</h4>
      <input
        placeholder="Drop Latitude"
        value={dropLat}
        onChange={(e) => setDropLat(e.target.value)}
      />
      <input
        placeholder="Drop Longitude"
        value={dropLng}
        onChange={(e) => setDropLng(e.target.value)}
      />
      <br />
      <button onClick={handleRequestRide}>Request Ride</button>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {ride && (
        <div>
          <h3>Ride Assigned</h3>
          <p>Ride ID: {ride.rideId}</p>
          <p>Driver: {ride.driverName}</p>
          <p>Status: {ride.status}</p>
        </div>
      )}
    </div>
  );
};

export default CustomerHome;
