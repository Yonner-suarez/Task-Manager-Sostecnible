import React, { useState, useEffect } from "react";
import { useTaskStore } from "../store/store";
import { createOrUpdateTask } from "../api/taskApi";
import "bootstrap/dist/css/bootstrap.min.css";
import { useMutation, useQueryClient } from "@tanstack/react-query";

export default function TaskDetail() {
  const { selectedTask, setSelectedTask } = useTaskStore();
  const queryClient = useQueryClient();
  const [form, setForm] = useState({
    title: "",
    description: "",
    priority: "MEDIA",
    status: "PENDIENTE",
    fechaVencimiento: "",
  });

  const mutation = useMutation({
    mutationFn: createOrUpdateTask,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["tasks"] });

      setSelectedTask(null);
    },
    onError: (error) => {
      alert("Hubo un error al guardar la tarea");
    },
  });

  useEffect(() => {
    if (selectedTask) {
      setForm({
        ...selectedTask,
        priority: selectedTask.priority.toUpperCase(),
        fechaVencimiento: selectedTask.fechaVencimiento || "",
      });
    }
  }, [selectedTask]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({
      ...form,
      [name]: name === "priority" ? value.toUpperCase() : value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    await createOrUpdateTask(form);
    mutation.mutate(form);
    setSelectedTask(null);
  };

  if (!selectedTask)
    return (
      <div className="text-center text-muted mt-5">
        Selecciona una tarea para ver detalles o crear nueva.
      </div>
    );

  // Fecha mínima: mañana
  const today = new Date();
  const tomorrow = new Date(today);
  tomorrow.setDate(today.getDate() + 1);
  const minDate = tomorrow.toISOString().split("T")[0];

  // Solo deshabilitar si la fecha ya viene de la DB
  const isDisabled = selectedTask?.fechaVencimiento ? true : false;

  return (
    <div className="card shadow-sm border-0 rounded-3 p-3">
      <h5 className="card-title mb-3">
        {form.idTask ? "Editar Tarea" : "Nueva Tarea"}
      </h5>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Título</label>
          <input
            type="text"
            className="form-control"
            name="title"
            value={form.title}
            onChange={handleChange}
            placeholder="Título de la tarea"
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Descripción</label>
          <textarea
            className="form-control"
            name="description"
            value={form.description}
            onChange={handleChange}
            placeholder="Descripción de la tarea"
            rows={4}
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Prioridad</label>
          <select
            className="form-select"
            name="priority"
            value={form.priority}
            onChange={handleChange}
          >
            <option value="ALTA">Alta</option>
            <option value="MEDIA">Media</option>
            <option value="BAJA">Baja</option>
          </select>
        </div>

        <div className="mb-3">
          <label className="form-label">Estado</label>
          <select
            className="form-select"
            name="status"
            value={form.status}
            onChange={handleChange}
          >
            <option value="PENDIENTE">Pendiente</option>
            <option value="EN PROGRESO">En Progreso</option>
            <option value="COMPLETADA">Completada</option>
          </select>
        </div>

        <div className="mb-3">
          <label className="form-label">Fecha de Vencimiento</label>
          <input
            type="date"
            className="form-control"
            name="fechaVencimiento"
            value={form.fechaVencimiento}
            onChange={handleChange}
            disabled={isDisabled}
            min={minDate}
          />
        </div>

        <div className="d-flex justify-content-between">
          <button type="submit" className="btn btn-success">
            Guardar
          </button>
          <button
            type="button"
            className="btn btn-secondary"
            onClick={() => setSelectedTask(null)}
          >
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
}
