//output portfolio index
var outputLinea = document.getElementById("linea");
var outputAudace = document.getElementById("audace");

//do it
//get data values from api
var rel = new XMLHttpRequest();
rel.open( "GET", "http://localhost:8082/get-audace-index", false );
rel.send( null );

var rel2 = new XMLHttpRequest();
rel2.open( "GET", "http://localhost:8082/get-linea-index", false );
rel2.send( null );

// response
var audace = rel.responseText;
var linea = rel2.responseText;

outputAudace.innerHTML = audace+"%";
outputLinea.innerHTML = linea+"%";

//update it
var timer = function(){
    //get data values from api
    var rel = new XMLHttpRequest();
    rel.open( "GET", "localhost:8082/get-audace-index/", false );
    rel.send( null );

    var rel2 = new XMLHttpRequest();
    rel2.open( "GET", "localhost:8082/get-linea-index/", false );
    rel2.send( null );

    // response
    var audace = rel.responseText;
    var linea = rel2.responseText;

    outputAudace.innerHTML = audace+"%";
    outputLinea.innerHTML = linea+"%";
}
setInterval(timer, 1000*60*10);