// Adresse du backend Spring Boot
const API_URL = "http://localhost:8080/api";

// ---------- Session (token + utilisateur connecte) ----------
export function getToken() {
  return localStorage.getItem("token");
}

export function getSessionUser() {
  try {
    return JSON.parse(localStorage.getItem("utilisateur") || "null");
  } catch {
    return null;
  }
}

export function saveSession(data) {
  localStorage.setItem("token", data.token);
  localStorage.setItem("utilisateur", JSON.stringify(data.utilisateur));
}

export function updateSessionUser(utilisateur) {
  localStorage.setItem("utilisateur", JSON.stringify(utilisateur));
}

export function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("utilisateur");
}

// ---------- Appel generique ----------
export async function apiFetch(path, options = {}) {
  const token = getToken();
  let response;
  try {
    response = await fetch(`${API_URL}${path}`, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...options.headers,
      },
    });
  } catch {
    throw new Error("Impossible de contacter le serveur. Le backend est-il lancé ?");
  }

  // Token expire ou invalide : retour a la connexion
  if (response.status === 401 && !path.startsWith("/auth/sign")) {
    logout();
    window.location.href = "/login";
    throw new Error("Session expirée");
  }

  if (response.status === 204) return null;
  const data = await response.json().catch(() => null);

  if (!response.ok) {
    let message = "Erreur serveur";
    if (data?.erreur) message = data.erreur;
    else if (response.status === 403) message = "Accès refusé";
    else if (data && typeof data === "object" && !data.status) message = Object.values(data).join(" · ");
    const error = new Error(message);
    error.status = response.status;
    throw error;
  }
  return data;
}

const get = (path) => apiFetch(path);
const post = (path, body) => apiFetch(path, { method: "POST", body: body ? JSON.stringify(body) : undefined });
const put = (path, body) => apiFetch(path, { method: "PUT", body: JSON.stringify(body) });
const del = (path) => apiFetch(path, { method: "DELETE" });

// ---------- Auth ----------
export const auth = {
  signin: (email, motDePasse) => post("/auth/signin", { email, motDePasse }),
  signup: (data) => post("/auth/signup", data),
  me: () => get("/auth/me"),
};

// ---------- Ressources ----------
export const utilisateursApi = {
  list: (role) => get(`/utilisateurs${role ? `?role=${role}` : ""}`),
  create: (data) => post("/utilisateurs", data),
  update: (id, data) => put(`/utilisateurs/${id}`, data),
  remove: (id) => del(`/utilisateurs/${id}`),
};

export const niveauxApi = {
  list: () => get("/niveaux"),
  create: (data) => post("/niveaux", data),
  update: (id, data) => put(`/niveaux/${id}`, data),
  remove: (id) => del(`/niveaux/${id}`),
};

export const sallesApi = {
  list: () => get("/salles"),
  create: (data) => post("/salles", data),
  update: (id, data) => put(`/salles/${id}`, data),
  remove: (id) => del(`/salles/${id}`),
};

export const coursApi = {
  list: () => get("/cours"),
  inscrits: (id) => get(`/cours/${id}/enfants`),
  create: (data) => post("/cours", data),
  update: (id, data) => put(`/cours/${id}`, data),
  remove: (id) => del(`/cours/${id}`),
};

export const enfantsApi = {
  list: () => get("/enfants"),
  create: (data) => post("/enfants", data),
  update: (id, data) => put(`/enfants/${id}`, data),
  remove: (id) => del(`/enfants/${id}`),
  inscrire: (id, coursId) => post(`/enfants/${id}/cours/${coursId}`),
  desinscrire: (id, coursId) => del(`/enfants/${id}/cours/${coursId}`),
};

export const mouvementsApi = {
  list: () => get("/mouvements"),
  create: (data) => post("/mouvements", data),
  remove: (id) => del(`/mouvements/${id}`),
};
