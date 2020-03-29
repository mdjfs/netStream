var offset = 0;
var parts = [];
var number = 0;
var type = null;
const MB = 1024 * 1024;
const blockSize = MB * 10;
const frameSize = MB / 4;

window.onload = function () {
    /* Se cargan los archivos, acto seguido
    se les añade que escuchen el evento Change. Este mismo
    hará que cada vez que cambien llame a la funcion load */
    file = document.getElementById('file');
    file.addEventListener('change', this.load(file));
}

function load(file){
    /* la funcion load, recibe el archivo y llama a la funcion readFile
    que se encargara de leer el archivo y una vez leido llamara el
    callback en este caso es getBase64 */
    readFile(readFile, file); 

}

function readFile(callback, file){
    /* la funcion readFile se dedica a leer el archivo y pasarselo al callback */

    // se inicializan variables 
    var firstfile = file.files[0]; // archivo
    var fileSize = firstfile.size; // tamaño del archivo
    type = firstfile.type.split("/")[1];
    var self = this;
    var ReaderBlock = null;

    var readEventHandler = function(e){
        /* Se encarga de leer el evento que pasa onload del FileReader */
        if(offset >= fileSize){
            //si el puntero esta situado en el final del archivo, el archivo se leyo completo
            console.log("Done reading file");
            sendfiles();
            return;
        }
        if(e.target.error == null){
            //si no hay error, se mueve el puntero y se llama al callback
            getBase64(e.target.result, fileSize);
            offset += e.target.result.byteLength;
            callback(readFile, file);
        }
        else{
            //si hay error, se muestra en consola
            console.log("Read error: " + e.target.error);
            return;
        }
    }

    ReaderBlock = function(_offset, _file){
        /* Esta funcion instancia FileReader y lee el archivo*/

        var r = new FileReader();
        var  blob = _file.slice(_offset, blockSize + _offset); //crea un blob partiendo el archivo
        r.onload = readEventHandler;
        r.readAsArrayBuffer(blob); //lee el pedazo del archivo
    }

    /* --------------- */

    /* Se tranca aqui si se lee el pedazo entero del archivo,
    se tiene que ir leyendo si es posible de 60 MB en 60 MB o
    tamaños parecidos para que no se ahogue JavaScript, puedes hacer la prueba
    cambiando pckgSize por fileSize y poniendo un video pesado */
    ReaderBlock(offset, firstfile);

    /* --------------- */

}

function getBase64(buffer, fileSize){
    /* Se encarga de codificar el buffer en String */

    var totalbytes = new Uint8Array(buffer); // convierte el total de buffer en Bytes
    var blockbytes = new Uint8Array(frameSize); // crea un bloque de 1024 bytes
    var pointer_bytes = 0; // posicion de lectura
    var acum_bytes = 0; // bytes leidos
    var actual_part = Math.floor(offset / frameSize); // partes de 1024 bytes leidas
    var total_part = Math.floor(fileSize / frameSize); // partes totales de 1024 bytes a leer
    if(frameSize < fileSize){
        for(pointer_bytes; pointer_bytes <= totalbytes.byteLength ; pointer_bytes++){
            /* lee totalbytes y guarda cada 1024 bytes */
            blockbytes[acum_bytes] = totalbytes[pointer_bytes]; // va leyendo las entradas
            if(pointer_bytes % frameSize == 0 && pointer_bytes != 0){
                // Cuando llega a los 1024 bytes, convierte el bloque de 1024 bytes a String
                acum_bytes = 0;
                actual_part++;
                part = "("+actual_part+"/"+total_part+")"+btoa(String.fromCharCode.apply(null, blockbytes));
                parts.push(part);
                // Aqui la idea seria llamar a la funcion sendfiles(part) para que lo envie al servidor
                // un ejemplo: un paquete seria (1/4)ASDKLFadsfklaqweurqmFEeradsdfa== 
                // lo puedes ver imprimiendo console.log(part)
            }
            acum_bytes++;
        }
    }
    else{
        part = "(1/1)"+btoa(String.fromCharCode.apply(null, totalbytes));
        parts.push(part);
    }
}

function manage_response(response){
    /* muestra el resultado que mando el servidor */
    number++;
    if(number < parts.length){
        sendfiles();
    }
    console.log(response['message']);
}


function sendfiles(){
    /* Envia un JSON al servidor */
    var name = document.getElementById("name");
    var myHeaders = new Headers();
    var raw = '{"name":"'+name.value+'","type":"'+type+'","part":"'+parts[number]+'"}';
    var requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
            redirect: 'follow'
        };
    fetch('video', requestOptions)
        .then(response => response.json())
        .catch(error => console.log('Error:' + error))
        .then(response => manage_response(response));
}