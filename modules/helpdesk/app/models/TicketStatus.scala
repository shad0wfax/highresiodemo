/**
 * copyright VisualRendezvous
 */
package models.helpdesk


import play.api.db._
import play.api.Play.current
import play.api.cache._
import anorm._
import anorm.SqlParser._
import scala.collection.immutable.Nil
import play.Logger

/**
 * 
 * @author Akshay Sharma
 * Jan 23, 2013
 */
case class TicketStatus(name: String, id: Long = -1)

object TicketStatus {
  private val CACHE_KEY = "ticketstatuses"

      // Parse a ImageCapture from a ResultSet
  private val simple = {
    get[Long]("hd_ticket_status.id") ~
    get[String]("hd_ticket_status.name") map {
      case id~name => TicketStatus(name, id)
    }
  }

  def create(status: TicketStatus): TicketStatus = {
    Logger.debug("inserting:: " + status)
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into hd_ticket_status(name) values (
            {name}
          )
        """
      ).on(
        'name -> status.name
      ).executeInsert()
    } match {
      case Some(long) =>  status.copy(id = long)
      case None => throw new RuntimeException("Error occurred inserting for status - " + status)
    }
  }
  
  def all(): Seq[TicketStatus] = {
    val statuses = Cache.getAs[Seq[TicketStatus]](CACHE_KEY)
    Logger.debug("cached statuses are of size::" + statuses.size)
    
    statuses match {
      case None => DB.withConnection { implicit connection =>
					SQL("select * from hd_ticket_status")
					.as(TicketStatus.simple *)
      				}
      case _ => statuses.get			      	
    }
    
  }

  def cache(statuses: Seq[TicketStatus]) = {
    Logger.debug("caching statuses of size::" + statuses.size)

    Cache.set(CACHE_KEY, statuses)
  }

}
