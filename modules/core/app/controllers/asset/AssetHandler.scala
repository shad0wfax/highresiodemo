/**
 * copyright VisualRendezvous
 */
package controllers.core.asset

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Controller
import models.core.dao.AssetDao
import play.Logger

/**
 * @author Akshay Sharma
 * Jan 15, 2013
 */
object AssetHandler extends Controller {
  
  def delete(id: Long) = Action {
    Logger.debug("Going to delete asset with id: " + id)
    
    AssetDao.delete(id)
    Ok("200")
  }
}