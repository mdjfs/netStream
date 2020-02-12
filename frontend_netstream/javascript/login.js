
function manage_response(response) {

}

function send() {
  var logname = document.getElementById("1");
  var logpass = document.getElementById("2");
  var form = new FormData();
  form.append("json", '{"constraint":"' +
    logname.value + '", "password":"' + logpass.value + '"}');
  fetch('http://localhost:8080/netStream/login', {
    method: 'POST',
    body: form,
  })
    .then(response => response.json())
    .catch(error => console.log('Error:' + error))
    .then(response => manage_response(response));
}

