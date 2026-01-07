import { useState, useEffect } from "react";
import { requestRide, getRideStatus, getDriverLocation } from "../api/rideApi";
import LiveMap from "../components/LiveMap";
import { connectCustomerSocket } from "../services/customerLocationSocket";

const CustomerHome = () => {
  const [pickupLat, setPickupLat] = useState("");
  const [pickupLng, setPickupLng] = useState("");
  const [dropLat, setDropLat] = useState("");
  const [dropLng, setDropLng] = useState("");

  const [driverLocation, setDriverLocation] = useState(null);

  const [ride, setRide] = useState(null);
  const [error, setError] = useState("");
  const [msg, setMsg] = useState("");

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
      setError(error.response?.data || "No drivers available or request failed");
    }
  };

  useEffect(() => {
    if (!ride?.rideId) return;
    const interval = setInterval(async () => {
      try {
        const updatedRide = await getRideStatus(ride.rideId);
        setRide(updatedRide);
        if (updatedRide.status === "COMPLETED") {
          clearInterval(interval);
        }
      } catch (err) {
        console.error("Error fetching ride status", err);
      }
    }, 4000);
    return () => clearInterval(interval);
  }, [ride?.rideId]);

  // Poll driver location when ride STARTED
  useEffect(() => {
    if (!ride || ride.status !== "STARTED") return;

    const interval = setInterval(async () => {
      try {
        const loc = await getDriverLocation(ride.rideId || ride.id);
        setDriverLocation(loc);
      } catch (err) {
        console.error("Failed to fetch driver location");
      }
    }, 5000);

    return () => clearInterval(interval);
  }, [ride?.status, ride?.rideId]);

  // websockets for live tracking of driver to subscribe purpose
  useEffect(() => {
    if (!ride?.driverId) return;

    connectCustomerSocket(ride.driverId, (location) => {
      setDriverLocation(location);
    });

    return () => disconnectCustomerSocket();
  }, [ride?.driverId]);

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
          <h3>Ride Details</h3>
          <p>Ride ID: {ride.rideId || ride.id}</p>

          {ride.driverName && <p>Driver: {ride.driverName}</p>}

          <p>Status: {ride.status}</p>

          {ride.status === "ASSIGNED" && <p>Driver assigned 🚗</p>}
          {ride.status === "STARTED" && <p>Ride started 🟢</p>}
          {ride.status === "COMPLETED" && <p>Ride completed ✅</p>}
        </div>
      )}
      {driverLocation && (
        <div>
          <h4>🚗 Driver Live Location</h4>
          <p>Latitude: {driverLocation.latitude}</p>
          <p>Longitude: {driverLocation.longitude}</p>
          <p>Last updated: {driverLocation.lastUpdated}</p>
        </div>
      )}
      {ride && ride.status == "STARTED" && driverLocation && (
        <LiveMap
          pickup={{ lat: ride.pickupLat, lng: ride.pickupLng }}
          drop={{ lat: ride.dropLat, lng: ride.dropLng }}
          driver={driverLocation}
        />
      )}
    </div>
  );
};

export default CustomerHome;
