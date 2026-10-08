'use strict'

import { afficher, Eleve } from './moyenne.js'
import figlet from 'figlet'
import chalk from 'chalk'

const titre = figlet.textSync('Groupe 1')
console.log(chalk.red(titre))
const eleves = [
  new Eleve('Aline', [15, 13, 11]),
  new Eleve('Brice', [13, 14, 18]),
  new Eleve('Cathy', [11, 10, 6]),
  new Eleve('Diego', [10, 10, 13]),
]

afficher(eleves)


