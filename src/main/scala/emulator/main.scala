import scala.io._
import play.api.libs.functional.syntax._
import play.api.libs.json._

/* TODO
 * 1) Parse args to select scenario
 * 2) Parse corresponding scenario file
 * 3) Post data to server according to the scenario file
 */

/* Scenario file format
 *  name:
 *    type: string
 *    exmaple: 'default scnenario'
 *    description: Name of the scenario
 *  interval:
 *      type: int | range
 *      example: 3 | 3-10
 *      description:
 *        | Message posting interval. If the interval is a range, post messages
 *        | with a random interval from begin to end.
 *  dataFormat:
 *    temp:
 *  example:
 *
 *    },
 *  }
 */
object main {
  case class Row(lat: Double, lng: Double, tmp: Int, battery: Int)
  object Row {
    implicit val rowFormat = Json.format[Row]
  }

  def debug(data: Array[Row]): Unit = {
    data.foreach{ println }
  }

  def main(args: Array[String]): Unit = {
    val rawData = Source.fromURL(getClass.getResource("test.json")).mkString
    val data = Json.parse(rawData).asOpt[Array[Row]]

    data match {
      case Some(x) => debug(x)
      case x => println(x)
    }
  }
}
