import React from "react";
import { useTaskStore } from "../utils/store";
import "bootstrap/dist/css/bootstrap.min.css";

export default function Sidebar() {
  const { setFilterPriority, setFilterStatus } = useTaskStore();

  const priorities = ["Alta", "Media", "Baja"];
  const statuses = ["Pendiente", "En Progreso", "Completada"];

  return (
    <div
      className="d-flex flex-column p-3 bg-light vh-100"
      style={{ minWidth: "200px" }}
    >
      <h4 className="mb-3">Filtrar por Prioridad</h4>
      <div className="d-flex flex-column mb-4">
        {priorities.map((p) => (
          <button
            key={p}
            className={`btn btn-outline-primary mb-2`}
            onClick={() => setFilterPriority(p)}
          >
            {p}
          </button>
        ))}
      </div>

      <h4 className="mb-3">Filtrar por Estado</h4>
      <div className="d-flex flex-column">
        {statuses.map((s) => (
          <button
            key={s}
            className={`btn btn-outline-secondary mb-2`}
            onClick={() => setFilterStatus(s)}
          >
            {s}
          </button>
        ))}
      </div>
    </div>
  );
}
