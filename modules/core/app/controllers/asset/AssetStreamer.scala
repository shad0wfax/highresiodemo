/**
 * copyright VisualRendezvous
 */
package controllers.core.asset

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.iteratee.Enumerator
import java.io.FileNotFoundException
import models.core.CaptureResource
import models.core.ImageResource
import models.core.AudioResource
import models.core.VideoResource
import java.io.FileInputStream
import java.io.BufferedInputStream
import java.io.File
import play.api.mvc.SimpleResult
import play.api.mvc.ResponseHeader
import play.api.mvc.Request

/**
 * @author Akshay Sharma
 * Jan 15, 2013
 */
object AssetStreamer extends Controller {
  
  // TODO: Make this chunked response for memory savings! - For now this is only serving and not streaming 
  def serve(assetId: String, xtn:String, assetType: String) = Action {

    val f2s: String = fileToServe(assetId, xtn, assetType)
	val file = new java.io.File(f2s)
	
	if (file.exists())
	  Ok.sendFile(content = file)
	else
	  Ok("404") 
  }

  // TODO: Make this chunked response for memory savings! - For now this is only serving and not streaming 
  def stream(assetId: String, xtn:String, assetType: String) = Action { request =>
    val f2s: String = fileToServe(assetId, xtn, assetType)
	val file = new File(f2s)
	
	if (file.exists()) {
	  request.headers.get("Range") match {
	    case Some(rangeHdr) => streamWithRangeRequest(request, rangeHdr, file, xtn)
	    case None => Ok.sendFile(content = file)  
	  }
	} else
	  Ok("404") 
  }
  
  // Taken from https://groups.google.com/forum/?fromgroups=#!topic/play-framework/-BN2eUXtzjI
  private def streamWithRangeRequest(request: Request[play.api.mvc.AnyContent], rangeHdr: String, file: File, xtn: String) = {
	  val range: (Long,Long) = rangeHdr.substring("bytes=".length).split("-") match {
	    case x if x.length == 1 => (x.head.toLong, file.length()-1)
	    case x => (x(0).toLong, x(1).toLong)
	  }
	
	  range match { case (start,end) =>
	    val stream =  new BufferedInputStream(new FileInputStream(file))
	    stream.skip(start)
	
	    SimpleResult(
	      header = ResponseHeader(PARTIAL_CONTENT,
	        Map(
	          CONNECTION -> "keep-alive",
	          ACCEPT_RANGES -> "bytes",
	          CONTENT_RANGE -> "bytes %d-%d/%d".format(start, end, file.length()),
	          CONTENT_LENGTH -> (end - start + 1).toString,
	          CONTENT_TYPE -> play.api.libs.MimeTypes.forExtension(xtn).get
	        )
	      ),
	      body = Enumerator.fromStream(stream)
	    )
	  }
  }
  
  private def fileToServe(assetId: String, xtn:String, assetType: String): String = assetType match {
    case "image" => ImageResource(assetId + "." + xtn).fileUrl
    case "audio" => AudioResource(assetId + "." + xtn).fileUrl
    case "video" => VideoResource(assetId + "." + xtn).fileUrl
    case _ => "" 
  }

}