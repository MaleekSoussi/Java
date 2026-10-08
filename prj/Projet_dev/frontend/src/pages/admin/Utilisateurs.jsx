import CrudPage from "../../components/CrudPage";
import { utilisateursApi } from "../../api";
import { euro, MOYENS_PAIEMENT } from "../../utils";

const load = () => utilisateursApi.list();

const fields = [
  { name: "prenom", label: "Prénom", required: true },
  { name: "nom", label: "Nom", required: true },
  { name: "email", label: "Email", type: "email", required: true },
  {
    name: "motDePasse",
    label: "Mot de passe",
    type: "password",
    requiredOnCreate: true,
    minLength: 8,
    help: "Laisser vide pour ne pas le changer",
    helpOnEditOnly: true,
  },
  {
    name: "role",
    label: "Rôle",
    type: "select",
    required: true,
    default: "PARENT",
    options: [
      { value: "PARENT", label: "Parent" },
      { value: "ADMIN", label: "Admin" },
    ],
  },
  { name: "modePaiement", label: "Mode de paiement", type: "select", options: MOYENS_PAIEMENT },
  { name: "nombrePaiements", label: "Nombre de paiements", type: "number", min: 1 },
];

const columns = [
  { label: "Nom", render: (u) => <strong>{u.prenom} {u.nom}</strong> },
  { label: "Email", render: (u) => u.email },
  {
    label: "Rôle",
    render: (u) => <span className={`badge ${u.role === "ADMIN" ? "badge-amber" : "badge-blue"}`}>{u.role}</span>,
  },
  { label: "Paiement", render: (u) => (u.modePaiement ? `${u.modePaiement}${u.nombrePaiements ? ` ×${u.nombrePaiements}` : ""}` : "—") },
  {
    label: "Solde restant",
    render: (u) =>
      u.role === "ADMIN" ? "—" : (
        <span className={Number(u.soldeRestant) > 0 ? "text-danger" : "text-success"}>{euro(u.soldeRestant)}</span>
      ),
  },
];

export default function Utilisateurs() {
  return (
    <CrudPage
      title="Utilisateurs"
      subtitle="Parents et administrateurs"
      load={load}
      create={utilisateursApi.create}
      update={utilisateursApi.update}
      remove={utilisateursApi.remove}
      fields={fields}
      columns={columns}
      addLabel="Nouvel utilisateur"
      deleteMessage={(u) => `Supprimer ${u.prenom} ${u.nom} ? Ses enfants et paiements seront aussi supprimés.`}
    />
  );
}
