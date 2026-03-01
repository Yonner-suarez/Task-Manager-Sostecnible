// Colores de prioridad
export function getPriorityBadge(priority) {
  switch (priority.toUpperCase()) {
    case "ALTA":
      return "bg-danger";
    case "MEDIA":
      return "bg-warning text-dark";
    case "BAJA":
      return "bg-success";
    default:
      return "bg-secondary";
  }
}

// Colores de estado
export function getStatusBadge(status) {
  switch (status.toUpperCase()) {
    case "PENDIENTE":
      return "bg-primary";
    case "EN PROGRESO":
      return "bg-info text-dark";
    case "COMPLETADA":
      return "bg-success";
    default:
      return "bg-secondary";
  }
}
