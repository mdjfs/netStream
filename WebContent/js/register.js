const id_input_email = "fieldemail";

function loadEnterDetectRegister(id_element){
    document.getElementById(id_element).addEventListener("keyup", function(event){
        var isenter = (event.key == "Enter");
        if(isenter)
            sendRegister();
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
    loadEnterDetectRegister(id_input_email);
    loadEnterDetectRegister(id_input_constraint);
    loadEnterDetectRegister(id_input_password);
    signInLoad();
}


function manageResponseRegister(response){
    if(response['results'] == "error"){
        var errors = document.getElementById("errors");
        errors.innerHTML = response['message'];
    }
    else{
        console.log(response['message']);
        send();
    }
}

function sendRegister(){
    var myHeaders = new Headers();
    var fieldconstraint = document.getElementById(id_input_email);
    var fieldusername = document.getElementById(id_input_constraint);
    var fieldpassword = document.getElementById(id_input_password);
    var raw = '{"email": "'+fieldconstraint.value+'", "name":"'+fieldusername.value+'", "password":"'+fieldpassword.value+'"}';
    console.log(raw);
    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
      };
      fetch('register', requestOptions)
        .then(response => response.json())
        .catch(error => console.log('Error:' + error))
        .then(response => manageResponseRegister(response));
}