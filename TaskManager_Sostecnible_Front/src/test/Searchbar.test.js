// src/test/SearchBar.test.js
import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import SearchBar from "../components/Searchbar";

jest.mock("../store/store", () => ({
  useTaskStore: () => ({
    searchQuery: "",
    setSearchQuery: jest.fn(),
  }),
}));

test("renderiza input y boton de búsqueda", () => {
  render(<SearchBar />);

  const input = screen.getByPlaceholderText("Buscar por título...");
  expect(input).toBeInTheDocument();

  const button = screen.getByRole("button");
  expect(button).toBeInTheDocument();

  fireEvent.change(input, { target: { value: "Tarea 1" } });
  // Aquí normalmente verificarías que setSearchQuery fue llamado
});
