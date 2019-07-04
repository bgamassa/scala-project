import scala.io._
import scala.util._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scalaj.http._
import java.io.File
import scala.util.control.Breaks._
import com.github.tototoshi.csv._

case class Scenario(val name: String, val interval: Int, val endpoint: String) {
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
  object Report {
    implicit val rowFormat = Json.format[Report]
    implicit val reportReads: Reads[Report] = (
      (JsPath \ "date").read[String] and
      (JsPath \ "gps_fix").read[Int] and
      (JsPath \ "latitude").read[Float] and
      (JsPath \ "longitude").read[Float] and
      (JsPath \ "altitude").read[Float] and
      (JsPath \ "temperature").read[Float] and
      (JsPath \ "battery").read[Float] and
      (JsPath \ "happiness_level").read[Int] and
      (JsPath \ "anger_level").read[Int] and
      (JsPath \ "stress_level").read[Int] and
      (JsPath \ "extra").read[String]
    )(Report.apply _)
  }

  def getListOfFiles(dir: File, extensions: List[String]): List[File] = {
    dir.listFiles.filter(_.isFile).toList.filter { file =>
      extensions.exists(file.getName.endsWith(_))
    }
  }

  def post(r: Report): Unit = {
    // create our object as a json string
    val reportAsJson = Json.toJson(r)
    val json: JsObject = reportAsJson.as[JsObject] + ("from" -> Json.toJson(Scenario.from))

    val result = Http(endpoint).postData(json.toString())
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000)).asString
    println(result)
  }

  def processJSONLines(data: Iterator[String]): Unit = {
    breakable {
      while (data.hasNext) {
        Json.parse(data.next()).asOpt[Report] match {
          case Some(r: Report) => this.post(r)
          case err => {
            println(err)
            break
          }
        }
        Thread.sleep(this.interval * 100L)
      }
    }
  }

  def processCSV(data: Iterator[Map[String, String]]): Unit = {
    while (data.hasNext) {
      val raw = data.next()
      val r = Report(
        raw("date"),
        raw("gps_fix").toInt,
        raw("latitude").toFloat,
        raw("longitude").toFloat,
        raw("altitude").toFloat,
        raw("temperature").toFloat,
        raw("battery").toFloat,
        raw("happiness_level").toInt,
        raw("anger_level").toInt,
        raw("stress_level").toInt,
        raw("extra")
      )
      this.post(r)
      Thread.sleep(this.interval * 100L)
    }
  }

  def start(): Unit = {
    println(this)

    // Get all csv files from the directory
    val csvs = getListOfFiles(new File(name), List("csv"))
    // Read them one by one
    csvs.foreach(c => Try(Source.fromFile(c)) match {
      case Success(data) => this.processCSV(CSVReader.open(c).iteratorWithHeaders)
      case Failure(err) => println("Failed to read scenario file `" + c.getName() +"`: " + err)
    })

    // Get all the json files from the directory
    val jsons = getListOfFiles(new File(name), List("json"))
    // Read and parse all lines one by one
    jsons.foreach(j => Try(Source.fromFile(j)) match {
      case Success(data) => this.processJSONLines(data.getLines)
      case Failure(err) => println("Failed to read scenario file `" + j.getName() +"`: " + err)
    })
  }
}

object Scenario {
  def empty = new Scenario("default", 30, "http://scala-aggregator-api.eu-gb.mybluemix.net/data")
  val from = System.getProperty("user.name") + "_" + scala.util.Random.nextInt(100)
}

