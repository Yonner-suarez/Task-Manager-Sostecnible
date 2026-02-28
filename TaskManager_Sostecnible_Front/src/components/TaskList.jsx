import React from "react";
import { useQuery } from "@tanstack/react-query";
import { fetchTasks } from "../api/task";
import { useTaskStore } from "../utils/store";
import "bootstrap/dist/css/bootstrap.min.css";

export default function TaskList() {
  const { filterPriority, filterStatus, searchQuery, setSelectedTask } =
    useTaskStore();

  const { data: tasks, isLoading } = useQuery({
    queryKey: [
      "tasks",
      { priority: filterPriority, status: filterStatus, search: searchQuery },
    ],
    queryFn: fetchTasks,
  });

  if (isLoading)
    return <div className="text-center mt-5 fs-5">Cargando tareas...</div>;

  if (!tasks || tasks.length === 0)
    return (
      <div className="text-center mt-5 fs-5">No hay tareas para mostrar.</div>
    );

  return (
    <div className="container-fluid mt-3">
      <div className="row">
        {tasks.map((task) => (
          <div className="col-12 col-md-6 col-lg-4 mb-4" key={task.id}>
            <div
              className="card h-100 shadow-sm border-0 rounded-3"
              style={{ cursor: "pointer", transition: "transform 0.2s" }}
              onClick={() => setSelectedTask(task)}
              onMouseEnter={(e) =>
                (e.currentTarget.style.transform = "scale(1.03)")
              }
              onMouseLeave={(e) =>
                (e.currentTarget.style.transform = "scale(1)")
              }
            >
              <div className="card-body d-flex flex-column">
                <h5 className="card-title fw-bold">{task.title}</h5>
                <p className="card-text text-truncate">
                  {task.description || "Sin descripción"}
                </p>
                <div className="mt-auto">
                  <span
                    className={`badge ${getPriorityBadge(task.priority)} me-2`}
                  >
                    {task.priority}
                  </span>
                  <span className={`badge ${getStatusBadge(task.status)}`}>
                    {task.status}
                  </span>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

// Colores de prioridad
function getPriorityBadge(priority) {
  switch (priority) {
    case "Alta":
      return "bg-danger";
    case "Media":
      return "bg-warning text-dark";
    case "Baja":
      return "bg-success";
    default:
      return "bg-secondary";
  }
}

// Colores de estado
function getStatusBadge(status) {
  switch (status) {
    case "Pendiente":
      return "bg-primary";
    case "En Progreso":
      return "bg-info text-dark";
    case "Completada":
      return "bg-success";
    default:
      return "bg-secondary";
  }
}
