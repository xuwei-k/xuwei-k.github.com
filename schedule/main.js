const download = function(filename, text) {
  const element = document.createElement('a');
  element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
  element.setAttribute('download', filename);

  element.style.display = 'none';
  document.body.appendChild(element);

  element.click();

  document.body.removeChild(element);
}

const queryParams = (function(){
  const arg = new Object;
  const pair = location.search.substring(1).split('&');
  for(let i = 0; pair[i]; i++) {
    const kv = pair[i].split('=');
    const value = parseInt(kv[1]);
    if(!Number.isNaN(value)) {
      arg[kv[0]] = value;
    }
  }
  return arg;
})();

const month = (function(){
  const v = queryParams["month"];
  if(v === undefined) {
    const today = new Date();
    return today.getMonth() + 1;
  } else {
    return v - 1;
  }
})();

const year = (function(){
  const v = queryParams["year"];
  if(v === undefined) {
    const today = new Date();
    return today.getFullYear();
  } else {
    return v;
  }
})();

const m2 = (function(){
  if (month == 12) {
    return 1;
  } else {
    return month + 1;
  }
})();

const y2 = (function(){
  if (m2 == 1 && queryParams["month"] === undefined) {
    return year + 1;
  } else {
    return year;
  }
})();

(function(){
const today = new Date();
let day_count = -(new Date(y2, m2 - 1)).getDay();
const max_day = new Date(y2, m2, 0).getDate();
let youbi = -1;
let html = '<table border="1">';
html += '<caption>' + y2 + '/' + m2 + '</caption>';
html+= '<tr>';
const dayOfWeeks = '日月火水木金土';
for(let i = 0; i< dayOfWeeks.length; i++){
const tmpChar = dayOfWeeks.substr(i, 1);
  html += '<th>' +tmpChar +'</th>';
}
html += '</tr>';

while( true ){
  if(youbi > 5) {
    html += '</tr>';
    if( day_count >= max_day ) {
      break;
    }
    youbi = 0;
  } else {
    youbi = youbi + 1;
  }

  if( youbi == 0 ){
    html +='<tr>';
  }

  let moji;
  day_count = day_count + 1;

  if( day_count < 1 || max_day<day_count ){
    html += '<td>-</td>';
  } else {
    html += '<td>' + day_count + '<br /' +
    `<form id="${day_count}">` +
      `<input name="${day_count}" type="radio" value="a" checked="checked">休み</input><br />` +
      `<input name="${day_count}" type="radio" value="b">仕事</input><br />` +
    `</form></td>`
  }
}

html += '</table>';
$("#calendar").html(html);
})();

$("#result").click(function(){
  const today = new Date();
  const max_day = new Date(y2, m2, 0).getDate();

  let result = [];

  for(let i = 0; i <= max_day; i++){
    switch($(`input[name=${i}]:checked`).val()) {
      case "a":
        break;
      case "b":
        result.push({
          "Subject" : "仕事",
          "Start Date" : `${m2}/${i}/${y2}`,
          "Start Time" : "8:30 AM",
          "End Date" : `${m2}/${i}/${y2}`,
          "End Time" : "17:00"
        })
        break;
    }
  }

  let csv = [
    "Subject",
    "Start Date",
    "Start Time",
    "End Date",
    "End Time"
  ].join(",") + "\n";

  for(let i = 0; i < result.length; i++){
    const x = result[i]
    csv += [
      x["Subject"],
      x["Start Date"],
      x["Start Time"],
      x["End Date"],
      x["End Time"]
    ].join(",")
    csv += "\n"
  }

  console.log(csv);

  $("#csv").html("<pre>" + csv + "</pre>");

  download(`schedule-${y2}-${m2}.csv`, csv);
});
