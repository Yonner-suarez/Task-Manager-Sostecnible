import React, { useState, useCallback } from "react";
import Sidebar from "./components/Sidebar";
import TaskList from "./pages/TaskList";
import SearchBar from "./components/Searchbar";
import TaskDetail from "./pages/TaskDetail";
import AuthModal from "./components/AuthModal";
import { useTaskStore } from "./store/store";

const INITIAL_TASK_STATE = {
  title: "",
  description: "",
  priority: "MEDIA",
  status: "PENDIENTE",
};

export default function App() {
  const { setSelectedTask } = useTaskStore();
  const [token, setToken] = useState(() => localStorage.getItem("token"));

  const handleLoginSuccess = useCallback((newToken) => {
    localStorage.setItem("token", newToken);
    setToken(newToken);
  }, []);

  const handleLogout = useCallback(() => {
    localStorage.removeItem("token");
    setToken(null);
  }, []);

  if (!token) {
    return <AuthModal onLoginSuccess={handleLoginSuccess} />;
  }

  return (
    <div className="d-flex vh-100 bg-light">
      <Sidebar onLogout={handleLogout} />

      <main className="flex-grow-1 p-3 overflow-hidden d-flex flex-column">
        <header className="d-flex justify-content-between align-items-center mb-3 bg-white p-3 rounded shadow-sm">
          <div className="d-flex align-items-center gap-3 flex-grow-1">
            <SearchBar />
          </div>

          <div className="d-flex gap-2">
            <button
              className="btn btn-success fw-bold shadow-sm d-flex align-items-center gap-2"
              onClick={() => setSelectedTask(INITIAL_TASK_STATE)}
            >
              <i className="bi bi-plus-lg"></i> Nueva Tarea
            </button>

            <button
              className="btn btn-outline-danger fw-bold shadow-sm d-flex align-items-center gap-2"
              onClick={handleLogout}
            >
              <i className="bi bi-box-arrow-right"></i> Salir
            </button>
          </div>
        </header>

        <div className="d-flex gap-3 flex-grow-1 overflow-hidden">
          <section className="flex-fill overflow-auto bg-white rounded shadow-sm p-2">
            <TaskList />
          </section>

          <aside
            style={{ width: "400px" }}
            className="bg-white rounded shadow-sm p-2 overflow-auto"
          >
            <TaskDetail />
          </aside>
        </div>
      </main>
    </div>
  );
}
