scalaVersion := "2.11.4"

resolvers += "bintray/paulp" at "https://dl.bintray.com/paulp/maven"
 
addCompilerPlugin("org.improving" %% "sxr" % "1.0.1")
  
scalacOptions += {
  "-P:sxr:base-directory:" + (sourceDirectories in Compile).value.mkString(":")
}

val SCALA_ORG = "org.scala-lang"

val modules1 =
"scala-compiler scala-reflect scalap scala-actors scala-library".split(" ").toList

val modules2 = Seq(
  "scala-parser-combinators" -> "1.0.2",
  "scala-partest" -> "1.0.1",
//  "scala-swing" -> "1.0.1"
  "scala-xml" -> "1.0.3"
).map{ case (a, b) => (a + "_2.11", b) }

val continuations = Seq(
  ("org.scala-lang.plugins", "scala-continuations-library_2.11", "1.0.2"),
  ("org.scala-lang.plugins", "scala-continuations-plugin_2.11.2", "1.0.2")
)

libraryDependencies += "org.apache.ant" % "ant" % "1.8.2"
libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
libraryDependencies += "jline" % "jline" % "2.12"
libraryDependencies ++= modules2.map{ case (n, v) =>
  "org.scala-lang.modules" % n % v
}

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.11.3" // partest

val ROOT = "https://oss.sonatype.org/content/repositories/releases/" + SCALA_ORG.replace('.', '/')

def sourceURL(n: String, v: String) = "/" + n + "/" + v + "/" + n + "-" + v + "-sources.jar"

sourceGenerators in Compile += task{
  val scalaV = scalaVersion.value

  Seq(
    modules1.map{ n => ROOT + sourceURL(n, scalaV) },
    modules2.map{ case (n, v) =>
      ROOT + "/modules" + sourceURL(n, v)
    },
    continuations.map{ case (_, n, v) =>
      ROOT + "/plugins" + sourceURL(n, v)
    }
  ).flatten.flatMap{ u =>
    println("downloading from " + u)
    IO.unzipURL(
      url(u),
      baseDirectory.value / "src" / "main" / "scala",
      new SimpleFilter(f => f.endsWith("scala") || f.endsWith("java"))
    ).toSeq
  }
}
