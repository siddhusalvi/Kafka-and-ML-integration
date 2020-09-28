import java.util
import java.util.Properties

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConverters._

object Consumer {
  def main(args: Array[String]): Unit = {
    try {
      val spark = SparkSession.builder().master("local[2]").appName("KafkaServer").getOrCreate()
      val sc = spark.sparkContext

      val topic = "StocksData"
      val port = 9092
      val path = "C:\\Users\\Siddesh\\IdeaProjects\\KafkaServer\\src\\main\\resourece"
      val script = "py_predictor.py"
      var command = "python " + path + "\\" + script

      val props = new Properties()
      props.put("bootstrap.servers", "localhost:" + port.toString)
      props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
      props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
      props.put("auto.offset.reset", "earliest")
      props.put("group.id", "consumer-group")

      val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
      consumer.subscribe(util.Arrays.asList(topic))
      while (true) {
        val record = consumer.poll(1000).asScala
        for (data <- record.iterator) {
          val line = data.value()
          val list = Array(line)
          val operation = sc.parallelize(list)
          val std_op = operation.pipe(command)
          std_op.foreach(println(_))
        }
      }
    } catch {
      case exception: Exception => println(exception)
    }
  }
}