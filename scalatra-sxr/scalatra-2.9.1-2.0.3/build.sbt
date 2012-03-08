resolvers += "xuwei-k repo" at "http://xuwei-k.github.com/mvn"

resolvers += "sonatype" at "https://oss.sonatype.org/content/repositories/releases/"

scalaVersion := "2.9.1"

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= (sourceDirectories in Compile).map{
  "-P:sxr:base-directory:" + _.mkString(":")
}

{
val V = "2.0.3"
val modules = Seq(
  "anti-xml","auth","fileupload","lift-json","scalate",
  "test","socketio","specs2","specs"//,"scalatest"
)
seq(
libraryDependencies ++= modules.map{ m =>
  "org.scalatra" %% ("scalatra-" + m) % V
}
,
(sourceGenerators in Compile) <+= (scalaVersion) map{
  S =>
  val sonatypeScalatra = "https://oss.sonatype.org/content/repositories/releases/org/scalatra/"
  def withUrl(m:String) = m -> <x>{sonatypeScalatra}scalatra-{m}_{S}/{V}/scalatra-{m}_{S}-{V}-sources.jar</x>.text 
  val base = "scalatra" -> <x>{sonatypeScalatra}scalatra_{S}/{V}/scalatra_{S}-{V}-sources.jar</x>.text 
  (base +: modules.map{withUrl} ).flatMap{case (name,url) =>
    IO.unzipURL(
      new java.net.URL(url)
      ,file("src") / "main" / "scala" / name
      ,new SimpleFilter(s => s.endsWith("scala") || s.endsWith("java") )
    ).toSeq
  }
}
)
}

