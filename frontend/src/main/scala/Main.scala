import org.scalajs.dom
import dom.document
import dom.ext.Ajax
import scala.io.Source
import java.net.URL
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.io._
import scala.scalajs.js.timers._
import scala.util._

object Main extends App {
    def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }

  def getData() : Unit = {
      val url =
        "http://localhost:9000/data"
        Ajax.get(
          url = url,
          headers = Map(
            "Content-type" -> "application/json",
            "Access-Control-Allow-Origin" -> "http://localhost:5000",
          )
          ).onSuccess { case xhr =>
          appendPar(document.body, xhr.responseText)
        }

    setTimeout(7000) { getData() }
  }

  //appendPar(document.body, getData())
  getData()
}
