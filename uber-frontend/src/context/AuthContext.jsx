import { createContext, useContext, useEffect, useState } from "react";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [token, setToken] = useState(null);
  const [role, setRole] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const savedToken = localStorage.getItem("token");
    const savedRole = localStorage.getItem("role");

    if (savedToken && savedRole.split(".").length === 3) {
      setToken(savedToken);
      setRole(savedRole);
      setIsAuthenticated(true);
    } else {
      localStorage.clear();
    }
    setLoading(false);
  }, []);

  const login = (jwtToken, userRole) => {
    localStorage.setItem("token", jwtToken);
    localStorage.setItem("role", userRole);

    setToken(jwtToken);
    setRole(userRole);
    setIsAuthenticated(true);
  };

  const logout = () => {
    localStorage.clear();
    setToken(null);
    setRole(null);
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider
      value={{ token, role, isAuthenticated, login, logout, loading }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
