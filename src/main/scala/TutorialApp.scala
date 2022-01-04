

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.{Element, KeyboardEvent}
import scala.scalajs.js.timers.setTimeout
import scala.util.Random

object TutorialApp {

  def main(args: Array[String]): Unit = {
    document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
      setupUI()
    })

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

  var grid: Element = _
  var tiles: Array[Array[Element]] = _
  var notFoundMsg: Element = _

  val dictionary: Array[String] = Words.words
  val challengeWords = Words.candidateWords

  val keyboardKeys = Seq("qwertyuiop", "asdfghjkl", "zxcvbnm")

  var word: String = challengeWords(new Random().nextInt(challengeWords.length))
  var guessBeingEntered: String = ""
  var roundNum = 0

  def setupUI(): Unit = {
    System.out.println(word)
    grid = document.getElementById("grid")
    notFoundMsg = document.getElementById("not-found-msg")
    notFoundMsg.classList.add("invisible")

    tiles = createTiles()
    addKeyboard()
    System.out.println(word)
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
      updateTiles()
    }
  }

  private def deletePressed(): Unit = {
    if(guessBeingEntered.length>0) {
      guessBeingEntered = guessBeingEntered.substring(0, guessBeingEntered.length - 1)
      updateTiles()
    }
  }

  private def enterPressed(): Unit = {

    if(guessBeingEntered.length==5) {
      if (!dictionary.contains(guessBeingEntered) && !challengeWords.contains(guessBeingEntered)) {
        notFoundMsg.classList.remove("invisible")
        setTimeout(1000) {
          notFoundMsg.classList.add("invisible")
        }
        return
      }
      for(n <- 0 to 4) {
        if(guessBeingEntered.charAt(n)==word.charAt(n)) { tiles(roundNum)(n).classList.add("correct")}
        else if(word.toList.contains(guessBeingEntered.charAt(n))) { tiles(roundNum)(n).classList.add("wrongpos")}
        else { tiles(roundNum)(n).classList.add("incorrect")}

        tiles(roundNum)(n).classList.remove("unchecked")
      }

      guessBeingEntered = ""
      roundNum = roundNum+1
    }
  }

  private def updateTiles(): Unit = {
    for(n <- 0 to 4) {
      tiles(roundNum)(n).textContent= ""
    }
    for(n <- 0 until guessBeingEntered.length) {
      tiles(roundNum)(n).textContent = guessBeingEntered.charAt(n).toString
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

}