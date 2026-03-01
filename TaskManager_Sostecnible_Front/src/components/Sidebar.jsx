import React from "react";
import { useTaskStore } from "../store/store";
import "bootstrap/dist/css/bootstrap.min.css";

export default function Sidebar() {
  const {
    filterPriority,
    setFilterPriority,
    filterStatus,
    setFilterStatus,
    sortBy,
    setSortBy,
  } = useTaskStore();

  const priorities = ["Alta", "Media", "Baja"];
  const statuses = ["Pendiente", "En Progreso", "Completada"];

  const handleClearFilters = () => {
    setFilterPriority("");
    setFilterStatus("");
    setSortBy("");
  };

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
            className={`btn mb-2 ${
              filterPriority === p
                ? "btn-primary text-white"
                : "btn-outline-primary"
            }`}
            onClick={() => setFilterPriority(p)}
          >
            {p}
          </button>
        ))}
      </div>

      <h4 className="mb-3">Filtrar por Estado</h4>
      <div className="d-flex flex-column mb-4">
        {statuses.map((s) => (
          <button
            key={s}
            className={`btn mb-2 ${
              filterStatus === s
                ? "btn-secondary text-white"
                : "btn-outline-secondary"
            }`}
            onClick={() => setFilterStatus(s)}
          >
            {s}
          </button>
        ))}
      </div>

      <h4 className="mb-3">Ordenar por Fecha</h4>
      <div className="d-flex flex-column mb-4">
        <div className="form-check">
          <input
            className="form-check-input"
            type="checkbox"
            id="sortSooner"
            checked={sortBy === "soonest"}
            onChange={() => setSortBy(sortBy === "soonest" ? "" : "soonest")}
          />
          <label className="form-check-label" htmlFor="sortSooner">
            Más Recientes
          </label>
        </div>
        <div className="form-check">
          <input
            className="form-check-input"
            type="checkbox"
            id="sortLater"
            checked={sortBy === "latest"}
            onChange={() => setSortBy(sortBy === "latest" ? "" : "latest")}
          />
          <label className="form-check-label" htmlFor="sortLater">
            Más Antiguas
          </label>
        </div>
      </div>

      <button
        className="btn btn-outline-dark mt-auto"
        onClick={handleClearFilters}
      >
        Limpiar filtros
      </button>
    </div>
  );
}
