resolvers += Resolver.url("Typesafe Releases", url("http://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)

addCompilerPlugin("org.scala-sbt.sxr" %% "sxr" % "0.3.0")

scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

scalaVersion := "2.10.4"

scalacOptions <+= (sourceDirectories in Compile).map{
  "-P:sxr:base-directory:" + _.mkString(":")
}

scalacOptions += "-Ystop-after:sxr"

build.settings
