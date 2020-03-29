


function manageResponse(response){
    if(response['results'] == "error"){
        var errors = document.getElementById("errors");
        errors.innerHTML = response['message'];
    }
    else{
        document.cookie = "constraint="+document.getElementById("fieldconstraint").value+";";
        window.location.href="dashboard.html";

    }
}

function send(){
    var myHeaders = new Headers();
    var fieldconstraint = document.getElementById("fieldconstraint");
    var fieldpassword = document.getElementById("fieldpassword");
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