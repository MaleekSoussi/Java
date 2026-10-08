'use strict'

export function Eleve(prenom, notes) {
  this.prenom = prenom
  this.notes = notes
}

export function getMoyenne(notes) {
  let somme = 0
  for (const note of notes) {
    somme += note
  }
  return somme / notes.length
}

export function afficher(eleves) {
  for (const item of eleves) {
    item.moyenne = getMoyenne(item.notes)
  }

  for (const item of eleves) {
    console.log(item.prenom, item.moyenne)
  }
}