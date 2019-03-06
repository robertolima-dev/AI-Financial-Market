import React from 'react'
import BaseTemplateAdmin from '../base/BaseTemplateAdmin'
import SimpleSelect from '../common/SimpleSelect'
import SimpleButton from '../common/SimpleButton'
import SimpleCurrencyInput from '../common/SimpleCurrencyInput'
import BankAccountSelectOption from '../common/BankAccountSelectOption'
import BankAccountSelectValue from '../common/BankAccountSelectValue'
import * as bankAccountActions from '../../redux/actions/bankAccountActions'
import * as depositBrlActions from '../../redux/actions/depositBrlActions'
import Alert from 'react-s-alert';
import StringUtils from '../../helpers/StringUtils'
import sleeper from '../../helpers/Sleeper'
import {push} from 'react-router-redux'
import {connect} from 'react-redux';
import appConstants from '../../constants/AppConstants'
import {Link} from 'react-router-dom'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap'
import BackgroundPlaceholderMarker from '../base/BackgroundPlaceholderMarker'

class AddDeposit extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            bankAccountOption: "",
            bankAccountOptions: null,
            amount: 0,
            confirmationModal: false,
            depositBrl: null,
            render: false
        }


    }

    componentDidMount() {
        //list bank accounts
        sleeper(300).then(() => {
            bankAccountActions.listCrypfyBankAccounts().then((bankAccounts) => {
                const options = bankAccounts.map((bankAccount) => {
                    return {
                        value: bankAccount.idbankAccount,
                        label: bankAccount.bank.name,
                        bankImg: bankAccount.bank.logo
                    }
                })
                this.setState({...this.state, bankAccountOptions: options, render: true});
            }).catch((ret) => {
                Alert.error(ret.message);
            })
        })
    }

    toggleDepositConfirmationModal() {

        if (this.state.depositBrl != null) {
            this.props.toDeposits();
            return;
        }

        if (this.state.confirmationModal)
            this.setState({...this.state, confirmationModal: false});
        else
            this.setState({...this.state, confirmationModal: true});
    }

    onSubmit(e) {
        e.preventDefault();
        this.refs.submitButton.setLoadingOn("processando..");
        this.clearForm();
        sleeper(1000).then(() => {
            depositBrlActions.addDepositBrl(this.state.amount, this.state.bankAccountOption).then((ret) => {
                this.refs.submitButton.setLoadingOff();
                Alert.success(ret.message);
                this.toggleDepositConfirmationModal();
                this.setState({...this.state, confirmationModal: true}, () => {
                    this.setState({...this.state, depositBrl: ret.response});
                })
            }).catch((ret) => {
                if (ret.status == 400) {
                    this.refs.submitButton.setLoadingOff();
                    Alert.error(ret.message);
                    ret.response.forEach((e) => {
                        if (e.code == 1000 || e.code == 1011)
                            this.refs.inputAmount.setError(e.message);
                        if (e.code == 1001)
                            this.refs.inputBankAccount.setError(e.message);
                    })
                } else {
                    Alert.error(ret.message);
                    this.refs.submitButton.setLoadingOff();
                }
            })
        })
    }

    clearForm() {
        this.refs.inputAmount.clearError();
        this.refs.inputBankAccount.clearError();
    }

    render() {
        const value = this.state.bankAccountOption && this.state.bankAccountOption.value;

        if (this.state.render)
            return (
                <div className="animated fadeIn">
                    <nav aria-label="breadcrumb">
                        <ol className="breadcrumb">
                            <li className="breadcrumb-item">
                                <Link to={appConstants.routes.dashboard}>Visão Geral</Link>
                            </li>
                            <li className="breadcrumb-item">
                                <Link to={appConstants.routes.depositsBrl}>Depósitos BRL</Link>
                            </li>
                            <li className="breadcrumb-item active" aria-current="page">Adicionar Depósito BRL</li>
                        </ol>
                    </nav>
                    <div className="centered-box">
                        <div className="text-center">
                        <span>
                            <img alt="bitcoin box" width={200}
                                 src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/deposit-bitcoin.svg"/>
                        </span>
                        </div>
                        <h1 className="text-center">Adicionar Intenção de
                            Depósito Real
                        </h1>
                        <h2 className="text-center description margin-top-20">
                            No processo de intenção de depósito você deve escolher uma de nossas contas bancárias e
                            o
                            valor que deseja depositar. Após o envio do comprovante e validação de depósito o saldo
                            estará disponível.
                        </h2>
                        <div className="instructions margin-top-30">
                            <ul>
                                <li>
                                <span>
                                    <i className="fas fa-check"></i>
                                    Os depósitos deverão ser feitos através de <strong>TED</strong>, <strong>DOC</strong> ou depósito na <strong>boca do caixa</strong>.
                                </span>

                                </li>
                                <li>
                                <span>
                                    <i className="fas fa-check"></i>
                                    Somente será aceito <strong>depósito originado</strong> de uma conta bancária do mesmo <strong>titular (CPF)</strong> cadastrado e validado na plataforma.
                                </span>
                                </li>
                                <li>
                                <span>
                                    <i className="fas fa-check"></i>
                                    Para tornar o <strong>processo de validação mais rápido</strong>, sugerimos que faça depósito com valores não inteiros (Ex.: 10.000,83)
                                </span>
                                </li>
                                <li>
                                <span>
                                    <i className="fas fa-check"></i>
                                    O valor <strong>mínimo</strong> de depósito é de R$ 1.000,00 (Mil Reais)
                                </span>
                                </li>
                                <li>
                                <span>
                                    <i className="fas fa-check"></i>
                                    Se o depósito estiver <strong>fora das especificações acima</strong>, o mesmo será devolvido sendo debitada a <strong>taxa</strong> padrão de depósito ({appConstants.labels.depositBrlFee}%)
                                </span>
                                </li>
                                <li>
                                <span>
                                    <i className="fas fa-check"></i>
                                    Após feito o depósito respeitando todas as especificações acima, deve-se enviar através de <strong>upload</strong> o comprovante de depósito com o <strong>CPF escrito (a mão ou digitalmente)</strong> para validação do mesmo
                                </span>
                                </li>
                                <li>
                                <span>
                                    <i className="fas fa-check"></i>
                                    Após o envio de comprovante o depósito será validado em até <strong>{appConstants.labels.maxDaysDepositVoucherAnalysis}
                                    dias úteis</strong>.
                                </span>
                                </li>
                            </ul>
                        </div>
                        <div className="clearfix"></div>
                        <div className="container-fluid">
                            <div className="row">
                                <div className="col-md-12 widget margin-top-40">
                                    <div className="body">
                                        <form onSubmit={this.onSubmit.bind(this)}>
                                            <div className="row">
                                                <div className="col-md-12">
                                                    <SimpleSelect required="true" ref="inputBankAccount"
                                                                  optionComponent={BankAccountSelectOption}
                                                                  valueComponent={BankAccountSelectValue}
                                                                  placeholder="Escolha a conta para depósito"
                                                                  label="Conta Bancária"
                                                                  value={this.state.bankAccountOption}
                                                                  onChange={(option) => {
                                                                      this.setState({
                                                                          ...this.state,
                                                                          bankAccountOption: option.value
                                                                      })
                                                                  }}
                                                                  options={this.state.bankAccountOptions}
                                                    />
                                                </div>
                                            </div>
                                            <div className="row">
                                                <div className="col-md-12">
                                                    <SimpleCurrencyInput required="true" selectAllOnFocus={true}
                                                                         allowEmpty={true}
                                                                         ref="inputAmount" onChange={(value) => {
                                                        this.setState({
                                                            ...this.state,
                                                            amount: StringUtils.toDecimal(value)
                                                        })
                                                    }} className="form-control" label="Valor"
                                                                         placeholder="Digite o Montante a ser depositado"/>
                                                </div>
                                            </div>
                                            <div className="form-separator"></div>
                                            <div className="row margin-top-20">
                                                <div className="col-md-12">
                                                    <SimpleButton ref="submitButton"
                                                                  className="btn btn-primary btn-lg btn-block">
                                                        Salvar
                                                    </SimpleButton>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <Modal isOpen={this.state.confirmationModal}
                           toggle={this.toggleDepositConfirmationModal.bind(this)}>
                        <ModalBody>
                            <div className="text-center">
                                <h1 className="text-center margin-top-40 font-20">
                                    1 - Faça o depósito para esta conta
                                </h1>
                                <div className="margin-top-30 dashed-box">
                                    <span>
                                        <img width="50"
                                             src={(this.state.depositBrl != null) ? this.state.depositBrl.bankLogo : ''}
                                             alt={(this.state.depositBrl != null) ? this.state.depositBrl.bankName : ''}
                                             className="img-thumbnail rounded-circle"/>
                                    </span>
                                    <span className="block">
                                        <strong>{(this.state.depositBrl != null) ? this.state.depositBrl.bankName : ''}</strong>
                                    </span>
                                    <span className="block description">
                                        <strong>{(this.state.depositBrl != null) ? StringUtils.getHumanNameBankAccountType(this.state.depositBrl.bankAccountType) : ''}</strong> AG <strong>{(this.state.depositBrl != null) ? this.state.depositBrl.bankAgency : ''}</strong> CC <strong>{(this.state.depositBrl != null) ? this.state.depositBrl.bankAccountNumber : ''}-{(this.state.depositBrl != null) ? this.state.depositBrl.bankAccountNumberDigit : ''}</strong>
                                    </span>
                                    <span className="block description">
                                        {(this.state.depositBrl != null) ? this.state.depositBrl.bankAccountDocumentType : null}
                                        <strong>{(this.state.depositBrl != null && this.state.depositBrl.bankAccountDocumentType == 'CPF') ? StringUtils.toCpf(this.state.depositBrl.bankAccountDocumentNumber) : ""} {(this.state.depositBrl != null && this.state.depositBrl.bankAccountDocumentType == 'CNPJ') ? StringUtils.toCnpj(this.state.depositBrl.bankAccountDocumentNumber) : ""}</strong>
                                    </span>
                                    <span className="block description">
                                        <strong>Mali Técnologia LTDA</strong>
                                    </span>
                                </div>
                                <h1 className="text-center margin-top-40 font-20">
                                    2 - Feito o depósito, envie o comprovante com seu CPF escrito conforme imagem
                                    abaixo
                                </h1>
                                <span>
                                    <img className="margin-top-30" width={230} alt="deposit brl voucher"
                                         src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/deposit-brl-voucher.svg"/>
                                </span>
                                <h2 className="text-center description margin-top-30">
                                    <strong className="red">Atenção</strong> não esqueça de escrever seu
                                    <strong>CPF</strong> no comprovante.
                                </h2>
                            </div>
                        </ModalBody>
                        <ModalFooter>
                            <SimpleButton onClick={this.props.toDeposits.bind(this)} ref="buttonCancel"
                                          className="btn btn-primary">
                                OK, Entendi
                            </SimpleButton>

                        </ModalFooter>
                    </Modal>
                </div>
            ); else return <BackgroundPlaceholderMarker/>
    }
}

const mapStateToProps = (state, ownProps) => {
    return {}
};

const mapDispatchToProps = (dispatch) => {
    return {
        toDeposits: () => dispatch(push(appConstants.routes.depositsBrl)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(AddDeposit);