
function manage_response(response) {
  alert(response["message"]);
  if(response["status"] == "200"){
    window.location.href = "index.html";
  }
}


var myHeaders = new Headers();


function send() {
  var logname = document.getElementById("1");
  var logpass = document.getElementById("2");
  raw = '{"constraint": "'+logname.value+'", "password":"'+logpass.value+'"}';
  var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
  };
  fetch('login', requestOptions)
    .then(response => response.json())
    .catch(error => console.log('Error:' + error))
    .then(response => manage_response(response));
}
