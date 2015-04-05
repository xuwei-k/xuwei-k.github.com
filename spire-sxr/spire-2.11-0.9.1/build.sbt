scalaVersion := "2.11.6"

resolvers += Opts.resolver.sonatypeReleases

resolvers += "bintray/paulp" at "https://dl.bintray.com/paulp/maven"

addCompilerPlugin("org.improving" %% "sxr" % "1.0.1")

scalacOptions += {
  "-P:sxr:base-directory:" + (sourceDirectories in Compile).value.mkString(":")
}

scalacOptions += "-language:experimental.macros"

val org = "org.spire-math"
val modules = Seq("spire", "spire-scalacheck-binding", "spire-macros")
val v = "0.9.1"

libraryDependencies ++= modules.map{ n => org %% n % v }

(sourceGenerators in Compile) += task{
  val sv = scalaBinaryVersion.value
  modules.flatMap{ N =>
    val n = N + "_" + sv
    val u = <x>{Resolver.SonatypeRepositoryRoot}/{"releases"}/{org.replace('.','/')}/{n}/{v}/{n}-{v}-sources.jar</x>.text
    IO.unzipURL(
      url(u),
      baseDirectory.value / "src/main/scala" / N,
      "*.scala"
    ).toSeq
  }
}
