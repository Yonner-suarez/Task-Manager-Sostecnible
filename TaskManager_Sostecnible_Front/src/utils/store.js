import { create } from "zustand";

export const useTaskStore = create((set) => ({
  filterPriority: null, // 'Alta', 'Media', 'Baja'
  filterStatus: null, // 'Pendiente', 'En Progreso', 'Completada'
  searchQuery: "",
  selectedTask: null,

  setFilterPriority: (priority) => set({ filterPriority: priority }),
  setFilterStatus: (status) => set({ filterStatus: status }),
  setSearchQuery: (query) => set({ searchQuery: query }),
  setSelectedTask: (task) => set({ selectedTask: task }),
}));
