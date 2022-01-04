import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.{Element, KeyboardEvent}

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

  var grid: Element = null

  var words = List("calls", "round", "hound", "evils", "badge", "reset", "nutty", "whose")
  var word: String = words(new Random().nextInt(words.length))
  var guessBeingEntered = ""


  val dictionary = loadWords()

  var textboxes: Array[Element] = makeTextBoxes

  def setupUI(): Unit = {
    grid = document.getElementById("grid")

    createNextTextBoxRow()
  }

  def loadWords() = Seq()

  private def createNextTextBoxRow(): Unit = {
    val rowDiv = document.createElement("div")
    rowDiv.classList.add("row")

    val el = document.createElement("div")
    el.classList.add("tile")
    textboxes = makeTextBoxes

    textboxes.foreach(t => rowDiv.appendChild(t))

    grid.appendChild(rowDiv)
  }

  private def makeTextBox = {
    val el = document.createElement("div")
    el.classList.add("tile")
    el.classList.add("unchecked")
    el
  }

  private def makeTextBoxes = Array(makeTextBox, makeTextBox, makeTextBox, makeTextBox, makeTextBox)

  private def characterEntered(c: String): Unit = {
    if(guessBeingEntered.length<5) {
      guessBeingEntered = guessBeingEntered + c
      updateTextBoxes
    }
  }

  private def deletePressed(): Unit = {
    if(guessBeingEntered.length>0) {
      guessBeingEntered = guessBeingEntered.substring(0, guessBeingEntered.length - 1)
      updateTextBoxes
    }
  }

  private def updateTextBoxes = {
    for(n <- 0 to 4) {
      textboxes(n).textContent= ""
    }
    for(n <- 0 to guessBeingEntered.length-1) {
      textboxes(n).textContent = guessBeingEntered.charAt(n).toUpper+""
    }
  }

  private def enterPressed(): Unit = {

    if(guessBeingEntered.length==5) {
      for(n <- 0 to 4) {
        if(guessBeingEntered.charAt(n)==word.charAt(n)) { textboxes(n).classList.add("correct")}
        else if(word.toList.contains(guessBeingEntered.charAt(n))) { textboxes(n).classList.add("wrongpos")}
        else { textboxes(n).classList.add("incorrect")}

        textboxes(n).classList.remove("unchecked")
      }

      guessBeingEntered = ""
      createNextTextBoxRow()
    }
  }

}