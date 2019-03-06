import React from 'react'

class TickerCrypto extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {

        if (this.scriptExists())
            return;

        this.SCRIPT_ID = "embed-widget-tickers";

        const script = document.createElement('script');
        script.id = this.SCRIPT_ID;
        script.type = 'text/javascript';
        script.src = 'https://s3.tradingview.com/external-embedding/embed-widget-tickers.js';
        script.async = true;
        script.onload = onload;

        const options = {
            "symbols": [
                {
                    "description": "Bitcoin/Real",
                    "proName": "FOXBIT:BTCBRL"
                },
                {
                    "description": "Bitcoin/Dólar",
                    "proName": "COINBASE:BTCUSD"
                },
                {
                    "description": "Ethereum/Dólar",
                    "proName": "COINBASE:ETHUSD"
                },
                {
                    "description": "Ripple/Dólar",
                    "proName": "KRAKEN:XRPUSD"
                },
                {
                    "description": "Litecoin/Dólar",
                    "proName": "COINBASE:LTCUSD"
                }
            ],
            "locale": "br"
        }

        const jsonNode = document.createTextNode(JSON.stringify(options));
        script.appendChild(jsonNode);
        document.getElementById('my-ticker-crypto').appendChild(script);
    }

    scriptExists () {
        return (document.getElementById(this.SCRIPT_ID) !== null) ? true : false;
    }


    render() {
        return (
            <div className="col-md-12 no-padd">
                <div id="my-ticker-crypto" className="tradingview-widget-container">
                    <div className="tradingview-widget-container__widget"></div>
                </div>
            </div>
        )
    }
}

export default TickerCrypto;