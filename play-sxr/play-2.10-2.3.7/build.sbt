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
val V = "2.3.7"
val modules1 = Seq(
  "anorm", "filters-helpers", "play-cache", "play-datacommons", "play-docs",
  "play-functional", "play-integration-test", "play-iteratees", "play-java-ebean",
  "play-java-jdbc", "play-json", "play-java", "play-test", "play-java-ws", "play-ws",
  "run-support", "play"
)

val modules2 = Seq(
  "play-exceptions" //, "build-link"
)

libraryDependencies ++= modules1.map{
  org %% _ % V
}

libraryDependencies ++= modules2.map{
  org % _ % V
}

sourceGenerators in Compile <+= (scalaBinaryVersion, baseDirectory).map{
  (S, base) =>
  val playRepo = "http://repo.typesafe.com/typesafe/maven-releases/" + org.replace('.','/') + "/"
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
