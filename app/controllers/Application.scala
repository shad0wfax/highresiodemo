/**
 * copyright HighresIO
 */
package controllers

import scala.concurrent.duration.DurationInt
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Controller
import models.core.dao.AssetDao
import models.core.Asset
import play.Logger

//

/**
 * @author Akshay Sharma
 * Dec 23, 2012
 */
object Application extends Controller {
  
  def index = Action {
    Ok(views.html.demo("Demo - HighresIO"))
  }
  
  def dashboard = Action {
    val assets:Seq[Asset] = AssetDao.all.sortWith((s, t) => s.created.compareTo(t.created) > 0)
    Ok(views.html.dashboard("Dashboard - HighresIO", assets))
  }
  
  def how = Action {
    Ok(views.html.how("How it works - HighresIO"))
  }
  
  def about = Action {
    Ok(views.html.about("About - HighresIO"))
  }
  
  def usecase = Action {
    Ok(views.html.usecase("Usecases - HighresIO"))
  }
  
  def features = Action {
    Ok(views.html.features("Features - HighresIO"))
  }
 
  def test = Action {request =>
    Logger.debug("Coming in - got the request")
    Logger.debug(" request headers " + request.headers)
    
    Ok("Got it - 200")
  }
  
  
}