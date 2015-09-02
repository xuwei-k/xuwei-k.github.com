resolvers += "bintray/paulp" at "https://dl.bintray.com/paulp/maven"
 
addCompilerPlugin("org.improving" %% "sxr" % "1.0.1")
  
scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

scalaVersion := "2.11.7"

val org = "com.typesafe.akka"

val prefix = "akka-"

val modules: Seq[String] = """cluster-sharding
contrib
cluster-metrics
distributed-data-experimental
cluster-tools
persistence-tck
cluster
multi-node-testkit
camel
typed-experimental
persistence
remote
slf4j
osgi
testkit
kernel
actor""".split("\n").map{prefix + _}.toSeq

val akkaVersion = "2.4.0-RC1"

libraryDependencies ++= modules.map(org %% _ % akkaVersion)

// https://github.com/akka/akka/blob/v2.4.0-RC1/project/Dependencies.scala
libraryDependencies ++= (
  ("org.fusesource" % "sigar" % "1.6.4") ::
  ("org.iq80.leveldb" % "leveldb" % "0.7") ::
  ("org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8") ::
  ("io.kamon" % "sigar-loader" % "1.6.6-rev002") ::
  Nil
)

sourceGenerators in Compile += task{
  val v = akkaVersion
  modules.flatMap{ m =>
    val n = m + "_" + scalaBinaryVersion.value
    val u = "https://oss.sonatype.org/content/repositories/releases/" + org.replace('.', '/') + "/" + n + "/" + v + "/" + n + "-" + v + "-sources.jar"
    println("downloading from " + u)
    IO.unzipURL(
      url(u),
      baseDirectory.value / "src" / "main" / "scala" / m.drop(prefix.length),
      new SimpleFilter(f => f.endsWith("scala") || f.endsWith("java"))
    ).toSeq
  }
}
