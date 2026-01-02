import api from "./axios"


export const requestRide =async(rideRequest)=>{
    // rideRequest MUST match backend RideRequest DTO
    const res=await api.post("/ride/request",rideRequest);
    return res.data;
}