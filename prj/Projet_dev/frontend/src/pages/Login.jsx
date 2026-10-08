import { useState } from "react";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { auth, getSessionUser, getToken, saveSession } from "../api";

export default function Login() {
  const [email, setEmail] = useState("");
  const [motDePasse, setMotDePasse] = useState("");
  const [erreur, setErreur] = useState("");
  const [chargement, setChargement] = useState(false);
  const navigate = useNavigate();

  // Deja connecte : on va directement dans son espace
  if (getToken() && getSessionUser()) return <Navigate to="/" replace />;

  async function handleSubmit(e) {
    e.preventDefault();
    setErreur("");
    setChargement(true);
    try {
      const data = await auth.signin(email, motDePasse);
      saveSession(data);
      navigate(data.utilisateur.role === "ADMIN" ? "/admin" : "/parent");
    } catch (err) {
      setErreur(err.status === 401 ? "Email ou mot de passe incorrect" : err.message);
    } finally {
      setChargement(false);
    }
  }

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <div className="auth-logo">✦</div>
        <h1>Connexion</h1>
        <p className="auth-subtitle">Accédez à votre espace</p>

        {erreur && <div className="alert alert-error">{erreur}</div>}

        <label className="field">
          <span>Email</span>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="exemple@mail.fr"
            required
            autoFocus
          />
        </label>

        <label className="field">
          <span>Mot de passe</span>
          <input
            type="password"
            value={motDePasse}
            onChange={(e) => setMotDePasse(e.target.value)}
            placeholder="••••••••"
            required
          />
        </label>

        <button type="submit" className="btn btn-primary btn-block" disabled={chargement}>
          {chargement ? "Connexion..." : "Se connecter"}
        </button>

        <p className="auth-switch">
          Pas encore de compte ? <Link to="/signup">Créer un compte</Link>
        </p>
      </form>
    </div>
  );
}
