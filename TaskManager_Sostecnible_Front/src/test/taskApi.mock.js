export const fetchTasks = jest.fn(() => Promise.resolve([]));
export const deleteTask = jest.fn(() => Promise.resolve());
export const createOrUpdateTask = jest.fn(() => Promise.resolve());
export const loginUser = jest.fn(() => Promise.resolve("token-falso-123"));
export const registerUser = jest.fn(() => Promise.resolve({ status: 201 }));
