import React from 'react'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap'
import SimpleSelect from '../common/SimpleSelect'
import SimpleInput from '../common/SimpleInput'
import SimpleButton from '../common/SimpleButton'
import BankAccountSelectOption from '../common/BankAccountSelectOption'
import BankAccountSelectValue from '../common/BankAccountSelectValue'
import * as bankAccountsAction from '../../redux/actions/bankAccountActions'
import {Radio, RadioGroup} from 'react-icheck'
import sleeper from '../../helpers/Sleeper'
import Alert from 'react-s-alert'
import {connect} from 'react-redux'
import StringUtils from '../../helpers/StringUtils'

class BankAccountAddEdit extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            modal: false,
            isNewBank: false,
            bank: {
                idbank:null,
                type:null,
                agency:"",
                accountNumber:"",
                accountNumberDigit: ""
            },
            banks: null,
        }
    }

    toggle() {
        if (this.state.modal)
            this.setState({...this.state, modal: false});
        else
            this.setState({...this.state, modal: true});
    }

    toggleNewBankAccount() {
        bankAccountsAction.listBanks().then((ret) => {
            const options = ret.response.map((b, i) => {
                return {value: b.idbank, label: b.name, bankImg: b.logo};
            })
            const newBank = {type:"CONTA_CORRENTE",idbank:"",agency:"",accountNumber:"",accountNumberDigit:""}
            this.setState({...this.state, modal: true, banks: options,bank:newBank,isNewBank:true});
        })
    }

    toggleEditBankAccount(id) {
        bankAccountsAction.listBanks().then((ret) => {
            const options = ret.response.map((b, i) => {
                return {value: b.idbank, label: b.name, bankImg: b.logo};
            })
            bankAccountsAction.find(id).then((ret) => {
                this.setState({...this.state, modal: true, banks: options,bank:ret.response,isNewBank:false});
            })
        })
    }

    clearForm() {
        this.refs.bankComponent.clearError();
        this.refs.accountNumberComponent.clearError();
        this.refs.agencyComponent.clearError();
        this.refs.accountNumberDigitComponent.clearError();
    }

    submit() {
        const bankMethodAction = (this.state.isNewBank) ? bankAccountsAction.add(this.state.bank) : bankAccountsAction.update(this.state.bank);
        this.clearForm();
        this.refs.buttonSubmit.setLoadingOn("Processando...");
        sleeper(1200).then(() => {
            bankMethodAction.then((ret) => {
                Alert.success(ret.message);
                this.toggle();
                this.props.onSuccess(ret.response);
            }).catch((ret) => {
                Alert.error(ret.message);
                this.refs.buttonSubmit.setLoadingOff();
                ret.response.map((e,i) => {
                    if(e.code == 1001)
                        this.refs.bankComponent.setError(e.message);
                    if(e.code == 1002)
                        this.refs.accountNumberComponent.setError(e.message);
                    if(e.code == 1003)
                        this.refs.agencyComponent.setError(e.message);
                    if(e.code == 1005)
                        this.refs.accountNumberDigitComponent.setError(e.message);
                })
            })
        })

    }

    render() {

        const header = (this.state.isNewBank) ? "Adicionar Conta Bancária" : "Editar Conta Bancária";
        const buttonName = (this.state.isNewBank) ? "Salvar" : "Editar";

        return (
            <div>
                <Modal isOpen={this.state.modal} toggle={this.toggle.bind(this)}>
                    <ModalHeader>
                        {header}
                    </ModalHeader>
                    <ModalBody>
                        <div className="row">
                            <div className="col-md-12">
                                <div className="margin-top-10 dashed-box">
                                    <div className="row">
                                        <div className="col-md-12">
                                            <span className="label">Nome:</span>
                                            <strong className="font-14">{this.props.session.user.user.user.name}</strong>
                                        </div>
                                        <div className="col-md-12">
                                            <span className="label">{this.props.session.user.user.user.documentType}:</span>
                                            <strong className="font-14">{(this.props.session.user.user.user.documentType == 'CPF') ? StringUtils.toCpf(this.props.session.user.user.user.documentNumber) : this.props.session.user.user.user.documentNumber}</strong>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row margin-top-20">
                            <div className="col-md-12">
                                <div className="form-group">
                                    <label>Tipo</label>
                                    <RadioGroup name="radio" value={(this.state.bank) ? this.state.bank.type : null} onChange={(e, value) => {
                                        this.setState({...this.state, bank: {...this.state.bank,type:value}})
                                    }}>
                                        <Radio
                                            value="CONTA_CORRENTE"
                                            radioClass="iradio_square-blue"
                                            increaseArea="20%"
                                            label="<span class='margin-left-10 margin-right-10'>Conta Corrente</span>"
                                        />
                                        <Radio
                                            value="CONTA_POUPANCA"
                                            radioClass="iradio_square-blue"
                                            increaseArea="20%"
                                            label="<span class='margin-left-10'>Conta Poupança</span>"
                                        />
                                    </RadioGroup>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-md-12">
                                <SimpleSelect required="true" additionalLink="Não encontrou seu banco ? Clique aqui" onClickAdditionalLink={() => {console.log("aaassa")}} ref="bankComponent"
                                              optionComponent={BankAccountSelectOption}
                                              valueComponent={BankAccountSelectValue}
                                              placeholder="Escolha o Banco.."
                                              label="Banco"
                                              value={this.state.bank.idbank}
                                              onChange={(option) => {
                                                  this.setState({
                                                      ...this.state,
                                                      bank: {...this.state.bank,idbank: (option != null) ? option.value : null}
                                                  })
                                              }}
                                              options={this.state.banks}/>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-md-4 col-12">
                                <SimpleInput required="true" placeholder="Digite sua agência" ref="agencyComponent" maxLength="9" onlyNumbers={true} value={this.state.bank.agency} onChange={(e) => {this.setState({...this.state,bank:{...this.state.bank,agency:e.target.value}})}} type="text" className="form-control" label="Agência"/>
                            </div>
                            <div className="col-md-5 col-8">
                                <SimpleInput required="true" placeholder="Digite sua conta sem dígito" ref="accountNumberComponent" maxLength="12" onlyNumbers={true} value={this.state.bank.accountNumber} onChange={(e) => {this.setState({...this.state,bank:{...this.state.bank,accountNumber:e.target.value}})}} type="text" className="form-control" label="Conta"/>
                            </div>
                            <div className="col-md-3 col-4">
                                <SimpleInput required="true" placeholder="Digite o dígito de sua conta" ref="accountNumberDigitComponent" maxLength="1" onlyNumbers={true} value={this.state.bank.accountNumberDigit} onChange={(e) => {this.setState({...this.state,bank:{...this.state.bank,accountNumberDigit:e.target.value}})}} type="text" className="form-control" label="Dígito"/>
                            </div>
                        </div>
                    </ModalBody>
                    <ModalFooter>
                        <SimpleButton onClick={this.submit.bind(this)} ref="buttonSubmit"
                                      className="btn btn-primary">
                            {buttonName}
                        </SimpleButton>
                        <Button color="secondary"
                                onClick={this.toggle.bind(this)}>Cancelar</Button>
                    </ModalFooter>
                </Modal>
            </div>
        );
    }
}
const mapStateToProps = (state, ownProps) => {
    return {
        session: state.session
    }
};

const mapDispatchToProps = (dispatch) => {
    return {}
};

export default connect(mapStateToProps, mapDispatchToProps, null, {withRef: true})(BankAccountAddEdit);