import axios from "axios";

const API_URL = import.meta.env.VITE_URL_API;

export const fetchTasks = async ({ queryKey }) => {
  const [_key, { priority, status, search, sortBy }] = queryKey;
  console.log(sortBy);
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
  if (task.idTask) {
    const response = await axios.put(`${API_URL}/${task.idTask}`, task);
    return response.data;
  } else {
    const response = await axios.post(API_URL, task);
    return response.data;
  }
};

export const deleteTask = async (taskId) => {
  if (taskId) {
    const response = await axios.delete(`${API_URL}/${taskId}`);
    return response.data;
  }
  return null;
};
