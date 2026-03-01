jest.mock("../api/taskApi", () => require("./taskApi.mock"));

import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import TaskList from "../pages/TaskList"; // ahora sí usará el mock
import { useTaskStore } from "../store/store";
import { useQuery } from "@tanstack/react-query";

// Mock del store y react-query
jest.mock("../store/store");
jest.mock("@tanstack/react-query");

// --- TESTS ---
test("muestra mensaje de cargando", () => {
  useTaskStore.mockReturnValue({
    filterPriority: "",
    filterStatus: "",
    searchQuery: "",
    setSelectedTask: jest.fn(),
  });

  useQuery.mockReturnValue({
    data: null,
    isLoading: true,
  });

  render(<TaskList />);
  expect(screen.getByText("Cargando tareas...")).toBeInTheDocument();
});

test("muestra mensaje cuando no hay tareas", () => {
  useTaskStore.mockReturnValue({
    filterPriority: "",
    filterStatus: "",
    searchQuery: "",
    setSelectedTask: jest.fn(),
  });

  useQuery.mockReturnValue({
    data: [],
    isLoading: false,
  });

  render(<TaskList />);
  expect(screen.getByText("No hay tareas para mostrar.")).toBeInTheDocument();
});

test("renderiza lista de tareas", () => {
  const mockSetSelectedTask = jest.fn();

  useTaskStore.mockReturnValue({
    filterPriority: "",
    filterStatus: "",
    searchQuery: "",
    setSelectedTask: mockSetSelectedTask,
  });

  const tasksMock = [
    {
      id: 1,
      title: "Tarea 1",
      description: "Descripción 1",
      priority: "Alta",
      status: "Pendiente",
    },
    {
      id: 2,
      title: "Tarea 2",
      description: "",
      priority: "Media",
      status: "En Progreso",
    },
  ];

  useQuery.mockReturnValue({ data: tasksMock, isLoading: false });

  render(<TaskList />);
  expect(screen.getByText("Tarea 1")).toBeInTheDocument();
  expect(screen.getByText("Descripción 1")).toBeInTheDocument();
  expect(screen.getByText("Tarea 2")).toBeInTheDocument();
  expect(screen.getByText("Sin descripción")).toBeInTheDocument();

  fireEvent.click(screen.getByText("Tarea 1"));
  expect(mockSetSelectedTask).toHaveBeenCalledWith(tasksMock[0]);
});
