/**
 * copyright VisualRendezvous
 */
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import play.api._
import play.api.cache._
import play.api.Play.current
import anorm._
import models.helpdesk.TicketStatus
import models.helpdesk.NavItem


/**
 * 
 * @author Akshay Sharma
 * Jan 20, 2013
 */
object Global extends GlobalSettings {
  
  override def onStart(app: Application) {
    Logger.debug("onStart - Calling inserts and init")
    val navitems = Navigation.init
    val statuses = Ticketstatus.init
    NavItem.cache(navitems)
    TicketStatus.cache(statuses)
    
  }
  
}

/**
 * Helpdesk Navigation
 */
object Navigation {

  def init: Seq[NavItem] = {
    val items = NavItem.all
    if(items.isEmpty) {
      
      Seq(
        NavItem("Tickets / Captures", "Highlights", "tickets/highlights", 1),
        NavItem("Tickets / Captures", "Photos", "tickets/photos", 1),
        NavItem("Tickets / Captures", "Audios", "tickets/audios", 1),
        NavItem("Tickets / Captures", "Speech to Texts", "tickets/s2t", 1),
        NavItem("Tickets / Captures", "Videos", "tickets/videos", 1),
//        NavItem("Tickets / Captures", "Screen Recordings", "tickets/screenrecordings", 1),
        NavItem("Tickets / Captures", "Email/Form/Direct", "tickets/simple", 1),
        NavItem("Tickets / Captures", "All Tickets/Captures", "tickets/all", 1),

//        NavItem("Funnels", "About", "funnels/about", 2),
        NavItem("Funnels", "Configure", "funnels/configure", 2),
//        NavItem("Funnels", "Customize", "funnels/customize", 2),
        
        NavItem("Services", "Configure", "services/configure", 3),
//        NavItem("Services", "Settings", "services/settings", 3),

        NavItem("Analytics", "Trends", "analytics/trends", 4),
        NavItem("Analytics", "Reports", "analytics/reports", 4),
        NavItem("Analytics", "Insights", "analytics/insights", 4),
//        NavItem("Analytics", "Performance", "analytics/performance", 4),
        
//        NavItem("Settings", "General", "settings/general", 5),
        NavItem("Settings", "Notifications", "settings/notifications", 5),
        NavItem("Settings", "Rules", "settings/rules", 5),
//        NavItem("Settings", "Template", "settings/template", 5),
        NavItem("Settings", "Integrations", "settings/integrations", 5),
        
        NavItem("Administration", "Api Key", "admin/apikey", 6),
        NavItem("Administration", "Agents", "admin/agents", 6),
        NavItem("Administration", "Roles / Groups", "admin/roles", 6),
//        NavItem("Administration", "Groups", "admin/groups", 6),
        NavItem("Administration", "Companies / People", "admin/companies", 6)
//        NavItem("Administration", "People", "admin/people", 6)
      ).foreach(NavItem.create)
    }
    items.isEmpty match {
      case true => NavItem.all
      case false => items
    }
  }
}
  
/**
 * Helpdesk Navigation
 */
object Ticketstatus {

  def init: Seq[TicketStatus] = {
    val statuses = TicketStatus.all
    if(statuses.isEmpty) {
      
      Seq(
        TicketStatus("Open"),
        TicketStatus("Closed"),
        TicketStatus("Hold")
      ).foreach(TicketStatus.create)
    }
    statuses.isEmpty match {
      case true => TicketStatus.all
      case false => statuses
    }
  }

}