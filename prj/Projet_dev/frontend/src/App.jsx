import { Routes, Route, Navigate } from "react-router-dom";
import { getSessionUser, getToken } from "./api";
import RequireRole from "./components/RequireRole";
import Layout from "./components/Layout";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Dashboard from "./pages/admin/Dashboard";
import Utilisateurs from "./pages/admin/Utilisateurs";
import Niveaux from "./pages/admin/Niveaux";
import Salles from "./pages/admin/Salles";
import Cours from "./pages/admin/Cours";
import EnfantsAdmin from "./pages/admin/Enfants";
import PaiementsAdmin from "./pages/admin/Paiements";
import Accueil from "./pages/parent/Accueil";
import MesEnfants from "./pages/parent/MesEnfants";
import MesPaiements from "./pages/parent/MesPaiements";

const MENU_ADMIN = [
  { to: "/admin", label: "Tableau de bord", icon: "📊", end: true },
  { to: "/admin/utilisateurs", label: "Utilisateurs", icon: "👤" },
  { to: "/admin/enfants", label: "Enfants", icon: "🧒" },
  { to: "/admin/cours", label: "Cours", icon: "🗓️" },
  { to: "/admin/niveaux", label: "Niveaux", icon: "📚" },
  { to: "/admin/salles", label: "Salles", icon: "🏫" },
  { to: "/admin/paiements", label: "Paiements", icon: "💶" },
];

const MENU_PARENT = [
  { to: "/parent", label: "Accueil", icon: "🏠", end: true },
  { to: "/parent/enfants", label: "Mes enfants", icon: "🧒" },
  { to: "/parent/paiements", label: "Mes paiements", icon: "💶" },
];

// Redirige vers le bon espace selon le role
function Home() {
  if (!getToken()) return <Navigate to="/login" replace />;
  return <Navigate to={getSessionUser()?.role === "ADMIN" ? "/admin" : "/parent"} replace />;
}

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />

      <Route
        path="/admin"
        element={
          <RequireRole role="ADMIN">
            <Layout titre="Espace admin" menu={MENU_ADMIN} />
          </RequireRole>
        }
      >
        <Route index element={<Dashboard />} />
        <Route path="utilisateurs" element={<Utilisateurs />} />
        <Route path="enfants" element={<EnfantsAdmin />} />
        <Route path="cours" element={<Cours />} />
        <Route path="niveaux" element={<Niveaux />} />
        <Route path="salles" element={<Salles />} />
        <Route path="paiements" element={<PaiementsAdmin />} />
      </Route>

      <Route
        path="/parent"
        element={
          <RequireRole role="PARENT">
            <Layout titre="Espace parent" menu={MENU_PARENT} />
          </RequireRole>
        }
      >
        <Route index element={<Accueil />} />
        <Route path="enfants" element={<MesEnfants />} />
        <Route path="paiements" element={<MesPaiements />} />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
