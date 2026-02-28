import React, { useState, useEffect } from "react";
import { useTaskStore } from "../utils/store";
import { createOrUpdateTask } from "../api/task";
import "bootstrap/dist/css/bootstrap.min.css";

export default function TaskDetail() {
  const { selectedTask, setSelectedTask } = useTaskStore();
  const [form, setForm] = useState({
    title: "",
    description: "",
    priority: "Media",
    status: "Pendiente",
  });

  useEffect(() => {
    if (selectedTask) setForm(selectedTask);
  }, [selectedTask]);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    await createOrUpdateTask(form);
    setSelectedTask(null);
  };

  if (!selectedTask)
    return (
      <div className="text-center text-muted mt-5">
        Selecciona una tarea para ver detalles o crear nueva.
      </div>
    );

  return (
    <div className="card shadow-sm border-0 rounded-3 p-3">
      <h5 className="card-title mb-3">
        {form.id ? "Editar Tarea" : "Nueva Tarea"}
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
            <option>Alta</option>
            <option>Media</option>
            <option>Baja</option>
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
            <option>Pendiente</option>
            <option>En Progreso</option>
            <option>Completada</option>
          </select>
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
