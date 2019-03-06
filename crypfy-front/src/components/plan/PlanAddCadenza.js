import React from 'react'
import {Bar} from 'react-chartjs-2';
import SimpleInput from '../common/SimpleInput'
import SimpleCurrencyInput from '../common/SimpleCurrencyInput'
import SimpleButton from '../common/SimpleButton'
import * as planActions from '../../redux/actions/planActions'
import {Radio, RadioGroup} from 'react-icheck'
import Alert from 'react-s-alert'
import sleeper from '../../helpers/Sleeper'
import StringUtils from '../../helpers/StringUtils'
import {connect} from 'react-redux'
import {push} from 'react-router-redux'
import appConstants from '../../constants/AppConstants'
import {Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap'
import BackgroundPlaceholderMarker from '../base/BackgroundPlaceholderMarker'

class PlanAddCadenza extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            render: false,
            dataChart: null,
            currentPerfomanceMonth: 0,
            lastMonthPerfomance: 0,
            lastThreeMonthPefomance: 0,
            planName: 'Meu Plano Cadenza',
            duration: '6',
            amount: 1000,
            idplan: 3,
            confirmationModal: false,
            userPlan: null,
            inputFee: 1,
            netAmount: 0,
            inputFeeValue: 0
        }
    }

    componentDidMount() {
        planActions.getPerfomance(this.state.idplan).then((ret) => {
            const labels = [];
            const data = [];
            ret.response.monthlyPerfomance.forEach((m) => {
                labels.push(m.monthName);
                data.push(m.perfomance);
            })

            const dataChart = {
                labels: labels,
                datasets: [
                    {
                        label: 'Perfomance (%)',
                        backgroundColor: '#fff',
                        borderColor: '#3B6AE6',
                        borderWidth: 2,
                        hoverBackgroundColor: '#3B6AE6',
                        hoverBorderColor: '#3B6AE6',
                        data: data
                    }
                ],
                yAxes: [{
                    gridLines: {
                        zeroLineColor: '#ffcc33'
                    }
                }]
            };

            const amount = (ret.response.userAvailableBalance > 1000) ? ret.response.userAvailableBalance : 1000;
            const netAmount = amount * (100 - this.state.inputFee) / 100;
            const inputFeeValue = amount - netAmount;

            this.setState({
                ...this.state,
                dataChart: dataChart,
                currentMonthPerfomance: ret.response.currentMonthPerfomance,
                lastMonthPerfomance: ret.response.lastMonthPerfomance,
                lastThreeMonthPefomance: ret.response.lastThreeMonthPerfomance,
                render: true,
                amount: amount,
                netAmount: StringUtils.toMoneyFormat(netAmount, 2, ',', '.'),
                inputFeeValue: StringUtils.toMoneyFormat(inputFeeValue, 2, ',', '.')
            })

        })
    }

    toggleConfirmationModal() {
        if (!this.state.confirmationModal)
            this.setState({...this.state, confirmationModal: true});
        else {
            if (this.state.userPlan != null) {
                this.props.toPlans();
                return;
            }
            this.setState({...this.state, confirmationModal: false});
        }
    }

    clearForm() {
        this.refs.planNameInput.clearError();
        this.refs.amountInput.clearError();
    }

    submitPlan() {
        this.clearForm();
        this.refs.submitPlanButton.setLoadingOn('processando..');
        sleeper(1000).then(() => {
            this.props.planAdd(this.state.idplan, this.state.planName, this.state.amount, this.state.duration).then((ret) => {
                this.refs.submitPlanButton.setLoadingOff();
                this.setState({...this.state, userPlan: ret.response, confirmationModal: true})
            }).catch((ret) => {
                Alert.error(ret.message);
                this.refs.submitPlanButton.setLoadingOff();
                let fieldError = false;
                ret.response.forEach((e) => {
                    if (e.code == 1001 || e.code == 1008) {
                        this.refs.planNameInput.setError(e.message);
                        fieldError = true;
                    }
                    if (e.code == 1004 || e.code == 1005 || e.code == 1007) {
                        this.refs.amountInput.setError(e.message);
                        fieldError = true;
                    }
                    if (!fieldError)
                        Alert.error(e.message);
                })
            })
        })
    }

    onChange(value) {
        const amount = StringUtils.toDecimal(value);
        const netAmount = amount * (100 - this.state.inputFee) / 100;
        const inputFeeValue = amount - netAmount;

        this.refs.inputInputFee.setValue(StringUtils.toMoneyFormat(inputFeeValue, 2, ',', '.'));
        this.refs.inputNetValue.setValue(StringUtils.toMoneyFormat(netAmount, 2, ',', '.'));

        this.setState({
            ...this.state,
            amount: amount
        })
    }

    render() {
        if (this.state.render)
            return (
                <div>
                    <div style={{zIndex: "9999999"}} className="row margin-top-50 animated fadeIn">
                        <div className="col-md-7 col-sm-12 col-12">
                            <h1>
                                Plano Cadenza
                                <span className="float-right font-15 badge badge-cadenza">
                                    Arrojado
                                </span>
                            </h1>
                            <p className="description margin-top-20">
                                Destinado à investidores de perfil Arrojado, o Plano Cadenza é indicado para quem procura obter o máximo de rendimentos a longo prazo, participando dos extremos do mercado, sem intenção de efetuar resgates frequentes dos seus lucros, participando de forma agressiva das movimentações do mercado.
                            </p>
                            <div className="row margin-top-20">
                                <div className="col-md-12">
                                    <div className="show-on-mobile-block">
                                        <Bar
                                            data={this.state.dataChart}
                                            height={250}
                                            options={{
                                                maintainAspectRatio: true,
                                                scales: {
                                                    xAxes: [{
                                                        stacked: false,
                                                        gridLines: {
                                                            display: false
                                                        }
                                                    }],
                                                    yAxes: [{
                                                        stacked: false,
                                                        gridLines: {
                                                            display: false
                                                        }
                                                    }]
                                                }
                                            }}
                                        />
                                    </div>
                                </div>
                            </div>
                            <div className="row margin-top-30">
                                <div className="col-md-12">
                                    <h3>Dados Plano</h3>
                                </div>
                            </div>
                            <div className="row margin-top-10">
                                <div className="col-md-6">
                                    <h5 className="font-15">Valor Mínimo Investimento: <strong>R$ 1.000,00</strong>
                                    </h5>
                                </div>
                                <div className="col-md-6">
                                    <h5 className="font-15">Taxa Entrada: <strong>1%</strong></h5>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-md-6">
                                    <h5 className="font-15">Taxa Perfomance 6 Meses: <strong>35%</strong></h5>
                                </div>
                                <div className="col-md-6">
                                    <h5 className="font-15">Taxa Perfomance 12 Meses: <strong>30%</strong></h5>
                                </div>
                            </div>
                            <div className="row margin-top-30">
                                <div className="col-md-12">
                                    <h3>Dados Perfomance</h3>
                                </div>
                            </div>
                            <div className="row margin-top-20">
                                <div className="col-md-6">
                                    <h5 className="font-15">Retorno mês atual: <strong>{this.state.currentMonthPerfomance}%</strong></h5>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-md-6">
                                    <h5 className="font-15">Retorno últimos 3 Meses: <strong>{this.state.lastThreeMonthPefomance}%</strong></h5>
                                </div>
                                <div className="col-md-6">
                                    <h5 className="font-15">Retorno último mês: <strong>{this.state.lastMonthPerfomance}%</strong></h5>
                                </div>
                            </div>
                            <div className="row margin-top-30">
                                <div className="col-md-12">
                                    <SimpleInput onChange={(e) => {
                                        this.setState({...this.state, planName: e.target.value})
                                    }} ref="planNameInput" value={this.state.planName} className="form-control"
                                                 label="Nome de seu plano"/>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-md-5">
                                    <SimpleCurrencyInput ref="amountInput" value={this.state.amount}
                                                         onChange={this.onChange.bind(this)}
                                                         className="form-control"
                                                         label="Valor Desejado Investimento"/>
                                </div>
                                <div className="col-md-3">
                                    <SimpleInput value={this.state.inputFeeValue} disabled="true"
                                                 ref="inputInputFee"
                                                 className="form-control"
                                                 label={"Taxa Entrada (" + this.state.inputFee + "%)"}/>
                                </div>
                                <div className="col-md-4">
                                    <SimpleInput value={this.state.netAmount} disabled="true" ref="inputNetValue"
                                                 className="form-control" label="Valor Final Investimento"/>
                                </div>
                            </div>
                            <div className="row">
                                <div style={{marginTop: "10px"}} className="col-md-12">
                                    <label className="label block">Duração</label>

                                    <RadioGroup name="radio" value={this.state.duration}
                                                onChange={(e, value) => {
                                                    this.setState({...this.state, duration: value})
                                                }}>
                                        <Radio
                                            value="6"
                                            radioClass="iradio_square-blue"
                                            increaseArea="20%"
                                            label="<span class='margin-left-10 margin-right-10'>6 Meses</span>"
                                        />
                                        <Radio
                                            value="12"
                                            radioClass="iradio_square-blue"
                                            increaseArea="20%"
                                            label="<span class='margin-left-10'>12 Meses</span>"
                                        />
                                    </RadioGroup>
                                </div>
                            </div>
                        </div>
                        <div className="col-md-5">
                            <div className="margin-top-30 hide-on-mobile-block">
                                <Bar
                                    data={this.state.dataChart}
                                    height={250}
                                    options={{
                                        maintainAspectRatio: true,
                                        scales: {
                                            xAxes: [{
                                                stacked: false,
                                                gridLines: {
                                                    display: false
                                                }
                                            }],
                                            yAxes: [{
                                                stacked: false,
                                                gridLines: {
                                                    display: false
                                                }
                                            }]
                                        }
                                    }}
                                />
                            </div>
                        </div>
                    </div>
                    <div className="row margin-top-30">
                        <div className="col-md-12">
                            <SimpleButton onClick={this.submitPlan.bind(this)} ref="submitPlanButton"
                                          className="btn btn-primary float-right">
                                Contratar Plano
                            </SimpleButton>
                        </div>
                    </div>
                    <Modal isOpen={this.state.confirmationModal}
                           toggle={this.toggleConfirmationModal.bind(this)}>
                        <ModalBody>
                            <div className="text-center">
                                <span>
                                    <img width={200}
                                         src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/plan-processing.svg"
                                         alt="plan processing"/>
                                </span>
                                <h5 className="margin-top-20">Parabéns, seu plano está sendo processado!</h5>
                                <p className="margin-top-20">Seu plano <strong>Cadenza {this.state.duration}</strong> foi
                                    contratado com sucesso e está sendo processado, dentro de algumas horas estará
                                    vigente!</p>
                            </div>
                        </ModalBody>
                        <ModalFooter>
                            <SimpleButton onClick={this.props.toPlans.bind(this)} ref="buttonCancel"
                                          className="btn btn-primary">
                                OK, Entendi
                            </SimpleButton>

                        </ModalFooter>
                    </Modal>
                </div>
            );
        else return <BackgroundPlaceholderMarker/>;
    }
}

const mapStateToProps = (state, ownProps) => {
    return {}
};

const mapDispatchToProps = (dispatch) => {
    return {
        toPlans: () => dispatch(push(appConstants.routes.plans)),
        planAdd: (idplan, planName, amount, duration) => dispatch(planActions.add(idplan, planName, amount, duration))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PlanAddCadenza);