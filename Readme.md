# Práctica Big Data Processing. 

Este proyecto escrito en Scala corresponde a la práctica que tuve que presentar como para el módulo Big Data Processing del BootCamp de Big Data & Machine Learning que cursé en KeepCoding en 2019/2020.

## Comenzando 🚀

El enunciado del ejercicio puede encontrarse [aquí](Enunciado.md)

### Pre-requisitos 📋
- Java 8
- [IntelliJ](https://www.jetbrains.com/idea/) IDE para programar en Scala
- [Scala 2.11.12](https://www.scala-lang.org/)
- [Apache Kafka 2.12-2.3.0](https://kafka.apache.org/)
- [sbt](https://www.scala-sbt.org/)

### Instalación 🔧

El proyecto se ha desarrollado y probado usando el sistema operativo Mac OS Catalina 10.15

Las librerías usadas dentro del proyecto son las siguientes:

- Spark Core
- Spark Sql
- Spark Streaming
- Spark Streaming Kafka
- Spark Sql Kafka

### Configuración ⚙️
Se ha usado **sbt** como gestor de dependencias. Dentro del archivo build.sbt se encuentran las librerías necesarias para el correcto funcionamiento del proyecto.

Dentro de la carpeta **Resources** se encuentra el fichero lista_negra.dat que contiene las palabras consideradas como "negativas".

También dentro de la carpeta **Resources** se encuentran los csv con la información de dispositivos, mensajes y usuarios. Los Ficheros son los siguientes: 

- **mensajes.csv** con los mensajes capturados por los diferentes dispositivos IoT.
- **dispositivos.csv** con la información de los diferentes dispositivos
- **usuarios.csv** con todos los usuarios de Celebram
- **palabras_excluir.dat** este fichero contiene palabaras que no se tienen en cuenta para el estudio (conjunciones, artículos y preposiciones)

## Deployment 📦

Al ejecutarse la aplicación (fichero Main.scala), se cargará los csv de Usuarios y dispositivos IoT que estarán guardados en los csv correspondientes en la carpeta **Resources**.

Después se iniciará la espera de mensajes desde Kafka, en este paso tendremos que ir a la consola de Kafka y ejecutar el siguiente comando para cargar el csv con los mensajes:

```bash
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test < ../../Proyectos_software/KC_Practica_BigData_Processing/src/main/resources/mensajes.csv
```

Cuando acabe la ventana (para las pruebas se ha establecido en 10 segundos) se mostrará el resultado con las 10 palabras más usadas, también se comprobará si la palabra más repetida coincide con alguna de las plabras de la lista negra, en ese caso, se mostrará notificación al usuario de la siguiente forma:

imagen notificación

## Obtención de mensajes 🖥️

Para la obtención de los mensajes, he creado un proyecto a parte en Scala que descarga Tweets en streaming y los almacena en un fichero de texto. Las librerías usadas han sido:

- Spark Streaming
- Spark Streaming Twitter
- twitter4j

Este es el código usado para tal fin:

```scala
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import twitter4j.Status

object pruebasFran extends App {
    // En este fichero estan las funciones que se usan para clasificar tweets etc
    import Utils._

    // Configurar spark, nombre e indicamos que usamos todos los cores disponibles
    val sparkConfiguration = new SparkConf()
            .setAppName("Clasificar Tweets")
            .setMaster("local[*]")

    // Crearmos el contexto con la configuracion anterior
    val sparkContext = new SparkContext(sparkConfiguration)

    // Configuramos el streaming cada 5 segundos
    val streamingContext = new StreamingContext(sparkContext, Seconds(5))

    // Crear el stream de Twitter (mirar README para saber cómo configurar las credenciales)
    var tweets: DStream[Status] = TwitterUtils.createStream(streamingContext, None)

    tweets = tweets.filter(_.getLang == "es")  // Filtramos tweets en espanol
    tweets = tweets.filter(tweetText => !(tweetText.getText contains "@"))  // Quitamos las menciones

    // Guardar en ficheros de texto
    tweets.map(tweetText => tweetText.getText).saveAsTextFiles("prueba.txt")

    // Mostrar por consola
    //tweets.map(tweetText => tweetText.getText).print()
    
    // Lanzamos el setreaming
    streamingContext.start()

    // Esperamos hasta que alguien lo pare
    streamingContext.awaitTermination()
}
```

## Aclaraciones ✏️

Al esquema de los mensajes he decidido incluir un nuevo campo correspondiente al ID del dispositivo IoT.

Al esquema de usuarios le he cambiado el tipo de dato a Int porque me parece más óptimo a nivel de BBDD.

A la hora de tratar los mensajes, se usa una función para desencriptarlos y no se tienen en cuenta las preposiciones, conjunciones ni artículos.

## Expresiones de Gratitud 🎁

* Comenta a otros sobre este proyecto 📢
* Da las gracias públicamente 🤓
* Sígueme en <a href="https://twitter.com/AsensiFj">Twitter</a> 🐦