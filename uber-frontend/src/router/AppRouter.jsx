import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Login from "../auth/Login";
import Signup from "../auth/Signup";
import ProtectedRoute from "../auth/ProtectedRoute";
import CustomerHome from "../customer/CustomerHome";
import DriverDashboard from "../driver/DriverDashboard";

const AppRouter = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />

        {/* TEMP placeholders */}
        <Route
          path="/customer/home"
          element={
            <ProtectedRoute allowedRole="CUSTOMER">
              <CustomerHome />
            </ProtectedRoute>
          }
        />
        <Route
          path="/driver/dashboard"
          element={
            <ProtectedRoute allowedRole="DRIVER">
              <DriverDashboard />
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
};

export default AppRouter;
