#!/usr/bin/env scalas
!#

/***
scalaVersion := "2.9.2"

resolvers += "xuwei-k" at "http://xuwei-k.github.com/mvn"

libraryDependencies ++= Seq(
  "com.github.xuwei-k" %% "ghscala" % "0.1"
)
*/

import com.github.xuwei_k.ghscala.GhScala

val client = new GhScala(false)

def githubPageList(user:String) = {
  client.repos(user).map{ repo =>
    repo -> client.refs(user,repo.name)
  }.filter{ case (repo,refs) =>
    refs.exists{ ref =>
      ref.isBranch && (ref.name == "gh-pages")
    }
  }
}

githubPageList("xuwei-k").map{case (repo,_) =>
  repo.name
}.foreach(println)
