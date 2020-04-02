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
const id_button_check_user = "checkuser";
const id_button_check_email = "checkemail";
const id_input_check_pass = "checkpass";

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
        document.getElementById(id_input_check_pass).hidden = true;
        document.getElementById(id_file).hidden = false;
        document.getElementById(id_text_status).innerHTML = "Select a file";
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
        var buttonCheck = document.getElementById(id_button_check_user);
        buttonCheck.hidden = ! buttonCheck.hidden;
        function sendUpdate(value, password){
            var myHeaders = new Headers();
            var raw = JSON.stringify({"password":password,"parameter":"name","value":value});
            var requestOptions = {
                method: 'POST',
                headers: myHeaders,
                body: raw,
                redirect: 'follow'
            };
            function charguenewSession(response){
                document.getElementById(id_text_status).innerHTML = response['message'];
                if(response['results'] != "error"){
                    if (window.localStorage) {
                        function setSession(response){
                            if(response['results'] != "error"){
                                window.localStorage.removeItem(session_data_parameter);
                                console.log(response['message']);
                                window.localStorage.setItem(session_data_parameter, response['message']);
                            }
                        }
                        function getData(callback){
                            var myHeaders = new Headers();
                            var requestOptions = {
                                method: 'PUT',
                                headers: myHeaders,
                                redirect: 'follow'
                              };
                              fetch('video', requestOptions)
                                .then(response => response.json())
                                .catch(error => console.log('Error:' + error))
                                .then(response => callback(response));
                        }
                        getData(setSession);
                     }
                    else
                    {
                        throw new Error('Tu Browser no soporta localStorage!');
                    }
                }
            }
            fetch('update', requestOptions).then(response => response.json())
            .catch(error => console.log('Error:' + error))
            .then(response => charguenewSession(response));
        }
        function getPassword(input){
            var box = document.getElementById(id_box_edit);
            box.hidden = false;
            var file = document.getElementById(id_file);
            file.hidden = true;
            document.getElementById(id_text_status).innerHTML = "Insert you pass";
            var inputPass = document.getElementById(id_input_check_pass);
            inputPass.hidden = false;
            input.hidden = ! input.hidden;
            buttonCheck.hidden = ! buttonCheck.hidden;
            username.hidden = ! username.hidden;
            inputPass.onkeyup = function(event){
                var isenter = (event.key == "Enter");
                if (isenter){
                    sendUpdate(input.value, inputPass.value);
                }
            }
        }
        inputUsername.onkeyup = function(event){
            var isenter = (event.key == "Enter");
            if(isenter){
                getPassword(inputUsername);
            }
        };
        var button = document.getElementById(id_button_check_user);
        button.onclick = function(){
            getPassword(inputUsername);
        };
    }
    button = document.getElementById(id_edit_email);
    button.onclick = function(){
        var username = document.getElementById(id_email);
        username.hidden = ! username.hidden;
        var inputUsername = document.getElementById(id_change_email);
        inputUsername.hidden = ! inputUsername.hidden;
        inputUsername.value = username.innerHTML;
        var buttonCheck = document.getElementById(id_button_check_email);
        buttonCheck.hidden = ! buttonCheck.hidden;
        function sendUpdate(value, password){
            var myHeaders = new Headers();
            console.log(value);
            var raw = JSON.stringify({"password":password,"parameter":"email","value":value});
            var requestOptions = {
                method: 'POST',
                headers: myHeaders,
                body: raw,
                redirect: 'follow'
            };
            function charguenewSession(response){
                document.getElementById(id_text_status).innerHTML = response['message'];
                if(response['results'] != "error"){
                    if (window.localStorage) {
                        function setSession(response){
                            if(response['results'] != "error"){
                                window.localStorage.removeItem(session_data_parameter);
                                console.log(response['message']);
                                window.localStorage.setItem(session_data_parameter, response['message']);
                            }
                        }
                        function getData(callback){
                            var myHeaders = new Headers();
                            var requestOptions = {
                                method: 'PUT',
                                headers: myHeaders,
                                redirect: 'follow'
                              };
                              fetch('video', requestOptions)
                                .then(response => response.json())
                                .catch(error => console.log('Error:' + error))
                                .then(response => callback(response));
                        }
                        getData(setSession);
                     }
                    else
                    {
                        throw new Error('Tu Browser no soporta localStorage!');
                    }
                }
            }
            fetch('update', requestOptions).then(response => response.json())
            .catch(error => console.log('Error:' + error))
            .then(response => charguenewSession(response));
        }
        function getPassword(input){
            var box = document.getElementById(id_box_edit);
            box.hidden = false;
            var file = document.getElementById(id_file);
            file.hidden = true;
            document.getElementById(id_text_status).innerHTML = "Insert you pass";
            var inputPass = document.getElementById(id_input_check_pass);
            inputPass.hidden = false;
            input.hidden = ! input.hidden;
            buttonCheck.hidden = ! buttonCheck.hidden;
            username.hidden = ! username.hidden;
            inputPass.onkeyup = function(event){
                var isenter = (event.key == "Enter");
                if (isenter){
                    sendUpdate(input.value, inputPass.value);
                }
            }
        }
        inputUsername.onkeyup = function(event){
            var isenter = (event.key == "Enter");
            if(isenter){
                getPassword(inputUsername);
            }
        };
        var button = document.getElementById(id_button_check_user);
        button.onclick = function(){
            getPassword(inputUsername);
        };
    }
}