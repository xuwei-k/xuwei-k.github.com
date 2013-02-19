resolvers ++= Seq(
  "http://xuwei-k.github.com/mvn"
).map{u => u at u}

resolvers += Opts.resolver.sonatypeReleases

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= (sourceDirectories in Compile) map (
  "-P:sxr:base-directory:" + _.mkString(":")
)

scalacOptions <++= scalaVersion.map(sv =>
  Seq("-deprecation", "-unchecked") ++ (if(sv.contains("2.10")) None else Some("-Ydependent-method-types"))
)

{
val org = "org.scalaz"
val modules = Seq(
  "core","concurrent","effect","iteratee","iterv","scalacheck-binding","tests","typelevel","example"
).map{"scalaz-" + _}
val v = "7.0.0-M8"
seq(
libraryDependencies <++= scalaVersion { sv =>
  modules.map{ n =>  org %% n % v }
}
/*
,
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
*/
)
}

sourceGenerators in Compile += task(Seq(GenerateTupleW.generateTupleW(file("src/main/scala"))))
