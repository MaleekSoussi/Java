import { Navigate } from "react-router-dom";
import { getSessionUser, getToken } from "../api";

// Protege une route : il faut etre connecte ET avoir le bon role
export default function RequireRole({ role, children }) {
  const user = getSessionUser();
  if (!getToken() || !user) return <Navigate to="/login" replace />;
  if (user.role !== role) return <Navigate to="/" replace />;
  return children;
}
