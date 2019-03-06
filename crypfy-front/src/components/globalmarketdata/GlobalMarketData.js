import React from 'react'
import BaseTemplateAdmin from '../base/BaseTemplateAdmin'
import CryptoMarketScreener from './CryptoMarketScreener'
import {Link} from 'react-router-dom'
import appConstants from '../../constants/AppConstants'
import BackgroundPlaceholderMarker from '../base/BackgroundPlaceholderMarker'

class GlobalMarketData extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            render: false
        }
    }

    componentDidMount() {
        this.setState({
            ...this.state, render: true
        })
    }

    render() {
        if (this.state.render)
            return (
                <div>
                    <div className="animated fadeIn">
                        <nav aria-label="breadcrumb">
                            <ol className="breadcrumb">
                                <li className="breadcrumb-item">
                                    <Link to={appConstants.routes.dashboard}>Visão Geral</Link>
                                </li>
                                <li className="breadcrumb-item active" aria-current="page">Dados Globais Mercado</li>
                            </ol>
                        </nav>
                        <div className="row">
                            <div className="col-md-12 margin-top-30">
                                <div className="widget">
                                    <div className="title">
                                        <h1>Dados Globais Mercado</h1>
                                        <small>Dados gerais do mercado de criptomoedas</small>
                                        <div className="clearfix"></div>
                                        <div className="line"></div>
                                    </div>
                                    <div style={{height: "800px"}} className="body">
                                        <CryptoMarketScreener/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>);
        else return <BackgroundPlaceholderMarker/>;
    }
}

export default GlobalMarketData;