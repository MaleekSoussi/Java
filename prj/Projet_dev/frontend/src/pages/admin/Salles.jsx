import CrudPage from "../../components/CrudPage";
import { sallesApi } from "../../api";

const load = () => sallesApi.list();

const fields = [
  { name: "nom", label: "Nom", required: true, placeholder: "Ex : Salle A" },
  { name: "capacite", label: "Capacité (enfants)", type: "number", min: 1, required: true },
];

const columns = [
  { label: "Nom", render: (s) => <strong>{s.nom}</strong> },
  { label: "Capacité", render: (s) => `${s.capacite} enfants` },
];

export default function Salles() {
  return (
    <CrudPage
      title="Salles"
      load={load}
      create={sallesApi.create}
      update={sallesApi.update}
      remove={sallesApi.remove}
      fields={fields}
      columns={columns}
      addLabel="Nouvelle salle"
      deleteMessage={(s) => `Supprimer ${s.nom} ? Les cours de cette salle n'auront plus de salle.`}
    />
  );
}
