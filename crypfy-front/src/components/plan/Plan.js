import React from 'react'
import BaseTemplateAdmin from '../base/BaseTemplateAdmin'
import SimpleButton from '../common/SimpleButton'
import * as planActions from '../../redux/actions/planActions'
import Moment from 'react-moment'
import StringUtils from '../../helpers/StringUtils'
import {Link} from 'react-router-dom'
import appConstants from '../../constants/AppConstants'
import {connect} from 'react-redux'
import {push} from 'react-router-redux'
import BackgroundPlaceholderMarker from '../base/BackgroundPlaceholderMarker'

class Plan extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            plans: [],
            render: false
        }

    }

    componentDidMount() {
        planActions.list(null).then((ret) => {
            this.setState({...this.state, render: true, plans: ret.response})
        })
    }

    render() {
        let planLines = null;

        if (this.state.plans && this.state.plans.length > 0)
            planLines = this.state.plans.map((p, i) => {
                return <div key={i} className={"plan-item margin-top-20 " + p.status.toLowerCase().replace('_', '-')}>
                    <div className="row">
                        <div className="col-md-12">
                            <div className={"plan-avatar float-left " + p.status.toLowerCase().replace('_', '-')}>
                               <span>
                                   <img width={30} alt={'plan ' + p.plan.name} src={p.plan.logo}/>
                               </span>
                            </div>
                            <span style={{lineHeight: "55px", marginLeft: "10px"}}
                                  className="plan-name float-left">{p.planName} <strong
                                className="description-12">{p.plan.name}
                                ({p.duration})</strong>
                            </span>
                            <div
                                className={'badge float-right badge-' + p.status.toLowerCase().replace('_', '-')}>{StringUtils.getHumanNamePlanStatus(p.status)}</div>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-3 col-sm-12 margin-top-20">
                            <div>
                                <span style={{display: "block"}} className="date-label">Início Plano</span>
                                <span className="date">
                                    {(p.startDate != null) ?
                                        <Moment format="DD/MM/YYYY" date={p.startDate}/> : "Processando.."}
                                </span>
                            </div>
                            <div>
                                <span style={{display: "block"}} className="date-label margin-top-10">Fim Plano</span>
                                <span className="date">
                                    {(p.endDate != null) ?
                                        <Moment format="DD/MM/YYYY" date={p.endDate}/> : "Processando.."}
                                </span>
                            </div>
                        </div>
                        <div className="col-md-3 col-sm-6 margin-top-20">
                            <label>Saldo Inicial</label>
                            <span className="value"><span
                                className="currency">R$</span>{StringUtils.toMoneyFormat(p.initialBalance, 2, ',', '.')}</span>
                        </div>
                        <div className="col-md-3 col-sm-12 margin-top-20">
                            <label>Lucro Total</label>
                            <span className="value"><span
                                className="currency">R$</span>{StringUtils.toMoneyFormat(p.totalProfit, 2, ',', '.')}<span
                                className="green">({p.totalProfitPercent}%)</span></span>
                        </div>
                        <div className="col-md-2 col-sm-12 margin-top-40">

                        </div>
                    </div>
                </div>
            });
        else
            planLines = <div className="widget col-md-12">
                <div className="no-results">
                        <span>
                            <img width={110} className="img-fluid margin-top-30" alt="no results"
                                 src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/no-result.svg"/>
                        </span>
                    <h3 className="margin-top-20">Ops, Você ainda não possuí nenhum Plano</h3>
                    <span className="description">
                        Você não possuí nenhum plano, talvez você queira contratar um <a
                        href="#" onClick={(e) => {
                        e.preventDefault();
                        this.props.toAddPlan();
                    }}> Novo Plano</a>
                    </span>
                </div>
            </div>

        if (this.state.render)
            return (
                <div className="animated fadeIn">
                    <nav aria-label="breadcrumb">
                        <ol className="breadcrumb">
                            <li className="breadcrumb-item">
                                <Link to={appConstants.routes.dashboard}>Visão Geral</Link>
                            </li>
                            <li className="breadcrumb-item active" aria-current="page">Planos</li>
                        </ol>
                    </nav>
                    <div>&nbsp;</div>
                    <div className="header-title">
                        <div className="float-left">
                            <span className="title">
                                 Meus Planos ({this.state.plans.length})
                            </span>
                            <div className="line"></div>
                        </div>
                        <span className="page-actions">
                            <SimpleButton onClick={() => {
                                this.props.toAddPlan()
                            }} className="btn btn-primary float-right">
                                Contratar Novo Plano
                            </SimpleButton>
                        </span>
                        <div className="dropdown page-mobile-actions float-right">
                            <button className="btn btn-primary dropdown-toggle" type="button"
                                    id="dropdownMenuButton"
                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i className="fas fa-ellipsis-v"></i>
                            </button>
                            <div className="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                <a onClick={() => {
                                    this.props.toAddPlan()
                                }} className="dropdown-item" href="#">Contratar Novo Plano</a>
                            </div>
                        </div>
                        <a className=""></a>
                    </div>
                    <div className="clearfix"></div>
                    <div className="margin-top-30">
                        {planLines}
                    </div>
                </div>
            )
        else return <BackgroundPlaceholderMarker/>
    }
}

const mapStateToProps = (state, ownProps) => {
    return {}
};

const mapDispatchToProps = (dispatch) => {
    return {
        toAddPlan: () => dispatch(push(appConstants.routes.addPlans))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Plan);