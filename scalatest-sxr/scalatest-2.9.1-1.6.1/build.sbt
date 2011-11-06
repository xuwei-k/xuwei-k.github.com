resolvers += "xuwei-k repo" at "http://xuwei-k.github.com/mvn"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
"org.scala-lang" % "scala-library" % "2.9.1",
"org.scala-lang" % "scala-compiler" % "2.9.1",
"org.scala-tools.testing" % "test-interface" % "0.5",
"org.antlr" % "stringtemplate" % "3.2",
"org.scala-tools.testing" % "scalacheck_2.9.1" % "1.9",
"org.easymock" % "easymockclassextension" % "3.0",
"org.jmock" % "jmock-legacy" % "2.5.1",
"org.mockito" % "mockito-all" % "1.6",
//"org.testng" % "testng" % "5.7",
"junit" % "junit" % "4.4",
"net.sourceforge.cobertura" % "cobertura" % "1.9.1",
"commons-io" % "commons-io" % "1.3.2",
"org.scalatest" %% "scalatest" % "1.6.1"
)

(sourceGenerators in Compile) <+= (sourceManaged in Compile) map{
  dir =>
  def baseURL = {
    "http://scala-tools.org/repo-releases/org/scalatest/scalatest_2.9.1/1.6.1/scalatest_2.9.1-1.6.1-" + (_:String) + "sources.jar" 
  }
  Seq(baseURL("")).flatMap{ u =>
    IO.unzipURL(
      new java.net.URL(u)
      ,file("src/main/scala")
      ,new SimpleFilter(s => s.endsWith("scala") || s.endsWith("java") )
    ).toSeq
  }
}

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= (sourceDirectories in Compile).map{
  "-P:sxr:base-directory:" + _.mkString(":")
}

