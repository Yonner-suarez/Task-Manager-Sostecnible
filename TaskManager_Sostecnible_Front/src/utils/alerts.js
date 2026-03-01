import Swal from "sweetalert2";

const Toast = Swal.mixin({
  toast: true,
  position: "top-end",
  showConfirmButton: false,
  timer: 3000,
  timerProgressBar: true,
});

export const showConfirm = async (
  title,
  text = "Esta acción no se puede deshacer",
  icon = "warning"
) => {
  const result = await Swal.fire({
    title: title,
    text: text,
    icon: icon,
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
    confirmButtonText: "Sí, continuar",
    cancelButtonText: "No, cancelar",
    reverseButtons: true,
  });

  return result.isConfirmed;
};

export const showAlert = (title, icon = "success", text = "") => {
  return Swal.fire({
    title,
    text,
    icon,
    confirmButtonColor: "#3085d6",
  });
};

export const showToast = (title, icon = "success") => {
  Toast.fire({
    icon,
    title,
  });
};

//Procesar los errores del backend
export const handleBackendError = (error) => {
  const errorData = error.response?.data;
  const message = errorData?.message || "Ocurrió un error inesperado";
  const details = errorData?.data;

  let detailText = "";
  if (details && typeof details === "object") {
    detailText = Object.values(details).join("\n");
  }

  showAlert("Error", "error", detailText || message);
};
