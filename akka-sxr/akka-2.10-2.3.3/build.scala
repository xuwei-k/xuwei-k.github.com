import sbt._, Keys._
import scala.tools.nsc.io.Directory

object build{

  final val tag = "v2.3.3"

  val zipUrl = "https://github.com/akka/akka/archive/" + tag  + ".zip"

  val filter = new SimpleFilter(s =>
    ( ! s.contains("project") ) && {
      s.endsWith("scala") || s.endsWith("java")
    }
  )

  val s = sourceGenerators in Compile <+= (sourceDirectory in Compile){
    d =>
    task{
      IO.withTemporaryDirectory{ tmp =>
        println("downloading from " + zipUrl)
        val files = IO.unzipURL(
          url(zipUrl)
          ,tmp
          ,filter
        )
        println("download complete " + zipUrl + " " + files.size + " files")
        mvFiles(tmp / ("akka-" + (tag.filter(_ != 'v')).replace('/', '-')), d)
      }
    }
  }

  val sxr = TaskKey[Unit]("sxr")

  lazy val sxrSetting = sxr <<= (compile in Compile, crossTarget).map{ (_, dir) =>
  }

  val modules = Seq(
    "actor", "testkit", "remote", "kernel", "zeromq", "osgi",
    "multi-node-testkit", "cluster", "slf4j", "agent", "camel"
  ).map("akka-" + _)

  val settings = Seq(s, sxrSetting,
    libraryDependencies ++= modules.map("com.typesafe.akka" %% _ % tag.filter('v' != ))
  )

  def deepFiles(base: File): Seq[(File, String)] = {
    val root = new Directory(base)
    root.deepFiles.map{f => f.jfile -> root.jfile.relativize(f.jfile).get.toString}.toSeq
  }

  def mvFiles(from: File, to: File): Seq[File] = {
    def mv(m: String, p: String){
      IO.move(from / m / ("src/" + p + "/scala/akka") , to / "scala" / m )
      IO.delete(from / m / "src")
    }
    modules.foreach{ m =>
      mv(m, "main")
    }
    to ** "*.scala" get
  }

}
