resolvers ++= {
  def r(url:String) = url at url
  Seq(
    "http://xuwei-k.github.com/mvn"
   ,"https://oss.sonatype.org/content/repositories/releases/"
   ,"http://repo.typesafe.com/typesafe/maven-releases/"
   ,"http://repo.akka.io/releases/"
  ) map r
}

scalaVersion := "2.9.1"

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= (sourceDirectories in Compile).map{
  "-P:sxr:base-directory:" + _.mkString(":")
}

libraryDependencies <++= (sbtDependency,scalaVersion){(s,v) =>
  Seq(
    s
   ,"org.scala-lang" % "scala-compiler" % v
   ,"org.scala-lang" % "jline" % v
  )
}

{
val V = "2.0"
val modules = Seq("anorm","console","play-test","templates","play")
seq(
libraryDependencies ++= modules.map{
  "play" %% _ % V
}
,
(sourceGenerators in Compile) <+= (scalaVersion) map{
  S =>
  val playRepo = "http://xuwei-k.github.com/mvn/play/"
  def withUrl(m:String) = m -> <x>{playRepo}{m}_{S}/{V}/{m}_{S}-{V}-sources.jar</x>.text
  modules.map{withUrl}.flatMap{case (name,url) =>
    IO.unzipURL(
      new java.net.URL(url)
      ,file("src") / "main" / "scala" / name
      ,new SimpleFilter(s => s.endsWith("scala") || s.endsWith("java") )
    ).toSeq
  }
}
)
}

