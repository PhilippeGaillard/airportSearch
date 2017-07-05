package src.main.scala

/**
  * Created by philippe on 02/07/2017.
  */
object ImportData{


  import kantan.csv._
  import kantan.csv.ops._
  import kantan.csv.generic._

  case class Countries (Id: Long,Code: String,Name: String,Continent: String,Wikipedia_link: String,Keywords: String)

  case class Runways(IdRunway: Long,AirportRef: Long, AirportIdent: String,Leng: Option[Long],Width: Option[Long],
                     Surface: String,Light: Option[Long], Closed: Option[Long],LeIdent: String, Lat: Option[Float],
                     Long: Option[Float], Elev: Option[Float],LeHead: Option[Float],LeDispl: Option[Float],
                     HeIdent: String, HeLat: Option[Float],HeLong: Option[Float],HeElev: Option[Float],HeHead: Option[Float],HeDispl: Option[Float])

  case class Airports(IdAirp: Long,Ident: String,Type: String,Name: String, Lat: Option[Float],Long: Option[Float],
                      Elevation: Option[Float], Continent: String,IsoCount: String, IsoRegion: String, Municip: String,
                      SchedServ: String,GpsCode: String, IataCode: String, LocalCode: String, Home:String, Wiki: String,Keywords: String)


  case class AirportsWithRunways(airport: Airports,listRunways: Option[List[Runways]])


  //Parse all .csv files

  val countryList = scala.io.Source.fromFile("countries.csv","UTF-8").mkString.readCsv[List,Countries](rfc.withHeader)

  val airportList = scala.io.Source.fromFile("airports.csv","UTF-8").mkString.readCsv[List,Airports](rfc.withHeader)

  val runwayList = scala.io.Source.fromFile("runways.csv","UTF-8").mkString.readCsv[List,Runways](rfc.withHeader)


  //combine runways and airports

  val airportListWithRunways = airportList.
    map(x=>AirportsWithRunways(x.get,Option(runwayList.filter(y=>y.get.AirportRef == x.get.IdAirp).map(z=>z.get))))




  //Reporting function

  def reportAirportInfo = {

    val counting = airportListWithRunways.groupBy(x=>x.airport.IsoCount).map(x=>(x._1,x._2.toArray.length,x._2))

    val orderedcount = counting.toList.sortWith(_._2>_._2)



    val leastNumber: Seq[(String, Int, List[String])] = orderedcount.map(x=>(x._1,x._2,x._3.flatMap(runw=>runw.listRunways)
      .flatMap(surf=>surf.map(_.Surface)).distinct)).takeRight(10).map(x=>(codeToCountryName(x._1),x._2,x._3))

    val largestNumber=orderedcount.map(x=>(x._1,x._2,x._3.flatMap(runw=>runw.listRunways)
      .flatMap(surf=>surf.map(_.Surface)).distinct)).take(10).map(x=>(codeToCountryName(x._1),x._2,x._3))



    // most popular LeIdent
    val runwaydesignations = runwayList.groupBy(x=>x.get.LeIdent)
      .map(x=>(x._1,x._2.toArray.length)).toList.sortWith(_._2>_._2).take(10)



    val resultList: (Seq[(String, Int, List[String])], List[(String, Int, List[String])], List[(String, Int)]) =
      (leastNumber,largestNumber,runwaydesignations)

    resultList
  }


  //verify if country name correct, send back country code
  def checkCountryName(inputString : String) = {

    var codeToResearch = ""


    if(countryList.filter(x=>x.get.Name.equalsIgnoreCase(inputString)).length != 0) {
      codeToResearch = countryList.filter(x=>x.get.Name.equalsIgnoreCase(inputString)).head.get.Code
    } else {
      if (countryList.filter(x => x.get.Code.equalsIgnoreCase(inputString)).length != 0) {
        codeToResearch = inputString
      } else {
        codeToResearch = "error country invalid"
      }
    }

    codeToResearch
  }


  //List airports of target country

  def listAirportsOfCountry(isoCode : String) = {

    val airportListForCountry: Seq[ImportData.AirportsWithRunways] =
      airportListWithRunways.filter(x=>x.airport.IsoCount.equalsIgnoreCase(isoCode))

    airportListForCountry
  }



  //Utility function for code-name correspondence
  def codeToCountryName(isoCode : String): String =  {

    countryList.filter(x => x.get.Code.equalsIgnoreCase(isoCode)).head.get.Name

  }




}
