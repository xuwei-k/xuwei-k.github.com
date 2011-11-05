scalaVersion := "2.9.1"

resolvers ++= Seq(
  "xuwei-k repo" at "http://xuwei-k.github.com/mvn"
)

addCompilerPlugin("org.scala-tools.sxr" % "sxr_2.9.1" % "0.2.8-SNAPSHOT")

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
  val u = Resolver.ScalaToolsReleasesRoot + "/org/scala-lang/scala-compiler/2.9.1/scala-compiler-2.9.1-sources.jar"
  IO.unzipURL(
     new java.net.URL(u)
    ,file("src/main/scala")
    ,new SimpleFilter(f => f.endsWith("scala") || f.endsWith("java") )
  ).toSeq
}
*/
)

