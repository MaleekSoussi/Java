import { useEffect, useState } from "react";
import CrudPage from "../../components/CrudPage";
import { coursApi, niveauxApi, sallesApi } from "../../api";
import { creneau, euro, JOURS_OPTIONS } from "../../utils";

const load = () => coursApi.list();

const columns = [
  { label: "Créneau", render: (c) => <strong>{creneau(c)}</strong> },
  {
    label: "Niveau",
    render: (c) => (c.niveauLibelle ? `${c.niveauLibelle} · ${euro(c.montant)}` : <span className="muted">—</span>),
  },
  { label: "Salle", render: (c) => c.salleNom || <span className="muted">—</span> },
  {
    label: "Places",
    render: (c) => (
      <span className={`badge ${c.placesDisponibles > 0 ? "badge-green" : "badge-red"}`}>
        {c.placesDisponibles > 0 ? `${c.placesDisponibles} libre(s)` : "Complet"}
      </span>
    ),
  },
  { label: "Inscrits", render: (c) => c.nombreInscrits },
];

export default function Cours() {
  const [salles, setSalles] = useState([]);
  const [niveaux, setNiveaux] = useState([]);

  useEffect(() => {
    sallesApi.list().then(setSalles).catch(() => {});
    niveauxApi.list().then(setNiveaux).catch(() => {});
  }, []);

  const fields = [
    { name: "jour", label: "Jour", type: "select", required: true, options: JOURS_OPTIONS },
    { name: "heureDebut", label: "Heure de début", type: "time", required: true },
    { name: "heureFin", label: "Heure de fin", type: "time", required: true },
    { name: "placesDisponibles", label: "Places disponibles", type: "number", min: 0, required: true },
    {
      name: "niveauId",
      label: "Niveau",
      type: "select",
      numeric: true,
      options: niveaux.map((n) => ({ value: n.id, label: `${n.libelle} (${euro(n.montant)})` })),
    },
    {
      name: "salleId",
      label: "Salle",
      type: "select",
      numeric: true,
      options: salles.map((s) => ({ value: s.id, label: `${s.nom} (${s.capacite} places)` })),
    },
  ];

  const toForm = (c) => ({
    jour: c.jour,
    heureDebut: c.heureDebut?.slice(0, 5) ?? "",
    heureFin: c.heureFin?.slice(0, 5) ?? "",
    placesDisponibles: c.placesDisponibles ?? "",
    niveauId: c.niveauId ?? "",
    salleId: c.salleId ?? "",
  });

  return (
    <CrudPage
      title="Cours"
      subtitle="Planning de la semaine"
      load={load}
      create={coursApi.create}
      update={coursApi.update}
      remove={coursApi.remove}
      fields={fields}
      columns={columns}
      toForm={toForm}
      addLabel="Nouveau cours"
      deleteMessage={(c) => `Supprimer le cours du ${creneau(c)} ? Les ${c.nombreInscrits} enfant(s) inscrit(s) seront désinscrits.`}
      detail={{ label: "Inscrits", render: (c) => <Inscrits key={c.id + "-" + c.nombreInscrits} cours={c} /> }}
    />
  );
}

function Inscrits({ cours }) {
  const [liste, setListe] = useState(null);
  const [erreur, setErreur] = useState("");

  useEffect(() => {
    coursApi.inscrits(cours.id).then(setListe).catch((e) => setErreur(e.message));
  }, [cours.id]);

  if (erreur) return <div className="alert alert-error">{erreur}</div>;
  if (!liste) return <p className="muted">Chargement...</p>;

  return (
    <div className="detail-panel">
      <h3>Enfants inscrits ({liste.length})</h3>
      {liste.length === 0 ? (
        <p className="muted">Aucun inscrit.</p>
      ) : (
        <ul className="chip-list">
          {liste.map((e) => (
            <li key={e.id} className="chip">
              <span>
                <strong>
                  {e.prenom} {e.nom}
                </strong>{" "}
                · parent : {e.parentNom}
              </span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
