resolvers += "bintray/paulp" at "https://dl.bintray.com/paulp/maven"
 
addCompilerPlugin("org.improving" %% "sxr" % "1.0.1")
  
scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

scalaVersion := "2.11.4"

val org = "com.typesafe.akka"

val modules = Seq(
  "akka-http-core-experimental",
  "akka-http-experimental",
  "akka-http-xml-experimental",
  "akka-http-testkit-experimental",
  "akka-stream-experimental",
  "akka-stream-tck-experimental",
  "akka-stream-testkit-experimental",
  "akka-stream-tests-experimental",
  "akka-http-spray-json-experimental"
)

val akkaVersion = "1.0-M1"

libraryDependencies ++= modules.map(org %% _ % akkaVersion)

libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.3"

sourceGenerators in Compile += task{
  val v = akkaVersion
  modules.map{ m =>
    val n = m + "_" + scalaBinaryVersion.value
    "https://oss.sonatype.org/content/repositories/releases/" + org.replace('.', '/') + "/" + n + "/" + v + "/" + n + "-" + v + "-sources.jar"
  }.flatMap{ u =>
    println("downloading from " + u)
    IO.unzipURL(
      url(u),
      baseDirectory.value / "src" / "main" / "scala",
      new SimpleFilter(f => f.endsWith("scala") || f.endsWith("java"))
    ).toSeq
  }
}
