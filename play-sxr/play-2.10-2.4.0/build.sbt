resolvers ++= Seq(
  "typesafe" at "http://repo.typesafe.com/typesafe/maven-releases/"
)

scalaVersion := "2.10.4"

scalacOptions <+= (sourceDirectories in Compile).map{
  "-P:sxr:base-directory:" + _.mkString(":")
}

resolvers +=  "bintray/paulp" at "https://dl.bintray.com/paulp/maven"

addCompilerPlugin("org.improving" %% "sxr" % "1.0.1")

val org = "com.typesafe.play"
val V = "2.4.0"
val modules1 = augmentString(
"""
anorm-tokenizer
anorm
filters-helpers
fork-run
play-akka-http-server-experimental
play-cache
play-datacommons
play-docs
play-functional
play-integration-test
play-iteratees
play-java-jdbc
play-java-jpa
play-java-ws
play-java
play-jdbc-api
play-jdbc-evolutions
play-jdbc
play-json
play-netty-server
play-server
play-specs2
play-streams-experimental
play-test
play-ws
play
run-support
"""
).lines.map(_.trim).filter(_.nonEmpty).toSeq

val modules2 = Seq(
  "play-exceptions", "play-netty-utils" //, "build-link"
)

libraryDependencies ++= modules1.map{
  org %% _ % V
}

libraryDependencies ++= modules2.map{
  org % _ % V
}

def checkSort() = {
  val m = modules1.map(_ + "_2.11").toList
  assert(m == m.sorted, "\n" + m + "\n" + m.sorted)
}

sourceGenerators in Compile <+= (scalaBinaryVersion, baseDirectory).map{
  (S, base) =>
  checkSort()
  val playRepo = "http://repo1.maven.org/maven2/" + org.replace('.','/') + "/"
  case class Module(name: String, url: String)
  val modules =
    modules1.map{m => Module(m, s"""${playRepo}${m}_${S}/${V}/${m}_${S}-${V}-sources.jar""")} ++
    modules2.map{m => Module(m, s"""${playRepo}${m}/${V}/${m}-${V}-sources.jar""")}
  modules.flatMap{ m =>
    IO.unzipURL(
      new URL(m.url)
      ,base / "src" / "main" / "scala" / m.name
      ,"*.scala" | "*.java"
    ).toSeq
  }
}
