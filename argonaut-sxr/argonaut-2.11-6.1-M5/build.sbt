addCompilerPlugin("org.improving" %% "sxr" % "1.0.1")
  
scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

scalacOptions += "-language:experimental.macros"

scalaVersion := "2.11.4"

val org = "io.argonaut"
val modules = Seq(
  "argonaut"
)
val v = "6.1-M5"

libraryDependencies <++= scalaVersion { sv =>
  modules.map{ n =>  org %% n % v }
}

(sourceGenerators in Compile) += task{
  val sv = scalaBinaryVersion.value
  modules.flatMap{ N =>
    val n = N + "_" + sv
    val u = <x>{Resolver.SonatypeRepositoryRoot}/{"releases"}/{org.replace('.','/')}/{n}/{v}/{n}-{v}-sources.jar</x>.text
    IO.unzipURL(
       url(u)
      ,baseDirectory.value / "src/main/scala" / N
      ,"*.scala"
    ).toSeq
  }
}
