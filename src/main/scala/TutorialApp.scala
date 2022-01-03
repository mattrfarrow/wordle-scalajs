import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.{Element, KeyboardEvent}

import scala.scalajs.js.annotation.JSExportTopLevel
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

  val dictionary = loadWords()

  var textboxes: Array[Element] = makeTextBoxes

  def setupUI(): Unit = {
    setupNextTextBox()
  }

  def loadWords() = Seq()

  def setupNextTextBox(): Unit = {
    textboxes = makeTextBoxes

    textboxes.foreach(t => document.body.appendChild(t))
  }

  def makeTextBox = document.createElement("textbox")
  def makeTextBoxes = Array(makeTextBox, makeTextBox, makeTextBox, makeTextBox, makeTextBox)

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    parNode.textContent = text
    targetNode.appendChild(parNode)
  }

  var guessBeingEntered = ""

  var words = List("calls", "round", "hound", "evils", "badge", "reset", "nutty", "whose")
  var word: String = words(new Random().nextInt(words.length))

  def characterEntered(c: String): Unit = {
    if(guessBeingEntered.length<5) {
      guessBeingEntered = guessBeingEntered + c
      updateTextBoxes
    }
  }

  def deletePressed(): Unit = {
    if(guessBeingEntered.length>0) {
      guessBeingEntered = guessBeingEntered.substring(0, guessBeingEntered.length - 1)
      updateTextBoxes
    }
  }

  def updateTextBoxes = {
    for(n <- 0 to 4) {
      textboxes(n).textContent= " "
    }
    for(n <- 0 to guessBeingEntered.length-1) {
      textboxes(n).textContent = guessBeingEntered.charAt(n)+""
    }
  }

  def enterPressed(): Unit = {
    if(guessBeingEntered.length==5) {

      for(n <- 0 to 4) {
        textboxes(n).classList.add("buttocks")
        if(guessBeingEntered.charAt(n)==word.charAt(n)) { textboxes(n).classList.add("green")}
        else if(word.toList.contains(guessBeingEntered.charAt(n))) { textboxes(n).classList.add("orange")}
      }

      appendPar(document.body, "")
      guessBeingEntered = ""
      setupNextTextBox()
    }

  }

}