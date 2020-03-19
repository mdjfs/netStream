var movie = null;
var thumbnail = null;
var partvideo = "";
var partthumb = "";

window.onload = function () {
    movie = document.getElementById('filemovie');
    thumbnail = document.getElementById('filethumbnail');
    movie.addEventListener('change', this.load(movie));
    thumbnail.addEventListener('change', this.load(thumb));
}

function load(file){
    base64(getBase64, file);
}

function base64(callback, file){
    var reader = new FileReader();
    reader.onload = function(e) {
        var contents = e.target.result;
        console.log(reader.result);
        var contentBuffer = getBase64(contents);
        var array = callback(contentBuffer);
    }
    reader.readAsArrayBuffer(file.files[0]);
}

function getBase64(buffer){
    var binary = '';
    var bytes = new Uint8Array(buffer);
    var u8 = new Uint8Array(1024);
    var pointer_bytes = 0;
    var acum_bytes = 0;
    var actual_part = 0;
    var total_part = bytes.byteLength / 1024;
    for(pointer_bytes; pointer_bytes < bytes.byteLength ; pointer_bytes++){
        u8[acum_bytes] = bytes[pointer_bytes];
        if(pointer_bytes % 1024 == 0 && pointer_bytes != 0){
            acum_bytes = 0;
            actual_part++;
            partvideo = "("+actual_part+"/"+total_part+")"+btoa(String.fromCharCode.apply(null, u8));
            partthumb = "";
            sendfiles();
        }
        acum_bytes++;

    }
    return binary;
}

let promise = new Promise(function(resolve, reject){
    u8[acum_bytes] = bytes[pointer_bytes];
        if(pointer_bytes % 1024 == 0 && pointer_bytes != 0){
            acum_bytes = 0;
            actual_part++;
            partvideo = "("+actual_part+"/"+total_part+")"+btoa(String.fromCharCode.apply(null, u8));
            partthumb = "";
            sendfiles();
        }
    acum_bytes++;
    pointer_bytes++;
    resolve("ready");
})



function manage_response(response){
    console.log(response["result"]);
}


function sendfiles(){
    var name = document.getElementById("name");
    var typethumb = document.getElementById("typethumb");
    var typevid = document.getElementById("typemovie");
    var myHeaders = new Headers();
    var raw = '{"name":"'+name.value+'","type_thumbnail":"'+typethumb.value
            +'","type_video":"'+typevid.value+'","part_video":"'+partvideo
            +'","part_thumbnail":"'+partthumb+'"}';
    var requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
            redirect: 'follow'
        };
    fetch('video', requestOptions)
        .then(response => response.json())
        .catch(error => console.log('Error:' + error))
        .then(response => manage_response(response));
}