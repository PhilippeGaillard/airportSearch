name := """AirportApp"""
organization := "PhilippeTest"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Confluent" at "http://packages.confluent.io/maven/"
)

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test

libraryDependencies +="org.scalatest" %% "scalatest" % "2.2.6"
libraryDependencies +="com.nrinaudo" %% "kantan.csv" % "0.1.19"
libraryDependencies += "com.nrinaudo" %% "kantan.csv-generic" % "0.1.19"

//libraryDependencies += filters

routesGenerator := InjectedRoutesGenerator



// Adds additional packages into Twirl
//TwirlKeys.templateImports += "PhilippeTest.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "PhilippeTest.binders._"
