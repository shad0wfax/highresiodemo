import play.api._
import play.api.cache._
import play.api.Play.current

import models._
import anorm._


object Global extends GlobalSettings {
  
  override def onStart(app: Application) {
    InitialData.insert()
  }
  
}

/**
 * Initial set of data to be imported 
 * in the sample application.
 */
object InitialData {

  def insert() = {
    val items = NavItem.all
    if(items.isEmpty) {
      
      Seq(
        NavItem("Tickets", "Highlights", "tickets/highlights", 1),
        NavItem("Tickets", "Photos", "tickets/photos", 1),
        NavItem("Tickets", "Audios", "tickets/audios", 1),
        NavItem("Tickets", "Speech to Texts", "tickets/s2t", 1),
        NavItem("Tickets", "Videos", "tickets/videos", 1),
//        NavItem("Tickets", "Screen Recordings", "tickets/screenrecordings", 1),
        NavItem("Tickets", "Email/ Form/ Direct", "tickets/simple", 1),
        NavItem("Tickets", "All Tickets", "tickets/all", 1),

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
    
    val cacheNav = items.isEmpty match {
      case true => NavItem.all
      case false => items
    }
    
    Cache.set("hdnavlist", cacheNav)
  }
  
}