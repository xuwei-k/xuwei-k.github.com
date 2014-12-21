resolvers += "bintray/paulp" at "https://dl.bintray.com/paulp/maven"
 
addCompilerPlugin("org.improving" %% "sxr" % "1.0.1")
  
scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

scalacOptions += "-language:experimental.macros"

scalaVersion := "2.11.4"

val org = "org.parboiled"

val modules = Seq(
  "parboiled"
)

val parboiledVersion = "2.0.1"

libraryDependencies ++= modules.map(org %% _ % parboiledVersion)

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

sourceGenerators in Compile += task{
  val v = parboiledVersion
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
