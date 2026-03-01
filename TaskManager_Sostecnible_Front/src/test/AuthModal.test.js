import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import AuthModal from "../components/AuthModal";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { showToast } from "../utils/alerts";

// Mocks
jest.mock("../utils/alerts");
jest.mock("@tanstack/react-query");
jest.mock("../api/taskApi", () => require("./taskApi.mock"));

describe("AuthModal - Pruebas de Autenticación", () => {
  const mockOnLoginSuccess = jest.fn();
  const mockInvalidateQueries = jest.fn();
  const mockRemoveQueries = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();

    useQueryClient.mockReturnValue({
      invalidateQueries: mockInvalidateQueries,
      removeQueries: mockRemoveQueries,
    });
  });

  test("debe llamar a loginUser cuando se envía el formulario de ingreso", () => {
    const mockMutate = jest.fn();
    useMutation.mockReturnValue({ mutate: mockMutate, isPending: false });

    render(<AuthModal onLoginSuccess={mockOnLoginSuccess} />);

    fireEvent.change(screen.getByLabelText(/Usuario o Email/i), {
      target: { value: "tester" },
    });

    fireEvent.change(screen.getByLabelText(/^Contraseña$/i), {
      target: { value: "password123" },
    });

    const loginBtn = screen.getByRole("button", { name: /ingresar/i });
    fireEvent.click(loginBtn);

    expect(mockMutate).toHaveBeenCalled();
  });

  test("debe validar que las contraseñas coincidan en el registro", () => {
    const mockMutate = jest.fn();
    useMutation.mockReturnValue({ mutate: mockMutate, isPending: false });

    render(<AuthModal onLoginSuccess={mockOnLoginSuccess} />);

    fireEvent.click(screen.getByText(/¿No tienes cuenta\? Regístrate/i));

    fireEvent.change(screen.getByLabelText(/Nombre de Usuario/i), {
      target: { value: "tester123" },
    });
    fireEvent.change(screen.getByLabelText(/Email/i), {
      target: { value: "test@test.com" },
    });

    fireEvent.change(screen.getByLabelText(/^Contraseña$/i), {
      target: { value: "123" },
    });
    fireEvent.change(screen.getByLabelText(/Confirmar Contraseña/i), {
      target: { value: "456" },
    });

    const registerBtn = screen.getByRole("button", { name: /registrarme/i });
    fireEvent.click(registerBtn);

    expect(showToast).toHaveBeenCalledWith(
      "Las contraseñas no coinciden",
      "error"
    );

    expect(mockMutate).not.toHaveBeenCalled();
  });

  test("debe mostrar el estado de carga cuando isPending es true", () => {
    useMutation.mockReturnValue({
      mutate: jest.fn(),
      isPending: true,
    });

    render(<AuthModal onLoginSuccess={mockOnLoginSuccess} />);

    expect(screen.getByText(/procesando\.\.\./i)).toBeInTheDocument();

    const submitBtn = screen.getByRole("button", { name: /procesando\.\.\./i });
    expect(submitBtn).toBeDisabled();
  });
});
