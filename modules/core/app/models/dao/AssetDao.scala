/**
 * copyright VisualRendezvous
 */
package models.core.dao

import play.api._
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.IOUtils
import java.io.FileOutputStream
import java.io.File
import java.util.UUID
import java.util.Date
import models.core.Image
import models.core.Audio
import models.core.Speech2Text
import models.core.Asset
import models.core.FileAsset
import models.core.CaptureResource
import models.core.Video
import models.core.Speech2Text
import java.util.NoSuchElementException
import models.core.Video

/**
 * @author Akshay Sharma
 * Dec 25, 2012
 */
object AssetDao {
  

  // Parse a ImageCapture from a ResultSet
  private val simple = {
    get[Long]("asset_capture.id") ~
    get[String]("asset_capture.email") ~
    get[String]("asset_capture.comment") ~
    get[String]("asset_capture.content") ~
    get[String]("asset_capture.extension") ~
    get[String]("asset_capture.asset_type") ~
    get[String]("asset_capture.ref") ~
    get[Date]("asset_capture.created") map {
      case id~email~comment~content~extension~assetType~ref~created => 
        assetType match {
        	case "image" => Image(email, comment, content, extension, ref, id, created)
        	case "audio" => Audio(email, comment, content, extension, ref, id, created)
        	case "s2t" => Speech2Text(email, comment, content, ref, id, created)
        	case "video" => Video(email, comment, content, extension, ref, id, created)
       	}
    }
  }

  def createAssetFromBase64(capture: Asset, base64Str: String): Asset = {
	val uniqueFileName = generateUniqueFileId()	  

    val asset = capture match {
	  case ic: Image => ic.copy(imageId = uniqueFileName)
	  case ac: Audio => ac.copy(audioId = uniqueFileName)
	}
	Logger.debug("createAssetFromBase64:: Updated content for this capture to :: " + asset.content)
	
	createAssetOnFile(asset, base64Str)
	create(asset)
  }
  
  def create(asset: Asset): Asset = {
    val insertId = saveAsset(asset)
    
    asset match {
	  case ic: Image => ic.copy(id = insertId)
	  case ac: Audio => ac.copy(id = insertId)
	  case s2t: Speech2Text => s2t.copy(id = insertId)
	  case vc: Video => vc.copy(id = insertId)
	}
  }
  
  private def saveAsset(asset: Asset): Long = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into asset_capture(content, email, comment, extension, asset_type, ref, created) values (
            {content}, {email}, {comment}, {extension}, {asset_type}, {ref}, {created}
          )
        """
      ).on(
        'email -> asset.email,
        'comment -> asset.comment,
        'content -> asset.content,
        'extension -> getExtension(asset),
        'asset_type -> asset.assetType,
        'ref -> asset.ref,
        'created -> asset.created
      ).executeInsert()
    } match {
      case Some(long) =>  long
      case None => throw new RuntimeException("Error occurred inserting for asset - " + asset)
    }
  }
  
  private def getExtension(asset: Asset):String = asset match {
    case fa: FileAsset => fa.extension 
    case _ => "" 
  }
  
  private def createAssetOnFile(capture: Asset, base64Str: String) = {
    val file = CaptureResource.from(capture)(capture.content + "." + getExtension(capture)).file
	val fileStream = new FileOutputStream(file);
	
	Logger.debug("createAssetOnFile:: file = " + file.getAbsolutePath())
	
	val base64: Base64 = new Base64
	val decoded = base64 decode(base64Str)
	
	//println("decoded image data = \n\n" + decoded)

	// TODO: Rewrite this using the Loan pattern? https://wiki.scala-lang.org/display/SYGN/Loan
	try {
		IOUtils.write(decoded, fileStream);
	} finally {
	  if (fileStream != null) fileStream.close();
	}
  } 
  
  private def generateUniqueFileId() = {UUID.randomUUID().toString()}
  
  def all(asset: Asset): Seq[Asset] = {
    DB.withConnection { implicit connection =>
      SQL("select * from asset_capture where asset_type = {assetType}")
      	.on("assetType" -> asset.assetType)
      	.as(AssetDao.simple *)
    }
  }

  def allForRefs(ref: Seq[String]): Seq[Asset] = {
    DB.withConnection { implicit connection =>
//      val refStr: String = ref.mkString("',  '")
//      Logger.debug("Selecting asset with ref string as ::" + refStr)
      	
//      SQL("select * from asset_capture where ref IN ({ref})")
//      	.on("ref" -> refStr)
//      	.as(AssetDao.simple *)
      
      // TODO: HUGE hole here -> Pron SQL Injection!!! The above should work but Anorm is screwing it up :(
      // More info at: http://stackoverflow.com/questions/9528273/in-clause-in-anorm
       SQL("select * from asset_capture where ref IN ('" + ref.mkString("', '") + "')")
       	.as(AssetDao.simple *)
    }

  }

  def all(): Seq[Asset] = {
    DB.withConnection { implicit connection =>
      SQL("select * from asset_capture")
      	.as(AssetDao.simple *)
    }
  }
  
  def delete(id: Long) = {
    DB.withConnection { implicit connection =>
      SQL("delete from asset_capture where id = {id}")
      	.on("id" -> id)
      	.executeUpdate
    }
  }
}