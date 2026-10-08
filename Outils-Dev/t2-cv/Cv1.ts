interface Diplome {
  grade: string
  annee: number
}

console.log('=======================')

// JOHN

let majeur: boolean

const monNom: string = 'John Smith'
const monAge: number = 35 
const mesEnfants: string[] = ['Tom', 'Ida', 'Zoé']

const monDiplome: Diplome = {
  grade: 'Master',
  annee: 2014,
}

majeur = estMajeur(monNom, monAge)

afficher1(monNom, monAge, mesEnfants)

afficher2(monDiplome)

// LINDA

let majeur2: boolean

const sonNom = 'Linda Gray'
const sonAge = 31
const sesEnfants = ['Eva', 'Jim']

const sonDiplome: Diplome = {
  grade: 'Licence',
  annee: 2016,
}

majeur2 = estMajeur(sonNom, sonAge)

afficher1(sonNom, sonAge, sesEnfants)

afficher2(sonDiplome)

// GUS

let majeur3: boolean

const unNom = 'Gus Wilson'
const unAge = 1

const desEnfants = [] as string[]

const unDiplome = null

majeur3 = estMajeur(unNom, unAge)

afficher1(unNom, unAge, desEnfants)

afficher2(unDiplome)

console.log('=======================')

// FONCTIONS

function estMajeur(nom: string, age: number) {
  const ok: boolean = age >= 18

  console.log(nom, 'est', ok ? 'majeur' : 'mineur')

  return ok
}

function afficher1(nom: string, age: number, enfants: string[]) {
  console.log(nom)
  console.log(age, 'ans')
  console.log(enfants.length, 'enfants')
}

function afficher2(diplome: null | Diplome) {
  if (diplome) {
    console.log(diplome.grade, 'en', diplome.annee)
  }
}
