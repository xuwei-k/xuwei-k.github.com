scalaVersion := "2.9.1"

resolvers ++= Seq(
  "xuwei-k repo" at "http://xuwei-k.github.com/mvn"
)

addCompilerPlugin("org.scala-tools.sxr" % "sxr_2.9.1" % "0.2.8-SNAPSHOT")

scalacOptions <+= ( sourceDirectories in Compile) map (
  "-P:sxr:base-directory:" + _.mkString(":")
)

libraryDependencies ++= Seq(
   "com.h2database" % "h2" % "1.2.140"
  ,"org.xerial" % "sqlite-jdbc" % "3.6.20"
  ,"org.apache.derby" % "derby" % "10.6.1.0"
  ,"org.hsqldb" % "hsqldb" % "2.0.0"
  ,"postgresql" % "postgresql" % "8.4-701.jdbc4"
  ,"mysql" % "mysql-connector-java" % "5.1.13"
  ,"net.sourceforge.jtds" % "jtds" % "1.2.4"
  ,"com.novocode" % "junit-interface" % "0.7"
  ,"net.sourceforge.fmpp" % "fmpp" % "0.9.13"
)

(sourceGenerators in Compile) <+= (scalaVersion) map{
  v =>
  val url = "http://scala-tools.org/repo-releases/org/scalaquery/scalaquery_2.9.0-1/0.9.5/scalaquery_2.9.0-1-0.9.5-sources.jar"
  IO.unzipURL(
     new java.net.URL(url)
    ,file("src/main/scala/")
    ,new SimpleFilter(f => f.endsWith("scala") || f.endsWith("java"))
  ).toSeq
}

