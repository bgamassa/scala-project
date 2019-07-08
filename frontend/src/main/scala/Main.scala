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
    happiness_level: Int,
    anger_level: Int,
    stress_level: Int,
    extra: String
  )

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }

  def JSONParse(targetNode: dom.Node, text: String, id: Int) {
    JSON.parse(text).asInstanceOf[js.Array[js.Dynamic]].foreach{
      elt => appendPar(targetNode, "id : %s  - Date: %s - Gps_fix: %s - Latitude: %s - Longitude: %s ".format(
        elt.id, elt.date, elt.gps_fix, elt.latitude, elt.longitude)
      + "Altitude: %s - Temperature: %s - Battery: %s - Happiness Level: %s ".format(elt.altitude, elt.temperature, elt.battery, elt.happiness_level)
      + "Anger Level: %s - Stress Level: %s - Extra: %s".format(elt.anger_level, elt.stress_level, elt.extra))
    }

    setTimeout(10000) { getData(targetNode, id + 50) }
  }

  def getData(targetNode: dom.Node, id: Int) : Unit = {
    val url = "https://scala-aggregator-api.eu-gb.mybluemix.net/data/all?&reverse=true&limit=50"
    Ajax.get(url).onSuccess { case xhr =>
      JSONParse(div, xhr.responseText, id)
    }
  }

  val div = document.createElement("div")
  val h1Node = document.createElement("h1")
  val h1value = document.createTextNode("Flux des dernières données")

  h1Node.appendChild(h1value)
  div.appendChild(h1Node)
  document.body.appendChild(div)
  getData(div, 1)
}
