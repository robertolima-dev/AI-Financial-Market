import React from 'react'
import StringUtils from '../../helpers/StringUtils'
import {connect} from 'react-redux'

class TotalBalanceSelection extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            activeMenu: 'totalBalance'
        }
    }

    onclick(activeMenu) {
        this.setState({...this.state,activeMenu:activeMenu})
    }

    render() {

        let activeBalance = null;

        if(this.state.activeMenu == 'availableBalance')
            activeBalance = <span><span className="description">Saldo Disponível</span>
                <span className="balance">{(this.props.totalBalance.balance) ? StringUtils.toMoneyFormat(this.props.totalBalance.balance.availableBalance,2, ',', '.') : '0,00'} <span className="currency">R$</span></span></span>
        if(this.state.activeMenu == 'blockedBalance')
            activeBalance = <span><span className="description">Saldo Bloqueado</span>
                <span className="balance">{(this.props.totalBalance.balance) ? StringUtils.toMoneyFormat(this.props.totalBalance.balance.blockedBalance,2, ',', '.') : '0,00'} <span className="currency">R$</span></span></span>
        if(this.state.activeMenu == 'bitcoinBalance')
            activeBalance = <span><span className="description">Saldo Bitcoin</span>
                <span className="balance">{(this.props.totalBalance.balance) ? StringUtils.toBitcoin(this.props.totalBalance.balance.bitcoinBalance) : '0.00000000'} <span className="currency">R$</span></span></span>
        if(this.state.activeMenu == 'totalBalance')
            activeBalance = <span><span className="description">Saldo Estimado</span>
                <span className="balance">{(this.props.totalBalance.balance) ? StringUtils.toMoneyFormat(this.props.totalBalance.balance.estimatedTotalBalance,2, ',', '.') : '0,00'} <span className="currency">R$</span></span></span>

        return (
            <div className="dropdown total-balance">
                <a href="#"style={{padding:"13px 0"}} className="btn value dropdown-toggle"
                   data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">
                    {activeBalance}
                </a>
                <div style={{padding:"0",borderRadius:"0"}} className="dropdown-menu float-right" aria-labelledby="dropdownMenuButton">
                    <ul className="total-box">
                        <li className={(this.state.activeMenu == 'availableBalance') ? 'active' : ''}>
                            <a onClick={this.onclick.bind(this,'availableBalance')}>
                                <i className="fas fa-lock-open green"></i>
                                <span className="description">Saldo Disponível</span>
                                <span className="balance">{(this.props.totalBalance.balance) ? StringUtils.toMoneyFormat(this.props.totalBalance.balance.availableBalance,2, ',', '.'): '0,00'} <span className="currency">R$</span></span>
                                <div className="clearfix"></div>
                            </a>
                        </li>
                        <li className={(this.state.activeMenu == 'blockedBalance') ? 'active' : ''}>
                            <a onClick={this.onclick.bind(this,'blockedBalance')}>
                                <i  className="fas fa-lock red"></i>
                                <span className="description">Saldo Bloqueado</span>
                                <span className="balance">{(this.props.totalBalance.balance) ? StringUtils.toMoneyFormat(this.props.totalBalance.balance.blockedBalance,2, ',', '.') : '0,00'} <span className="currency">R$</span></span>
                                <div className="clearfix"></div>
                            </a>
                        </li>
                        <li className={(this.state.activeMenu == 'bitcoinBalance') ? 'active' : ''}>
                            <a onClick={this.onclick.bind(this,'bitcoinBalance')}>
                                <i  className="fab fa-btc green"></i>
                                <span className="description">Saldo Bitcoin</span>
                                <span className="balance">{(this.props.totalBalance.balance) ? StringUtils.toBitcoin(this.props.totalBalance.balance.bitcoinBalance) : '0.00000000'} <span className="currency">BTC</span></span>
                                <div className="clearfix"></div>
                            </a>
                        </li>
                        <li className={(this.state.activeMenu == 'totalBalance') ? 'active' : ''}>
                            <a onClick={this.onclick.bind(this,'totalBalance')}>
                                <i className="fas fa-check-circle green"></i>
                                <span className="description">Saldo Estimado</span>
                                <span className="balance total">{(this.props.totalBalance.balance) ? StringUtils.toMoneyFormat(this.props.totalBalance.balance.estimatedTotalBalance,2, ',', '.') : '0,00'} <span className="currency">R$</span></span>
                                <div className="clearfix"></div>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    return {
        totalBalance: state.totalBalance
    }
};

const mapDispatchToProps = (dispatch) => {
    return {}
};

export default connect(mapStateToProps, mapDispatchToProps)(TotalBalanceSelection);