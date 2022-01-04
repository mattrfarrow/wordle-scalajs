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

  var words: Seq[String] = List("calls", "round", "hound", "evils", "badge", "reset", "nutty", "whose")
  var word: String = words(new Random().nextInt(words.length))
  var guessBeingEntered: String = ""


  val dictionary = loadWords()

  var tiles: Array[Element] = makeTiles

  def setupUI(): Unit = {
    grid = document.getElementById("grid")

    createNextTileRow()
  }

  def loadWords() = Seq()

  private def createNextTileRow(): Unit = {
    val rowDiv = document.createElement("div")
    rowDiv.classList.add("row")

    val el = document.createElement("div")
    el.classList.add("tile")
    tiles = makeTiles

    tiles.foreach(t => rowDiv.appendChild(t))

    grid.appendChild(rowDiv)
  }

  private def makeTile = {
    val el = document.createElement("div")
    el.classList.add("tile")
    el.classList.add("unchecked")
    el
  }

  private def makeTiles = Array(makeTile, makeTile, makeTile, makeTile, makeTile)

  private def characterEntered(c: String): Unit = {
    if(guessBeingEntered.length<5) {
      guessBeingEntered = guessBeingEntered + c
      updateTiles
    }
  }

  private def deletePressed(): Unit = {
    if(guessBeingEntered.length>0) {
      guessBeingEntered = guessBeingEntered.substring(0, guessBeingEntered.length - 1)
      updateTiles
    }
  }

  private def updateTiles = {
    for(n <- 0 to 4) {
      tiles(n).textContent= ""
    }
    for(n <- 0 to guessBeingEntered.length-1) {
      tiles(n).textContent = guessBeingEntered.charAt(n).toUpper+""
    }
  }

  private def enterPressed(): Unit = {

    if(guessBeingEntered.length==5) {
      for(n <- 0 to 4) {
        if(guessBeingEntered.charAt(n)==word.charAt(n)) { tiles(n).classList.add("correct")}
        else if(word.toList.contains(guessBeingEntered.charAt(n))) { tiles(n).classList.add("wrongpos")}
        else { tiles(n).classList.add("incorrect")}

        tiles(n).classList.remove("unchecked")
      }

      guessBeingEntered = ""
      createNextTileRow()
    }
  }

}