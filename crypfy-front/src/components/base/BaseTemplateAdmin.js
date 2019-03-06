import React from 'react'
import WrapperAbsolute from '../common/WrapperAbsolute'
import {Link} from 'react-router-dom'
import appConstants from '../../constants/AppConstants'
import {connect} from 'react-redux';
import TotalBalanceSelection from './TotalBalanceSelection'
import * as userActions from '../../redux/actions/userActions'
import TickerCrypto from '../base/TickerCrypto'
import Settings from '../settings/Settings'
import OwlCarousel from 'react-owl-carousel2';
import 'react-owl-carousel2/lib/styles.css';
import * as dashboardActions from '../../redux/actions/dashboardActions'
import StringUtils from '../../helpers/StringUtils'

class BaseTemplateAdmin extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            sidebar: false,
            settingsModal: false,
            sidebarPlans: false,
            sidebarDeposits: false,
            sidebarWithdraws: false,
            tickers: null,
            activeMenu: this.props.activeMenu
        }

        this.options = {
            items: 4,
            nav: false,
            rewind: true,
            autoplay: true,
            loop: true,
            dots: false,
            autoplayTimeout: 3600,
            responsive: {
                // breakpoint from 0 up
                0: {
                    items: 1
                },
                // breakpoint from 480 up
                480: {
                    items: 2
                },
                // breakpoint from 768 up
                768: {
                    items: 3
                },
                1224: {
                    items: 4
                },
                1824: {
                    items: 5
                }
            }
        };
        dashboardActions.tickers().then((ret) => {
            this.setState({...this.state, tickers: ret.response})
        })
    }

    setActiveMenu(pathname) {
         this.setState({...this.state,activeMenu:pathname})
    }

    toggleMobileSidebar() {
        if (this.state.sidebar)
            this.setState({...this.state, sidebar: false});
        else
            this.setState({...this.state, sidebar: true});
    }

    logout(e) {
        e.preventDefault();
        this.props.logout();
    }

    toggleSettingsModal(e) {
        e.preventDefault();
        this.refs.settingsComponent.getWrappedInstance().toggleModal();
    }

    toggleMobileSubSidebar(control, e) {
        e.preventDefault();
        if (control == 'plans') {
            if (this.state.sidebarPlans) {
                this.setState({...this.state, sidebarPlans: false});
            } else {
                this.setState({...this.state, sidebarPlans: true});
            }
        }
        if (control == 'deposits') {
            if (this.state.sidebarDeposits) {
                this.setState({...this.state, sidebarDeposits: false});
            } else {
                this.setState({...this.state, sidebarDeposits: true});
            }
        }
        if (control == 'withdraws') {
            if (this.state.sidebarWithdraws) {
                this.setState({...this.state, sidebarWithdraws: false});
            } else {
                this.setState({...this.state, sidebarWithdraws: true});
            }
        }
    }

    render() {
        let tickers = null;
        if (this.state.tickers != null)
            tickers = this.state.tickers.map((t, i) => {
                let percent = null;
                let secondPercent = null;

                if(t.percent > 0)
                    percent = <span className="green">({t.percent.toFixed(2)}%)</span>;
                if(t.percent < 0)
                    percent = <span className="red">({t.percent.toFixed(2)}%)</span>;
                if(t.percent == 0)
                    percent = <span>({t.percent}%)</span>;

                if(t.secondPercent > 0)
                    secondPercent = <span className="green">({t.secondPercent.toFixed(2)}%)</span>;
                if(t.secondPercent < 0)
                    secondPercent = <span className="red">({t.secondPercent.toFixed(2)}%)</span>;
                if(t.secondPercent == 0)
                    secondPercent = <span>({t.secondPercent}%)</span>;
                return (
                    <div key={i} className="real-time-widget">
                        <div className="content">
                            <div className="head">
                                <span className="value float-left">{t.label}</span>
                                <span className="past-value float-right">
                                            <span className="block">
                                                <strong>{(t.secondLabel != null) ? t.secondLabel : ""}</strong>
                                            </span> {(t.secondValue != null) ? StringUtils.toMoneyFormat(t.secondValue,2,',','.') : ""} {secondPercent}
                                        </span>
                            </div>
                            <div className="clearfix"></div>
                            <span className="number">
                                {(t.value != null) ?  <span><span className="currency">R$</span>{StringUtils.toMoneyFormat(t.value,2,',','.')}</span> : ""}{percent}
                            </span>
                        </div>
                    </div>
                );
            })
        let owl = (tickers != null) ?  <OwlCarousel ref="car" options={this.options}>
            {tickers}
        </OwlCarousel> : null;

        return (
            <WrapperAbsolute style={{backgroundColor: "rgb(244, 244, 244)"}}>
                <div className="nav-top">
                    <div className="container-fluid">
                        <div className="row">
                            <div className="col-md-12">
                                <div className="row">
                                    <div className="col-md-12">
                                        <a href={appConstants.routes.dashboard} className="logo">
                                            <img width={100}
                                                 src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/full-logo-crypfy.svg"/>
                                        </a>
                                        <ul className="nav float-right">
                                            <li className="nav-item">
                                                <div className="dropdown">
                                                    <a href="#" style={{
                                                        textTransform: "none",
                                                        fontSize: "14px",
                                                        fontWeight: "300",
                                                        color: "#fff"
                                                    }} className="btn dropdown-toggle" data-toggle="dropdown"
                                                       aria-haspopup="true"
                                                       aria-expanded="false">
                                                        <span className="margin-right-10"><img
                                                            className="rounded-circle" width="30"
                                                            src={(this.props.session.user.user) ? this.props.session.user.user.user.avatar : ""}/></span>
                                                        {(this.props.session.user.user && this.props.session.user.user.user.name.length > 15) ? this.props.session.user.user.user.name.substring(0, 15) : ""}
                                                        {(this.props.session.user.user && this.props.session.user.user.user.name.length <= 15) ? this.props.session.user.user.user.name : ""}
                                                        {(this.props.totalBalance.balance.pendingAccount) ?
                                                            <span className="warning-profile">!</span> : ''}
                                                    </a>
                                                    <div className="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                                        <a onClick={this.toggleSettingsModal.bind(this)}
                                                           className="dropdown-item" href="#">
                                                            Configurações {(this.props.totalBalance.balance.pendingAccount) ?
                                                                <span
                                                                    className="badge badge-danger animated infinite bounce">!</span> : ''}
                                                        </a>
                                                        <div className="dropdown-divider"></div>
                                                        <a onClick={this.logout.bind(this)} className="dropdown-item"
                                                           href="#"> Logout</a>
                                                    </div>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="container-fluid">
                    <div className="row">
                        {owl}
                    </div>
                    <div className="row header">
                        <div className="col-md-12">
                            <ul className="menu nav float-left">
                                <li className={"nav-item " + (this.state.activeMenu == '/secured/dashboard' ? "active" : "")}>
                                    <Link to={appConstants.routes.dashboard}>
                                        Visão Geral
                                    </Link>
                                </li>
                                <li className={"nav-item " + (this.state.activeMenu == '/secured/plans' || this.state.activeMenu == '/secured/plans/add' ? "active" : "")}>
                                    <a className="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
                                       aria-expanded="false">
                                        Planos
                                    </a>
                                    <div className="dropdown-menu animated fadeIn">
                                        <Link
                                            className={"dropdown-item " + (this.state.activeMenu == '/secured/plans' ? "active" : "")}
                                            to={appConstants.routes.plans}>
                                            Meus Planos
                                        </Link>
                                        <Link
                                            className={"dropdown-item " + (this.state.activeMenu == '/secured/plans/add' ? "active" : "")}
                                            to={appConstants.routes.addPlans}>
                                            Contratar Plano
                                        </Link>
                                    </div>
                                </li>
                                <li className={"nav-item " + (this.state.activeMenu == '/secured/deposits-brl' || this.state.activeMenu == '/secured/deposits-brl/add' ? "active" : "")}>
                                    <a className="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
                                       aria-expanded="false">
                                        Depósitos
                                    </a>
                                    <div className="dropdown-menu animated fadeIn">
                                        <Link
                                            className={"dropdown-item " + (this.state.activeMenu == '/secured/deposits-brl' ? "active" : "")}
                                            to={appConstants.routes.depositsBrl}>
                                            BRL
                                        </Link>
                                        <a className={"dropdown-item "} target="blank" href="https://crypfy.zendesk.com/hc/pt-br/articles/360003915051-Fazendo-um-Dep%C3%B3sito-em-Bitcoin-BTC">
                                            Bitcoin
                                        </a>
                                    </div>
                                </li>
                                <li className={"nav-item " + (this.state.activeMenu == '/secured/withdraws-brl' || this.state.activeMenu == '/secured/plan-take-profits' || this.state.activeMenu == '/secured/bank-accounts' || this.state.activeMenu == '/secured/withdraws-brl/add' ? "active" : "")}>
                                    <a className="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
                                       aria-expanded="false">
                                        Saques
                                    </a>
                                    <div className="dropdown-menu animated fadeIn">
                                        <Link
                                            className={"dropdown-item " + (this.state.activeMenu == '/secured/withdraws-brl' ? "active" : "")}
                                            to={appConstants.routes.withdrawsBrl}>
                                            BRL
                                        </Link>
                                        <div className="dropdown-divider"></div>
                                        <Link
                                            className={"dropdown-item " + (this.state.activeMenu == '/secured/bank-accounts' ? "active" : "")}
                                            to={appConstants.routes.bankAccount}>
                                            Minhas Contas Bancárias
                                        </Link>
                                        <Link
                                            className={"dropdown-item " + (this.state.activeMenu == '/secured/plan-take-profits' ? "active" : "")}
                                            to={appConstants.routes.planTakeProfit}>
                                            Movimentação de Lucro
                                        </Link>
                                    </div>
                                </li>
                                <li className={"nav-item " + (this.state.activeMenu == '/secured/global-market-data' ? "active" : "")}>
                                    <Link to={appConstants.routes.globalMarketData}>
                                        Dados Globais Mercado
                                    </Link>
                                </li>
                            </ul>
                            <a className="sidebar-button" onClick={this.toggleMobileSidebar.bind(this)}>
                                <i style={{fontSize: "24px"}} className="lnr lnr-menu"></i>
                            </a>
                            <div className="balances float-right">
                                <TotalBalanceSelection/>
                            </div>
                            <div className="clearfix"></div>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-12">
                            <ul className={(this.state.sidebar) ? "nav-mobile active" : "nav-mobile"}>
                                <li className={(this.state.activeMenu == '/secured/dashboard' ? "active" : "")}>
                                    <Link to={appConstants.routes.dashboard}>
                                        Visão Geral
                                    </Link>
                                </li>
                                <li className={(this.state.activeMenu == '/secured/plans' || this.state.activeMenu == '/secured/plans/add' ? "active" : "")}>
                                    <a onClick={this.toggleMobileSubSidebar.bind(this, 'plans')} href="#">
                                        Planos
                                    </a>
                                    <ul className={(this.state.sidebarPlans) ? "active" : ""}>
                                        <li className={(this.state.activeMenu == '/secured/plans' ? "active" : "")}>
                                            <Link to={appConstants.routes.plans}>
                                                Meus Planos
                                            </Link>
                                        </li>
                                        <li className={(this.state.activeMenu == '/secured/plans/add' ? "active" : "")}>
                                            <Link to={appConstants.routes.addPlans}>
                                                Contratar Planos
                                            </Link>
                                        </li>
                                    </ul>
                                </li>
                                <li className={(this.state.activeMenu == '/secured/deposits-brl' || this.state.activeMenu == '/secured/deposits-brl/add' ? "active" : "")}>
                                    <a onClick={this.toggleMobileSubSidebar.bind(this, 'deposits')} href="#">
                                        Depósitos
                                    </a>
                                    <ul className={(this.state.sidebarDeposits) ? "active" : ""}>
                                        <li className={(this.state.activeMenu == '/secured/deposits-brl' ? "active" : "")}>
                                            <Link to={appConstants.routes.depositsBrl}>
                                                BRL
                                            </Link>
                                        </li>
                                        <li>
                                            <a className={"dropdown-item "} target="blank" href="https://crypfy.zendesk.com/hc/pt-br/articles/360003915051-Fazendo-um-Dep%C3%B3sito-em-Bitcoin-BTC">
                                                Bitcoin
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                                <li className={(this.state.activeMenu == '/secured/withdraws-brl' || this.state.activeMenu == '/secured/bank-accounts' || this.state.activeMenu == '/secured/plan-take-profits' ? "active" : "")}>
                                    <a onClick={this.toggleMobileSubSidebar.bind(this, 'withdraws')} href="#">
                                        Saques
                                    </a>
                                    <ul className={(this.state.sidebarWithdraws) ? "active" : ""}>
                                        <li className={(this.state.activeMenu == '/secured/withdraws-brl' ? "active" : "")}>
                                            <Link to={appConstants.routes.withdrawsBrl}>
                                                BRL
                                            </Link>
                                        </li>
                                        <li className={(this.state.activeMenu == '/secured/bank-accounts' ? "active" : "")}>
                                            <Link to={appConstants.routes.bankAccount}>
                                                Minhas Contas Bancárias
                                            </Link>
                                        </li>
                                        <li className={(this.state.activeMenu == '/secured/plan-take-profits' ? "active" : "")}>
                                            <Link to={appConstants.routes.planTakeProfit}>
                                                Movimentação de Lucro
                                            </Link>
                                        </li>
                                    </ul>
                                </li>
                                <li className={(this.state.activeMenu == '/secured/global-market-data' ? "active" : "")}>
                                    <Link to={appConstants.routes.globalMarketData}>
                                        Dados Globais Mercado
                                    </Link>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div style={{backgroundColor: '#f4f4f4', paddingBottom: 50}} className="container-fluid">
                    {this.props.children}
                </div>
                <Settings ref="settingsComponent"/>
            </WrapperAbsolute>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    return {
        session: state.session,
        totalBalance: state.totalBalance
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        logout: () => dispatch(userActions.logout())
    }
};

export default connect(mapStateToProps, mapDispatchToProps,null,{ withRef : true})(BaseTemplateAdmin);