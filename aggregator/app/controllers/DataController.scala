package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.db._

import com.github.takezoe.scala.jdbc._
import play.api.db._
import play.api.libs.json._
import java.sql._

import play.api.libs.functional.syntax._
import java.text.SimpleDateFormat

case class JSReport(
  from: String,
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
object JSReport {
  implicit val rowFormat = Json.format[JSReport]
  implicit val reportReads: Reads[JSReport] = (
    (JsPath \ "from").read[String] and
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
  )(JSReport.apply _)
}

case class Report(
  id: Option[Int],
  from: Option[String],
  date: Option[Timestamp],
  gps_fix: Option[Int],
  latitude: Option[Float],
  longitude: Option[Float],
  altitude: Option[Float],
  temperature: Option[Int],
  battery: Option[Int],
  happiness_level: Option[Int],
  anger_level: Option[Int],
  stress_level: Option[Int],
  extra: Option[String]
)

object Report {
  implicit object timestampFormat extends Format[Timestamp] {
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'")
    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(new Timestamp(format.parse(str).getTime))
    }
    def writes(ts: Timestamp) = JsString(format.format(ts))
  }

  implicit val format: Format[Report] = Json.format

  def parse(id: Int,
    from: String,
    date: Timestamp,
    gps_fix: Int,
    latitude: Float,
    longitude: Float,
    altitude: Float,
    temperature: Int,
    battery: Int,
    happiness_level: Int,
    anger_level: Int,
    stress_level: Int,
    extra: String): Report = {
      new Report(
        Some(id),
        Some(from),
        Some(date),
        Some(gps_fix),
        Some(latitude),
        Some(longitude),
        Some(altitude),
        Some(temperature),
        Some(battery),
        Some(happiness_level),
        Some(anger_level),
        Some(stress_level),
        Some(extra)
      )
  }
}

@Singleton
class DataController @Inject()(cc: ControllerComponents, db: Database) extends AbstractController(cc) {

  def postData = Action(parse.json) { implicit request: Request[JsValue] =>
    val ins = request.body.as[JSReport]

    val conn = db.getConnection()

    val res = DB.autoClose(conn) { db =>
      db.update(sql"""
      INSERT INTO report (
        "from",
        date,
        gps_fix,
        latitude,
        longitude,
        altitude,
        temperature,
        battery,
        happiness_level,
        anger_level,
        stress_level,
        extra)
      VALUES (
        ${ins.from},
        ${new Timestamp(ins.date.toFloat.toInt)},
        ${ins.gps_fix},
        ${ins.latitude},
        ${ins.longitude},
        ${ins.altitude},
        ${ins.temperature},
        ${ins.battery},
        ${ins.happiness_level},
        ${ins.anger_level},
        ${ins.stress_level},
        ${ins.extra}
      )""")
    }

    Ok("ok")
  }

  def getData(minID: Int, from: String, reverse: Boolean, limit: Int) = Action { implicit request: Request[AnyContent] =>
    val conn = db.getConnection()

    val query = if (reverse)
      sql"""SELECT * FROM report WHERE
        id >= ${minID} AND
        "from" LIKE ${from}
        ORDER BY id DESC
        LIMIT ${limit}"""
      else sql"""SELECT * FROM report WHERE
        id >= ${minID} AND
        "from" LIKE ${from}
        ORDER BY id ASC
        LIMIT ${limit}"""

    val reports: Seq[Report] = DB.autoClose(conn) { db =>
      db.select(query, Report.parse _)
    }

    Ok(Json.toJson(reports))
  }

  def getActors = Action { implicit request: Request[AnyContent] =>
    val conn = db.getConnection()

    val query = sql"""SELECT DISTINCT "from" FROM report"""

    val reports: Seq[String] = DB.autoClose(conn) { db =>
      db.select(query) { rs =>
        rs.getString("from")
      }
    }

    Ok(Json.toJson(reports))
  }

}
