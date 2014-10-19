scalaVersion := "2.10.4"

resolvers += Resolver.url("Typesafe Releases", url("http://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)

addCompilerPlugin("org.scala-sbt.sxr" %% "sxr" % "0.3.0")

scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }
 
val org = "com.github.julien-truffaut"

val modules = Seq(
  "core", "generic", "law", "macro"
).map("monocle-" + _)

val monocleVersion = "0.5.1"

sourceGenerators in Compile += task{
  val v = scalaBinaryVersion.value
  def sourceURL(name: String) = {
    val n = name + "_" + v
s"""${DefaultMavenRepository.root}${org.replace('.','/')}/${n}/${monocleVersion}/${n}-${monocleVersion}-sources.jar"""
  }
  val srcURLs = modules.map{n => n.replace("monocle-", "") -> sourceURL(n) }
  srcURLs.flatMap{ case (name, u) =>
    IO.unzipURL(
       url(u)
      ,baseDirectory.value / ("src/main/scala/" + name )
      ,"*.scala"
    ).toSeq
  }
}

libraryDependencies ++= modules.map{ m =>
  org %% m % monocleVersion
}

addCompilerPlugin("org.spire-math"  %% "kind-projector" % "0.5.2")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)

libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
