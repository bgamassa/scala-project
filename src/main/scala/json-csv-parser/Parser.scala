import play.api.libs.json._
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import java.io.InputStream

object Parser extends App {

  // case class definition
  case class Device(lat: Double, lng: Double, tmp: Int, battery: Int)

  // Json parser
  def read_json(input: InputStream) = {
    val jsonString: JsValue = Json.parse(input)

    implicit val deviceReads = Json.reads[Device]
    val deviceFromJson: JsResult[Device] = Json.fromJson[Device](jsonString)

    deviceFromJson match {
      case JsSuccess(d: Device, path: JsPath) => println("device: " + d)
      case e: JsError => println("Errors: " + JsError.toJson(e).toString())
    }
  }

  // CSV parser
  def read_csv(input: java.net.URL) = {
    val reader = input.asCsvReader[Device](rfc.withHeader)
    // handle error case
    reader.foreach(println)
  }

  // entry point
  val json_file = getClass.getResourceAsStream("/test.json")
  val json_csv = getClass.getResource("/test.csv")

  read_json(json_file)
  read_csv(json_csv)
}
