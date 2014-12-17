resolvers += "bintray/paulp" at "https://dl.bintray.com/paulp/maven"
 
addCompilerPlugin("org.improving" %% "sxr" % "1.0.1")
  
scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

scalaVersion := "2.11.4"

ivyScala ~= { _.map(_.copy(overrideScalaVersion = true)) }

val org = "org.scalatra"

val modules = (
"common auth fileupload atmosphere scalate json commands jetty test scalatest specs2 swagger swagger-ext spring slf4j"
).split(' ').map("scalatra-" + _).toSeq :+ "scalatra"

val v = "2.4.0.M2"

libraryDependencies ++= modules.map(org %% _ % v)

val json4sVersion = "3.2.10"

libraryDependencies ++= (
  ("org.json4s" %% "json4s-jackson" % json4sVersion) ::
  ("org.json4s" %% "json4s-native" % json4sVersion) ::
  ("ch.qos.logback" % "logback-classic" % "1.1.2") ::
  ("org.testng" % "testng" % "6.8.8") ::
  Nil
)

sourceGenerators in Compile += task{
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
