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

/**
 * @author Akshay Sharma
 * Jan 15, 2013
 */
object AssetStreamer extends Controller {
  
  // TODO: Make this chunked response for memory savings! - For now this is only serving and not streaming 
  def stream(assetId: String, xtn:String, assetType: String) = Action {

    val fileToServe: String = assetType match {
	  case "image" => ImageResource(assetId + "." + xtn).fileUrl
	  case "audio" => AudioResource(assetId + "." + xtn).fileUrl
	  case "video" => VideoResource(assetId + "." + xtn).fileUrl
	  case _ => "" 
	}
	val file = new java.io.File(fileToServe)
	
	
	if (file.exists())
	  Ok.sendFile(content = file)
	else
	  Ok("404") 

  	

	
	  
//    val data = getDataStream    
//    Ok.stream(
//      Enumerator("", "", "").andThen(Enumerator.eof)
//    )
  }

}