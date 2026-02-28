// src/test/TaskDetail.test.js
import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import TaskDetail from "../components/TaskDetail";

jest.mock("../utils/store", () => ({
  useTaskStore: () => ({
    selectedTask: null,
    setSelectedTask: jest.fn(),
  }),
}));

jest.mock("../api/task", () => ({
  createOrUpdateTask: jest.fn(),
}));

test("muestra mensaje cuando no hay tarea seleccionada", () => {
  render(<TaskDetail />);
  expect(
    screen.getByText("Selecciona una tarea para ver detalles o crear nueva.")
  ).toBeInTheDocument();
});
