import React from "react";
import { useTaskStore } from "../utils/store";
import "bootstrap/dist/css/bootstrap.min.css";

export default function SearchBar() {
  const { searchQuery, setSearchQuery } = useTaskStore();

  return (
    <div className="mb-3">
      <div className="input-group">
        <input
          type="text"
          className="form-control"
          placeholder="Buscar por título..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
        <button className="btn btn-primary" type="button" onClick={() => {}}>
          🔍
        </button>
      </div>
    </div>
  );
}
