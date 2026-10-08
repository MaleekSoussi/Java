import { useEffect, useState } from "react";
import CrudPage from "../../components/CrudPage";
import InscriptionsEnfant from "../../components/InscriptionsEnfant";
import { enfantsApi, niveauxApi, utilisateursApi } from "../../api";
import { dateFr } from "../../utils";

const load = () => enfantsApi.list();

const columns = [
  { label: "Enfant", render: (e) => <strong>{e.prenom} {e.nom}</strong> },
  { label: "Parent", render: (e) => e.parentNom },
  { label: "Naissance", render: (e) => dateFr(e.dateNaissance) || "—" },
  { label: "Niveau", render: (e) => e.niveauLibelle || <span className="muted">—</span> },
  { label: "Cours", render: (e) => <span className="badge badge-blue">{e.cours.length}</span> },
];

export default function Enfants() {
  const [parents, setParents] = useState([]);
  const [niveaux, setNiveaux] = useState([]);

  useEffect(() => {
    utilisateursApi.list("PARENT").then(setParents).catch(() => {});
    niveauxApi.list().then(setNiveaux).catch(() => {});
  }, []);

  const fields = [
    { name: "prenom", label: "Prénom", required: true },
    { name: "nom", label: "Nom", required: true },
    { name: "dateNaissance", label: "Date de naissance", type: "date" },
    { name: "etablissement", label: "Établissement" },
    {
      name: "niveauId",
      label: "Niveau",
      type: "select",
      numeric: true,
      options: niveaux.map((n) => ({ value: n.id, label: n.libelle })),
    },
    {
      name: "utilisateurId",
      label: "Parent",
      type: "select",
      numeric: true,
      required: true,
      options: parents.map((p) => ({ value: p.id, label: `${p.prenom} ${p.nom} (${p.email})` })),
    },
  ];

  const toForm = (e) => ({
    prenom: e.prenom,
    nom: e.nom,
    dateNaissance: e.dateNaissance ?? "",
    etablissement: e.etablissement ?? "",
    niveauId: e.niveauId ?? "",
    utilisateurId: e.utilisateurId ?? "",
  });

  return (
    <CrudPage
      title="Enfants"
      subtitle="Tous les enfants inscrits à l'école"
      load={load}
      create={enfantsApi.create}
      update={enfantsApi.update}
      remove={enfantsApi.remove}
      fields={fields}
      columns={columns}
      toForm={toForm}
      addLabel="Nouvel enfant"
      deleteMessage={(e) => `Supprimer ${e.prenom} ${e.nom} ? Il/elle sera désinscrit(e) de tous ses cours.`}
      detail={{
        label: "Cours",
        render: (e, reload) => <InscriptionsEnfant key={e.id} enfant={e} onChange={reload} />,
      }}
    />
  );
}
