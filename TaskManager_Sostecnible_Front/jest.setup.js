// jest.setup.js
import "@testing-library/jest-dom";
globalThis.importMeta = {
  env: {
    VITE_URL_API: "http://localhost:3000", // o tu URL real de desarrollo
  },
};
