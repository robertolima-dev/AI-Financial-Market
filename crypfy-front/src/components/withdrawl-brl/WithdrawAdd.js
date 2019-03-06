import React from 'react'
import BaseTemplateAdmin from '../base/BaseTemplateAdmin'
import SimpleSelect from '../common/SimpleSelect'
import SimpleButton from '../common/SimpleButton'
import SimpleCurrencyInput from '../common/SimpleCurrencyInput'
import BankAccountSelectOption from '../common/BankAccountSelectOption'
import BankAccountSelectValue from '../common/BankAccountSelectValue'
import StringUtils from '../../helpers/StringUtils'
import appConstants from '../../constants/AppConstants'
import {Link} from 'react-router-dom'
import BankAccountAddEdit from '../bank-account/BankAccountAddEdit'
import * as bankAccountActions from '../../redux/actions/bankAccountActions'
import * as withdrawBrlActions from '../../redux/actions/withdrawBrlActions'
import * as totalBalanceActions from '../../redux/actions/totalBalanceActions'
import sleeper from '../../helpers/Sleeper'
import Alert from 'react-s-alert'
import {connect} from 'react-redux'
import {push} from 'react-router-redux'
import SimpleInput from "../common/SimpleInput";
import BackgroundPlaceholderMarker from '../base/BackgroundPlaceholderMarker'

class WithdrawAdd extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            render: false,
            bankAccountOptions: null,
            bankAccountOption: null,
            amount: null,
            fee: 17,
            netAmount: 0
        }
    }

    componentDidMount() {
        this.loadBankAccounts();
    }

    loadBankAccounts() {
        sleeper(300).then(() => {
            bankAccountActions.list().then((ret) => {
                const options = ret.response.map((bankAccount) => {
                    return {
                        value: bankAccount.idbankAccount,
                        label: bankAccount.bank.name,
                        bankImg: bankAccount.bank.logo
                    }
                })
                this.setState({...this.state, render: true, bankAccountOptions: options})
            })
        })
    }

    clearForm() {
        this.refs.inputAmount.clearError();
        this.refs.inputBankAccount.clearError();
    }

    onSubmit(e) {
        e.preventDefault();
        this.clearForm();
        this.refs.submitButton.setLoadingOn("Processando...");
        sleeper(1200).then(() => {
            withdrawBrlActions.add(this.state.bankAccountOption, this.state.amount).then((ret) => {
                this.refs.submitButton.setLoadingOff();
                Alert.success(ret.message);
                this.props.updateTotalBalance();
                this.props.toWithdraws();
            }).catch((ret) => {
                this.refs.submitButton.setLoadingOff();
                Alert.error(ret.message);
                ret.response.forEach((e, i) => {
                    if (e.code == 1000 || e.code == 1013 || e.code == 1014 || e.code == 1009 || e.code == 1016)
                        this.refs.inputAmount.setError(e.message);
                    if (e.code == 1001)
                        this.refs.inputBankAccount.setError(e.message);
                })
            })
        })
    }

    onAddNewBankSuccess(bankAccount) {
        this.refs.inputBankAccount.addNewItem({
            value: bankAccount.idbankAccount,
            label: bankAccount.bank.name,
            bankImg: bankAccount.bank.logo
        });
        this.refs.inputBankAccount.setValue(bankAccount.idbankAccount);
        this.setState({...this.state,bankAccountOption:bankAccount.idbankAccount});

    }

    onChange(value) {
        let netAmount = StringUtils.toDecimal(value) - this.state.fee;
        if (netAmount < 0)
            netAmount = 0;
        this.refs.inputNetAmount.setValue(StringUtils.toMoneyFormat(netAmount, 2, ',', '.'));
        this.setState({
            ...this.state,
            amount: StringUtils.toDecimal(value)
        })
    }

    render() {
        if (this.state.render)
            return (
                <div>
                    <div className="animated fadeIn">
                        <nav aria-label="breadcrumb">
                            <ol className="breadcrumb">
                                <li className="breadcrumb-item">
                                    <Link to={appConstants.routes.dashboard}>Visão Geral</Link>
                                </li>
                                <li className="breadcrumb-item">
                                    <Link to={appConstants.routes.withdrawsBrl}>Saques BRL</Link>
                                </li>
                                <li className="breadcrumb-item active" aria-current="page">Adicionar Saque BRL</li>
                            </ol>
                        </nav>
                        <div className="centered-box">
                            <div className="text-center">
                                <span>
                                    <img alt="bitcoin box" width={200}
                                         src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/withdraw-add.svg"/>
                                </span>
                            </div>
                            <h1 className="text-center">Adicionar Intenção de
                                Saque BRL
                            </h1>
                            <h2 className="text-center description margin-top-20">
                                No processo de intenção de saque você deve escolher uma conta bancária cadastrada (Conta
                                Vinculada ao CPF cadastrado plataforma) e o
                                valor que deseja sacar, este valor deverá estar disponível em sua conta(Saldo
                                Disponível).
                                Este processo pode demorar até {appConstants.labels.maxDaysWithdrawBrlAnalysis} dias
                                úteis.
                            </h2>
                            <div className="instructions margin-top-30">
                                <ul>
                                    <li>
                                        <span>
                                            <i className="fas fa-check"></i>
                                            O saque só será processado com sucesso se a titularidade <strong>(CPF ou CNPJ)</strong> da conta bancária cadastrada for a mesma cadastrada e validada na plataforma.
                                        </span>
                                    </li>
                                    <li>
                                        <span>
                                            <i className="fas fa-check"></i>
                                            Para cada intenção de saque, será debitado da quantia solicitada, o valor de <strong>taxa</strong> fixa e irreajustável de <strong>R$ 17,00 (Dezessete reais)</strong>
                                        </span>
                                    </li>
                                    <li>
                                        <span>
                                            <i className="fas fa-check"></i>
                                            Se o saque estiver <strong>fora das especificações acima</strong>, o mesmo será rejeitado
                                        </span>
                                    </li>
                                    <li>
                                        <span>
                                            <i className="fas fa-check"></i>
                                            Limite de saque diário é de <strong>R$ 10.000,00 (Dez Mil Reais)</strong>
                                        </span>
                                    </li>
                                    <li>
                                        <span>
                                            <i className="fas fa-check"></i>
                                            O processo de saque será efetuado em até <strong>{appConstants.labels.maxDaysWithdrawBrlAnalysis} dias úteis</strong>.
                                        </span>
                                    </li>
                                    <li>
                                        <span>
                                            <i className="fas fa-check"></i>
                                            Valor Mínimo permitido para saque é de <strong>R$ 30,00</strong>.
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
                                                        <SimpleSelect required="true"
                                                                      additionalLink="Cadastrar nova Conta Bancária"
                                                                      onClickAdditionalLink={() => {
                                                                          this.refs.bankAccountAddEditComponent.wrappedInstance.toggleNewBankAccount()
                                                                      }} ref="inputBankAccount"
                                                                      optionComponent={BankAccountSelectOption}
                                                                      valueComponent={BankAccountSelectValue}
                                                                      placeholder="Escolha a conta para saque"
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
                                                    <div className="col-md-6 col-12">
                                                        <SimpleCurrencyInput required="true" selectAllOnFocus={true}
                                                                             allowEmpty={true}
                                                                             ref="inputAmount"
                                                                             onChange={this.onChange.bind(this)}
                                                                             className="form-control"
                                                                             label="Valor Desejado"
                                                                             placeholder="Digite o Montante a ser sacado"/>
                                                    </div>
                                                    <div className="col-md-3 col-6">
                                                        <SimpleInput disabled="true" label="Taxa"
                                                                     className="form-control" ref="inputFee"
                                                                     value={StringUtils.toMoneyFormat(this.state.fee, 2, ',', '.')}/>
                                                    </div>
                                                    <div className="col-md-3 col-6">
                                                        <SimpleInput disabled="true" label="Valor Final"
                                                                     className="form-control" ref="inputNetAmount"
                                                                     value={StringUtils.toMoneyFormat(this.state.netAmount, 2, ',', '.')}/>
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
                    </div>
                    <BankAccountAddEdit key="bankAccountAddEditComponent" ref="bankAccountAddEditComponent"
                                        onSuccess={this.onAddNewBankSuccess.bind(this)}/>
                </div>);
        else return <BackgroundPlaceholderMarker/>;
    }
}

const mapStateToProps = (state, ownProps) => {
    return {}
};

const mapDispatchToProps = (dispatch) => {
    return {
        toWithdraws: () => dispatch(push(appConstants.routes.withdrawsBrl)),
        updateTotalBalance: () => dispatch(totalBalanceActions.updateTotalBalance())
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(WithdrawAdd);