DefaultOptions.addResolvers

resolvers ++= Seq(
  "http://xuwei-k.github.com/mvn"
).map{u => u at u}

resolvers += Opts.resolver.sonatypeReleases

scalaVersion := "2.10.1-RC2"

addCompilerPlugin("org.scala-tools.sxr" % "sxr_2.10" % "0.2.8-SNAPSHOT")

scalacOptions <+= (sourceDirectories in Compile) map (
  "-P:sxr:base-directory:" + _.mkString(":")
)

libraryDependencies ++= Seq(
  "play" %% "play-test" % "2.1.0",
  "org.scalatest" % "scalatest_2.10.0" % "1.8"
)

{
val org = "com.github.seratch"
val modules = Seq(
  "config", "interpolation", "play-plugin", "test" //,"mapper-generator"
).map{"scalikejdbc-" + _} :+ "scalikejdbc"
val v = "1.4.7"
seq(
libraryDependencies <++= scalaVersion { sv =>
  modules.map{ n =>  org %% n % v }
},
(sourceGenerators in Compile) <+= scalaBinaryVersion.map {
  sv =>
  modules.flatMap{ N =>
    val n = N + "_" + sv
    val u = <x>{Resolver.ScalaToolsReleasesRoot}/{org.replace('.','/')}/{n}/{v}/{n}-{v}-sources.jar</x>.text
    IO.unzipURL(
       url(u)
      ,file("src/main/scala") / N
      ,"*.scala"
    ).toSeq
  }
}
)
}

