resolvers += Resolver.url("Typesafe Releases", url("http://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)

addCompilerPlugin("org.scala-sbt.sxr" %% "sxr" % "0.3.0")

scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

scalaVersion := "2.10.4"

scalacOptions <+= (sourceDirectories in Compile).map{
  "-P:sxr:base-directory:" + _.mkString(":")
}

scalacOptions += "-Ystop-after:sxr"

val modules = Seq(
  "actor", "testkit", "remote", "kernel", "zeromq", "osgi", "dataflow", "file-mailbox",
  "multi-node-testkit", "cluster", "slf4j", "agent", "camel", "contrib", "persistence-experimental",
  "transactor"
)

val akkaVersion = "2.3.7"
val org = "com.typesafe.akka"

libraryDependencies ++= modules.map(m =>
  org %% s"akka-$m" % akkaVersion
)

(sourceGenerators in Compile) += task{
  val v = scalaBinaryVersion.value
  def sourceURL(name: String) = {
    val n = name + "_" + v
s"""${DefaultMavenRepository.root}${org.replace('.','/')}/${n}/${akkaVersion}/${n}-${akkaVersion}-sources.jar"""
  }
  val srcURLs = modules.map{n => n -> sourceURL("akka-" + n) }
  srcURLs.flatMap{ case (name,u) =>
    IO.unzipURL(
       url(u)
      ,baseDirectory.value / ("src/main/scala/" + name )
      ,"*.scala" | "*.java"
    ).toSeq
  }
}

