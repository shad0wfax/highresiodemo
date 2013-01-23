/**
 * copyright VisualRendezvous
 */
package views.util

import models.NavItem
import models.core._
import play.api.cache._
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
    Cache.getAs[Seq[NavItem]]("hdnavlist").get
  }
  
  def iconForAssetType(asset: Asset): String = (asset.ref: @unchecked) match {
    case CaptureConstants.HIGHLIGHT => "icon-pencil"
    case CaptureConstants.PHOTO => "icon-camera"
    case CaptureConstants.AUDIO_FLASH => "icon-headphones"
    case CaptureConstants.S2T => "icon-text-width"
  }
  
  def iconForAssets(assets: Seq[Asset], ref: String): String = ref match {
    case "all" => ""
    case _ => assets match {
	    case Nil => ""
	    case x :: xs => iconForAssetType(x)
    }
  }
  
}