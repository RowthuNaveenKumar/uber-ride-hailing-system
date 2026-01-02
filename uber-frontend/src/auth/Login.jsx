import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginCustomer, loginDriver } from "../api/authApi";
import { useAuth } from "../context/AuthContext";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("CUSTOMER"); // 👈 NEW
  const [error, setError] = useState("");

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      let data;

      if (role === "CUSTOMER") {
        data = await loginCustomer(email, password);
      } else {
        data = await loginDriver(email, password);
      }

      const token = data.token;
      const payload = JSON.parse(atob(token.split(".")[1]));
      const userRole = payload.role;

      login(token, userRole);

      if (userRole === "CUSTOMER") {
        navigate("/customer/home");
      } else {
        navigate("/driver/dashboard");
      }
    } catch (err) {
      setError("Invalid email or password");
    }
  };

  return (
    <div>
      <h2>Login</h2>

      {error && <p style={{ color: "red" }}>{error}</p>}

      <form onSubmit={handleSubmit}>
        <select value={role} onChange={(e) => setRole(e.target.value)}>
          <option value="CUSTOMER">Customer</option>
          <option value="DRIVER">Driver</option>
        </select>

        <br />

        <input
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <br />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <br />

        <button type="submit">Login</button>
      </form>
    </div>
  );
}

export default Login;
