/**
 * copyright VisualRendezvous
 */
package views.util

import models.NavItem
import play.api.cache._
import play.api.Play.current
import scala.collection.immutable.TreeMap
import scala.collection.immutable.Map

/**
 * @author Akshay Sharma
 * Jan 21, 2013
 */
object NavList {
  
  /**
   * Get the navigation as a map ordered (treemap), by groupNum, with each value containing a list of NavItem for that group.
   */
  lazy val navigation: Map[Int, Seq[NavItem]] = TreeMap(navlist.groupBy(_.groupNum).toSeq:_*)

  private def navlist: Seq[NavItem] = {
    Cache.getAs[Seq[NavItem]]("hdnavlist").get
  }
}