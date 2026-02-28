import axios from "axios";

const API_URL = "http://localhost:1629/tasks";

export const fetchTasks = async ({ queryKey }) => {
  const [_key, { priority, status, search }] = queryKey;
  const response = await axios.get(API_URL, {
    params: { priority, status, search },
  });
  return response.data;
};

export const fetchTaskById = async (id) => {
  const response = await axios.get(`${API_URL}/${id}`);
  return response.data;
};

export const createOrUpdateTask = async (task) => {
  if (task.id) {
    const response = await axios.put(`${API_URL}/${task.id}`, task);
    return response.data;
  } else {
    const response = await axios.post(API_URL, task);
    return response.data;
  }
};
