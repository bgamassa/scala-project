import org.scalajs.dom
import dom.document
import dom.ext.Ajax
import scala.io.Source
import java.net.URL
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {
    def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }

  def getData() = {
      var text = ""
      val url =
        "http://localhost:9000/data"
      Ajax.get(
        url = url,
        headers = Map(
          "Content-type" -> "application/json",
          "Access-Control-Allow-Origin" -> "http://localhost:5000",
        )
        ).foreach { case xhr =>
        text = xhr.responseText
      }
      text
  }

  appendPar(document.body, getData())
}