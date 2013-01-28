scalaVersion := "2.10.0"

resolvers ++= Seq(
  "http://xuwei-k.github.com/mvn"
).map{u => u at u}

resolvers += Opts.resolver.sonatypeReleases

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= (sourceDirectories in Compile) map (
  "-P:sxr:base-directory:" + _.mkString(":")
)

scalacOptions += "-language:experimental.macros"

{
val org = "org.spire-math"
val modules = Seq("spire", "spire-scalacheck-binding")
val v = "0.3.0-RC2"
seq(
libraryDependencies <++= scalaVersion { sv => 
  modules.map{ n =>  org %% n % v }
}
,
(sourceGenerators in Compile) <+= scalaBinaryVersion.map {
  sv =>
  modules.flatMap{ N =>
    val n = N + "_" + sv
    val u = <x>{Resolver.ScalaToolsReleasesRoot}/{org.replace('.','/')}/{n}/{v}/{n}-{v}-sources.jar</x>.text
    IO.unzipURL(
       url(u)
      ,file("src/main/scala") / N
      ,"*.scala" | "*.java"
    ).toSeq
  }
}
)
}
