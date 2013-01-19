resolvers ++= {
  Seq(
    "http://xuwei-k.github.com/mvn"
  ) map { u => u at u}
}

resolvers += Opts.resolver.sonatypeReleases

scalaVersion := "2.9.2"

scalacOptions ++= Seq("-deprecation", "-unchecked")

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= (sourceDirectories in Compile).map{
  "-P:sxr:base-directory:" + _.mkString(":")
}

libraryDependencies += "org.scalatest" %% "scalatest" % "1.8"

{
val V = "2.5.0"
val org = "org.mongodb"
val modules = "core gridfs query commons".split(' ').map{"casbah-"+}
seq(
libraryDependencies ++= modules.map{
  org %% _ % V
}.toSeq
,
sourceGenerators in Compile <+= scalaVersion.map{ S =>
  val sonatype = "https://oss.sonatype.org/content/repositories/releases/"
  def withUrl(m:String) = m -> <x>{sonatype}{org.replace('.','/')}/{m}_{S}/{V}/{m}_{S}-{V}-sources.jar</x>.text
  modules.map{withUrl}.flatMap{case (name,url) =>
    IO.unzipURL(
      new URL(url)
      ,file("src") / "main" / "scala" / name
      ,"*.scala" | "*.java"
    )
  }.toSeq
}
)
}

