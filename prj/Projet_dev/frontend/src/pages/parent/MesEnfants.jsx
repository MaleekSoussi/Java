import { useEffect, useState } from "react";
import CrudPage from "../../components/CrudPage";
import InscriptionsEnfant from "../../components/InscriptionsEnfant";
import { enfantsApi, niveauxApi } from "../../api";
import { dateFr } from "../../utils";

const load = () => enfantsApi.list();

const columns = [
  { label: "Enfant", render: (e) => <strong>{e.prenom} {e.nom}</strong> },
  { label: "Naissance", render: (e) => dateFr(e.dateNaissance) || "—" },
  { label: "Établissement", render: (e) => e.etablissement || "—" },
  { label: "Niveau", render: (e) => e.niveauLibelle || <span className="muted">Non défini</span> },
  { label: "Cours", render: (e) => <span className="badge badge-blue">{e.cours.length}</span> },
];

export default function MesEnfants() {
  const [niveaux, setNiveaux] = useState([]);

  useEffect(() => {
    niveauxApi.list().then(setNiveaux).catch(() => {});
  }, []);

  const fields = [
    { name: "prenom", label: "Prénom", required: true },
    { name: "nom", label: "Nom", required: true },
    { name: "dateNaissance", label: "Date de naissance", type: "date" },
    { name: "etablissement", label: "Établissement scolaire" },
    {
      name: "niveauId",
      label: "Niveau",
      type: "select",
      numeric: true,
      options: niveaux.map((n) => ({ value: n.id, label: n.libelle })),
    },
  ];

  const toForm = (e) => ({
    prenom: e.prenom,
    nom: e.nom,
    dateNaissance: e.dateNaissance ?? "",
    etablissement: e.etablissement ?? "",
    niveauId: e.niveauId ?? "",
  });

  return (
    <CrudPage
      title="Mes enfants"
      subtitle="Cliquez sur « Cours » pour inscrire un enfant à un cours"
      load={load}
      create={enfantsApi.create}
      update={enfantsApi.update}
      remove={enfantsApi.remove}
      fields={fields}
      columns={columns}
      toForm={toForm}
      addLabel="Ajouter un enfant"
      emptyText="Vous n'avez pas encore ajouté d'enfant"
      deleteMessage={(e) => `Supprimer ${e.prenom} ? Il/elle sera désinscrit(e) de tous ses cours.`}
      detail={{
        label: "Cours",
        render: (e, reload) => <InscriptionsEnfant key={e.id} enfant={e} onChange={reload} />,
      }}
    />
  );
}
