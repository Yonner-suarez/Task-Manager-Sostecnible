// src/test/TaskDetail.test.js
import React from "react";
import { QueryClient } from "@tanstack/react-query";
import { QueryClientProvider } from "@tanstack/react-query";
import { render, screen } from "@testing-library/react";
import TaskDetail from "../pages/TaskDetail";

const queryClient = new QueryClient();

jest.mock("../store/store", () => ({
  useTaskStore: () => ({
    selectedTask: null,
    setSelectedTask: jest.fn(),
  }),
}));

jest.mock("../api/taskApi", () => ({
  createOrUpdateTask: jest.fn(),
}));

test("muestra mensaje cuando no hay tarea seleccionada", () => {
  render(
    <QueryClientProvider client={queryClient}>
      <TaskDetail />
    </QueryClientProvider>
  );
  expect(
    screen.getByText("Selecciona una tarea para ver detalles o crear nueva.")
  ).toBeInTheDocument();
});
