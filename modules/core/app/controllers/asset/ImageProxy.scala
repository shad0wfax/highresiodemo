/**
 * copyright HighresiO
 */
package controllers.core.asset

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Controller
import models.core.dao.AssetDao
import play.Logger
import play.api.libs.ws.WS
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import org.apache.commons.codec.binary.Base64
import play.api.mvc.SimpleResult
import play.api.mvc.ResponseHeader
import play.api.libs.iteratee.Enumerator
import play.api.libs.json.Json

/**
 * @author Akshay Sharma
 * Mar 1, 2013
 */
object ImageProxy extends Controller {
  
  /**
   * Based on the logic from this nodejs proxy for html2canvas:
   * https://github.com/BugHerd/html2canvas-proxy-node
   * Original proxy in python:
   * https://github.com/niklasvh/html2canvas-proxy
   */
  def improxy = Action { request =>
    Logger.debug("Got the following params: " + request)
    val queryString: Map[String, Seq[String]] = request.queryString
    val url = queryString.get("url").get(0)
    val callback = queryString.get("callback").get(0)
    Logger.debug("params  : " + url + "\t" + callback)
    
    if (url == null || callback == null) {
      BadRequest(callback + "('error:Missing params')")
    }

    val imageBinary = getImage(url)
    
    if (imageBinary.isDefined) {
      val image = imageBinary.get._1
      val contentType = imageBinary.get._2.get
      val base64: Base64 = new Base64
      val imageBase64 = new String(base64.encode(image))
      
      Logger.debug(" type of imageBinary.get  = " + imageBase64.getClass.getCanonicalName
        + "\t content-type = " + contentType)

      val responseData  = "data:" + contentType + ";base64, "  + imageBase64
      val res: String  = callback + "('" + responseData + "')"

      SimpleResult(
        header = ResponseHeader(200,
          Map(
            CONTENT_TYPE -> "application/javascript",
            "Access-Control-Allow-Origin" -> "*",
            "Access-Control-Request-Method" -> "*",
            "Access-Control-Allow-Methods" -> "OPTIONS, GET",
            "Access-Control-Allow-Headers" -> "*"
          )
        ),
        body = Enumerator(res)
      )

    } else { 
      BadRequest(callback + "('error:Application error')")
    }
    
  }
  
  private def getImage(url: String) = {
    val responseFuture = WS.url(url)
      .get()

    val resultFuture = responseFuture map { response =>
      response.status match {
        case 200 => Some((response.getAHCResponse.getResponseBodyAsBytes, response.header("Content-Type")))
        case _ => None
      }
    }

    Await.result(resultFuture, 5 seconds)
  }
}