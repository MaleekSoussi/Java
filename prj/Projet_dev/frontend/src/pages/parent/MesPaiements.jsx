import { useEffect, useState } from "react";
import { auth, mouvementsApi, updateSessionUser } from "../../api";
import { dateFr, euro, MOYENS_PAIEMENT } from "../../utils";

export default function MesPaiements() {
  const [moi, setMoi] = useState(null);
  const [paiements, setPaiements] = useState([]);
  const [form, setForm] = useState({ montant: "", moyenPaiement: "Carte bancaire" });
  const [erreur, setErreur] = useState("");
  const [succes, setSucces] = useState("");
  const [envoi, setEnvoi] = useState(false);

  async function charger() {
    try {
      const [me, liste] = await Promise.all([auth.me(), mouvementsApi.list()]);
      setMoi(me);
      updateSessionUser(me);
      setPaiements(liste);
    } catch (e) {
      setErreur(e.message);
    }
  }

  useEffect(() => {
    charger();
  }, []);

  async function payer(e) {
    e.preventDefault();
    setErreur("");
    setSucces("");
    setEnvoi(true);
    try {
      await mouvementsApi.create({ montant: Number(form.montant), moyenPaiement: form.moyenPaiement });
      setSucces(`Paiement de ${euro(form.montant)} enregistré.`);
      setForm({ ...form, montant: "" });
      await charger();
    } catch (err) {
      setErreur(err.message);
    } finally {
      setEnvoi(false);
    }
  }

  if (!moi) return erreur ? <div className="alert alert-error">{erreur}</div> : <p className="muted">Chargement...</p>;

  const solde = Number(moi.soldeRestant ?? 0);
  const totalPaye = paiements.reduce((s, m) => s + Number(m.montant), 0);

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h1>Mes paiements</h1>
          <p className="page-subtitle">Le solde est calculé à partir des cours suivis par vos enfants</p>
        </div>
      </div>

      <div className="stats">
        <div className={`card stat ${solde > 0 ? "stat-accent" : ""}`}>
          <span className="stat-label">Solde restant</span>
          <span className="stat-value">{euro(solde)}</span>
        </div>
        <div className="card stat">
          <span className="stat-label">Total payé</span>
          <span className="stat-value">{euro(totalPaye)}</span>
        </div>
      </div>

      <div className="grid-2">
        <form className="card" onSubmit={payer}>
          <h2>Effectuer un paiement</h2>
          {erreur && <div className="alert alert-error">{erreur}</div>}
          {succes && <div className="alert alert-success">{succes}</div>}

          <label className="field">
            <span>Montant (€)</span>
            <input
              type="number"
              min="0.01"
              step="0.01"
              value={form.montant}
              onChange={(e) => setForm({ ...form, montant: e.target.value })}
              placeholder={solde > 0 ? solde.toFixed(2) : "0.00"}
              required
            />
          </label>
          {solde > 0 && (
            <button
              type="button"
              className="btn btn-link"
              onClick={() => setForm({ ...form, montant: solde.toFixed(2) })}
            >
              Payer la totalité ({euro(solde)})
            </button>
          )}

          <label className="field">
            <span>Moyen de paiement</span>
            <select value={form.moyenPaiement} onChange={(e) => setForm({ ...form, moyenPaiement: e.target.value })}>
              {MOYENS_PAIEMENT.map((m) => (
                <option key={m.value} value={m.value}>
                  {m.label}
                </option>
              ))}
            </select>
          </label>

          <button type="submit" className="btn btn-primary btn-block" disabled={envoi}>
            {envoi ? "Enregistrement..." : "Payer"}
          </button>
        </form>

        <div className="card">
          <h2>Historique</h2>
          {paiements.length === 0 ? (
            <p className="muted">Aucun paiement pour le moment.</p>
          ) : (
            <ul className="list">
              {paiements.map((m) => (
                <li key={m.id}>
                  <span>
                    {dateFr(m.date)} · {m.moyenPaiement}
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
