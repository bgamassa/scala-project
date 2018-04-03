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

        ${ins.temperature getOrElse 0 },
        ${ins.battery getOrElse 0 },
        ${ins.extra getOrElse "" }
      )""")
    }

    Ok("ok")
  }
}
