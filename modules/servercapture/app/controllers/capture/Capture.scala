/**
 * copyright VisualRendezvous
 */
package controllers.servercapture.capture

import scala.collection.immutable.Map
import scala.concurrent.duration.DurationInt
import akka.actor.Props
import models.servercapture.CaptureImage
import models.servercapture.Record
import models.servercapture.CaptureSpeech2Text
import models.servercapture.CaptureFlashAudio
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Request
import play.libs.Akka
import java.io.File
import java.util.UUID
import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.IOUtils
import java.io.FileOutputStream
import play.Logger
import models.core.VideoResource
import models.servercapture.CaptureAsset
import models.core.Video
import models.core.ImageResource
import models.core.Image
import models.core.CaptureConstants

/**
 * @author Akshay Sharma
 * Jan 10, 2013
 */
object Capture extends Controller {
  
  // 10 MB size limit for now
  def captureImage = Action(parse.urlFormEncoded(maxLength = 10 * 1024 * 1024)) { request =>
    capture(request, "image")
  }
  
  // 10 MB size limit for now
  def speechToText = Action(parse.urlFormEncoded(maxLength = 10 * 1024 * 1024)) { request =>
    capture(request, "speech2Text")
  }
  
    // 10 MB size limit for now
  def captureFlashAudio = Action(parse.urlFormEncoded(maxLength = 10 * 1024 * 1024)) { request =>
    capture(request, "audioFlash")
  }
  
//  def captureMobVideo = Action(storeVidFile) {request =>
//    val vidFile = request.body
//    
//    Ok("200") 
//  }
//  
//  val storeVidFile = parse.using {request =>
//    val vidFile = VideoResource(UUID.randomUUID().toString() + ".mov")
//    parse.file(to = vidFile.file)
//  }
//
  
//  def captureMobVideo = Action(parse.temporaryFile) { request =>
//	request.body.moveTo(VideoResource(UUID.randomUUID().toString() + ".mov").file)
//	Ok("File uploaded")
//  }
  
  def captureMobiVideo = Action(parse.multipartFormData) { request =>
    request.body.file("vid").map { vid =>
//	  val filename = vid.filename 
//	  val contentType = vid.contentType
//	  vid.ref.moveTo(VideoResource(filename).file)
	  
      
      val uniqueId = UUID.randomUUID().toString();
	  val vidRes = VideoResource(uniqueId + ".mov")
	  // Create the file using play's infrastructure!
	  vid.ref.moveTo(vidRes.file)
	  
	  asyncSave(CaptureAsset(
	    Video("creativeaisle@gmail.com", "Default comment - See my video", uniqueId, "mov", CaptureConstants.VIDEO_MOBILE)))
	  
	  Ok("File uploaded")
	}.getOrElse {
	  Ok("Not Ok!")
	}
  }
  
  def captureMobiPicture = Action(parse.multipartFormData) { request =>
    request.body.file("pic").map { pic =>
      val uniqueId = UUID.randomUUID().toString();
	  val picRes = ImageResource(uniqueId + ".mov")
	  // Create the file using play's infrastructure!
	  pic.ref.moveTo(picRes.file)
	  
	  asyncSave(CaptureAsset(
	    Image("creativeaisle@gmail.com", "Default comment - My thousand words", uniqueId, "mov", CaptureConstants.PHOTO_MOBILE)))
	  
	  Ok("File uploaded")
	}.getOrElse {
	  Ok("Not Ok!")
	}
  }


  
  private def capture(request: Request[Map[String,Seq[String]]], capType: String) = {
	val body: Map[String, Seq[String]] = request.body
	val dataBody: Option[Seq[String]] = body.get("data") 

	// Expecting data body
	// println("Testing to see if data :: dataBody = " + dataBody)
	
	dataBody.map { datas =>
	  val capture = capType match {
	    case "image" => CaptureImage(datas)
	    case "speech2Text" => CaptureSpeech2Text(datas)
	    case "audioFlash" => CaptureFlashAudio(datas) 
	  }
	  asyncSave(capture)
	  
	  Ok("200")
	}.getOrElse {
		Logger.debug("data parameter not sent in the request body")
    	BadRequest("Expecting urlFormEncoded data request body")  
  	}
    
  }
  
  private def asyncSave(capture: Any) = {
    // TODO: Improve Akka usage - follow webchat sample instead of scheduling mechanism (message sending)
	// Schedule right away
	val captureActor = Akka.system.actorOf(Props(new Record()))
	Akka.system.scheduler.scheduleOnce(0 second, captureActor, capture)
  }
  
}