resolvers += "bintray/paulp" at "https://dl.bintray.com/paulp/maven"
 
addCompilerPlugin("org.improving" %% "sxr" % "1.0.1")
  
scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

scalaVersion := "2.10.6"

val org = "com.trueaccord.scalapb"

val modules = List(
  "compilerplugin_2.10",
  "scalapb-runtime_2.10"
)

val v = "0.5.14"

libraryDependencies ++= modules.map(org % _ % v)

def src(dir: File, groupId: String, artifactId: String, v: String) = {
  val u = "http://repo1.maven.org/maven2/" + groupId.replace('.', '/') + "/" + artifactId + "/" + v + "/" + artifactId + "-" + v + "-sources.jar"
  println("downloading from " + u)
  IO.unzipURL(
    url(u),
    dir / "src" / "main" / "scala" / artifactId.split("_").head,
    new SimpleFilter({ f =>
      (f.endsWith("scala") || f.endsWith("java")) && {
        (f != "com/trueaccord/scalapb/Scalapb.java") || (artifactId != "compilerplugin_2.10")
      }
    })
  ).toSeq
}

sourceGenerators in Compile += task{
  modules.flatMap(
    src(baseDirectory.value, org, _, v)
  ) ++ src(
    baseDirectory.value, "com.trueaccord.lenses", "lenses_2.10", "0.4.1"
  )
}
