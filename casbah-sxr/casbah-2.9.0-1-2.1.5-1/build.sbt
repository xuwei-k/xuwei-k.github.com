// https://github.com/mongodb/mongo-java-driver/raw/r2.6.3/lib/testng-5.8-jdk15.jar

scalaVersion := "2.9.0-1"

resolvers ++= Seq(
  "xuwei-k repo" at "http://xuwei-k.github.com/mvn"
)

addCompilerPlugin("org.scala-tools.sxr" % "sxr_2.9.1" % "0.2.8-SNAPSHOT")

libraryDependencies ++= Seq(
  "com.mongodb.casbah" %% "casbah-core" % "2.1.5-1"
)

scalacOptions <+= ( sourceDirectories in Compile) map (
  "-P:sxr:base-directory:" + _.mkString(":")
)

(sourceGenerators in Compile) <+= (scalaVersion) map{
  v =>
  def sourceURL(name:String,scalaV:String,casbahV:String) = {
    new java.net.URL(
      "http://scala-tools.org/repo-releases/com/mongodb/casbah/casbah-" +
       name + "_" + scalaV + "/" + casbahV + "/casbah-" +
       name + "_" + scalaV + "-" + casbahV + "-sources.jar"
    )
  }
  Seq(
    "commons","core","gridfs","query"
  ).flatMap{ module =>
    IO.unzipURL(
       sourceURL(module, v ,"2.1.5-1")
      ,new File("src/main/scala/" + module )
      ,new SimpleFilter(_.endsWith("scala"))
    ).toSeq
  } ++ IO.unzipURL(
     new java.net.URL("http://repo1.maven.org/maven2/org/mongodb/mongo-java-driver/2.6.3/mongo-java-driver-2.6.3-sources.jar")
    ,new File("src/main/java/")
    ,new SimpleFilter(_.endsWith("java"))
  )
}

