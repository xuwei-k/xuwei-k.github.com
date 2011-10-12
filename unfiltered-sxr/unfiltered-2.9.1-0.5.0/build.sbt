scalaVersion := "2.9.1"

resolvers ++= Seq(
  "xuwei-k repo" at "http://xuwei-k.github.com/mvn"
)

addCompilerPlugin("org.scala-tools.sxr" % "sxr_2.9.1" % "0.2.8-SNAPSHOT")

scalacOptions <+= ( sourceDirectories in Compile) map (
  "-P:sxr:base-directory:" + _.mkString(":")
)

libraryDependencies ++= Seq(
   "javax.servlet" % "servlet-api" % "2.3"
  ,"org.scala-tools.testing" %% "specs" % "1.6.9"
  ,"net.databinder" %% "dispatch-mime" % "0.8.5"
  ,"net.databinder" %% "dispatch-http" % "0.8.5"
  ,"commons-io" % "commons-io" % "1.4"
  ,"commons-fileupload" % "commons-fileupload" % "1.2.1"
  ,"org.eclipse.jetty" % "jetty-webapp" % "7.2.2.v20101205"
  ,"org.eclipse.jetty" % "jetty-ajp" % "7.2.2.v20101205"
  ,"org.jboss.netty" % "netty" % "3.2.5.Final"
  ,"net.liftweb" %% "lift-json" % "2.4-M4"
  ,"net.databinder" %% "dispatch-oauth" % "0.8.5"
  ,"org.fusesource.scalate" % "scalate-core" % "1.5.2"
  ,"org.fusesource.scalate" % "scalate-util" % "1.5.2"
  ,"org.scala-lang" % "scala-compiler" % "2.9.1"
  ,"org.mockito" % "mockito-core" % "1.8.5"
  ,"org.scalatest" % "scalatest" % "1.3"
  ,"commons-codec" % "commons-codec" % "1.4"
)

(sourceGenerators in Compile) <+= (scalaVersion) map{
  v =>
  val unfilteredV = "0.5.0"
  def sourceURL(name:String) = {
    ScalaToolsReleases.root + "/net/databinder/unfiltered-" +
    name + "_" + v + "/" + unfilteredV + "/unfiltered-" +
    name + "_" + v + "-" + unfilteredV + "-sources.jar"
  }
  val srcURLs = Seq(
    "filter","agents","uploads","utils","jetty","jetty-ajp","netty-server",
    "netty","json","websockets","oauth","scalate","spec","scalatest"
  ).map{n => n -> sourceURL(n) } ++ Seq(
     "unfiltered" -> 
     { ScalaToolsReleases.root + "/net/databinder/unfiltered_" + v + "/" +
      unfilteredV + "/unfiltered_2.9.1-" + unfilteredV + "-sources.jar" }
  )
  srcURLs.flatMap{ case (name,url) =>
    IO.unzipURL(
       new java.net.URL(url)
      ,new File("src/main/scala/" + name )
      ,new SimpleFilter(_.endsWith("scala"))
    ).toSeq
  }
}

