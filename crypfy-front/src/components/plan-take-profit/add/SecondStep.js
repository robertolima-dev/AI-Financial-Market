import React from 'react'
import SimpleButton from '../../common/SimpleButton'
import {Button, ModalBody, ModalFooter} from 'reactstrap'
import StringUtils from '../../../helpers/StringUtils'
import Moment from 'react-moment'
import SimpleCurrencyInput from '../../common/SimpleCurrencyInput'

class SecondStep extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            plans: (this.props.plans) ? this.props.plans : null,
            selectedPlans: null,
            render: (this.props.render) ? this.props.render : false
        };
        this.selectedPlans = [];
        this.buttonNextPage = React.createRef();
    }

    setRender(render) {
        let promise = new Promise((resolve, reject) => {
            this.setState({...this.state, render: render}, () => {
                resolve();
            });
        })
        return promise;
    }

    setPlans(plans) {
        this.setState({...this.state, plans: plans})
    }

    setInputFieldError(refId, message) {
        this.refs[refId].setError(message);
    }

    onChangeInput(id, value) {
        this.selectedPlans = [];
        const plans = this.state.plans.map((p, i) => {
            if (p.iduserPlan == id) {
                this.selectedPlans.push({...p, value: StringUtils.toDecimal(value)});
                return {...p, value: StringUtils.toDecimal(value)}
            }
            else {
                if (p.active == 'active')
                    this.selectedPlans.push(p);
                return p;
            }
        })
        this.props.onSelectNewPlan(this.selectedPlans);
        this.setState({...this.state, plans: plans, selectedPlans: this.selectedPlans});
    }

    onSelectPlan(idx) {
        this.selectedPlans = [];
        const newPlans = this.state.plans.map((p, i) => {
            if (i == idx) {
                if (p.active == 'active') {
                    this.refs['input-value-' + p.iduserPlan].setValue(null).then(() => {
                        this.refs['input-value-' + p.iduserPlan].clearError(null);
                        this.refs['input-value-' + p.iduserPlan].setDisabled(true);
                    });
                    return {...p, active: '', value: null}
                } else {
                    this.refs['input-value-' + p.iduserPlan].setValue(p.availableProfit).then(() => {
                        this.refs['input-value-' + p.iduserPlan].clearError(null);
                        this.refs['input-value-' + p.iduserPlan].setDisabled(false);
                    });
                    this.selectedPlans.push({...p, active: 'active', value: p.availableProfit});
                    return {...p, active: 'active', value: p.availableProfit}
                }
            } else {
                if (p.active == 'active')
                    this.selectedPlans.push(p);
                return p;
            }
        })
        this.buttonNextPage.setDisabled((this.selectedPlans.length > 0) ? false : true);
        this.props.onSelectNewPlan(this.selectedPlans);
        this.setState({...this.state, plans: newPlans, selectedPlans: this.selectedPlans});
    }

    onNextPage() {
        this.props.onNextPage();
    }

    onCancel() {
        this.props.onCancel();
    }

    setButtonNextDisabled(disabled) {
        this.buttonNextPage.setDisabled(disabled);
    }

    render() {
        if (!this.state.render)
            return null;
        let selectablePlans = null;
        if (this.state.plans != null && this.state.plans.length > 0) {
            selectablePlans = this.state.plans.map((p, i) => {

                const actionButton = (p.active == '') ?
                    <SimpleButton key={'add-plan-' + p.iduserPlan} ref={'add-plan-' + p.iduserPlan}
                                  onClick={this.onSelectPlan.bind(this, i)}
                                  className="btn btn-primary btn-lg btn-block">
                        Selecionar Plano
                    </SimpleButton> :
                    <SimpleButton key={'remove-plan-' + p.iduserPlan} ref={'remove-plan-' + p.iduserPlan}
                                  onClick={this.onSelectPlan.bind(this, i)} className="btn btn-danger btn-lg btn-block">
                        Desmarcar Plano
                    </SimpleButton>

                return (
                    <div key={i} className="col-md-4 margin-top-20">
                        <div className={'take-profit-plan-selection ' + p.active}>
                            <div className="grayscale-area">
                                <span className="fa fa-check selected"></span>
                                <div style={{position: "relative"}}>
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
                                </div>
                                <div className="clearfix"></div>
                                <div className="margin-top-20">
                                    <h5 className="heading">
                                        Lucro Disponível
                                    </h5>
                                    <h1 className="balance-value">
                                    <span
                                        className="currency">R$</span> {StringUtils.toMoneyFormat(p.availableProfit, 2, ',', '.')}
                                    </h1>
                                </div>
                                <div className="row margin-top-10">
                                    <div className="col-md-6">
                                        <h5 className="heading">Início Plano</h5>
                                        <span className="date">
                                        <Moment format="DD/MM/YYYY" date={p.startDate}/>
                                    </span>
                                    </div>
                                    <div className="col-md-6">
                                        <h5 className="heading">Início Plano</h5>
                                        <span className="date">
                                        <Moment format="DD/MM/YYYY" date={p.startEnd}/>
                                    </span>
                                    </div>
                                </div>
                                <div className="row margin-top-10">
                                    <div className="col-md-12">
                                        <SimpleCurrencyInput
                                            disabled={(p.active != 'active')}
                                            value={p.value}
                                            onChange={this.onChangeInput.bind(this, p.iduserPlan)}
                                            ref={'input-value-' + p.iduserPlan}
                                            type="text" className="form-control"
                                            label="Valor Solicitado"/>
                                    </div>
                                </div>
                            </div>
                            <div className="row margin-top-10">
                                <div className="col-md-12">
                                    {actionButton}
                                </div>
                            </div>
                        </div>
                    </div>
                );
            })

            return (
                <div className="animated fadeIn">
                    <ModalBody>
                        <h1 className="text-center margin-top-20">Escolha o plano e o valor solicitado</h1>
                        <div className="row margin-top-30">
                            {selectablePlans}
                        </div>
                    </ModalBody>
                    <ModalFooter>
                        <Button color="secondary"
                                onClick={this.onCancel.bind(this)}>Cancelar</Button>
                        <SimpleButton disabled={true} onClick={this.onNextPage.bind(this)} ref={(b) => {
                            this.buttonNextPage = b
                        }}
                                      className="btn btn-primary">
                            Prosseguir
                        </SimpleButton>
                    </ModalFooter>
                </div>
            );
        } else {
            return (
                <div className="animated fadeIn">
                    <ModalBody>
                        <div className="row">
                            <div className="no-results">
                            <span>
                                <img width={110} className="img-fluid margin-top-30" alt="no results"
                                     src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/no-result.svg"/>
                            </span>
                                <h3 className="margin-top-20">Ops, Você não possuí planos vigentes</h3>
                                <span className="description">
                                    Não foram encontrados planos na sua conta
                                </span>
                            </div>
                        </div>
                    </ModalBody>
                    <ModalFooter>
                        <Button color="secondary"
                                onClick={this.onCancel.bind(this)}>Cancelar</Button>
                    </ModalFooter>
                </div>
            );
        }
    }
}

export default SecondStep;