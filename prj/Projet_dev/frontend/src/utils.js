export const JOURS = ["LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI", "DIMANCHE"];

export const JOURS_OPTIONS = JOURS.map((j) => ({ value: j, label: jourLabel(j) }));

export const MOYENS_PAIEMENT = ["Espèces", "Chèque", "Virement", "Carte bancaire"].map((m) => ({
  value: m,
  label: m,
}));

export function jourLabel(jour) {
  if (!jour) return "";
  return jour.charAt(0) + jour.slice(1).toLowerCase();
}

// "14:00:00" -> "14h00"
export function heure(h) {
  if (!h) return "";
  const [hh, mm] = h.split(":");
  return `${hh}h${mm}`;
}

export function creneau(c) {
  return `${jourLabel(c.jour)} ${heure(c.heureDebut)} – ${heure(c.heureFin)}`;
}

export function euro(n) {
  const v = Number(n ?? 0);
  return v.toLocaleString("fr-FR", { style: "currency", currency: "EUR" });
}

export function dateFr(d) {
  if (!d) return "";
  const [y, m, j] = d.split("-");
  return `${j}/${m}/${y}`;
}
