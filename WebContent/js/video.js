const id_status = "status";
const id_button_upload = "go";
const id_file_video = "video";
const id_file_thumbnail = "thumbnail";
const id_progressbar = "progress";
const id_name = "name";

function changeStatus(message){
    var progressbar = document.getElementById(id_progressbar);
    if(message.includes("|")){
        message = message.split("|");
        progressbar.style.width = message[1];
        document.getElementById(id_status).innerHTML = message[0];
    }
    else{
        progressbar.style.width = "0%";
        document.getElementById(id_status).innerHTML = message;
    }


}

function uploadVideo(parts){
    var number = 0;
    var name = document.getElementById(id_name);
    var type = document.getElementById(id_file_video).files[0].type.split("/")[1];
    function send(callback){
        var myHeaders = new Headers();
        var raw = '{"name":"'+name.value+'","type":"'+type+'","part":"'+parts[number]+'"}';
        var requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
            redirect: 'follow'
        };
        function readResponse(response){
            if(response['results'] != "error"){
                var json = JSON.parse(response['message']);
                if(json['ready'] == "false"){
                    document.getElementById(id_status).innerHTML = "Uploading...";
                    document.getElementById(id_progressbar).style.width = json['percent'];
                    number++;
                    callback(send);
                }
                else{
                    document.getElementById(id_progressbar).style.width = "0%";
                    document.getElementById(id_status).innerHTML = "Ready Video ! ";
                }
            }
            else{
                    document.getElementById(id_status).innerHTML = response['message'];
            }
        }
        fetch('video', requestOptions).then(response => response.json())
        .catch(error => console.log('Error:' + error))
        .then(response => readResponse(response));
    }
    send(send);
}

function uploadThumbnail(parts){
    var number = 0;
    var name = document.getElementById(id_name);
    var type = document.getElementById(id_file_thumbnail).files[0].type.split("/")[1];
    function send(callback){
        var myHeaders = new Headers();
        var raw = '{"name":"'+name.value+'","type":"'+type+'","part":"'+parts[number]+'"}';
        var rawdelete = '{"name":"'+name.value+'","type":"'+type+'"}';
        var requestOptionsDelete = {
            method: 'DELETE',
            headers: myHeaders,
            body: rawdelete,
            redirect: 'follow'
        };
        var requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
            redirect: 'follow'
        };
        function readResponse(response){
            if(response['results'] != "error"){
                var json = JSON.parse(response['message']);
                if(json['ready'] == "false"){
                    document.getElementById(id_status).innerHTML = "Uploading...";
                    document.getElementById(id_progressbar).style.width = json['percent'];
                    number++;
                    callback(send);
                }
                else{
                    document.getElementById(id_progressbar).style.width = "0%";
                    document.getElementById(id_status).innerHTML = "Ready Thumbnail ! ";
                    var video = document.getElementById(id_file_video).files[0];
                    if(video != null)
                        fileCodificator(video, changeStatus, uploadVideo);
                }
            }
            else{
                    document.getElementById(id_status).innerHTML = response['message'];
            }
        }
        fetch('video', requestOptionsDelete).then(response => response.json())
        .catch(error => console.log('Error:' + error))
        .then(response => console.log(response['message']));
        fetch('video', requestOptions).then(response => response.json())
        .catch(error => console.log('Error:' + error))
        .then(response => readResponse(response));
    }
    send(send);
}

window.onload = function(){
    dashboardLoad(function(){

    });
    var button = document.getElementById(id_button_upload);
    button.onclick = function(){
        var thumbnail = document.getElementById(id_file_thumbnail).files[0];
        if(thumbnail != null){
            fileCodificator(thumbnail, changeStatus, uploadThumbnail);
        }
        else{
            var video = document.getElementById(id_file_video).files[0];
            if(video != null){
                fileCodificator(video, changeStatus, uploadVideo);
            }
        }

    }
}