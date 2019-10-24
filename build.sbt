name := "KC_Practica_BigData_Processing"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.3.4"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.3.4"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.3.4" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-8" % "2.3.4"
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.3.0"