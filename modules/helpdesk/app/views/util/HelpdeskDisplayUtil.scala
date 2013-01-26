/**
 * copyright VisualRendezvous
 */
package views.util

import models.helpdesk.NavItem
import models.core._
import play.api.Play.current
import scala.collection.immutable.TreeMap
import scala.collection.immutable.Map

/**
 * @author Akshay Sharma
 * Jan 21, 2013
 */
object HelpdeskDisplayUtil {
  
  /**
   * Get the navigation as a map ordered (treemap), by groupNum, with each value containing a list of NavItem for that group.
   */
  lazy val navigation: Map[Int, Seq[NavItem]] = TreeMap(navlist.groupBy(_.groupNum).toSeq:_*)

  private def navlist: Seq[NavItem] = {
    NavItem.all
  }
  
  def iconForAssetType(asset: Asset): String = (asset.ref: @unchecked) match {
    case CaptureConstants.HIGHLIGHT => "icon-pencil"
    case CaptureConstants.PHOTO | CaptureConstants.PHOTO_MOBILE => "icon-camera"
    case CaptureConstants.AUDIO_FLASH => "icon-headphones"
    case CaptureConstants.S2T => "icon-text-width"
    case CaptureConstants.VIDEO_MOBILE | CaptureConstants.VIDEO_WEB => "icon-facetime-video"
  }
  
  def iconForAssets(assets: Seq[Asset], ref: String): String = ref match {
    case "all" => ""
    case _ => assets match {
	    case Nil => ""
	    case x :: xs => iconForAssetType(x)
    }
  }
  
  def countTickets(ref: String, groupedAssets: Map[String, Seq[Asset]]): Int = {
    groupedAssets.get(ref) match {
      case None => 0
      case Some(x) => x.size
    }
  }
  
}