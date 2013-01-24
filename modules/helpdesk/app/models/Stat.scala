/**
 * copyright VisualRendezvous
 */
package models.helpdesk

import models.core.Asset
import scala.collection.immutable.Map
import models.core.dao.AssetDao

/**
 * @author Akshay Sharma
 * Jan 23, 2013
 */
//trait Stat {
//  def name: String
//} 
//case class TicketStat(name: String, counts:Seq[(String, String)]) extends Stat


object HDStat {

  def dashboardStat: Map[String, Seq[Asset]] = {
    val allAssets = AssetDao.all
   	allAssets.groupBy(_.ref)    
  }
  
}

