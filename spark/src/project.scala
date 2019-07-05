import org.apache.spark.sql._
import org.apache.log4j._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import kafka.serializer.StringDecoder
import org.apache.spark.sql.SparkSession
import play.api.libs.json.{JsError, JsSuccess, Json}


class StreamsProcessor(brokers: String) {
  case class BrainImplent(
    id: String,
    user: String
  )
  
  implicit val BrainImplentFormat = Json.format[BrainImplent]

  def JsontoBrainImplent(json: String) = Json.parse(json).validate[BrainImplent] match {
    case JsError(e) => println(e); None
    case JsSuccess(t, _) => Some(t)
  }

  def loadData(): Unit = {
    val topic: Set[String] = Set("test")
  
    val spark = SparkSession.builder()
    .appName("Emotion reader")
    .config("spark.cores.max", "10")
    .master("local[*]")
    .getOrCreate()
    
    val sc = spark.sparkContext
    val ssc = new StreamingContext(sc, Seconds(10))
    val kafkaProperties: Map[String, String] = Map("metadata.broker.list" -> "localhost:9092")
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
    ssc, kafkaProperties, topic)

    
    messages.flatMap(JsontoBrainImplent)
  }
}

object StreamsProcessor {
  def main(args: Array[String]): Unit = {
    new StreamsProcessor("localhost:9094").process()
  }
}

