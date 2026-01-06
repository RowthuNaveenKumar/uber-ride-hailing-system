import React, { useEffect, useState } from "react";
import {
  completeRide,
  getAssignedRide,
  goOnline,
  startRide,
} from "../api/driverApi";

import {
  connectLocationSocket,
  sendLocationUpdate,
  disconnectLocationSocket,
} from "../services/locationSocket";

const DriverDashboard = () => {
  const [lat, setLat] = useState("");
  const [lng, setLng] = useState("");
  const [ride, setRide] = useState(null);
  const [msg, setMsg] = useState("");

  const handleGoOnline = async () => {
    await goOnline();
    setMsg("Driver is online");
  };

  // const handleUpdateLocation = async () => {
  //   await updateLocation(Number(lat), Number(lng));
  //   setMsg("Location updated");
  // };

  const handleViewRide = async () => {
    const data = await getAssignedRide();
    setRide(data);
  };

  const handleStartRide = async () => {
    await startRide(ride.id);

    // 🔥 re-fetch updated ride from backend
    setRide({
      ...ride,
      status: "STARTED",
    });

    setMsg("Ride started");
  };

  const handleCompleteRide = async () => {
    await completeRide(ride.id);
    setRide({
      ...ride,
      status: "COMPLETED",
    });
    setMsg("Ride completed");
  };

  const handleUpdateLocation = () => {
    sendLocationUpdate(Number(lat), Number(lng));
    setMsg("📍 Location sent via WebSocket");
  };

  useEffect(() => {
    connectLocationSocket(() => {
      console.log("✅ Driver WebSocket connected");
    });

    return () => {
      disconnectLocationSocket();
    };
  }, []);

  useEffect(() => {
    if (!lat || !lng) return;

    const interval = setInterval(() => {
      sendLocationUpdate(Number(lat), Number(lng));
    }, 3000);

    return () => clearInterval(interval);
  }, [lat, lng]);

  return (
    <div>
      <h2>Driver Dashboard</h2>

      <button onClick={handleGoOnline}>Go Online</button>

      <h4>Update Location</h4>
      <input
        placeholder="Latitude"
        value={lat}
        onChange={(e) => setLat(e.target.value)}
      />
      <input
        placeholder="Longitude"
        value={lng}
        onChange={(e) => setLng(e.target.value)}
      />
      <button onClick={handleUpdateLocation}>Update Location</button>
      <hr />
      <button onClick={handleViewRide}>View Assigned Ride</button>

      {ride && (
        <div>
          <p>Ride ID: {ride.id}</p>
          <p>
            Pickup: {ride.pickupLat}, {ride.pickupLng}
          </p>
          <p>
            Drop: {ride.dropLat}, {ride.dropLng}
          </p>
          <p>Status: {ride.status}</p>

          {ride.status === "ASSIGNED" && (
            <button onClick={handleStartRide}>Start Ride</button>
          )}

          {ride.status === "STARTED" && (
            <button onClick={handleCompleteRide}>Complete Ride</button>
          )}

          {ride.status === "COMPLETED" && <p>Ride Completed ✅</p>}
        </div>
      )}

      {msg && <p>{msg}</p>}
    </div>
  );
};

export default DriverDashboard;
