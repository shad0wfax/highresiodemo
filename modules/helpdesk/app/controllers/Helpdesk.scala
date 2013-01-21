/**
 * copyright VisualRendezvous
 */
package controllers.helpdesk

import play.api.mvc._

/**
 * @author Akshay Sharma
 * Jan 19, 2013
 */
object Helpdesk extends Controller {
  def index = Action {
	  println("Coming into helpdesk")
       Ok(views.html.helpdesk("Demo - HighResIo"))
  }  
}