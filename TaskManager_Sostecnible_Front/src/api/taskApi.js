import axios from "axios";
import { handleBackendError, showToast } from "../utils/alerts";

const API_URL = import.meta.env.VITE_URL_API;

export const fetchTasks = async ({ queryKey }) => {
  const [_key, { priority, status, search, sortBy }] = queryKey;
  const response = await axios.get(API_URL, {
    params: { priority, status, search, sortBy },
  });
  return response.data;
};

export const fetchTaskById = async (id) => {
  const response = await axios.get(`${API_URL}/${id}`);
  return response.data;
};

export const createOrUpdateTask = async (task) => {
  try {
    let response;
    if (task.idTask) {
      response = await axios.put(`${API_URL}/${task.idTask}`, task);
      showToast("Tarea actualizada correctamente", "success");
    } else {
      response = await axios.post(API_URL, task);
      showToast("Tarea creada con éxito", "success");
    }
    return response.data;
  } catch (error) {
    handleBackendError(error);
    throw error;
  }
};

export const deleteTask = async (taskId) => {
  try {
    if (taskId) {
      const response = await axios.delete(`${API_URL}/${taskId}`);
      showToast("Tarea eliminada", "info");
      return response.data;
    }
  } catch (error) {
    handleBackendError(error);
    throw error;
  }
};
