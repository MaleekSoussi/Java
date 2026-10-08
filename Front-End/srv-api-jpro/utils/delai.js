export function parseDelaiMs(rawDelay) {
  if (rawDelay === undefined) {
    return 0
  }

  const parsed = Number.parseInt(String(rawDelay ?? ''), 10)

  if (Number.isNaN(parsed) || parsed < 0) {
    return null
  }

  // On limite le délai à 30 secondes
  return Math.min(parsed, 30000)
}

export function wait(ms) {
  return new Promise((resolve) => {
    setTimeout(resolve, ms)
  })
}
