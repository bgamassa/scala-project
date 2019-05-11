import org.scalajs.dom
import dom.document
import dom.html
import dom.ext.Ajax
import scala.io.Source
import java.net.URL
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.io._
import scala.scalajs.js.timers._
import scala.util._
import scala.scalajs.js.{JSON, URIUtils}
import scala.scalajs.js


object Main extends App {

  case class Report(
    date: String,
    gps_fix: Int,
    latitude: Float,
    longitude: Float,
    altitude: Float,
    temperature: Float,
    battery: Float,
    extra: String
  )

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }

  def findMax[A](x: A, y: A) = {
    if (x > y) x else y
  }

  def JSONParse(targetNode: dom.Node, text: String) {
    JSON.parse(text).asInstanceOf[js.Array[js.Dynamic]].foreach{
      elt => appendPar(targetNode, "id : %s  - Date: %s - Gps_fix: %s - Latitude: %s - Longitude: %s ".format(
        elt.id, elt.date, elt.gps_fix, elt.latitude, elt.longitude)
      + "Altitude: %s - Temperature: %s - Battery: %s - Extra: %s".format(elt.altitude, elt.temperature, elt.battery, elt.extra))
    }

    val max = JSON.parse(text).asInstanceOf[js.Array[js.Dynamic]].map(e => e.id).reduceLeft(findMax)

    setTimeout(10000) { getData(targetNode, max.toString) }
  }

  def getData(targetNode: dom.Node, id: String) : Unit = {
    val url = "http://scala-aggregator-api.eu-gb.mybluemix.net/data/all?reverse=true&minID=" + id
    Ajax.get(url).onSuccess { case xhr =>
      JSONParse(div, xhr.responseText)
    }
  }

  val div = document.createElement("div")
  val h1Node = document.createElement("h1")
  val h1value = document.createTextNode("Flux des dernières données")

  h1Node.appendChild(h1value)
  div.appendChild(h1Node)
  document.body.appendChild(div)
  getData(div, "1")
}
