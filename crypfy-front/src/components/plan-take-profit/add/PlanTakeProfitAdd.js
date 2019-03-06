import React from 'react'
import SimpleButton from '../../common/SimpleButton'
import {Modal, ModalBody, ModalFooter, Button} from 'reactstrap'
import * as planActions from '../../../redux/actions/planActions'
import * as planTakeProfitActions from '../../../redux/actions/planTakeProfitRequestActions'
import Alert from 'react-s-alert'
import sleeper from '../../../helpers/Sleeper'
import FirstStep from './FirstStep'
import SecondStep from './SecondStep'
import ThirdStep from './ThirdStep'

class PlanTakeProfitAdd extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            modal: false,
            currentStep: 1,
            plans: null,
            selectedPlans: null
        }
        this.selectedPlans = [];
    }

    loadPlans() {
        planActions.list('IN_PROGRESS').then((ret) => {
            const plans = ret.response.map((p, i) => {
                return {...p, active: '', value: null};
            })
            this.setState({...this.state, plans: plans});
        })
    }

    componentDidMount() {
        this.loadPlans();
    }

    toggle() {
        if (this.state.modal)
            this.setState({...this.state, modal: false});
        else {
            this.loadPlans();
            this.setState({...this.state, modal: true, currentStep: 1});
        }
    }

    onSelectNewPlan(selectedPlans) {
        this.setState({...this.state,selectedPlans:selectedPlans})
    }

    onSubmit() {
        const selectedPlans = this.state.selectedPlans.map((p,i) => {
           return {iduserPlan:p.iduserPlan,amount:p.value};
        });
        sleeper(1200).then(() => {
            planTakeProfitActions.add(selectedPlans).then((ret) => {
                this.refs.thirdStepComponent.setSubmitButtonLoadingOff();
                this.toggle();
                Alert.success(ret.message);
                this.props.onSucess();
            }).catch((ret) => {
                console.log(ret);
                this.refs.thirdStepComponent.setSubmitButtonLoadingOff();
                this.goBackToSecondStep();
                Alert.error(ret.message);
                for(let key in ret.response) {
                    ret.response[key].forEach((e) => {
                        if (e.code == 1000 || e.code == 1003 || e.code == 1006 || e.code == 1007) {
                            this.refs.secondStepComponent.setInputFieldError('input-value-' + key,e.message);
                        }
                    })
                }
            })
        })
    }

    goToSecondStep() {
        planActions.list('IN_PROGRESS').then((ret) => {
            const plans = ret.response.map((p, i) => {
                return {...p, active: '', value: null};
            })
            this.refs.secondStepComponent.setPlans(plans);
            this.refs.secondStepComponent.setRender(true);
            this.refs.firstStepComponent.setRender(false);
        })
    }

    goToThirdStep() {
        this.refs.secondStepComponent.setRender(false).then(() => {
            this.refs.thirdStepComponent.setRender(true);
            this.refs.thirdStepComponent.setSelectedPlans(this.state.selectedPlans);
        });
    }

    goBackToSecondStep() {
        this.refs.thirdStepComponent.setRender(false);
        this.refs.secondStepComponent.setRender(true).then(() => {
            this.refs.secondStepComponent.setButtonNextDisabled(false);

        });
    }

    render() {
        return (
            <Modal className="modal-full" isOpen={this.state.modal}
                   toggle={this.toggle.bind(this)}>
                <FirstStep render={true} ref="firstStepComponent" onCancel={() => {this.setState({...this.state,modal:false})}} onNextPage={this.goToSecondStep.bind(this)}/>
                <SecondStep render={false} ref="secondStepComponent" onSelectNewPlan={this.onSelectNewPlan.bind(this)} onCancel={() => {this.setState({...this.state,modal:false})}} onNextPage={this.goToThirdStep.bind(this)}/>
                <ThirdStep render={false} ref="thirdStepComponent" onBack={this.goBackToSecondStep.bind(this)} onSubmit={this.onSubmit.bind(this)}/>
            </Modal>
        );
    }
}

export default PlanTakeProfitAdd;