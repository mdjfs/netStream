
function chargueMyVideos(response){
    if(response['results'] != "error"){
        var iteratorjson = JSON.parse(response['message']);
        for(var i=1; i<11; i++){
            if(iteratorjson.hasOwnProperty(i.toString())){
                json = JSON.parse(iteratorjson[i.toString()]);
                if(json.hasOwnProperty("thumbnail")){
                    var img = document.getElementById("img"+i.toString());
                    var jsonThumbnail = JSON.parse(json['thumbnail']);
                    img.src = getPath() + "video?user="+json['username']+"&name="+
                    jsonThumbnail['name']+"."+jsonThumbnail['type'];
                    img.hidden = false;
                }
                var hrefvideo = document.getElementById(i.toString());
                var textp = document.getElementById("text"+i)
                var textvideo = json['username'] + " - " + json['name'];
                hrefvideo.setAttribute('href', getPath() + "video?user="+json['username']+"&name="+
                json['name']+"."+json['type']);
                textp.innerHTML = textvideo;

            }
        }
    }
}




window.onload = function(){
    dashboardLoad(function(){

    });
    var raw = "";
    var myHeaders = new Headers();
    var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
    };

    fetch("news", requestOptions)
    .then(response => response.json())
    .catch(error => console.log('Error:' + error))
    .then(response => chargueMyVideos(response));
}