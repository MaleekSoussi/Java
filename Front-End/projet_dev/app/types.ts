export interface Utilisateur {
  id_utilisateur: number
  nom: string
  prenom: string
  email: string
  mot_de_passe: string
  role: string
  mode_paiement: string
  nombre_paiements: number
  solde_restant: number
}

export interface Enfant {
  id_enfant: number
  nom: string
  prenom: string
  date_naissance: string
  etablissement: string
  niveau_etude: string
  id_utilisateur: number
}

export interface Salle {
  id_salle: number
  nom: string
  capacite: number
}

export interface Cours {
  id_cours: number
  jour: string
  heure_debut: string
  heure_fin: string
  place_disponible: number
  niveau: string
  montant: number
  id_salle: number
}

export interface Inscription {
  id_cours: number
  id_enfant: number
  date_inscription: string
}

export interface Mouvement {
  id_mouvement: number
  id_utilisateur: number
  date: string
  montant: number
  moyen_paiement: string
}
