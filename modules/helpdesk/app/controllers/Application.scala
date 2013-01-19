/**
 * copyright VisualRendezvous
 */
package controllers.helpdesk

import play.api.mvc.Action
import play.api.mvc.Controller

/**
 * @author Akshay Sharma
 * Dec 23, 2012
 */
object Application extends Controller {
  
  def index = Action {
    Ok("200")
  }  
}