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

  var textbox: Element = null

  def setupUI(): Unit = {
    setupNextTextBox()

  }

  def setupNextTextBox(): Unit = {
    textbox = document.createElement("textbox")
    textbox.textContent = ""

    document.body.appendChild(textbox)
  }

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
      textbox.textContent = guessBeingEntered
    }
  }

  def deletePressed(): Unit = {
    if(guessBeingEntered.length>0) {
      guessBeingEntered = guessBeingEntered.substring(0, guessBeingEntered.length - 1)
      textbox.textContent = guessBeingEntered
    }
  }

  def enterPressed(): Unit = {
    if(guessBeingEntered.length==5) {
      var result = ""
      guessBeingEntered.zip(word).foreach(letters => {
        if(letters._1==letters._2) { result = result + "Y"}
        else if(word.toList.contains(letters._1)) { result = result + "y"}
        else {result = result + "n"}
      })
      appendPar(document.body, "Guess vs reality: "+result)
      guessBeingEntered = ""

      setupNextTextBox()
    }

  }

}