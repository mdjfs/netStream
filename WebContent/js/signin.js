const id_input_constraint = "fieldconstraint";
const id_input_password = "fieldpassword";
const id_img_mayus_alert = "alert";
const id_errors_text = "errors";

const session_data_parameter = "dataNetStream";
// create session
function createSession(response){
    if(response['results'] == "error"){
        var errors = document.getElementById(id_errors_text);
        errors.innerHTML = response['message'];
    }
    else{
        if (window.localStorage) {
            window.localStorage.removeItem(session_data_parameter);
            window.localStorage.setItem(session_data_parameter, response['message']);
            window.location.href = "dashboard.html";
         }
        else
        {
            throw new Error('Tu Browser no soporta localStorage!');
        }
    }
}
// ---

// get data
function getData(){
    var myHeaders = new Headers();
    var requestOptions = {
        method: 'PUT',
        headers: myHeaders,
        redirect: 'follow'
      };
      fetch('video', requestOptions)
        .then(response => response.json())
        .catch(error => console.log('Error:' + error))
        .then(response => createSession(response));
}
// ---

// send login
function manageResponse(response){
    if(response['results'] == "error"){
        var errors = document.getElementById(id_errors_text);
        errors.innerHTML = response['message'];
    }
    else{
        getData();
    }
}

function send(){
    var myHeaders = new Headers();
    var fieldconstraint = document.getElementById(id_input_constraint);
    var fieldpassword = document.getElementById(id_input_password);
    var raw = '{"constraint": "'+fieldconstraint.value+'", "password":"'+fieldpassword.value+'"}';
    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
      };
      fetch('login', requestOptions)
        .then(response => response.json())
        .catch(error => console.log('Error:' + error))
        .then(response => manageResponse(response));
}
// ----
// enter detect
function loadEnterDetect(id_element){
    document.getElementById(id_element).addEventListener("keyup", function(event){
        var isenter = (event.key == "Enter");
        if(isenter)
            send();
    });
}

// ---

// mayus detect
var repeat=false;
// --------
// mayus load
function loadMayusDetect(){
    loadEnterDetect(id_input_constraint);
    document.getElementById(id_input_password).addEventListener("keyup", function(event) {
        var ismayus = (event.key == "CapsLock");
        var statusmayus = event.getModifierState("CapsLock");
        if(ismayus){
            if(repeat){
                document.getElementById(id_img_mayus_alert).hidden = true;
                repeat=false;
            }
            else{
                document.getElementById(id_img_mayus_alert).hidden = ! statusmayus;
                repeat=true;
            }
        }
        else{
            document.getElementById(id_img_mayus_alert).hidden = ! statusmayus; 
            repeat = false;
        }
        // ---

        var isenter = (event.key == "Enter");
        if(isenter)
            send();
  });
}
window.onload = function(){
    if (window.localStorage) {
        var data = window.localStorage.getItem(session_data_parameter);
        if( data != null){
            window.location.href = "dashboard.html";
        }
    }
    else
    {
        throw new Error('Tu Browser no soporta sessionStorage!');
    }
    loadMayusDetect();
}

function signInLoad(){
    if (window.localStorage) {
        var data = window.localStorage.getItem(session_data_parameter);
        if( data != null){
            window.location.href = "dashboard.html";
        }
    }
    else
    {
        throw new Error('Tu Browser no soporta sessionStorage!');
    }
    loadMayusDetect();
}