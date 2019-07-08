package main

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import org.apache.spark.streaming.kafka._
import kafka.serializer.StringDecoder

import org.apache.spark.rdd.RDD
import org.apache.spark.sql._

import com.datastax.spark.connector.SomeColumns
import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.streaming._
import com.datastax.spark.connector._

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

object KafkaIntegration {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("Kafka Integration")
      .config("spark.cores.max", "1")
      .config("spark.sql.warehouse.dir", "/tmp")
      .config("spark.cassandra.connection.host", "localhost")
      .config("spark.cassandra.connection.port", "9042")
      .master("local[*]")
      .getOrCreate()

    val connector = CassandraConnector.apply(spark.sparkContext.getConf)
    val sc = spark.sparkContext
    val ssc = new StreamingContext(sc, Seconds(10))

    Logger.getRootLogger.setLevel(Level.ERROR)

    val temp = ssc.cassandraTable("brain_implant", "report")
    temp.foreach(println)

    val kafkaParams = Map("metadata.broker.list" -> "localhost:9092")

    val topics = List("test").toSet

    // Kafka stream contains (topic, message) pairs
    val events = KafkaUtils
      .createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
      .map(_._2)

      events
        .map(JsonUtil.fromJson[JSReport](_))
        .foreachRDD(currentRdd => {
          val spark =
            SparkSession.builder.config(currentRdd.sparkContext.getConf).getOrCreate()
          import spark.implicits._

          val simpleDF: DataFrame = currentRdd.toDF()
          simpleDF.createOrReplaceTempView("simpleDF")

          spark.sql("select * from simpleDF").show()

          process(currentRdd).saveToCassandra("brain_implant", "report", SomeColumns(
            "from",
            "date",
            "gps_fix",
            "latitude",
            "longitude",
            "altitude",
            "temperature",
            "battery",
            "happiness_level",
            "anger_level",
            "stress_level",
            "extra"
          ))
        })

      ssc.checkpoint("kafka Integration")
      ssc.start
      ssc.awaitTermination()
  }

  def process(r: RDD[JSReport]): RDD[JSReport] = {
    r
  }
}
