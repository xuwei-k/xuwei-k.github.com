scalaVersion := "2.10.0-M1"

resolvers ++= Seq(
  "xuwei-k repo" at "http://xuwei-k.github.com/mvn"
)

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= ( sourceDirectories in Compile) map (
  "-P:sxr:base-directory:" + _.mkString(":")
)

(sourceGenerators in Compile) <+= (scalaVersion) map {
  v =>
  val u = "http://scala-tools.org/repo-releases/org/scala-lang/scala-library/"+v+"/scala-library-"+v+"-sources.jar"
  IO.unzipURL(
     new java.net.URL(u)
    ,new File("src/main/scala/")
    ,new SimpleFilter( f => f.endsWith("scala") || f.endsWith("java") )
  ).toSeq
}

TaskKey[Unit]("slide") <<= (sources in Compile,scalaVersion) map { (files,v) =>
  val u = "http://xuwei-k.github.com/scala-library-sxr/scala-library-" + v + "/"
  def pathString(f:File):String = {
    f.getAbsolutePath.split('/').dropWhile("scala" !=).tail.mkString("/")
  }
  IO.write(
    file("../slide/scala-library" ) ,
    files.map{f =>
      "\n\n!SLIDE\n\n" + <iframe src={ u + pathString(f) } width="1200" height="600" />
    }.mkString
  )
}

