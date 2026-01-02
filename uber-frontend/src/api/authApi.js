import api from "./axios";

// CUSTOMER LOGIN
export const loginCustomer = async (email, password) => {
  const res = await api.post("/auth/login", { email, password });
  return res.data;
};

// DRIVER LOGIN
export const loginDriver = async (email, password) => {
  const res = await api.post("/driver/login", { email, password });
  return res.data;
};

// CUSTOMER
export const signupCustomer = async (data) => {
  const res = await api.post("/auth/signup", {
    name: data.name,
    email: data.email,
    password: data.password,
    role: "CUSTOMER",
  });
  return res.data; // { token }
};

// DRIVER
export const signupDriver = async (data) => {
  const res = await api.post("/driver/signup", {
    name: data.name,
    email: data.email,
    password: data.password,
    vehicleNumber: data.vehicleNumber,
  });
  return res.data; // { token }
};
