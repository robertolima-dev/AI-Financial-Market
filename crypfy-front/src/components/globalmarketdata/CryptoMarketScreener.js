import React from 'react'

class CryptoMarketScreener extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {

        if (this.scriptExists())
            return;

        this.SCRIPT_ID = "embed-widget-screener";

        const script = document.createElement('script');
        script.id = this.SCRIPT_ID;
        script.type = 'text/javascript';
        script.src = 'https://s3.tradingview.com/external-embedding/embed-widget-screener.js';
        script.async = true;
        script.onload = onload;

        const options =  {
            "width": "100%",
            "height": "100%",
            "defaultColumn": "overview",
            "screener_type": "crypto_mkt",
            "displayCurrency": "USD",
            "locale": "br"
        }

        const jsonNode = document.createTextNode(JSON.stringify(options));
        script.appendChild(jsonNode);
        document.getElementById('my-crypto-market-screener').appendChild(script);
    }

    scriptExists () {
        return (document.getElementById(this.SCRIPT_ID) !== null) ? true : false;
    }

    render() {
        return (
            <div id="my-crypto-market-screener" className="tradingview-widget-container">
                <div className="tradingview-widget-container__widget"></div>
            </div>
        );
    }
}

export default CryptoMarketScreener;