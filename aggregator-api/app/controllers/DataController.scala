package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.db._

import com.github.takezoe.scala.jdbc._
import play.api.db._
import play.api.libs.json._
import java.sql._

import play.api.libs.json.Json._
import play.api.libs.json._
import java.text.SimpleDateFormat

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
        Some(extra)
      )
  }
}

@Singleton
class DataController @Inject()(cc: ControllerComponents, db: Database) extends AbstractController(cc) {

  def getData = Action { implicit request: Request[AnyContent] =>
    val conn = db.getConnection()

    val reports: Seq[Report] = DB.autoClose(conn) { db =>
      db.select("SELECT * FROM report", Report.parse _)
    }

    Ok(Json.toJson(reports))
  }

  def postData = Action(parse.json) { implicit request: Request[JsValue] =>
    val ins = request.body.as[Report]

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
        extra)
      VALUES (
        ${ins.from getOrElse "" },
        ${ins.date getOrElse (new Timestamp(System.currentTimeMillis / 1000)) },
        ${ins.gps_fix getOrElse -2 },
        ${ins.latitude getOrElse 0 },
        ${ins.longitude getOrElse 0 },
        ${ins.altitude getOrElse 0 },
        ${ins.temperature getOrElse 0 },
        ${ins.battery getOrElse 0 },
        ${ins.extra getOrElse "" }
      )""")
    }

    Ok("ok")
  }
}
