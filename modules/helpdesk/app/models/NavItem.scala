/**
 * copyright VisualRendezvous
 */
package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

/**
 * @author Akshay Sharma
 * Jan 20, 2013
 */

case class NavItem(group: String, item: String, relativeUrl: String, groupNum: Int, id: Long = -1)


object NavItem {
  
    // Parse a ImageCapture from a ResultSet
  private val simple = {
    get[Long]("hd_nav_item.id") ~
    get[String]("hd_nav_item.group_name") ~
    get[String]("hd_nav_item.item") ~
    get[Int]("hd_nav_item.group_num") ~
    get[String]("hd_nav_item.relative_url") map {
      case id~group~item~groupNum~relativeUrl => NavItem(group, item, relativeUrl, groupNum, id)
    }
  }

  def create(item: NavItem): NavItem = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into hd_nav_item(group_name, item, relative_url, group_num) values (
            {group}, {item}, {relativeUrl}, {groupNum}
          )
        """
      ).on(
        'group -> item.group,
        'item -> item.item,
        'relativeUrl -> item.relativeUrl,
        'groupNum -> item.groupNum
      ).executeInsert()
    } match {
      case Some(long) =>  item.copy(id = long)
      case None => throw new RuntimeException("Error occurred inserting for asset - " + item)
    }
  }
  
  def all(): Seq[NavItem] = {
    DB.withConnection { implicit connection =>
      SQL("select * from hd_nav_item")
      	.as(NavItem.simple *)
    }
  }

}