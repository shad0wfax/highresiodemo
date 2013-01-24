import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "highresio"
  val appVersion      = "1.0-SNAPSHOT"

//  val appDependencies = Seq(
//    // Add your project dependencies here,
//    jdbc,
//    anorm,
//    "commons-codec" % "commons-codec" % "1.7",
//    "commons-io" % "commons-io" % "2.4",
//    //"org.apache.httpcomponents" % "httpclient" % "4.2.1"
//    "org.scalaj" %% "scalaj-http" % "0.3.6"
//  )
//
//
//  val main = play.Project(appName, appVersion, appDependencies).settings(
//    // Add your own project settings here      
//  )

  val coreDeps = Seq(
    jdbc,
    anorm,
    "commons-codec" % "commons-codec" % "1.7",
    "commons-io" % "commons-io" % "2.4",
    //"org.apache.httpcomponents" % "httpclient" % "4.2.1"
    "org.scalaj" %% "scalaj-http" % "0.3.6"
  )

  val servercaptureDeps = Seq()
  val helpdeskDeps = Seq()
  val mainDeps = Seq()
  
  lazy val core = play.Project(appName + "-core", appVersion, coreDeps, path = file("modules/core"))
  lazy val servercapture = play.Project(
    appName + "-servercapture", appVersion, servercaptureDeps, path = file("modules/servercapture")
  ).dependsOn(core)
   .aggregate(core)
  
  lazy val helpdesk = play.Project(
    appName + "-helpdesk", appVersion, helpdeskDeps, path = file("modules/helpdesk")
  ).settings(
       templatesImport ++= Seq("models.core._", "models.helpdesk._")
  ).dependsOn(
      core
  ).aggregate(
      core
  )

  lazy  val main = play.Project(appName, appVersion, mainDeps).settings(
      // Add your own project settings here
      templatesImport ++= Seq("models.core._")
      //templatesImport += "models.core._" 
  ).dependsOn(
      core, servercapture, helpdesk
  ).aggregate(
       core, servercapture, helpdesk
  )
  
  
  
}
