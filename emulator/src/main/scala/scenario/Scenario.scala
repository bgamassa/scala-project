import scala.io._
import scala.util._
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Scenario(val name: String, val interval: Int, val endpoint: String) {
  case class Row(lat: Double, lng: Double, tmp: Int, battery: Int)
  object Row {
    implicit val rowFormat = Json.format[Row]
  }

  def post(r: Row): Unit = {
    println(r)
  }

  def processNextLine(data: Iterator[String]): Unit = {
    Json.parse(data.next()).asOpt[Row] match {
      case Some(r: Row) => this.post(r)
      case err => println(err)
    }

    Thread.sleep(this.interval * 1000L)
    if (data.hasNext) {
      this.processNextLine(data)
    } else {
      this.start()
    }
  }

  def start(): Unit = {
    println(this)
    Try(Source.fromFile(name + ".json")).orElse(
    Try(Source.fromFile(name + ".csv"))
    ) match {
      case Success(data) => this.processNextLine(data.getLines)
      case Failure(err) => println("Failed to load scenario file `" + name +"`: " + err)
    }
  }
}

object Scenario {
  def empty = new Scenario("default", 3, "http://localhost:3000/evt")
}

