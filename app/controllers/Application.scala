package controllers

import play.api._
import play.api.mvc._
import controllers.util._
import play.api.libs.json._
import models.DepthSide
import models.DepthSide._

object Application extends Controller {
  
  def index = Action { implicit request =>
    Ok(Render.withStds("index.scaml"))
  }
  
  def grid = Action { implicit request =>
    Ok(Json.toJson(DepthSide.buySideCopy.d))
  }
}