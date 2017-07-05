package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import src.main.scala.ImportData
/**
  * Created by philippe on 03/07/2017.
  */
class ReportController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def reportPage() = Action { implicit request: Request[AnyContent] =>
    val reportResults = ImportData.reportAirportInfo
    Ok(views.html.reports.report(reportResults))
  }


}
