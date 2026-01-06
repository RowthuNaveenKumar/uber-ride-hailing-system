import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let stompClient = null;

export const connectLocationSocket = (onConnect) => {
  stompClient = new Client({
    webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
    debug: () => {},
    reconnectDelay: 5000,
  });
  stompClient.onConnect = () => {
    console.log("✅ Driver WS Connected");
    onConnect();
  };
  stompClient.activate();
};

export const sendLocationUpdate = (latitude, longitude) => {
  const token = localStorage.getItem("token");
  if (!stompClient || !stompClient.connected) return;

  stompClient.publish({
    destination: "/app/location/update",
    body: JSON.stringify({
      latitude,
      longitude,
      token: `Bearer ${token}`,
    }),
  });
};

export const disconnectLocationSocket = () => {
  if (stompClient) stompClient.deactivate();
};
