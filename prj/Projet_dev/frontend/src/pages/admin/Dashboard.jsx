import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { coursApi, enfantsApi, mouvementsApi, utilisateursApi } from "../../api";
import { dateFr, euro } from "../../utils";

export default function Dashboard() {
  const [data, setData] = useState(null);
  const [erreur, setErreur] = useState("");

  useEffect(() => {
    Promise.all([utilisateursApi.list("PARENT"), enfantsApi.list(), coursApi.list(), mouvementsApi.list()])
      .then(([parents, enfants, cours, mouvements]) => setData({ parents, enfants, cours, mouvements }))
      .catch((e) => setErreur(e.message));
  }, []);

  if (erreur) return <div className="alert alert-error">{erreur}</div>;
  if (!data) return <p className="muted">Chargement...</p>;

  const totalEncaisse = data.mouvements.reduce((s, m) => s + Number(m.montant), 0);
  const totalRestant = data.parents.reduce((s, p) => s + Math.max(0, Number(p.soldeRestant ?? 0)), 0);
  const placesLibres = data.cours.reduce((s, c) => s + (c.placesDisponibles ?? 0), 0);
  const debiteurs = data.parents.filter((p) => Number(p.soldeRestant) > 0);

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h1>Tableau de bord</h1>
          <p className="page-subtitle">Vue d'ensemble de l'école</p>
        </div>
      </div>

      <div className="stats">
        <Stat label="Parents" value={data.parents.length} to="/admin/utilisateurs" />
        <Stat label="Enfants" value={data.enfants.length} to="/admin/enfants" />
        <Stat label="Cours" value={data.cours.length} hint={`${placesLibres} places libres`} to="/admin/cours" />
        <Stat label="Encaissé" value={euro(totalEncaisse)} to="/admin/paiements" />
        <Stat label="Reste à payer" value={euro(totalRestant)} accent />
      </div>

      <div className="grid-2">
        <div className="card">
          <h2>Soldes à régler</h2>
          {debiteurs.length === 0 ? (
            <p className="muted">Tout est réglé 🎉</p>
          ) : (
            <ul className="list">
              {debiteurs.map((p) => (
                <li key={p.id}>
                  <span>
                    {p.prenom} {p.nom}
                  </span>
                  <strong className="text-danger">{euro(p.soldeRestant)}</strong>
                </li>
              ))}
            </ul>
          )}
        </div>

        <div className="card">
          <h2>Derniers paiements</h2>
          {data.mouvements.length === 0 ? (
            <p className="muted">Aucun paiement enregistré.</p>
          ) : (
            <ul className="list">
              {data.mouvements.slice(0, 6).map((m) => (
                <li key={m.id}>
                  <span>
                    {dateFr(m.date)} · {m.utilisateurNom}
                  </span>
                  <strong className="text-success">{euro(m.montant)}</strong>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
}

function Stat({ label, value, hint, to, accent }) {
  const contenu = (
    <>
      <span className="stat-label">{label}</span>
      <span className="stat-value">{value}</span>
      {hint && <span className="stat-hint">{hint}</span>}
    </>
  );
  const cls = `card stat ${accent ? "stat-accent" : ""}`;
  return to ? (
    <Link to={to} className={cls}>
      {contenu}
    </Link>
  ) : (
    <div className={cls}>{contenu}</div>
  );
}
