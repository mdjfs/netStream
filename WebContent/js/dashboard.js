
const id_element_identifier = "constraint";
const id_element_imgprofile = "profile";
const id_element_navigator = "navigation";
const id_element_logout = "logout";
const session_data_parameter = "dataNetStream";

// vars of navigator
var moverect = -40;
var scroll = false;
var onuse = false;
// ---------
//statics functions of navigator
function move(){
    var navigator = document.getElementById(id_element_navigator);
    navigator.hidden = false;
    navigator.style.top = moverect + "%";
    moverect++;
    if(moverect <= 9){
        setTimeout("move()", 10);
    }
    if(moverect == 9){
        scroll = true;
    }
}
function remove(){
    var navigator = document.getElementById(id_element_navigator);
    navigator.style.top = moverect + "%";
    if(moverect >= -40){
        setTimeout("remove()", 10);
    }
    if(moverect == -40){
        navigator.hidden = true;
        scroll = false;
    }
    moverect--;
}
// ----
//chargue the navigator
function chargueNavigator(){
    var img = document.getElementById(id_element_imgprofile);
    var navigator = document.getElementById(id_element_navigator);
    function display(){
        setTimeout("move()", 10);
    }
    function save(){
        setTimeout("remove()", 10);
    }
    img.onmouseenter = function(){
        if(!scroll)
            display();
    };
    img.onmouseleave = function(){
        setTimeout(function(){
            if(scroll && !onuse){
                save();
            }
        }, 5000);
    };
    document.body.onmouseenter = function(){
        setTimeout(function(){
            if(scroll && !onuse){
                save();
            }
        }, 5000);
    };
    navigator.onmouseenter = function(){
        onuse=true;
        if(!scroll)
            display();
    };
    navigator.onmouseleave = function(){
        onuse=false;
        if(scroll){
            save();
        }
    };
}
// ----------
// chargue img
function chargueProfileIMG(actualpath, user, callback){
    var img = document.getElementById(id_element_imgprofile);
    img.src = actualpath + "video?user="+user+"/profile&name=profile.png";
    img.onerror = function(){
        img.src = actualpath + "video?user="+user+"/profile&name=profile.jpg";
        img.onerror = function(){
            img.src = actualpath + "video?user="+user+"/profile&name=profile.jpeg";
            img.onerror = function(){
                img.src = "sources/default.png";
                callback();
            }
            callback();
        }
        callback();
    }
}
// ----
// returns actual path
function getPath(){
    var url = window.location.href;
    url = url.split("/");
    var actualpath = "";
    for(var i = 0; i < url.length; i++){
        if(i != url.length -1){
            actualpath += url[i] + "/";
        }
    }
    return actualpath;
}
// ---
window.onload = function(){
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
        var identifier = document.getElementById(id_element_identifier);
        identifier.innerHTML = user;
        chargueProfileIMG(getPath(), user);
    }
    else{
        window.localStorage.removeItem(session_data_parameter);
        window.location.href = "index.html";
    }
    chargueNavigator();
    document.getElementById(id_element_logout).onclick = function(){
        window.localStorage.removeItem(session_data_parameter);
        window.location.href = "index.html";
    }
}

function dashboardLoad(callback){
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
        var identifier = document.getElementById(id_element_identifier);
        identifier.innerHTML = user;
        chargueProfileIMG(getPath(), user, callback);
    }
    else{
        window.localStorage.removeItem(session_data_parameter);
        window.location.href = "index.html";
    }
    chargueNavigator();
    document.getElementById(id_element_logout).onclick = function(){
        window.localStorage.removeItem(session_data_parameter);
        window.location.href = "index.html";
    }
}