scalaVersion := "2.10.3"

resolvers ++= Seq(
  "xuwei-k repo" at "http://xuwei-k.github.com/mvn"
)

addCompilerPlugin("org.scala-sbt.sxr" %% "sxr" % "0.3.0-SNAPSHOT")

scalacOptions <+= ( sourceDirectories in Compile) map (
  "-P:sxr:base-directory:" + _.mkString(":")
)

{
val org = "net.databinder"
val modules = Seq(
  "filter","agents","uploads","filter-uploads","util","jetty","jetty-ajp","netty-server","netty-uploads",
  "netty","json4s","netty-websockets","oauth","oauth2","mac","spec","scalatest","filter-async","directives"
).map("unfiltered-" + _) :+ "unfiltered"
val unfilteredV = "0.7.0"
seq(
(sourceGenerators in Compile) <+= scalaBinaryVersion.map{
  v =>
  def sourceURL(name: String) = {
    val n = name + "_" + v
s"""${DefaultMavenRepository.root}${org.replace('.','/')}/${n}/${unfilteredV}/${n}-${unfilteredV}-sources.jar"""
  }
  val srcURLs = modules.map{n => n.replace("unfiltered-","") -> sourceURL(n) }
  srcURLs.flatMap{ case (name,u) =>
    IO.unzipURL(
       url(u)
      ,file("src/main/scala/" + name )
      ,"*.scala" | "*.java"
    ).toSeq
  }
}
,
libraryDependencies ++= modules.map{ m =>
  org %% m % unfilteredV
}
)
}
