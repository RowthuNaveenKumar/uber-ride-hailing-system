import api from "./axios";

export const requestRide = async (rideRequest) => {
  // rideRequest MUST match backend RideRequest DTO
  const res = await api.post("/ride/request", rideRequest);
  return res.data;
};
export const getRideStatus = async (rideId) => {
  const res = await api.get(`/ride/view/${rideId}`);
  return res.data;
};

export const getDriverLocation =async(rideId)=>{
  const res=await api.get(`/driver/location/ride/${rideId}`);
  return res.data;
}
