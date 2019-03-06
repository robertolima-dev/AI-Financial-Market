import React from 'react'
import SimpleButton from '../../common/SimpleButton'
import {Button, ModalBody, ModalFooter} from 'reactstrap'
import StringUtils from '../../../helpers/StringUtils'

class ThirdStep extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            render: (this.props.render) ? this.props.render : false,
            selectedPlans: [],
            total : 0
        }
    }

    setRender(render) {
        this.setState({...this.state, render});
    }

    setSelectedPlans(selectedPlans) {
        let total = 0;
        selectedPlans.forEach((p) => {
            total += p.value;
        })
        this.setState({...this.state, selectedPlans: selectedPlans,total:total});
    }

    onBack() {
        this.props.onBack();
    }

    onSubmit() {
        this.refs.buttonSubmit.setLoadingOn("Processando...");
        this.props.onSubmit();
    }

    setSubmitButtonLoadingOn(label) {
        this.refs.buttonSubmit.setLoadingOn(label);
    }

    setSubmitButtonLoadingOff() {
        this.refs.buttonSubmit.setLoadingOff();
    }

    render() {

        const plans = this.state.selectedPlans.map((p, i) => {
            return (
                <li key={i}>
                    <div className="row">
                        <div className="col-md-6 col-6">
                            <span className="float-left">{p.planName}<strong> {p.plan.name}({p.duration})</strong></span>
                        </div>
                        <div className="col-md-6 col-6">
                            <span className="float-right">
                                <span className="currency">R$</span> {StringUtils.toMoneyFormat(p.value, 2, ',', '.')}
                            </span>
                        </div>
                    </div>
                </li>
            )
        })

        if (this.state.render)
            return (
                <div className="animated fadeIn">
                    <ModalBody>
                        <h1 className="text-center margin-top-20">Confirme os planos({this.state.selectedPlans.length}) e valores</h1>
                        <div className="margin-top-30">
                            <ul className="overview-take-profit">
                                {plans}
                                <li>
                                    <span className="float-left">
                                         <strong className="font-13">TOTAL</strong>
                                    </span>
                                    <span className="float-right">
                                        <span className="currency">R$</span> <strong>{StringUtils.toMoneyFormat(this.state.total,2,',','.')}</strong></span>
                                    <div className="clearfix"></div>
                                </li>
                            </ul>
                        </div>
                    </ModalBody>
                    <ModalFooter>
                        <Button color="secondary" onClick={this.onBack.bind(this)}>Voltar</Button>
                        <SimpleButton ref="buttonSubmit" onClick={this.onSubmit.bind(this)}
                                      className="btn btn-primary">
                            Confirmar e Salvar Movimentações de Lucro
                        </SimpleButton>
                    </ModalFooter>
                </div>
            );
        else return null;
    }
}

export default ThirdStep;