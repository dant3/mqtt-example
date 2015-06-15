package com.github.dant3.mqttexample.utils

import scala.util.Random

object NamesGenerator {
  private val vocals = Vector("a", "e", "i", "o", "u", "ei", "ai", "ou", "j", "ji", "y", "oi", "au", "oo")
  private val startConsonants = Vector("b", "c", "d", "f", "g", "h", "k",
    "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "z",
    "ch", "bl", "br", "fl", "gl", "gr", "kl", "pr", "st", "sh", "th")
  private val endConsonants = Vector("b", "d", "f", "g", "h", "k", "l", "m",
    "n", "p", "r", "s", "t", "v", "w", "z", "ch", "gh", "nn", "st",
    "sh", "th", "tt", "ss", "pf", "nt")
  private val nameInstructions = Vector("vd", "cvdvd", "cvd", "vdvd", "cvdvddvdv", "vddvdvdv", "cdvdv")

  def getName: String = firstCharUppercase(getNameByInstructions(getRandomElementFrom(nameInstructions)))

  private def randomInt(min: Int, max: Int): Int = Random.nextInt(max - min) + min

  private def getNameByInstructions(nameInstructions: String): String = {
    val letters: Seq[String] = for {
      i <- 0 to nameInstructions.length - 1
    } yield nameInstructions.charAt(i) match {
      case 'v' => getRandomElementFrom(vocals)
      case 'c' => getRandomElementFrom(startConsonants)
      case 'd' => getRandomElementFrom(endConsonants)
    }
    letters.mkString
  }

  private def firstCharUppercase(name: String): String = Character.toString(name.charAt(0)).toUpperCase + name.substring(1)
  private def getRandomElementFrom(seq:Seq[String]):String = seq(randomInt(0, seq.size - 1))
}
