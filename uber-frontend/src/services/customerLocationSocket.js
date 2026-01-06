import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let stompClient = null;

export const connectCustomerSocket = (driverId, onMessage) => {
  stompClient = new Client({
    webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
    reconnectDelay: 5000,
  });
  stompClient.onConnect = () => {
    stompClient.subscribe(`/topic/location/${driverId}`, (msg) => {
      const data = JSON.parse(msg.body);
      onMessage(data);
    });
  };
  stompClient.activate();
};

export const disconnectCustomerSocket = () => {
  if (stompClient) stompClient.deactivate();
};
