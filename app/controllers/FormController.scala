package controllers

import javax.inject._

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import src.main.scala.ImportData
import src.main.scala.ImportData.AirportsWithRunways

import scala.collection.mutable.ArrayBuffer
import src.main.scala

case class CountryName(name: String)

// NOTE: Add the following to conf/routes to enable compilation of this class:
/*
GET     /search        controllers.FormController.searchGet
POST    /search        controllers.FormController.searchPost
*/

/**
 * country search form controller for Play Scala
 */
class FormController @Inject()(mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc) {

  val resultList = ArrayBuffer[String]()
  var resultAirports = Seq[AirportsWithRunways]()

  def searchGet() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.search.form(FormController.searchForm,resultList,resultAirports))
  }



  def searchPost() = Action { implicit request: MessagesRequest[AnyContent] =>
    FormController.searchForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.search.form(formWithErrors,resultList,resultAirports))
      },
      userProvidedString => {
        /* binding success, you get the actual value. */       
        /* flashing uses a short lived cookie */
        val coutryCodeCheck = ImportData.checkCountryName(userProvidedString.name)

        if (coutryCodeCheck.equalsIgnoreCase("error country invalid")){
          resultList.append("error country invalid")
          Redirect(routes.FormController.searchGet)
        }else{
          resultList.append(scala.ImportData.codeToCountryName(coutryCodeCheck))
          resultAirports = scala.ImportData.listAirportsOfCountry(coutryCodeCheck)
          Redirect(routes.FormController.searchGet)
        }


      }
    )
  }



}

object FormController {
  val searchForm = Form(
    mapping(
      "name" -> nonEmptyText
    )(CountryName.apply)(CountryName.unapply)
  )

}