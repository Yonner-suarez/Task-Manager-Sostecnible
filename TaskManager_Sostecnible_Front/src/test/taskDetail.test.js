// src/test/TaskDetail.test.js
import React from "react";
import { render, screen } from "@testing-library/react";
import TaskDetail from "../pages/TaskDetail";

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
  render(<TaskDetail />);
  expect(
    screen.getByText("Selecciona una tarea para ver detalles o crear nueva.")
  ).toBeInTheDocument();
});
