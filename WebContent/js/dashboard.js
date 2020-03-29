

window.onload = function(){
    var user = document.cookie.split(";")[0].split("=")[1];
    if(user == null){
        document.getElementById("welcome").innerHTML = "You arent loged !!!";
    }
    else{
        document.getElementById("welcome").innerHTML = "Welcome "+ user;
    }
}
