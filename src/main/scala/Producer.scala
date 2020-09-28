import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.util
import java.util.Properties
import org.apache.log4j.BasicConfigurator
import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.{Deserializer, Serializer}
import scala.io.Source

object Producer {
  def main(args: Array[String]): Unit = {
    try{
    BasicConfigurator.configure()
    val topic = "StocksData"
    val port = 9092
    val file = "src/main/resourece/stocks.csv"
    writeToKafka(topic, port, file)
  }catch{
      case exception:Exception => println(exception)
    }
  }

  //Function to send stream
  def writeToKafka(topic: String, port: Int, file: String): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:" + port.toString)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)

    //Getting file data
    val bufferedSource = Source.fromFile(file)
    for (line <- bufferedSource.getLines) {
      val txt = line + "\n"
      val record = new ProducerRecord[String, String](topic, "key", txt)
      producer.send(record)
    }
    bufferedSource.close
    producer.close()
  }
}

