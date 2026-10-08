import { useState } from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { getSessionUser, logout } from "../api";

export default function Layout({ titre, menu }) {
  const navigate = useNavigate();
  const user = getSessionUser();
  const [ouvert, setOuvert] = useState(false);

  function deconnexion() {
    logout();
    navigate("/login");
  }

  return (
    <div className="layout">
      <aside className={`sidebar ${ouvert ? "sidebar-open" : ""}`}>
        <div className="sidebar-top">
          <div className="brand">
            <span className="brand-logo">✦</span>
            <span>{titre}</span>
          </div>
          <button className="menu-toggle" onClick={() => setOuvert(!ouvert)} aria-label="Menu">
            ☰
          </button>
        </div>

        <nav className="nav">
          {menu.map((item) => (
            <NavLink key={item.to} to={item.to} end={item.end} onClick={() => setOuvert(false)}>
              <span className="nav-icon">{item.icon}</span>
              {item.label}
            </NavLink>
          ))}
        </nav>

        <div className="sidebar-user">
          <div className="avatar">{(user?.prenom?.[0] || "?").toUpperCase()}</div>
          <div className="sidebar-user-info">
            <strong>
              {user?.prenom} {user?.nom}
            </strong>
            <span>{user?.email}</span>
          </div>
          <button className="btn-logout" onClick={deconnexion} title="Se déconnecter" aria-label="Se déconnecter">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
              <polyline points="16 17 21 12 16 7" />
              <line x1="21" y1="12" x2="9" y2="12" />
            </svg>
          </button>
        </div>
      </aside>

      <main className="content">
        <Outlet />
      </main>
    </div>
  );
}
