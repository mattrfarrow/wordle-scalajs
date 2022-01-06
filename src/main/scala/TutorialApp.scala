

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.{Element, KeyboardEvent}

import scala.collection.mutable
import scala.collection.mutable._
import scala.scalajs.js.timers.setTimeout
import scala.util.Random

object TutorialApp {

  var grid: Element = _
  var tiles: Array[Array[Element]] = _
  var notFoundMsg: Element = _

  val dictionary: Array[String] = Words.words
  val challengeWords: Array[String] = Words.candidateWords

  val keyboardKeys = Seq("qwertyuiop", "asdfghjkl", "zxcvbnm")

  private var wordToGuess = randomWord
  var guessBeingEntered: String = ""
  var roundNum = 0
  private var gameOver = false

  private val keyByLetter = new mutable.HashMap[Char, Element]()

  def main(args: Array[String]): Unit = {
    document.addEventListener("DOMContentLoaded", { (_: dom.Event) => setupUI() })

    document.addEventListener("keypress", { (e: dom.Event) =>
      val k = e.asInstanceOf[KeyboardEvent]
      if(k.key=="Enter") {
        enterPressed()
      } else {
        characterEntered(k.key)
      }
    })

    document.addEventListener("keydown", { (e: dom.Event) =>
      val k = e.asInstanceOf[KeyboardEvent]
      if(k.keyCode==8) {
        deletePressed()
      }
    })
  }

  def setupUI(): Unit = {
    System.out.println(wordToGuess)
    grid = document.getElementById("grid")
    notFoundMsg = document.getElementById("not-found-msg")
    notFoundMsg.classList.add("invisible")

    tiles = createTiles()
    addKeyboard()
    System.out.println(wordToGuess)
  }

  def loadDictionary() = Seq()

  private def createTiles(): Array[Array[Element]] = {
    val localTiles = Array.fill[Array[Element]](6)(Array[Element]())

    for(rowNum <- 0 to 5){
      val rowDiv = document.createElement("div")
      rowDiv.classList.add("row")

      localTiles(rowNum) = makeTileRow
      localTiles(rowNum).foreach(t => rowDiv.appendChild(t))

      grid.appendChild(rowDiv)
    }
    localTiles
  }

  private def makeTileRow = Array(makeTile, makeTile, makeTile, makeTile, makeTile)

  private def makeTile = {
    val el = document.createElement("div")
    el.classList.add("tile")
    el.classList.add("unchecked")
    el
  }

  private def characterEntered(c: String): Unit = {
    if(guessBeingEntered.length<5) {
      guessBeingEntered = guessBeingEntered + c
      updateCurrentTileRow()
    }
  }

  private def deletePressed(): Unit = {
    if(guessBeingEntered.length>0 && !gameOver) {
      guessBeingEntered = guessBeingEntered.substring(0, guessBeingEntered.length - 1)
      updateCurrentTileRow()
    }
  }

  private def enterPressed(): Unit = {
    if(gameOver) {
      beginNextGame()
      return
    }

    if(guessBeingEntered.length==5) {
      if (!dictionary.contains(guessBeingEntered) && !challengeWords.contains(guessBeingEntered)) {
        showMessageBriefly("That word is not in the dictionary.")
        return
      }

      updateTilesAndKeyboardAfterGuessEntered

      if(guessBeingEntered == wordToGuess) {
        showMessage("Nice. Press enter to play again")
        gameOver = true
        return
      }

      if(roundNum==5) {
        showMessage("The word was "  + wordToGuess.toUpperCase + ". Press enter to play again!")
        gameOver = true
      }

      guessBeingEntered = ""
      roundNum = roundNum+1
    }

  }

  private def updateTilesAndKeyboardAfterGuessEntered = {
    for (n <- 0 to 4) {
      val keyboardKey = keyByLetter(guessBeingEntered.charAt(n))
      if (guessBeingEntered.charAt(n) == wordToGuess.charAt(n)) {
        tiles(roundNum)(n).classList.add("correct")
        keyboardKey.classList.remove("wrongpos-key")
        keyboardKey.classList.add("correct-key")
      }
      else if (wordToGuess.toList.contains(guessBeingEntered.charAt(n))) {
        tiles(roundNum)(n).classList.add("wrongpos")
        if (!keyboardKey.classList.contains("correct-key")) {
          keyboardKey.classList.add("wrongpos-key")
        }

      }
      else {
        tiles(roundNum)(n).classList.add("incorrect")
        keyboardKey.classList.add("incorrect-key")
      }

      tiles(roundNum)(n).classList.remove("unchecked")
    }
  }

  private def showMessageBriefly(message: String) = {
    notFoundMsg.textContent=message
    notFoundMsg.classList.remove("invisible")
    setTimeout(1000) {
      notFoundMsg.classList.add("invisible")
    }
  }

  private def showMessage(message: String) = {
    notFoundMsg.textContent=message
    notFoundMsg.classList.remove("invisible")
  }

  private def updateCurrentTileRow(): Unit = {
    for(n <- 0 to 4) {
      tiles(roundNum)(n).textContent= ""
    }
    for(n <- 0 until guessBeingEntered.length) {
      tiles(roundNum)(n).textContent = guessBeingEntered.charAt(n).toString
    }
  }

  private def clearAllRileRows(): Unit = {
    for (round <- 0 to 5) {
      for(n <- 0 to 4) {
        tiles(round)(n).textContent= ""
      }
    }
  }

  private def addKeyboard(): Unit = {
    val keyboard = document.getElementById("keyboard")

    for(keyLine <- 0 to 2) {
      val rowDiv = document.createElement("div")
      rowDiv.classList.add("key-line")

      if(keyLine==1) {
        val spacer = document.createElement("button")
        spacer.classList.add("spacer")
        rowDiv.appendChild(spacer)
      }

      for(key <- keyboardKeys(keyLine).toCharArray) {
        val button = document.createElement("button")
        button.textContent = key.toString
        button.classList.add("key")
        button.addEventListener("click", { (e: dom.MouseEvent) =>
          characterEntered(key.toString)
        })
        keyByLetter.put(key, button)
        rowDiv.appendChild(button)
      }

      if(keyLine==2) {
        val button = document.createElement("button")
        button.textContent = "Enter"
        button.classList.add("widekey")
        button.addEventListener("click", { (e: dom.MouseEvent) =>
          enterPressed()
        })
        rowDiv.appendChild(button)
      }
      if(keyLine==2) {
        val button = document.createElement("button")
        button.textContent = "DEL"
        button.classList.add("widekey")
        button.addEventListener("click", { (e: dom.MouseEvent) =>
          deletePressed()
        })
        rowDiv.appendChild(button)
      }

      keyboard.appendChild(rowDiv)
    }

  }

  private def resetTilesStyling() = {
    for(round <- 0 to 5) {
      for(n <- 0 to 4) {
        val tile = tiles(round)(n)
        tile.classList.remove("correct")
        tile.classList.remove("incorrect")
        tile.classList.remove("wrongpos")
        tile.classList.add("unchecked")
      }
    }
  }

  private def resetKeyboardStyling(): Unit = {
    for(button <- keyByLetter.valuesIterator) {
      button.classList.remove("correct-key")
      button.classList.remove("wrongpos-key")
      button.classList.remove("incorrect-key")
    }
  }

  private def beginNextGame(): Unit = {
    wordToGuess = randomWord
    guessBeingEntered = ""
    gameOver = false
    roundNum = 0
    notFoundMsg.classList.add("invisible")
    clearAllRileRows()
    resetKeyboardStyling()
    resetTilesStyling()
  }

  private def randomWord = {
    challengeWords(new Random().nextInt(challengeWords.length))
  }
}