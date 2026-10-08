import { useCallback, useEffect, useState } from "react";
import CrudPage from "../../components/CrudPage";
import { mouvementsApi, utilisateursApi } from "../../api";
import { dateFr, euro, MOYENS_PAIEMENT } from "../../utils";

const columns = [
  { label: "Date", render: (m) => dateFr(m.date) },
  { label: "Parent", render: (m) => <strong>{m.utilisateurNom}</strong> },
  { label: "Montant", render: (m) => <span className="text-success">{euro(m.montant)}</span> },
  { label: "Moyen", render: (m) => m.moyenPaiement || "—" },
];

export default function Paiements() {
  const [parents, setParents] = useState([]);
  const [total, setTotal] = useState(0);

  useEffect(() => {
    utilisateursApi.list("PARENT").then(setParents).catch(() => {});
  }, []);

  // On calcule le total a chaque rechargement de la liste
  const load = useCallback(async () => {
    const liste = await mouvementsApi.list();
    setTotal(liste.reduce((s, m) => s + Number(m.montant), 0));
    return liste;
  }, []);

  const fields = [
    {
      name: "utilisateurId",
      label: "Parent",
      type: "select",
      numeric: true,
      required: true,
      options: parents.map((p) => ({
        value: p.id,
        label: `${p.prenom} ${p.nom} — reste ${euro(p.soldeRestant)}`,
      })),
    },
    { name: "montant", label: "Montant (€)", type: "number", min: 0.01, step: "0.01", required: true },
    { name: "moyenPaiement", label: "Moyen de paiement", type: "select", required: true, options: MOYENS_PAIEMENT },
    { name: "date", label: "Date", type: "date", default: new Date().toISOString().slice(0, 10) },
  ];

  return (
    <CrudPage
      title="Paiements"
      subtitle="Chaque paiement diminue le solde restant du parent"
      load={load}
      create={async (data) => {
        await mouvementsApi.create(data);
        setParents(await utilisateursApi.list("PARENT"));
      }}
      remove={mouvementsApi.remove}
      fields={fields}
      columns={columns}
      addLabel="Enregistrer un paiement"
      emptyText="Aucun paiement enregistré"
      deleteMessage={(m) => `Supprimer le paiement de ${euro(m.montant)} de ${m.utilisateurNom} ?`}
      toolbar={<span className="pill">Total encaissé : {euro(total)}</span>}
    />
  );
}
