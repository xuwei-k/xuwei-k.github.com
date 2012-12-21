scalaVersion := "2.10.0"

crossVersion := CrossVersion.full

resolvers ++= Seq(
  "http://xuwei-k.github.com/mvn",
  "https://oss.sonatype.org/content/repositories/releases"
).map{u => u at u}

addCompilerPlugin("org.scala-tools.sxr" % "sxr_2.10.0" % "0.2.8-SNAPSHOT")

scalacOptions <+= ( sourceDirectories in Compile) map (
  "-P:sxr:base-directory:" + _.mkString(":")
)

addCompilerPlugin("org.scala-lang.plugins" % "continuations" % "2.10.0")

scalacOptions += "-P:continuations:enable"

libraryDependencies <++= (scalaVersion) {
  v =>
  val SCALA = "org.scala-lang"
  Seq(
    SCALA % "jline" % v
   ,SCALA % "scala-compiler" % v
   ,"org.apache.ant" % "ant" % "1.8.2"
  )
}

seq(
/*
(sourceGenerators in Compile) <+= (scalaVersion) map {
  v =>
  val ROOT = Resolver.ScalaToolsReleasesRoot + "/org/scala-lang/" 
  ({ val n = "continuations"
    ROOT + "plugins/"+n+"/" + v + "/"+n+"-" + v + "-sources.jar"
  } +: Seq("scala-library","scala-compiler","scala-reflect","scalap","scala-partest","scala-actors").map{ n =>
    ROOT + n + "/" + v + "/"+n+"-" + v + "-sources.jar"
  }).flatMap{ u =>
    IO.unzipURL(
       url(u)
      ,file("src/main/scala")
      ,new SimpleFilter(f => f.endsWith("scala") || f.endsWith("java") )
    ).toSeq
  }
}
*/
)
