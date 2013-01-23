/**
 * copyright VisualRendezvous
 */
package controllers.helpdesk

import play.api.mvc._
//import scala.concurrent.duration.DurationInt
//import play.api.libs.concurrent.Execution.Implicits.defaultContext
import models.core.dao.AssetDao
import models.core.Asset
import models.core.CaptureConstants


/**
 * @author Akshay Sharma
 * Jan 19, 2013
 */
object Helpdesk extends Controller {
  def index = Action {
    Ok(views.html.helpdesk("Demo - HighResIo"))
  }
  
  def ticketsAll = Action {
    val assets:Seq[Asset] = sortedAssets("all")
    sendOk("All tickets received or created", "Tickets > All", "All tickets", "all", assets)
  }
  
  def ticketsHighlight = Action {
    val assets:Seq[Asset] = sortedAssets(CaptureConstants.HIGHLIGHT)
    sendOk("All Highlight Tickets", "Tickets > Highlights", "Highlights", "image", assets)
  }
  
  def ticketsPhoto = Action {
    val assets:Seq[Asset] = sortedAssets(CaptureConstants.PHOTO)
    sendOk("All Photo Tickets", "Tickets > Photos", "Photos", "image", assets)
  }
  
  def ticketsAudio = Action {
    val assets:Seq[Asset] = sortedAssets(CaptureConstants.AUDIO_FLASH)
    sendOk("All Audio Tickets", "Tickets > Audios", "Audios", "audio", assets)
  }
  
  def ticketsS2T = Action {
    val assets:Seq[Asset] = sortedAssets(CaptureConstants.S2T)
    sendOk("All Speech to Text Tickets", "Tickets > Speech to Texts", "Speech2Texts", "s2t", assets)
  }
  
  def comingSoon = Action {
    Ok(views.html.comingsoon(title = "Coming soon..."))	
  }
  
  def funnelsConfigure = Action {
    Ok(views.html.funnels.hdfunnels())	
  }
  
  def servicesConfigure = Action {
    Ok(views.html.services.hdservices())	
  }
  
  
  
  private def sortedAssets(ref: String): Seq[Asset] = {
    val assets = ref match { 
	    case"all" => AssetDao.all
	    case _ => AssetDao.allForRefs(ref)
    }
    assets.sortWith((s, t) => s.created.compareTo(t.created) > 0)
  }
  
  private def sendOk(message: String, crumb: String, title: String, ref: String, assets: Seq[Asset]) = {
    Ok(views.html.tickets.list(
    message = message,
    crumb = crumb, 
    title = "(" + assets.size + ") " + title, 
    assets = assets,
    ref))		
  }
}