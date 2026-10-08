import { useEffect, useState } from "react";
import { coursApi, enfantsApi } from "../api";
import { creneau, euro } from "../utils";

// Liste des cours d'un enfant + inscription / desinscription
export default function InscriptionsEnfant({ enfant, onChange }) {
  const [cours, setCours] = useState([]);
  const [choix, setChoix] = useState("");
  const [erreur, setErreur] = useState("");
  const [occupe, setOccupe] = useState(false);

  async function chargerCours() {
    try {
      setCours(await coursApi.list());
    } catch (e) {
      setErreur(e.message);
    }
  }

  useEffect(() => {
    chargerCours();
  }, []);

  const inscrits = new Set(enfant.cours.map((c) => c.id));
  const disponibles = cours.filter(
    (c) =>
      !inscrits.has(c.id) &&
      c.placesDisponibles > 0 &&
      (!enfant.niveauId || !c.niveauId || c.niveauId === enfant.niveauId)
  );

  async function action(fn) {
    setErreur("");
    setOccupe(true);
    try {
      await fn();
      setChoix("");
      await Promise.all([onChange(), chargerCours()]);
    } catch (e) {
      setErreur(e.message);
    } finally {
      setOccupe(false);
    }
  }

  return (
    <div className="detail-panel">
      {erreur && <div className="alert alert-error">{erreur}</div>}

      <h3>Cours de {enfant.prenom}</h3>
      {enfant.cours.length === 0 ? (
        <p className="muted">Aucun cours pour le moment.</p>
      ) : (
        <ul className="chip-list">
          {enfant.cours.map((c) => (
            <li key={c.id} className="chip">
              <span>
                <strong>{creneau(c)}</strong>
                {c.niveauLibelle && ` · ${c.niveauLibelle}`}
                {c.salleNom && ` · ${c.salleNom}`}
                {c.montant != null && ` · ${euro(c.montant)}`}
              </span>
              <button
                className="chip-remove"
                disabled={occupe}
                title="Désinscrire"
                onClick={() => {
                  if (window.confirm(`Désinscrire ${enfant.prenom} de ce cours ?`)) {
                    action(() => enfantsApi.desinscrire(enfant.id, c.id));
                  }
                }}
              >
                ✕
              </button>
            </li>
          ))}
        </ul>
      )}

      <div className="inline-form">
        <select value={choix} onChange={(e) => setChoix(e.target.value)}>
          <option value="">
            {disponibles.length ? "— Choisir un cours —" : "Aucun cours disponible pour ce niveau"}
          </option>
          {disponibles.map((c) => (
            <option key={c.id} value={c.id}>
              {creneau(c)} · {c.niveauLibelle || "Tous niveaux"} · {c.placesDisponibles} place(s)
              {c.montant != null ? ` · ${euro(c.montant)}` : ""}
            </option>
          ))}
        </select>
        <button
          className="btn btn-primary"
          disabled={!choix || occupe}
          onClick={() => action(() => enfantsApi.inscrire(enfant.id, choix))}
        >
          Inscrire
        </button>
      </div>
    </div>
  );
}
