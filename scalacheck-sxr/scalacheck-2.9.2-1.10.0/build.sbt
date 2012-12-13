resolvers += "xuwei-k repo" at "http://xuwei-k.github.com/mvn"

resolvers += "sonatype" at "https://oss.sonatype.org/content/repositories/releases/"

scalaVersion := "2.9.2"

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= (sourceDirectories in Compile).map{
  "-P:sxr:base-directory:" + _.mkString(":")
}

{
val V = "1.10.0"
seq(
libraryDependencies += "org.scala-tools.testing" % "test-interface" % "0.5"
,
(sourceGenerators in Compile) <+= (scalaVersion) map{
  S =>
  val sonatype = "https://oss.sonatype.org/content/repositories/releases/org/scalacheck/"
  val u =  <x>{sonatype}scalacheck_{S}/{V}/scalacheck_{S}-{V}-sources.jar</x>.text 
  IO.unzipURL(
    url(u)
    ,file("src") / "main" / "scala"
    ,new SimpleFilter(s => s.endsWith("scala") || s.endsWith("java") )
  ).toSeq
}
)
}
