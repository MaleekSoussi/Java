import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { auth, saveSession } from "../api";

export default function Signup() {
  const [form, setForm] = useState({ nom: "", prenom: "", email: "", motDePasse: "", confirmation: "" });
  const [erreur, setErreur] = useState("");
  const [chargement, setChargement] = useState(false);
  const navigate = useNavigate();

  function changer(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setErreur("");
    if (form.motDePasse !== form.confirmation) {
      setErreur("Les deux mots de passe ne sont pas identiques");
      return;
    }
    setChargement(true);
    try {
      const data = await auth.signup({
        nom: form.nom,
        prenom: form.prenom,
        email: form.email,
        motDePasse: form.motDePasse,
      });
      saveSession(data);
      navigate("/parent");
    } catch (err) {
      setErreur(err.message);
    } finally {
      setChargement(false);
    }
  }

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <div className="auth-logo">✦</div>
        <h1>Créer un compte</h1>
        <p className="auth-subtitle">Inscrivez-vous pour inscrire vos enfants aux cours</p>

        {erreur && <div className="alert alert-error">{erreur}</div>}

        <div className="form-row">
          <label className="field">
            <span>Prénom</span>
            <input name="prenom" value={form.prenom} onChange={changer} required autoFocus />
          </label>
          <label className="field">
            <span>Nom</span>
            <input name="nom" value={form.nom} onChange={changer} required />
          </label>
        </div>

        <label className="field">
          <span>Email</span>
          <input name="email" type="email" value={form.email} onChange={changer} required />
        </label>

        <label className="field">
          <span>Mot de passe</span>
          <input
            name="motDePasse"
            type="password"
            value={form.motDePasse}
            onChange={changer}
            minLength={8}
            placeholder="8 caractères minimum"
            required
          />
        </label>

        <label className="field">
          <span>Confirmer le mot de passe</span>
          <input
            name="confirmation"
            type="password"
            value={form.confirmation}
            onChange={changer}
            minLength={8}
            required
          />
        </label>

        <button type="submit" className="btn btn-primary btn-block" disabled={chargement}>
          {chargement ? "Création..." : "Créer mon compte"}
        </button>

        <p className="auth-switch">
          Déjà inscrit ? <Link to="/login">Se connecter</Link>
        </p>
      </form>
    </div>
  );
}
