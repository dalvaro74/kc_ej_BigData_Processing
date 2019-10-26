package IOT_Sniffer

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object Main {

    def main(args: Array[String]): Unit = {
        Logger.getLogger("org").setLevel(Level.ERROR)

        val dispositivosDS = cargaDispositivos()
        val usuariosDS = cargaUsuarios()
        dispositivosDS.show()
        usuariosDS.show()
    }

    // Cargar usuarios
    def cargaUsuarios(): Dataset[Usuario] = {
        val spark = SparkSession
                .builder()
                .appName("Cargar Dispositivos")
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
}
