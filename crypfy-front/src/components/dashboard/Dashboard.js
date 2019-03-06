import React from 'react'
import * as userActions from '../../redux/actions/userActions'
import {connect} from 'react-redux'
import * as dashboardActions from '../../redux/actions/dashboardActions'
import StringUtils from '../../helpers/StringUtils'
import Moment from 'react-moment'
import moment from 'moment'
import OwlCarousel from 'react-owl-carousel2';
import 'react-owl-carousel2/lib/styles.css';
import {Link} from 'react-router-dom'
import appConstants from '../../constants/AppConstants'
import StatWidget from './StatWidget'
import {Sparklines, SparklinesCurve, SparklinesLine} from 'react-sparklines'
import BackgroundPlaceholderMarker from '../base/BackgroundPlaceholderMarker'
import ReactEcharts from 'echarts-for-react';

class Dashboard extends React.Component {
    constructor(props) {
        super(props);

        document.body.style.backgroundColor = "rgb(244, 244, 244)";

        this.opt = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    animation: false
                }
            },
            grid: {
                width: 'auto',
                left: '5%',
                right: '2%',
                top:'5%'
            },
            xAxis: {
                type: 'category',
                data: [],
                splitLine: {
                    show: false
                },
                axisLabel: {
                    color:'#777',
                    fontSize:13,
                    rotate:45,
                    inside:true,
                    interval:15,
                    showMinLabel:false,
                    showMaxLabel:false
                },
                axisLine:{
                    lineStyle: {
                        color:'#777',
                        type: 'dotted'
                    }
                },
                axisTick:{
                    color:'#ddd'
                },
            },
            yAxis: {
                type: 'value',
                boundaryGap: [0, '100%'],
                splitLine: {
                    show: false
                },
                axisLabel: {
                    color:'#777',
                    fontSize:13,
                    rotate:45,
                    interval:10
                },
                axisLine:{
                    lineStyle: {
                        color:'#777',
                        type: 'dotted'
                    }
                },
                axisTick:{
                    color:'#ddd'
                },
                max:'dataMax'
            },
            series: [{
                name: 'Saldo',
                type: 'line',
                showSymbol: false,
                hoverAnimation: false,
                data: [],
                lineStyle : {
                    color:'rgba(65, 111, 186,1)',
                    width: 2
                },
                itemStyle : {
                    color:'rgba(65, 111, 186,1)'
                },
                areaStyle: {
                    color:'rgb(65, 111, 186,.2)'
                },
                symbolSize: "10"
            }]
        };

        this.owlOptions = {
            items: 1,
            nav: false,
            rewind: true,
            autoplay: false
        };

        this.state = {
            render: false,
            data: null,
            test: "",
            withdrawlWidgetStyle: "details"
        }
    }

    componentDidMount() {
        dashboardActions.data().then((ret) => {
            let labels = [];
            let data = [];
            ret.response.balanceEvolution.forEach((b) => {
                labels.push(moment(b.date).format("DD/MM/YYYY"));
                data.push(b.balance.toFixed(2));
            })

            this.opt.series[0].data = data;
            this.opt.xAxis.data = labels;

            this.setState({...this.state, render: true, data: ret.response})
        })
    }

    render() {
        if (this.state.render) {
            //section in progress plan
            const plans = this.state.data.inProgressPlans.map((p, i) => {
                return <div style={{position: "relative", zIndex: "999"}} key={i}
                            className="col-12 col-sm-12 col-md-12 col-lg-6 col-xl-6 margin-top-30">
                    <div className={"card-plan " + p.status.toLowerCase().replace('_', '-')}>
                        <div className="row">
                            <div className="col-md-12">
                                <div className={"plan-avatar float-left " + p.status.toLowerCase().replace('_', '-')}>
                                   <span>
                                       <img width={30} alt={'plan ' + p.plan.name} src={p.plan.logo}/>
                                   </span>
                                </div>
                                <div className="plan-name">
                                    <span className="float-left">
                                        {p.planName} <strong className="margin-top- description-12">Plano {p.plan.name}
                                        ({p.duration})</strong>
                                    </span>
                                </div>
                                <div
                                    className={'badge float-right badge-' + p.status.toLowerCase().replace('_', '-')}>{StringUtils.getHumanNamePlanStatus(p.status)}</div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-md-12 margin-top-20">
                                <span className="heading">Lucro Total</span>
                                <span className="balance-value float-left">
                                    <span
                                        className="currency">R$</span> {StringUtils.toMoneyFormat(p.totalProfit, 2, ',', '.')}
                                    <span className="green">({p.totalProfitPercent}%)</span>
                                </span>
                            </div>
                        </div>
                        <div className="row margin-top-10">
                            <div className="col-md-4">
                                <span className="heading">Saldo Atual</span>
                                <span className="currency-2">
                                    R$
                                </span>
                                <span
                                    className="balance-value-2"> {StringUtils.toMoneyFormat(p.currentBalance, 2, ',', '.')}</span>
                            </div>
                            <div className="col-md-4">
                                <span className="heading">Lucro Disponível</span>
                                <span className="currency-2">
                                    R$
                                </span>
                                <span
                                    className="balance-value-2"> {StringUtils.toMoneyFormat(p.availableProfit, 2, ',', '.')}</span>
                            </div>
                            <div className="col-md-4">
                                <span className="heading">Lucro Retirado</span>
                                <span className="currency-2">
                                    R$
                                </span>
                                <span
                                    className="balance-value-2"> {StringUtils.toMoneyFormat(p.sumWithdraw, 2, ',', '.')}</span>
                            </div>
                        </div>
                        <div className="row margin-top-10">
                            <div className="col-md-4">
                                <span className="heading">Saldo Inicial</span>
                                <span className="currency-2">
                                    R$
                                </span>
                                <span
                                    className="balance-value-2"> {StringUtils.toMoneyFormat(p.initialBalance, 2, ',', '.')}</span>
                            </div>
                            <div className="col-md-4">
                                <span className="heading"> Início Plano</span>
                                <span className="date">
                                    <Moment format="DD/MM/YYYY" date={p.startDate}/>
                                </span>
                            </div>
                            <div className="col-md-4">
                                <span className="heading"> Término Plano</span>
                                <span className="date">
                                    <Moment format="DD/MM/YYYY" date={p.endDate}/>
                                </span>
                            </div>
                        </div>
                        <div className="sparkline">
                            <Sparklines width={100} height={25} data={p.evolutionPoints} margin={0}>
                                <SparklinesCurve
                                    style={{fill: "rgb(65, 111, 186)", strokeWidth: "0.4", fillOpacity: ".2"}}
                                    color="rgb(65, 111, 186)"/>
                            </Sparklines>
                        </div>
                    </div>
                </div>
            })


            //section balance evolution
            const balanceEvolution = (this.state.data && this.state.data.balanceEvolution.length > 0) ? <section>
                <div className="row margin-top-30">
                    <div className="col-md-12">
                        <div className="widget">
                            <div className="title">
                                <h1>Evolução de Saldo</h1>
                                <small>Histórico de evolução de saldo</small>
                                <div className="clearfix"></div>
                                <div className="line"></div>
                            </div>
                            <div style={{width:"100%"}} className="body">
                                <ReactEcharts
                                    option={this.opt}
                                    style={{height: '400px', width: '100%'}}
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </section> : "";

            //section welcome
            const welcome = (this.state.data && this.state.data.balanceEvolution.length == 0 && this.state.data.inProgressPlans.length == 0) ?
                <section>
                    <div className="row margin-top-30">
                        <div className="col-md-12">
                            <div className="widget">
                                <div className="body">
                                    <OwlCarousel ref="owl" options={this.owlOptions}>
                                        <div>
                                            <div className="row">
                                                <div className="col-md-7">
                                                    <h1 className="font-25" style={{lineHeight: "40px"}}>
                                                        Comece agora na melhor plataforma de investimento e
                                                        gerenciamento de criptomoedas
                                                    </h1>
                                                    <div style={{marginRight: "7px"}}
                                                         className="browser-mockup with-url show-on-mobile-block margin-top-30">
                                                        <iframe width="100%" height="315"
                                                                src="https://www.youtube.com/embed/IHeA0g2qRAk"
                                                                frameBorder="0" allow="autoplay; encrypted-media"
                                                                allowFullScreen></iframe>
                                                    </div>
                                                    <h2 className="font-18 margin-top-50">Portifólios
                                                        e Inteligência Artificial</h2>
                                                    <p className="description margin-top-20">
                                                        Nossa Inteligência Artifical identifica automaticamente quais
                                                        são as Criptomoedas mais promissoras e cria portfólios com
                                                        enorme potencial de lucratividade, incluindo dispositivo de
                                                        segurança para prevenção dos períodos de queda.
                                                    </p>
                                                </div>
                                                <div className="col-md-5 hide-on-mobile-block">
                                                    <div style={{marginRight: "7px"}}
                                                         className="browser-mockup with-url">
                                                        <iframe width="100%" height="315"
                                                                src="https://www.youtube.com/embed/IHeA0g2qRAk"
                                                                frameBorder="0" allow="autoplay; encrypted-media"
                                                                allowFullScreen></iframe>
                                                    </div>

                                                </div>
                                            </div>
                                            <div className="row">
                                                <div className="col-md-12">
                                                    <Link className="btn btn-primary margin-right-10 margin-top-20"
                                                          to={appConstants.routes.addPlans}>
                                                        Conheça Nossos Planos
                                                    </Link>
                                                    <a href="#" onClick={(e) => {
                                                        e.preventDefault();
                                                        this.refs.owl.next()
                                                    }} className="btn btn-success margin-top-20">
                                                        Entenda Sobre Depósitos
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                        <div>
                                            <div className="row">
                                                <div className="col-md-7">
                                                    <h1 className="font-25" style={{lineHeight: "40px"}}>
                                                        Faça Depósitos
                                                    </h1>
                                                    <h2 className="font-18 margin-top-50">Depósitos em BRL ou
                                                        Bitcoin</h2>
                                                    <p className="description margin-top-20">
                                                        Para contratação de novos planos você deve ter em sua conta
                                                        saldo disponível. Para adicionar saldo na plataforma você deverá
                                                        realizar depósitos, e pode optar por opções de depósito em
                                                        <strong>BRL</strong> ou
                                                        depósitos em <strong>Bitcoin</strong>.
                                                    </p>
                                                </div>
                                                <div className="col-md-5 text-center hide-on-mobile-block">
                                                    <span>
                                                        <img alt="bitcoin box" width={260}
                                                             src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/deposit-bitcoin.svg"/>
                                                    </span>
                                                </div>
                                            </div>
                                            <div className="row">
                                                <div className="col-md-12">
                                                    <Link className="btn btn-primary margin-right-10 margin-top-20"
                                                          to={appConstants.routes.depositsBrlAdd}>
                                                        Faça um Depósito em BRL
                                                    </Link>
                                                    <a href="#" onClick={(e) => {
                                                        e.preventDefault();
                                                        alert('função ainda não disponível')
                                                    }} className="btn btn-success margin-top-20">
                                                        Faça um Depósito em Bitcoin
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </OwlCarousel>
                                </div>
                            </div>
                        </div>
                    </div>
                </section> : "";


            const totals = (this.state.data && this.state.data.totalDeposit > 0) ?
                <div className="section">
                    <div className="row">
                        <div className="col-md-4 col-sm-12 col-lg-4 col-12">
                            <StatWidget items={[{
                                label: "Efetuado BRL",
                                currency: "R$",
                                value: StringUtils.toMoneyFormat(this.state.data.totalDeposit, 2, ',', '.'),
                                active: "active"
                            }, {
                                label: "Efetuado BITCOIN",
                                currency: "BTC",
                                value: StringUtils.toBitcoin(0),
                                active: ""
                            },
                                {
                                    label: "Confirmados",
                                    currency: "",
                                    value: this.state.data.totalDepositConfirmed,
                                    active: ""
                                },
                                {
                                    label: "Aguardando Comprovante",
                                    currency: "",
                                    value: this.state.data.totalDepositWaitingPhotoUpload,
                                    active: ""
                                },
                                {
                                    label: "Em Análise",
                                    currency: "",
                                    value: this.state.data.totalDepositWaitingApproval,
                                    active: ""
                                },
                                {
                                    label: "Negados",
                                    currency: "",
                                    value: this.state.data.totalDepositDenied,
                                    active: ""
                                },
                                {
                                    label: "Cancelados",
                                    currency: "",
                                    value: this.state.data.totalDepositCancelled,
                                    active: ""
                                }
                            ]} name="Depósito"/>
                        </div>
                        <div className="col-md-4 col-sm-12 col-lg-4 col-12">
                            <StatWidget items={[{
                                label: "Efetuado BRL",
                                currency: "R$",
                                value: StringUtils.toMoneyFormat(this.state.data.totalWithdraw, 2, ',', '.'),
                                active: "active"
                            }, {
                                label: "Efetuado Bitcoin",
                                currency: "BTC",
                                value: StringUtils.toBitcoin(0),
                                active: ""
                            },
                                {
                                    label: "Confirmados",
                                    currency: "",
                                    value: this.state.data.totalWithdrawConfirmed,
                                    active: ""
                                },
                                {
                                    label: "Em Análise",
                                    currency: "",
                                    value: this.state.data.totalWithdrawWaitingApproval,
                                    active: ""
                                },
                                {
                                    label: "Processando",
                                    currency: "",
                                    value: this.state.data.totalWithdrawProcessing,
                                    active: ""
                                },
                                {
                                    label: "Negados",
                                    currency: "",
                                    value: this.state.data.totalWithdrawDenied,
                                    active: ""
                                },
                                {
                                    label: "Cancelados",
                                    currency: "",
                                    value: this.state.data.totalWithdrawCancelled,
                                    active: ""
                                }]} name="Saque"/>
                        </div>
                        <div className="col-md-4 col-sm-12 col-lg-4 col-12">
                            <StatWidget items={[{
                                label: "Todos",
                                currency: "",
                                value: this.state.data.totalPlans,
                                active: "active"
                            }, {
                                label: "Vigente",
                                currency: "",
                                value: this.state.data.totalPlansInProgress,
                                active: ""
                            }, {
                                label: "Processando",
                                currency: "",
                                value: this.state.data.totalPlansProcessing,
                                active: ""
                            }, {
                                label: "Finalizado",
                                currency: "",
                                value: this.state.data.totalPlansFinished,
                                active: ""
                            },
                                {
                                    label: "Audace",
                                    currency: "",
                                    value: this.state.data.totalPlansAudace,
                                    active: ""
                                },
                                {
                                    label: "Línea",
                                    currency: "",
                                    value: this.state.data.totalPlansLinea,
                                    active: ""
                                }
                            ]} name="Planos"/>
                        </div>
                    </div>
                </div> : "";
            return (
                <div className="animated fadeIn">
                    {totals}
                    <section>
                        <div className="row">
                            {plans}
                        </div>
                    </section>
                    {balanceEvolution}
                    {welcome}
                </div>
            );
        }
        else
            return <BackgroundPlaceholderMarker/>;
    }
}

const mapStateToProps = (state, ownProps) => {
    return {
        session: state.session
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        doSecurityFilter: () => dispatch(userActions.doSecurityFilter())
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Dashboard);