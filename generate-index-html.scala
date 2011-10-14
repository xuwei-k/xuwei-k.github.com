import scala.io.Source
import scala.util.parsing.json.JSON.parseFull

type JsonType = Map[String,List[Map[String,String]]]

def getJson[T](url:String):T = {
  val str  = Source.fromURL(url,"UTF-8").mkString  
  parseFull(str).get.asInstanceOf[T]
}

def getRepositories(user:String):List[String] = {
  val json = getJson[JsonType]("https://github.com/api/v2/json/repos/show/" + user)  
  json("repositories").map{_.apply("url").replace("https://github.com/" + user + "/","")}
}

def hasGithubPage(repository:String):Boolean = {
  val json = getJson[Map[String,Map[String,String]]]("https://github.com/api/v2/json/repos/show/xuwei-k/"+repository+"/branches") 
  json("branches").contains("gh-pages")
}

def githubPageList(user:String) = {
  getRepositories(user).filter{ hasGithubPage }
}

def html(user:String) = {
  <ul>{
    githubPageList(user).map{ r =>
      <li><a href={ r }>{r}</a></li>
    }
  }</ul>
}

