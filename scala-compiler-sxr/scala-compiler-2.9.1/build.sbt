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
