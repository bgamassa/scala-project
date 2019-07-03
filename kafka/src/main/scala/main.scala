import java.util.Properties
import org.apache.kafka.clients.producer._
import net.liftweb.json._
import net.liftweb.json.Serialization.write

case class Thermo(id: Int, from: String, date: String,
                  gps_fix: Int, latitude: Int,
                  longitude: Int, altitude: Int,
                  temperature: Int, battery: Int, extra: String)


class Producer {

  def writeToKafka(topic: String): Unit = {
  
    val therm = Thermo(3, "sa", "lut", 3, 4, 5, 6, 7, 8, "mg")
    implicit val formats = DefaultFormats
    val jsonString = write(therm)

    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("client.id", "ScalaProducerExample")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("auto.create.topics.enable", "true")
    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String](topic, jsonString)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.send(record)
    producer.close()
  }
}

object Producer {  
  def main(args: Array[String]): Unit = {
    new Producer().writeToKafka("test")
  }
}
