//output variables for prices
var outputBuyPriceMBtc = document.getElementById("buyPriceMBtc");
var outputBuyPriceFoxbit = document.getElementById("buyPriceFoxbit");
var outputBuyPriceNCoins = document.getElementById("buyPriceNCoins");
var outputBuyPriceBTY = document.getElementById("buyPriceBTY");
var outputBuyLtcPriceMBtc = document.getElementById("ltcBuyPriceMBtc")
var outputBuyLtcPriceNCoins = document.getElementById("ltcBuyPriceNCoins")
//sell
var outputSellPriceMBtc = document.getElementById("sellPriceMBtc");
var outputSellPriceFoxbit = document.getElementById("sellPriceFoxbit");
var outputSellPriceNCoins = document.getElementById("sellPriceNCoins");
var outputSellPriceBTY = document.getElementById("sellPriceBTY");
var outputSellLtcPriceMBtc = document.getElementById("ltcSellPriceMBtc")
var outputSellLtcPriceNCoins = document.getElementById("ltcSellPriceNCoins")

//output variables for relations
var output11 = document.getElementById("relation11");
var output12 = document.getElementById("relation12");
var output13 = document.getElementById("relation13");

var output21 = document.getElementById("relation21");
var output22 = document.getElementById("relation22");
var output23 = document.getElementById("relation23");

var output31 = document.getElementById("relation31");
var output32 = document.getElementById("relation32");
var output33 = document.getElementById("relation33");

var output41 = document.getElementById("relation41");
var output42 = document.getElementById("relation42");
var output43 = document.getElementById("relation43");

//ltc relations
var outputLtc12 = document.getElementById("relationLtc12");
var outputLtc21 = document.getElementById("relationLtc21");

//output means
var outputMeanBtcBuy = document.getElementById("meanBtcBuy");
var outputMeanBtcSell = document.getElementById("meanBtcSell");
var outputMeanLtcBuy = document.getElementById("meanLtcBuy");
var outputMeanLtcSell = document.getElementById("meanLtcSell");

//negociation amount
var btcDealSize = 40000;
var ltcDealSize = 10000;

//control
var control = 0, count=0;

//do everything - every 10 sec
var timer = function(){

    //get data values from api
    var rel = new XMLHttpRequest();
    rel.open( "GET", "http://localhost:8090/api/"+btcDealSize+"/"+ltcDealSize, false );
    rel.send( null );
    //json response
    var relationsList = JSON.parse(rel.responseText);

    //mercado btc output
    outputBuyPriceMBtc.innerHTML = "R$ "+Number(relationsList.mBtcBuyPrice).toFixed(2);
    outputSellPriceMBtc.innerHTML = "R$ "+Number(relationsList.mBtcSellPrice).toFixed(2);

    //foxbit btc output
    outputBuyPriceFoxbit.innerHTML = "R$ "+Number(relationsList.foxbitBuyPrice).toFixed(2);
    outputSellPriceFoxbit.innerHTML = "R$ "+Number(relationsList.foxbitSellPrice).toFixed(2);

    //negocie coins btc output
    outputBuyPriceNCoins.innerHTML = "R$ "+Number(relationsList.nCoinsBuyPrice).toFixed(2);
    outputSellPriceNCoins.innerHTML = "R$ "+Number(relationsList.nCoinsSellPrice).toFixed(2);

    //bitcoin to you btc output
    outputBuyPriceBTY.innerHTML = "R$ "+Number(relationsList.bTYBuyPrice).toFixed(2);
    outputSellPriceBTY.innerHTML = "R$ "+Number(relationsList.bTYSellPrice).toFixed(2);

    //ltc mbtc output
    outputBuyLtcPriceMBtc.innerHTML = "R$ "+Number(relationsList.mBtcLtcBuyPrice).toFixed(2);
    outputSellLtcPriceMBtc.innerHTML = "R$ "+Number(relationsList.mBtcLtcSellPrice).toFixed(2);

    //ltc ncoins output
    outputBuyLtcPriceNCoins.innerHTML = "R$ "+Number(relationsList.nCoinsLtcBuyPrice).toFixed(2);
    outputSellLtcPriceNCoins.innerHTML = "R$ "+Number(relationsList.nCoinsLtcSellPrice).toFixed(2);

    //output relations
    output11.innerHTML = Number(relationsList.mBtcToFoxbit).toFixed(2)+"%";
    output12.innerHTML = Number(relationsList.mBtcToNCoins).toFixed(2)+"%";
    output13.innerHTML = Number(relationsList.mBtcToBTY).toFixed(2)+"%";

    output21.innerHTML = Number(relationsList.foxbitToMBtc).toFixed(2)+"%";
    output22.innerHTML = Number(relationsList.foxbitToNCoins).toFixed(2)+"%";
    output23.innerHTML = Number(relationsList.foxbitToBTY).toFixed(2)+"%";

    output31.innerHTML = Number(relationsList.nCoinsToMBtc).toFixed(2)+"%";
    output32.innerHTML = Number(relationsList.nCoinsToFoxbit).toFixed(2)+"%";
    output33.innerHTML = Number(relationsList.nCoinsToBTY).toFixed(2)+"%";

    output41.innerHTML = Number(relationsList.bTYouToMBtc).toFixed(2)+"%";
    output42.innerHTML = Number(relationsList.bTYouToFoxbit).toFixed(2)+"%";
    output43.innerHTML = Number(relationsList.bTYouToNCoins).toFixed(2)+"%";

    //ltc relations
    outputLtc12.innerHTML = Number(relationsList.ltcMBtcToNCoins).toFixed(2)+"%";
    outputLtc21.innerHTML = Number(relationsList.ltcNCoinsToMBtc).toFixed(2)+"%";

    //set colors
    if(relationsList.mBtcToFoxbit>0){
        document.getElementById("relation11").style.color = 'green'
    }else {
        document.getElementById("relation11").style.color = 'red'
    }
    if(relationsList.mBtcToNCoins>0){
        document.getElementById("relation12").style.color = 'green'
    }else{
        document.getElementById("relation12").style.color = 'red'
    }
    if(relationsList.mBtcToBTY>0){
        document.getElementById("relation13").style.color = 'green'
    }else{
        document.getElementById("relation13").style.color = 'red'
    }

    if(relationsList.foxbitToMBtc>0){
        document.getElementById("relation21").style.color = 'green'
    }else{
        document.getElementById("relation21").style.color = 'red'
    }
    if(relationsList.foxbitToNCoins>0){
        document.getElementById("relation22").style.color = 'green'
    }else{
        document.getElementById("relation22").style.color = 'red'
    }
    if(relationsList.foxbitToBTY>0){
        document.getElementById("relation23").style.color = 'green'
    }else{
        document.getElementById("relation23").style.color = 'red'
    }

    if(relationsList.nCoinsToMBtc>0){
        document.getElementById("relation31").style.color = 'green'
    }else{
        document.getElementById("relation31").style.color = 'red'
    }
    if(relationsList.nCoinsToFoxbit>0){
        document.getElementById("relation32").style.color = 'green'
    }else{
        document.getElementById("relation32").style.color = 'red'
    }
    if(relationsList.nCoinsToBTY>0){
        document.getElementById("relation33").style.color = 'green'
    }else{
        document.getElementById("relation33").style.color = 'red'
    }

    if(relationsList.bTYouToMBtc>0){
        document.getElementById("relation41").style.color = 'green'
    }else{
        document.getElementById("relation41").style.color = 'red'
    }
    if(relationsList.bTYouToFoxbit>0){
        document.getElementById("relation42").style.color = 'green'
    }else{
        document.getElementById("relation42").style.color = 'red'
    }
    if(relationsList.bTYouToNCoins>0){
        document.getElementById("relation43").style.color = 'green'
    }else{
        document.getElementById("relation43").style.color = 'red'
    }
    //ltc colors
    if(relationsList.ltcMBtcToNCoins>0){
        document.getElementById("relationLtc12").style.color = 'green'
    }else{
        document.getElementById("relationLtc12").style.color = 'red'
    }
    if(relationsList.ltcNCoinsToMBtc>0){
        document.getElementById("relationLtc21").style.color = 'green'
    }else{
        document.getElementById("relationLtc21").style.color = 'red'
    }

    //output means
    outputMeanBtcBuy.innerHTML = "R$ "+Number(relationsList.btcMeanBuyPrice).toFixed(2);
    outputMeanBtcSell.innerHTML = "R$ "+Number(relationsList.btcMeanSellPrice).toFixed(2);
    outputMeanLtcBuy.innerHTML = "R$ "+Number(relationsList.ltcMeanBuyPrice).toFixed(2);
    outputMeanLtcSell.innerHTML = "R$ "+Number(relationsList.ltcMeanSellPrice).toFixed(2);

    //negocie coins oportunity?
    if( (relationsList.nCoinsToFoxbit>0.5 || relationsList.nCoinsToBTY>0.5 || relationsList.ltcNCoinsToMBtc>0.5) && ((count-control)>6*10) ){

        var rel = new XMLHttpRequest();
        rel.open( "GET", "http://localhost:8082/call", false );
        rel.send( null );
        control=count;
    }
    //control
    count++;

}
setInterval(timer, 10000);

//control inputs
var inputBtc = document.getElementById("btcInput");
var inputLtc = document.getElementById("ltcInput");
//renew stuff
inputBtc.oninput = function() {
    btcDealSize = this.value;
}
inputLtc.oninput = function() {
    ltcDealSize = this.value;
}
