addCompilerPlugin("org.improving" %% "sxr" % "1.0.1")
  
scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

scalaVersion := "2.11.4"

val org = "org.http4s"
val modules = Seq(
  "argonaut",
  "blazecore",
  "blazeclient",
  "blazeserver",
  "client",
  "core",
  "dsl",
  "jawn",
  "jetty",
  "json4s-jackson",
  "json4s-native",
  "json4s",
  "server",
  "servlet",
  "tomcat"
).map("http4s-" + _)

val v = "0.5.2"

libraryDependencies <++= scalaVersion { sv =>
  modules.map{ n =>  org %% n % v }
}

(sourceGenerators in Compile) += task{
  val sv = scalaBinaryVersion.value
  modules.flatMap{ N =>
    val n = N + "_" + sv
    val u = s"""${DefaultMavenRepository.root}${org.replace('.', '/')}/${n}/${v}/${n}-${v}-sources.jar"""
    IO.unzipURL(
       url(u)
      ,baseDirectory.value / "src/main/scala" / N
      ,"*.scala" | "*.java"
    ).toSeq
  }
}
