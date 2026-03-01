import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import App from "../App";
import { useTaskStore } from "../store/store";

// Mocks de los componentes hijos para aislar App
jest.mock("../components/Sidebar", () => () => (
  <div data-testid="sidebar">Sidebar</div>
));
jest.mock("../pages/TaskList", () => () => (
  <div data-testid="task-list">TaskList</div>
));
jest.mock("../components/Searchbar", () => () => (
  <div data-testid="searchbar">SearchBar</div>
));
jest.mock("../pages/TaskDetail", () => () => (
  <div data-testid="task-detail">TaskDetail</div>
));
jest.mock("../components/AuthModal", () => ({ onLoginSuccess }) => (
  <div data-testid="auth-modal">
    <button onClick={() => onLoginSuccess("fake-token")}>Login Mock</button>
  </div>
));

// Mock del store de Zustand
jest.mock("../store/store", () => ({
  useTaskStore: jest.fn(),
}));

describe("App Component - Flujo Principal", () => {
  const mockSetSelectedTask = jest.fn();

  beforeEach(() => {
    localStorage.clear();
    jest.clearAllMocks();
    useTaskStore.mockReturnValue({
      setSelectedTask: mockSetSelectedTask,
    });
  });

  test("debe mostrar AuthModal si no hay token en localStorage", () => {
    render(<App />);
    expect(screen.getByTestId("auth-modal")).toBeInTheDocument();
    expect(screen.queryByTestId("sidebar")).not.toBeInTheDocument();
  });

  test("debe mostrar el contenido principal si el usuario se loguea", () => {
    render(<App />);

    // Simulamos login exitoso a través del mock de AuthModal
    const loginBtn = screen.getByText("Login Mock");
    fireEvent.click(loginBtn);

    expect(localStorage.getItem("token")).toBe("fake-token");
    expect(screen.getByTestId("sidebar")).toBeInTheDocument();
    expect(screen.getByTestId("task-list")).toBeInTheDocument();
  });

  test("debe limpiar el token y volver al login al hacer clic en Salir", () => {
    // Seteamos un token inicial
    localStorage.setItem("token", "valid-token");

    render(<App />);

    // Verificamos que estamos dentro
    const logoutBtn = screen.getByRole("button", { name: /salir/i });
    fireEvent.click(logoutBtn);

    expect(localStorage.getItem("token")).toBeNull();
    expect(screen.getByTestId("auth-modal")).toBeInTheDocument();
  });

  test("debe abrir el formulario de nueva tarea al hacer clic en Nueva Tarea", () => {
    localStorage.setItem("token", "valid-token");
    render(<App />);

    const newTaskBtn = screen.getByRole("button", { name: /nueva tarea/i });
    fireEvent.click(newTaskBtn);

    expect(mockSetSelectedTask).toHaveBeenCalledWith({
      title: "",
      description: "",
      priority: "MEDIA",
      status: "PENDIENTE",
    });
  });
});
