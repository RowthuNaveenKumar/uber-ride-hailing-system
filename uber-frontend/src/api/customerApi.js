import api from "./axios";

export const getRideStatus = async (rideId) => {
  const res = await api.get(`/ride/view/${rideId}`);
  return res.data;
};
