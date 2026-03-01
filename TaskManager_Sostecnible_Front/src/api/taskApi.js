import axios from "axios";
import { handleBackendError, showToast } from "../utils/alerts";

const API_URL = import.meta.env.VITE_URL_API;

const api = axios.create({
  baseURL: API_URL,
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => {
    return response.data?.data !== undefined
      ? response.data.data
      : response.data;
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      window.location.reload();
    }
    return Promise.reject(error);
  }
);

/* --- SERVICIOS DE TAREAS --- */

export const fetchTasks = async ({ queryKey }) => {
  const [, params] = queryKey;
  try {
    return await api.get("/tasks", { params });
  } catch (error) {
    handleBackendError(error);
    throw error;
  }
};

export const fetchTaskById = (id) => api.get(`/tasks/${id}`);

export const createOrUpdateTask = async (task) => {
  try {
    const isUpdate = Boolean(task.idTask);
    const endpoint = isUpdate ? `/tasks/${task.idTask}` : "/tasks";
    const method = isUpdate ? "put" : "post";

    const data = await api[method](endpoint, task);
    showToast(isUpdate ? "Tarea actualizada" : "Tarea creada", "success");
    return data;
  } catch (error) {
    handleBackendError(error);
    throw error;
  }
};

export const deleteTask = async (taskId) => {
  try {
    if (!taskId) return;
    await api.delete(`/tasks/${taskId}`);
    showToast("Tarea eliminada", "info");
  } catch (error) {
    handleBackendError(error);
    throw error;
  }
};

/* --- SERVICIOS DE USUARIO --- */

export const loginUser = async (identifier, password) => {
  try {
    const isEmail = identifier.includes("@");
    const payload = {
      username: isEmail ? "" : identifier,
      email: isEmail ? identifier : "",
      password: password,
    };

    return await api.post("/user/login", payload);
  } catch (error) {
    handleBackendError(error);
    throw error;
  }
};

export const registerUser = async (userData) => {
  try {
    // userData trae { userName, email, password }
    const response = await api.post("/user", userData);
    return response;
  } catch (error) {
    handleBackendError(error);
    throw error;
  }
};

export default api;
