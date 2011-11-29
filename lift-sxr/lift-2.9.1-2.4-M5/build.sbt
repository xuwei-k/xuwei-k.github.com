scalaVersion := "2.9.1"

resolvers ++= Seq(
  "xuwei-k repo" at "http://xuwei-k.github.com/mvn"
)

addCompilerPlugin("org.scala-tools.sxr" % "sxr_2.9.1" % "0.2.8-SNAPSHOT")

scalacOptions <+= ( sourceDirectories in Compile) map (
  "-P:sxr:base-directory:" + _.mkString(":")
)

libraryDependencies ++= Seq(
"ch.qos.logback" % "logback-classic" % "0.9.27",
"org.slf4j" % "slf4j-api" % "1.6.1",
"log4j" % "log4j" % "1.2.16",
"org.scala-tools.testing" %% "specs" % "1.6.9",
"org.scala-tools.testing" %% "scalacheck" % "1.9",
"com.thoughtworks.paranamer" % "paranamer" % "2.3",
"org.scala-lang" % "scalap" % "2.9.1",
"javax.mail" % "mail" % "1.4.4",
"commons-codec" % "commons-codec" % "1.4",
"nu.validator.htmlparser" % "htmlparser" % "1.2.1",
"joda-time" % "joda-time" % "1.6.2",
"commons-httpclient" % "commons-httpclient" % "3.1",
"javax.servlet" % "servlet-api" % "2.5",
"net.sourceforge.jwebunit" % "jwebunit-htmlunit-plugin" % "2.5",
"commons-fileupload" % "commons-fileupload" % "1.2.2",
"org.mortbay.jetty" % "jetty" % "6.1.26",
"org.mockito" % "mockito-all" % "1.8.5",
"org.scalaz" %% "scalaz-core" % "6.0.2",
"org.mongodb" % "mongo-java-driver" % "2.6.5",
"com.h2database" % "h2" % "1.2.147",
"org.apache.derby" % "derby" % "10.7.1.1",
"org.apache.directory.server" % "apacheds-server-integ" % "1.5.5",
"org.scala-libs" %% "scalajpa" % "1.4",
"javax.persistence" % "persistence-api" % "1.0",
"net.databinder" %% "dispatch-http" % "0.7.8",
"org.squeryl" %% "squeryl" % "0.9.4"
)

(sourceGenerators in Compile) <+= (scalaVersion) map{
  v =>
  val liftV = "2.4-M5"
  def sourceURL(name:String) = {
    ScalaToolsReleases.root + "/net/liftweb/lift-" +
    name + "_" + v + "/" + liftV + "/lift-" +
    name + "_" + v + "-" + liftV + "-sources.jar"
  }
  val srcURLs = Seq(
    "common","actor","json","util","testkit","webkit","proto","db","record","json-scalaz","json-ext","mongodb","mapper","ldap","mongodb-record","jpa","wizard","couchdb","squeryl-record"
  ).map{n => n -> sourceURL(n) };
  srcURLs.flatMap{ case (name,url) =>
    IO.unzipURL(
       new java.net.URL(url)
      ,new File("src/main/scala/" + name )
      ,new SimpleFilter(f => f.endsWith("scala") || f.endsWith("java"))
    ).toSeq
  }
}

