import CrudPage from "../../components/CrudPage";
import { niveauxApi } from "../../api";
import { euro } from "../../utils";

const load = () => niveauxApi.list();

const fields = [
  { name: "libelle", label: "Libellé", required: true, placeholder: "Ex : Débutant" },
  { name: "montant", label: "Prix d'un cours (€)", type: "number", min: 0, step: "0.01", required: true },
];

const columns = [
  { label: "Libellé", render: (n) => <strong>{n.libelle}</strong> },
  { label: "Prix d'un cours", render: (n) => euro(n.montant) },
];

export default function Niveaux() {
  return (
    <CrudPage
      title="Niveaux"
      subtitle="Le prix d'un cours dépend de son niveau"
      load={load}
      create={niveauxApi.create}
      update={niveauxApi.update}
      remove={niveauxApi.remove}
      fields={fields}
      columns={columns}
      addLabel="Nouveau niveau"
      deleteMessage={(n) => `Supprimer le niveau « ${n.libelle} » ? Les cours et enfants de ce niveau n'auront plus de niveau.`}
    />
  );
}
