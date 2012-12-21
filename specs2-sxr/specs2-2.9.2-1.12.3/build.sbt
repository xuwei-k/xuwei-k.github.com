scalaVersion := "2.9.2"

scalaBinaryVersion <<= scalaVersion

crossVersion := CrossVersion.full

resolvers ++= Seq(
  "http://xuwei-k.github.com/mvn"
).map{u => u at u}

resolvers += Opts.resolver.sonatypeReleases

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= (sourceDirectories in Compile) map (
  "-P:sxr:base-directory:" + _.mkString(":")
)

libraryDependencies <++= scalaVersion { scala_version => Seq(
  "org.specs2" %% "specs2-scalaz-core" % "6.0.1",
  "org.scala-lang" % "scala-compiler" % scala_version % "optional",
  if (scala_version contains "-1") "org.scalacheck" % "scalacheck_2.9.1" % "1.9" % "optional"
  else                             "org.scalacheck" %% "scalacheck" % "1.9" % "optional",
  "org.scala-tools.testing" % "test-interface" % "0.5" % "optional",
  "org.hamcrest" % "hamcrest-all" % "1.1" % "optional",
  "org.mockito" % "mockito-all" % "1.9.0" % "optional",
  "junit" % "junit" % "4.7" % "optional",
  "org.pegdown" % "pegdown" % "1.0.2" % "optional",
  "org.specs2" % "classycle" % "1.4.1" % "optional"
  )
}

seq(
(sourceGenerators in Compile) <+= (scalaVersion) map {
  sv =>
  val v = "1.12.3"
  val n = "specs2" + "_" + sv
  val u = Resolver.ScalaToolsReleasesRoot + "/org/specs2/"+n+"/" + v + "/"+n+"-" + v + "-sources.jar"
  IO.unzipURL(
     url(u)
    ,file("src/main/scala")
    ,new SimpleFilter(f => f.endsWith("scala") || f.endsWith("java") )
  ).toSeq
}
)
