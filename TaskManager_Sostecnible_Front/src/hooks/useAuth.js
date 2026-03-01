import { useState, useCallback } from "react";

export const useAuth = () => {
  const [token, setToken] = useState(localStorage.getItem("token"));

  const isTokenExpired = useCallback((token) => {
    if (!token) return true;
    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      return payload.exp * 1000 < Date.now();
    } catch (e) {
      return true;
    }
  }, []);

  const login = (newToken) => {
    localStorage.setItem("token", newToken);
    setToken(newToken);
  };

  const logout = useCallback(() => {
    localStorage.removeItem("token");
    setToken(null);
  }, []);

  if (token && isTokenExpired(token)) {
    logout();
  }

  return { token, login, logout };
};
