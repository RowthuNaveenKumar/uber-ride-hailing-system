import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { signupCustomer, signupDriver } from "../api/authApi";
import { useAuth } from "../context/AuthContext";

function Signup() {
  const [role, setRole] = useState("CUSTOMER");
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [vehicleNumber, setVehicleNumber] = useState("");
  const [error, setError] = useState("");

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      let data;

      if (role === "CUSTOMER") {
        data = await signupCustomer({ name, email, password });
      } else {
        data = await signupDriver({
          name,
          email,
          password,
          vehicleNumber,
        });
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
      setError("Signup failed. Email may already exist.");
    }
  };

  return (
    <div>
      <h2>Signup</h2>

      {error && <p style={{ color: "red" }}>{error}</p>}

      <form onSubmit={handleSubmit}>
        <select value={role} onChange={(e) => setRole(e.target.value)}>
          <option value="CUSTOMER">Customer</option>
          <option value="DRIVER">Driver</option>
        </select>

        <br />

        <input
          placeholder="Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />

        <br />

        <input
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        <br />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <br />

        {role === "DRIVER" && (
          <>
            <input
              placeholder="Vehicle Number"
              value={vehicleNumber}
              onChange={(e) => setVehicleNumber(e.target.value)}
              required
            />
            <br />
          </>
        )}

        <button type="submit">Signup</button>
      </form>
    </div>
  );
}

export default Signup;
