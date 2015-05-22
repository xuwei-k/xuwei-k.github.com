scalaVersion := "2.11.6"

resolvers += "bintray/paulp" at "https://dl.bintray.com/paulp/maven"

addCompilerPlugin("org.improving" %% "sxr" % "1.0.1")

scalacOptions += {
  "-P:sxr:base-directory:" + (sourceDirectories in Compile).value.mkString(":")
}

val org = "org.apache.kafka"
val modules = Seq("kafka")
val v = "0.8.2.1"

libraryDependencies ++= modules.map{ n => org %% n % v }

(sourceGenerators in Compile) += task{
  def sourceURL(name: String) = {
    val n = name + "_" + scalaBinaryVersion.value
    s"""${DefaultMavenRepository.root}${org.replace('.','/')}/${n}/${v}/${n}-${v}-sources.jar"""
  }
  modules.map{n => n -> sourceURL(n) }.flatMap{ case (moduleName, u) =>
    IO.unzipURL(
      url(u),
      baseDirectory.value / "src/main/scala" / moduleName,
      "*.scala" | "*.java"
    ).toSeq
  }
}
