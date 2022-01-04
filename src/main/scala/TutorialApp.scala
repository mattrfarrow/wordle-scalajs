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

  var grid: Element = _
  var tiles: Array[Array[Element]] = _

  val dictionary: Seq[String] = loadDictionary()

  var words: Seq[String] = List("calls", "round", "hound", "evils", "badge", "reset", "nutty", "whose")
  var word: String = words(new Random().nextInt(words.length))
  var guessBeingEntered: String = ""
  var roundNum = 0

  def setupUI(): Unit = {
    grid = document.getElementById("grid")

    tiles = createTiles()
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

  private def makeTile = {
    val el = document.createElement("div")
    el.classList.add("tile")
    el.classList.add("unchecked")
    el
  }

  private def makeTileRow = Array(makeTile, makeTile, makeTile, makeTile, makeTile)

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

  private def updateTiles(): Unit = {
    for(n <- 0 to 4) {
      tiles(roundNum)(n).textContent= ""
    }
    for(n <- 0 until guessBeingEntered.length) {
      tiles(roundNum)(n).textContent = guessBeingEntered.charAt(n).toUpper+""
    }
  }

  private def enterPressed(): Unit = {

    if(guessBeingEntered.length==5) {
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

}