import { Fragment, useCallback, useEffect, useState } from "react";

/**
 * Page generique : tableau + formulaire d'ajout / modification + suppression.
 *
 * fields  : champs du formulaire  { name, label, type, required, options, min, step, placeholder,
 *                                   requiredOnCreate, help }
 * columns : colonnes du tableau   { label, render: (item) => contenu }
 * detail  : ligne depliable       { label, render: (item, reload) => contenu }
 */
export default function CrudPage({
  title,
  subtitle,
  load,
  create,
  update,
  remove,
  fields,
  columns,
  addLabel = "Ajouter",
  emptyText = "Aucun élément pour le moment",
  deleteMessage = () => "Supprimer cet élément ?",
  toForm,
  toPayload,
  detail,
  toolbar,
}) {
  const [items, setItems] = useState([]);
  const [chargement, setChargement] = useState(true);
  const [erreur, setErreur] = useState("");
  const [erreurForm, setErreurForm] = useState("");
  const [form, setForm] = useState(null); // null = formulaire ferme
  const [editId, setEditId] = useState(null); // null = creation
  const [enregistrement, setEnregistrement] = useState(false);
  const [ouvertId, setOuvertId] = useState(null);

  const reload = useCallback(async () => {
    try {
      setItems(await load());
      setErreur("");
    } catch (e) {
      setErreur(e.message);
    } finally {
      setChargement(false);
    }
  }, [load]);

  useEffect(() => {
    reload();
  }, [reload]);

  function formVide() {
    return Object.fromEntries(fields.map((f) => [f.name, f.default ?? ""]));
  }

  function ouvrirCreation() {
    setErreurForm("");
    setEditId(null);
    setForm(formVide());
  }

  function ouvrirModification(item) {
    setErreurForm("");
    setEditId(item.id);
    const valeurs = toForm
      ? toForm(item)
      : Object.fromEntries(fields.map((f) => [f.name, f.type === "password" ? "" : item[f.name] ?? ""]));
    setForm(valeurs);
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  function fermer() {
    setForm(null);
    setEditId(null);
  }

  function changer(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  // Convertit le formulaire en JSON pour l'API : "" -> null, nombres -> Number
  function payloadParDefaut(valeurs) {
    const out = {};
    for (const f of fields) {
      const v = valeurs[f.name];
      if (v === "" || v === undefined) out[f.name] = null;
      else if (f.type === "number" || f.numeric) out[f.name] = Number(v);
      else out[f.name] = v;
    }
    return out;
  }

  async function enregistrer(e) {
    e.preventDefault();
    setErreurForm("");
    setEnregistrement(true);
    try {
      const payload = toPayload ? toPayload(form, editId) : payloadParDefaut(form);
      if (editId) await update(editId, payload);
      else await create(payload);
      fermer();
      await reload();
    } catch (err) {
      setErreurForm(err.message);
    } finally {
      setEnregistrement(false);
    }
  }

  async function supprimer(item) {
    if (!window.confirm(deleteMessage(item))) return;
    try {
      await remove(item.id);
      await reload();
    } catch (err) {
      setErreur(err.message);
    }
  }

  const nbColonnes = columns.length + 1;

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h1>{title}</h1>
          {subtitle && <p className="page-subtitle">{subtitle}</p>}
        </div>
        <div className="page-actions">
          {toolbar}
          {create && !form && (
            <button className="btn btn-primary" onClick={ouvrirCreation}>
              + {addLabel}
            </button>
          )}
        </div>
      </div>

      {erreur && <div className="alert alert-error">{erreur}</div>}

      {form && (
        <form className="card form-card" onSubmit={enregistrer}>
          <h2>{editId ? "Modifier" : addLabel}</h2>
          {erreurForm && <div className="alert alert-error">{erreurForm}</div>}

          <div className="form-grid">
            {fields.map((f) => (
              <Champ
                key={f.name}
                field={f}
                value={form[f.name] ?? ""}
                onChange={changer}
                creation={!editId}
              />
            ))}
          </div>

          <div className="form-actions">
            <button type="button" className="btn btn-ghost" onClick={fermer}>
              Annuler
            </button>
            <button type="submit" className="btn btn-primary" disabled={enregistrement}>
              {enregistrement ? "Enregistrement..." : "Enregistrer"}
            </button>
          </div>
        </form>
      )}

      <div className="card table-card">
        {chargement ? (
          <p className="muted padded">Chargement...</p>
        ) : (
          <table className="table">
            <thead>
              <tr>
                {columns.map((c) => (
                  <th key={c.label}>{c.label}</th>
                ))}
                <th></th>
              </tr>
            </thead>
            <tbody>
              {items.map((item) => (
                <Fragment key={item.id}>
                  <tr className={ouvertId === item.id ? "row-open" : ""}>
                    {columns.map((c) => (
                      <td key={c.label} data-label={c.label}>
                        {c.render(item)}
                      </td>
                    ))}
                    <td className="row-actions">
                      {detail && (
                        <button
                          className="btn btn-small"
                          onClick={() => setOuvertId(ouvertId === item.id ? null : item.id)}
                        >
                          {detail.label} {ouvertId === item.id ? "▴" : "▾"}
                        </button>
                      )}
                      {update && (
                        <button className="btn btn-small" onClick={() => ouvrirModification(item)}>
                          Modifier
                        </button>
                      )}
                      {remove && (
                        <button className="btn btn-small btn-danger" onClick={() => supprimer(item)}>
                          Supprimer
                        </button>
                      )}
                    </td>
                  </tr>
                  {detail && ouvertId === item.id && (
                    <tr className="row-detail">
                      <td colSpan={nbColonnes}>{detail.render(item, reload)}</td>
                    </tr>
                  )}
                </Fragment>
              ))}
              {items.length === 0 && (
                <tr>
                  <td colSpan={nbColonnes} className="empty">
                    {emptyText}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

function Champ({ field: f, value, onChange, creation }) {
  const requis = f.required || (f.requiredOnCreate && creation);
  const commun = { id: f.name, name: f.name, value, onChange, required: requis };

  let input;
  if (f.type === "select") {
    input = (
      <select {...commun}>
        <option value="">{f.placeholder ?? "— Choisir —"}</option>
        {(f.options || []).map((o) => (
          <option key={o.value} value={o.value}>
            {o.label}
          </option>
        ))}
      </select>
    );
  } else {
    input = (
      <input
        {...commun}
        type={f.type || "text"}
        min={f.min}
        max={f.max}
        step={f.step}
        minLength={f.minLength}
        placeholder={f.placeholder}
      />
    );
  }

  return (
    <label className="field" htmlFor={f.name}>
      <span>
        {f.label}
        {requis && <em className="required">*</em>}
      </span>
      {input}
      {f.help && (!f.helpOnEditOnly || !creation) && <small>{f.help}</small>}
    </label>
  );
}
