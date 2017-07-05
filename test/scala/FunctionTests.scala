package scala

/**
  * Created by philippe on 04/07/2017.
  */


import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import org.scalatest._
import src.main.scala.ImportData.Countries

class FunctionTests extends FlatSpec with Matchers {


// test scala functions used to fill web pages

  "a country case class" should "have a name" in {
    Countries(302672,"AD","Andorra","EU","http://en.wikipedia.org/wiki/Andorra","").Name should be ("Andorra")

  }

  "csv parser" should "parse countries.csv" in {

    scala.io.Source.fromFile("countries.csv","UTF-8").mkString.readCsv[List,Countries](rfc.withHeader).head.get.Name should be ("Andorra")
  }

  "code to country correspondence" should "give name from code" in {

    src.main.scala.ImportData.codeToCountryName("AD") should be ("Andorra")
  }




}
