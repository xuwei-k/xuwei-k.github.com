scalaVersion := "2.10.0-M3"

resolvers ++= Seq(
  "http://xuwei-k.github.com/mvn",
  "https://oss.sonatype.org/content/repositories/releases"
).map{u => u at u}

addCompilerPlugin("org.scala-tools.sxr" % "sxr_2.10.0-M3" % "0.2.8-SNAPSHOT")

scalacOptions <+= ( sourceDirectories in Compile) map (
  "-P:sxr:base-directory:" + _.mkString(":")
)

libraryDependencies <++= (scalaVersion) {
  v =>
  val SCALA = "org.scala-lang"
  Seq(
    SCALA % "jline" % v
   ,SCALA % "scalap" % v
   ,"org.apache.ant" % "ant" % "1.8.2"
  )
}

seq(
/*
(sourceGenerators in Compile) <+= (scalaVersion) map {
  v =>
  Seq("library","compiler").map{ n =>
    Resolver.ScalaToolsReleasesRoot + "/org/scala-lang/scala-"+n+"/2.10.0-M3/scala-"+n+"-2.10.0-M3-sources.jar"
  }.flatMap{ u =>
    IO.unzipURL(
       new java.net.URL(u)
      ,file("src/main/scala")
      ,new SimpleFilter(f => f.endsWith("scala") || f.endsWith("java") )
    ).toSeq
  }
}
*/
)

