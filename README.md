
# <h1>netStream</h1>
Bienvenido ! 
netStream es una pequeña app web para manejo de usuarios y videos, esta implantada con un backend hecho en java, se
comunica con el frontend estrictamente con jsons

En general, la aplicacion se diseño para que sea jerarquice errores, de tal manera que el manejo de jsons con postman
se hace comodo, no tienes que aprenderte las llaves que necesita cada json, como dicen por ahi... Plug and Play ! 


# <h4>¿Y entonces, cómo pruebo el backend?</h4>
Debido a que el frontend se encuentra en desarrollo, actualmente hay algunas funcionalidades pero no todas... Sin embargo, como
el frontend es independiente completamente del backend, gracias al software <b>Postman</b> podras enviar y recibir jsons del 
backend, solo basta con montar el servidor en eclipse con Apache Tomcat, poner la URL del servlet segun sea, el metodo
segun sea, y enviar jsons via raw, no tienes *por qué* saberte las llaves del json, el servlet te lo dirá...

<b>Pero, cómo las veo?</b>

Cuando envies un json en raw, envia un json vacio... Algo así: 
{}

<h3>Login</h3>

servlet: /netStream/login
metodos: POST


<h3>Register</h3>

servlet: /netStream/register
metodos: POST

<h3>Upload</h3>

servlet: /netStream/upload
metodos: POST

<h3>Delete</h3>
servlet: /netStream/delete
metodos: POST

<h3>Video</h3>
servlet: /netStream/video
metodos: GET (proximamente), POST
<br/>
<br/>
<br/>
<b>Algunas acotaciones:</b>

La mayoria de las cosas son autodescriptibles, en register tienes user, email and password, ahora
para login necesitas tu constraint ¿Cual es tu constraint? tu username o email. Para delete y update solo contraseña

Para video, necesitas que las tramas que esten pasando ya vengan segmentadas y codificadas en String gracias a <b>Base64</b>
Hay maneras de demostrarlo con archivos pequeños, pero con videos, se necesita un script en JS para subirlo al server, no
basta directamente con postman
