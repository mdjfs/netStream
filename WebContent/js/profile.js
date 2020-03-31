const id_profile_img = "profileimg";
const id_box_edit = "boxedit";
const id_button_editphoto = "editphoto";
const id_button_exitbox = "exitbox";
const id_file = "file";
const id_text_status = "status";
const id_progresscontainer = "progresscontainer";
const id_progress = "progress";
const id_username = "username";
const id_email = "email";
const id_edit_username = "editusername";
const id_edit_email = "editemail";
const id_change_username = "changeusername";
const id_change_email = "changeemail";

function chargeProfile(){
    var img = document.getElementById(id_profile_img);
    img.src = document.getElementById(id_element_imgprofile).src;
}

function changeStatus(message){
    if(message.includes("|")){
        document.getElementById(id_progresscontainer).hidden = false;
        message = message.split("|");
        document.getElementById(id_text_status).innerHTML = message[0];
        document.getElementById(id_progress).style.width = message[1];

    }
    else{
        document.getElementById(id_progresscontainer).hidden = true;
        document.getElementById(id_text_status).innerHTML = message;
    }
}
function uploadFile(parts){
    var user = null;
    if (window.localStorage) {
        var data = window.localStorage.getItem(session_data_parameter);
        if( data == null){
            window.location.href = "index.html";
        }
        else{
            var json = JSON.parse(data);
            user = json['username'];
        }
     }
    else
    {
        throw new Error('Tu Browser no soporta sessionStorage!');
    }
    if(user != null){
        var number = 0;
        var type = document.getElementById(id_file).files[0].type.split("/")[1];
        function send(callback){
            var myHeaders = new Headers();
            var raw = '{"name":"'+user+'/profile/profile'+'","type":"'+type+'","part":"'+parts[number]+'"}';
            var rawdelete = '{"name":"'+user+'/profile/profile'+'","type":"'+type+'"}';
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
                        document.getElementById(id_progresscontainer).hidden = false;
                        document.getElementById(id_text_status).innerHTML = "Uploading...";
                        document.getElementById(id_progress).style.width = json['percent'];
                        number++;
                        callback(send);
                    }
                    else{
                        document.getElementById(id_progresscontainer).hidden = true;
                        document.getElementById(id_text_status).innerHTML = "Ready ! ";
                    }
                }
                else{
                        document.getElementById(id_text_status).innerHTML = response['message'];
                        window.location.reload();
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
    else{
        window.localStorage.removeItem(session_data_parameter);
        window.location.href = "index.html";
    }
}

window.onload = function(){
    dashboardLoad(chargeProfile);
    var button = document.getElementById(id_button_editphoto);
    button.onclick =  function(){
        var box = document.getElementById(id_box_edit);
        box.hidden = ! box.hidden;
    }
    button = document.getElementById(id_button_exitbox);
    button.onclick = function() {
        var box = document.getElementById(id_box_edit);
        box.hidden = true;
    }
    var file = document.getElementById(id_file);
    file.onchange = function(){
        fileCodificator(file.files[0], changeStatus, uploadFile)
    }
    if (window.localStorage) {
        var data = window.localStorage.getItem(session_data_parameter);
        if( data == null){
            window.location.href = "index.html";
        }
        else{
            var json = JSON.parse(data);
            user = json['username'];
            email = json['email'];
            document.getElementById(id_username).innerHTML = user;
            document.getElementById(id_email).innerHTML = email;
        }
    }
    button = document.getElementById(id_edit_username);
    button.onclick = function(){
        var username = document.getElementById(id_username);
        username.hidden = ! username.hidden;
        var inputUsername = document.getElementById(id_change_username);
        inputUsername.hidden = ! inputUsername.hidden;
        inputUsername.value = username.innerHTML;
    }

}