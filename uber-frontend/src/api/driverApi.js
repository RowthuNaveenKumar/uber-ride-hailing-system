import api from "./axios";

export const goOnline = async () => {
  return api.post("/driver/go-online");
};

export const updateLocation = async (lat, lng) => {
  return api.post("/driver/location/update", {
    latitude: lat,
    longitude: lng,
  });
};

export const getAssignedRide = async () => {
  const res = await api.get("/ride/driver/assigned");
  return res.data;
};

export const startRide = async (rideId) => {
  return api.post(`/ride/driver/start/${rideId}`);
};

export const completeRide = async (rideId) => {
  return api.post(`/ride/driver/complete/${rideId}`);
};
