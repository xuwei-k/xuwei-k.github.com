scalaVersion := "2.11.6"

resolvers += "bintray/paulp" at "https://dl.bintray.com/paulp/maven"

val scalazBintray = "https://dl.bintray.com/scalaz/releases"

resolvers += "Scalaz Bintray Repo" at scalazBintray

addCompilerPlugin("org.improving" %% "sxr" % "1.0.1")

scalacOptions += {
  "-P:sxr:base-directory:" + (sourceDirectories in Compile).value.mkString(":")
}

val org = "org.scalaz.stream"
val modules = Seq("scalaz-stream")
val v = "0.7a"

libraryDependencies ++= modules.map{ n => org %% n % v }

(sourceGenerators in Compile) += task{
  val sv = scalaBinaryVersion.value
  modules.flatMap{ N =>
    val n = N + "_" + sv
    val u = s"${scalazBintray}/${org.replace('.','/')}/$n/$v/$n-$v-sources.jar"
    IO.unzipURL(
      url(u),
      baseDirectory.value / "src/main/scala" / N,
      "*.scala"
    ).toSeq
  }
}
