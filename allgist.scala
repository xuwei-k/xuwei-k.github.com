// scala allgist.scala > allgist.html
val x = xml.XML.load(new java.net.URL("http://gist.github.com/api/v1/xml/gists/xuwei-k"))
(x \\ "repo").foreach{n => println(<script src={"https://gist.github.com/"+n.text+".js"} /> )}

