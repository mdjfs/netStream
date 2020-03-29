
function manageResponseRegister(response){
    if(response['results'] == "error"){
        var errors = document.getElementById("errors");
        errors.innerHTML = response['message'];
    }
    else{
        send();
    }
}

function sendRegister(){
    var myHeaders = new Headers();
    var fieldconstraint = document.getElementById("fieldconstraint");
    var fieldusername = document.getElementById("fieldusername");
    var fieldpassword = document.getElementById("fieldpassword");
    var raw = '{"email": "'+fieldconstraint.value+'", "name":"'+fieldusername.value+'", "password":"'+fieldpassword.value+'"}';
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