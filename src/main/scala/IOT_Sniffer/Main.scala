package IOT_Sniffer

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.streaming.Trigger

object Main {

    def main(args: Array[String]): Unit = {
        Logger.getLogger("org").setLevel(Level.ERROR)

        val dispositivosDS = cargaDispositivos()
        println("Cargando dispositivos IOT....")

        val usuariosDS = cargaUsuarios()
        println("Cargando usuarios....")

        dispositivosDS.show()
        usuariosDS.show()

        capturaStreaming()
    }

    // Captura de datos en streaming desde Kafka
    def capturaStreaming(): Unit = {
        val incluirTimestamp: Boolean = true

        val spark = SparkSession
                .builder()
                .appName("Kafka Spark")
                .master("local[*]")
                .getOrCreate()

        import spark.implicits._

        val data = spark.readStream
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "test")
                .option("includeTimestamp", incluirTimestamp)
                .load()
                .selectExpr("CAST(value AS STRING)")
                .as[String]

        import spark.implicits._

        // Con esto obtengo un dataset con el esquema de campos del case class de mensaje con todos los datos del csv
        val mensajesDS = data
                .filter(x => x.split(",")(0).toLowerCase != "iot id")  // Excluyo la cabecera del fichero
                .map(_.split(","))
                .map(atributos => new Mensaje(
                    atributos(1).toString,
                    atributos(2).toString,
                    atributos(3).toInt,
                    atributos(0).toInt
                ))

        // Obtener las diferentes palabras del mensaje
        val conteoPalabras = mensajesDS
                .select($"pContenidoMensaje")  // Selecciono solo la columna del mensaje
                .as[String]  // Convierto a array de strings
                .flatMap(cadena => desencriptaMensaje(cadena).split(" "))  // Desencripto y separo por espacios para encontrar palabras sueltas
                .filter(_.contains("http") == false)  // Eliminar enlaces
                .groupBy("value")  // Agrupo por la única columna
                .count()  // Muestro el contador de cada palabra

        val query = conteoPalabras.writeStream
                .outputMode("complete")
                .format("console")
                .trigger(Trigger.ProcessingTime("10 second"))
                .start()
                .awaitTermination()
    }

    // Cargar usuarios
    def cargaUsuarios(): Dataset[Usuario] = {
        val spark = SparkSession
                .builder()
                .appName("Cargar Usuarios")
                .master("local[*]")
                .getOrCreate()

        val dataSamplesRDD = spark.sparkContext.textFile(getClass.getResource("/usuarios.csv").getPath)
        val header = dataSamplesRDD.first()
        val datosRDD = dataSamplesRDD
                .filter(line => line != header)
                .map(_.split(","))
                .map(atributos => new Usuario(
                    atributos(0).toInt,
                    atributos(1).toString,
                    atributos(2).toString,
                    atributos(3).toInt,
                    atributos(4).toString
                ))

        import spark.implicits._

        val a = spark.createDataset(datosRDD)

        a
    }

    // Cargar dispositivos
    def cargaDispositivos(): Dataset[IOT] = {
        val spark = SparkSession
                .builder()
                .appName("Cargar Dispositivos")
                .master("local[*]")
                .getOrCreate()

        val dataSamplesRDD = spark.sparkContext.textFile(getClass.getResource("/dispositivos.csv").getPath)
        val header = dataSamplesRDD.first()
        val datosRDD = dataSamplesRDD
                .filter(line => line != header)
                .map(_.split(","))
                .map(atributos => new IOT(
                    atributos(0).toInt,
                    atributos(1).toBoolean,
                    atributos(2).toString
                ))

        import spark.implicits._

        val a = spark.createDataset(datosRDD)

        a
    }

    // Desencripta el mensaje (La función es simulada dadas las especificaciones)
    def desencriptaMensaje(pMensaje: String): String = {
        pMensaje
    }
}
