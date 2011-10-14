import scala.io.Source
import scala.util.parsing.json.JSON.parseFull

def getJson[T](url:String):T = {
  val str  = Source.fromURL(url,"UTF-8").mkString  
  parseFull(str).get.asInstanceOf[T]
}

def getRepositories(user:String):List[String] = {
  type JsonType = Map[String,List[Map[String,String]]]
  val json = getJson[JsonType]("https://github.com/api/v2/json/repos/show/" + user)  
  json("repositories").map{_.apply("url").replace("https://github.com/" + user + "/","")}
}

def hasGithubPage(user:String,repository:String):Boolean = {
  val json = getJson[Map[String,Map[String,String]]]("https://github.com/api/v2/json/repos/show/"+user+"/"+repository+"/branches") 
  json("branches").contains("gh-pages")
}

def githubPageList(user:String) = {
  getRepositories(user).filter{ r => hasGithubPage(user,r) }
}

def html(user:String) = {
  <ul>{
    githubPageList(user).map{ r =>
      <li><a href={ r }>{r}</a></li>
    }
  }</ul>
}

