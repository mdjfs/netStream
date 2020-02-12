
function manage_response(response) {
  alert(response["message"]);
  if(response["status"] == "200"){
    window.location.href = "index.html";
  }
}

function send() {
  var logname = document.getElementById("1");
  var logpass = document.getElementById("2");
  var form = new FormData();
  form.append("json", '{"constraint":"' +
    logname.value + '", "password":"' + logpass.value + '"}');
  fetch('login', {
    method: 'POST',
    credentials: 'include',
    body: form,
  })
    .then(response => response.json())
    .catch(error => console.log('Error:' + error))
    .then(response => manage_response(response));
}

