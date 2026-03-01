import React, { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { loginUser, registerUser } from "../api/taskApi";
import { showToast } from "../utils/alerts";

export default function AuthModal({ onLoginSuccess }) {
  const [isRegister, setIsRegister] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const queryClient = useQueryClient();

  const [formData, setFormData] = useState({
    userName: "",
    email: "",
    identifier: "", // Solo para login
    password: "",
    confirmPassword: "",
  });

  // Mutación de Login
  const loginMutation = useMutation({
    mutationFn: () => loginUser(formData.identifier, formData.password),
    onSuccess: (token) => {
      queryClient.removeQueries();
      queryClient.invalidateQueries({ queryKey: ["tasks"] });
      onLoginSuccess(token);
    },
  });

  // Mutación de Registro
  const registerMutation = useMutation({
    mutationFn: () =>
      registerUser({
        userName: formData.userName,
        email: formData.email,
        password: formData.password,
      }),
    onSuccess: () => {
      showToast("Registro exitoso, ahora puedes ingresar", "success");
      setIsRegister(false);
    },
  });

  const handleSubmit = (e) => {
    e.preventDefault();

    if (isRegister) {
      if (formData.password !== formData.confirmPassword) {
        return showToast("Las contraseñas no coinciden", "error");
      }
      registerMutation.mutate();
    } else {
      loginMutation.mutate();
    }
  };

  const isPending = loginMutation.isPending || registerMutation.isPending;

  return (
    <div
      className="modal show d-block bg-dark bg-opacity-75"
      style={{ backdropFilter: "blur(8px)" }}
    >
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content border-0 shadow-lg">
          <div
            className={`modal-header ${
              isRegister ? "bg-success" : "bg-primary"
            } text-white text-center`}
          >
            <h5 className="modal-title fw-bold w-100">
              {isRegister ? "Crear Cuenta" : "Task Manager - Ingreso"}
            </h5>
          </div>

          <form onSubmit={handleSubmit} className="p-4">
            {isRegister && (
              <>
                <div className="mb-3">
                  <label className="form-label fw-semibold">
                    Nombre de Usuario
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="ej: juanperez1"
                    onChange={(e) =>
                      setFormData({ ...formData, userName: e.target.value })
                    }
                    required
                  />
                </div>
                <div className="mb-3">
                  <label className="form-label fw-semibold">Email</label>
                  <input
                    type="email"
                    className="form-control"
                    placeholder="juan@ejemplo.com"
                    onChange={(e) =>
                      setFormData({ ...formData, email: e.target.value })
                    }
                    required
                  />
                </div>
              </>
            )}

            {!isRegister && (
              <div className="mb-3">
                <label className="form-label fw-semibold">
                  Usuario o Email
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={formData.identifier}
                  onChange={(e) =>
                    setFormData({ ...formData, identifier: e.target.value })
                  }
                  required
                />
              </div>
            )}

            <div className="mb-3">
              <label className="form-label fw-semibold">Contraseña</label>
              <div className="input-group">
                <input
                  type={showPassword ? "text" : "password"}
                  className="form-control"
                  value={formData.password}
                  onChange={(e) =>
                    setFormData({ ...formData, password: e.target.value })
                  }
                  required
                />
                <button
                  className="btn btn-outline-secondary"
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  <i
                    className={`bi ${showPassword ? "bi-eye-slash" : "bi-eye"}`}
                  ></i>
                </button>
              </div>
            </div>

            {isRegister && (
              <div className="mb-3">
                <label className="form-label fw-semibold">
                  Confirmar Contraseña
                </label>
                <input
                  type={showPassword ? "text" : "password"}
                  className="form-control"
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      confirmPassword: e.target.value,
                    })
                  }
                  required
                />
              </div>
            )}

            <button
              type="submit"
              className={`btn ${
                isRegister ? "btn-success" : "btn-primary"
              } w-100 fw-bold py-2 shadow-sm`}
              disabled={isPending}
            >
              {isPending
                ? "Procesando..."
                : isRegister
                ? "Registrarme"
                : "Ingresar"}
            </button>

            <div className="text-center mt-3">
              <button
                type="button"
                className="btn btn-link text-decoration-none"
                onClick={() => setIsRegister(!isRegister)}
              >
                {isRegister
                  ? "¿Ya tienes cuenta? Ingresa aquí"
                  : "¿No tienes cuenta? Regístrate"}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
