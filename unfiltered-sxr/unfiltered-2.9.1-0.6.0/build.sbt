scalaVersion := "2.9.1"

resolvers ++= Seq(
  "xuwei-k repo" at "http://xuwei-k.github.com/mvn"
)

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= ( sourceDirectories in Compile) map (
  "-P:sxr:base-directory:" + _.mkString(":")
)

{
val modules = Seq(
  "filter","agents","uploads","util","jetty","jetty-ajp","netty-server",
  "netty","json","netty-websockets","oauth","spec","scalatest","filter-async"
)
val unfilteredV = "0.6.0"
seq(
(sourceGenerators in Compile) <+= (scalaVersion) map{
  v =>
  def sourceURL(name:String) = {
    DefaultMavenRepository.root + "/net/databinder/unfiltered-" +
    name + "_" + v + "/" + unfilteredV + "/unfiltered-" +
    name + "_" + v + "-" + unfilteredV + "-sources.jar"
  }
  val srcURLs = modules.map{n => n -> sourceURL(n) } ++ Seq(
     "unfiltered" ->
     { DefaultMavenRepository.root + "/net/databinder/unfiltered_" + v + "/" +
      unfilteredV + "/unfiltered_" + v + "-" + unfilteredV + "-sources.jar" }
  )
  srcURLs.flatMap{ case (name,url) =>
    IO.unzipURL(
       new java.net.URL(url)
      ,new File("src/main/scala/" + name )
      ,new SimpleFilter(_.endsWith("scala"))
    ).toSeq
  }
}
,
libraryDependencies ++= modules.map{ m =>
  "net.databinder" %% ("unfiltered-" + m)  % unfilteredV
}
)
}
