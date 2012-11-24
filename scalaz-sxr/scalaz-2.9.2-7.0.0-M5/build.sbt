resolvers += "xuwei-k repo" at "http://xuwei-k.github.com/mvn"

resolvers += "sonatype" at "https://oss.sonatype.org/content/repositories/releases/"

scalaVersion := "2.9.2"

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= (sourceDirectories in Compile).map{
  "-P:sxr:base-directory:" + _.mkString(":")
}

scalacOptions <++= (scalaVersion).map(sv =>
  Seq("-deprecation", "-unchecked") ++ (if(sv.contains("2.10")) None else Some("-Ydependent-method-types"))
)

{
val V = "7.0.0-M5"
val modules = Seq(
  "core","concurrent","effect","example","iteratee","iterv","scalacheck-binding","tests","typelevel"
)
seq(
libraryDependencies ++= modules.map{ m =>
  "org.scalaz" %% ("scalaz-" + m) % V
}
,
(sourceGenerators in Compile) <+= (scalaVersion) map{
  S =>
  val url = "https://oss.sonatype.org/content/repositories/releases/org/scalaz/"
  def withUrl(m:String) = m -> <x>{url}scalaz-{m}_{S}/{V}/scalaz-{m}_{S}-{V}-sources.jar</x>.text
  val base = "scalaz" -> <x>{url}scalaz_{S}/{V}/scalaz_{S}-{V}-sources.jar</x>.text
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

