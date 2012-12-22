resolvers += "xuwei-k repo" at "http://xuwei-k.github.com/mvn"

resolvers += Opts.resolver.sonatypeReleases

scalaVersion := "2.9.2"

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= (sourceDirectories in Compile).map{
  "-P:sxr:base-directory:" + _.mkString(":")
}

{
val V = "2.4"
val names = Seq(
  "specs2-support","scalatest-support","core","junit3-support","compiler-plugin"
).map{"scalamock-" +}
seq(
libraryDependencies ++= {
  names.map{"org.scalamock" %% _ % V}
}
,
(sourceGenerators in Compile) <+= scalaVersion.map{
  S =>
  val sonatype = "https://oss.sonatype.org/content/repositories/releases/org/scalamock/"
  names.map{ n =>
    <x>{sonatype}{n}_{S}/{V}/{n}_{S}-{V}-sources.jar</x>.text 
  }.flatMap{ u =>
    IO.unzipURL(
      url(u)
      ,file("src") / "main" / "scala"
      ,new SimpleFilter(s => s.endsWith("scala") || s.endsWith("java") )
    ).toSeq
  }
}
)
}
