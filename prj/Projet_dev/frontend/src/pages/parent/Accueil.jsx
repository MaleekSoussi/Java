import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { auth, enfantsApi, updateSessionUser } from "../../api";
import { euro, heure, JOURS, jourLabel } from "../../utils";

export default function Accueil() {
  const [moi, setMoi] = useState(null);
  const [enfants, setEnfants] = useState([]);
  const [erreur, setErreur] = useState("");

  useEffect(() => {
    Promise.all([auth.me(), enfantsApi.list()])
      .then(([me, liste]) => {
        setMoi(me);
        updateSessionUser(me);
        setEnfants(liste);
      })
      .catch((e) => setErreur(e.message));
  }, []);

  if (erreur) return <div className="alert alert-error">{erreur}</div>;
  if (!moi) return <p className="muted">Chargement...</p>;

  // Planning de la semaine : tous les cours de tous les enfants, groupes par jour
  const planning = JOURS.map((jour) => ({
    jour,
    cours: enfants
      .flatMap((e) => e.cours.map((c) => ({ ...c, enfant: e.prenom })))
      .filter((c) => c.jour === jour)
      .sort((a, b) => a.heureDebut.localeCompare(b.heureDebut)),
  })).filter((j) => j.cours.length > 0);

  const solde = Number(moi.soldeRestant ?? 0);

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h1>Bonjour {moi.prenom} 👋</h1>
          <p className="page-subtitle">Voici le résumé de votre espace</p>
        </div>
      </div>

      <div className="stats">
        <div className={`card stat ${solde > 0 ? "stat-accent" : ""}`}>
          <span className="stat-label">Solde restant</span>
          <span className="stat-value">{euro(solde)}</span>
          <Link to="/parent/paiements" className="stat-hint link">
            {solde > 0 ? "Régler maintenant →" : "Voir mes paiements →"}
          </Link>
        </div>
        <Link to="/parent/enfants" className="card stat">
          <span className="stat-label">Enfants</span>
          <span className="stat-value">{enfants.length}</span>
          <span className="stat-hint">Gérer mes enfants →</span>
        </Link>
        <div className="card stat">
          <span className="stat-label">Cours par semaine</span>
          <span className="stat-value">{enfants.reduce((s, e) => s + e.cours.length, 0)}</span>
        </div>
      </div>

      <div className="card">
        <h2>Planning de la semaine</h2>
        {planning.length === 0 ? (
          <p className="muted">
            Aucun cours pour le moment. <Link to="/parent/enfants">Inscrivez vos enfants</Link> à un cours.
          </p>
        ) : (
          <div className="planning">
            {planning.map((j) => (
              <div key={j.jour} className="planning-day">
                <h3>{jourLabel(j.jour)}</h3>
                {j.cours.map((c, i) => (
                  <div key={i} className="planning-item">
                    <span className="planning-time">
                      {heure(c.heureDebut)} – {heure(c.heureFin)}
                    </span>
                    <strong>{c.enfant}</strong>
                    <span className="muted">
                      {c.niveauLibelle || "Cours"}
                      {c.salleNom ? ` · ${c.salleNom}` : ""}
                    </span>
                  </div>
                ))}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
