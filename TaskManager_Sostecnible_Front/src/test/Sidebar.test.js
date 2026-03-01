// src/test/Sidebar.test.js
import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import Sidebar from "../components/Sidebar";

jest.mock("../store/store", () => ({
  useTaskStore: () => ({
    setFilterPriority: jest.fn(),
    setFilterStatus: jest.fn(),
  }),
}));

test("renderiza botones de prioridad y estado", () => {
  render(<Sidebar />);

  ["Alta", "Media", "Baja"].forEach((text) =>
    expect(screen.getByText(text)).toBeInTheDocument()
  );

  ["Pendiente", "En Progreso", "Completada"].forEach((text) =>
    expect(screen.getByText(text)).toBeInTheDocument()
  );
});
