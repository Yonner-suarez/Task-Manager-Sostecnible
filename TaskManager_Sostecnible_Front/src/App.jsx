import React from "react";
import Sidebar from "./components/sidebar";
import TaskList from "./components/TaskList";
import SearchBar from "./components/Searchbar";
import TaskDetail from "./components/TaskDetail";
import { useTaskStore } from "./utils/store";

export default function App() {
  const { setSelectedTask } = useTaskStore();

  return (
    <div className="d-flex vh-100">
      <Sidebar />
      <div className="flex-grow-1 p-3">
        <div className="d-flex justify-content-between mb-3">
          <SearchBar />
          <button
            className="btn btn-success"
            onClick={() =>
              setSelectedTask({
                title: "",
                description: "",
                priority: "Media",
                status: "Pendiente",
              })
            }
          >
            + Nueva Tarea
          </button>
        </div>
        <div className="d-flex gap-3">
          <div className="flex-fill">
            <TaskList />
          </div>
          <div style={{ width: "350px" }}>
            <TaskDetail />
          </div>
        </div>
      </div>
    </div>
  );
}
