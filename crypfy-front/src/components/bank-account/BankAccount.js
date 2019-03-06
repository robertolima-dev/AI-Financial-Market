import React from 'react'
import BaseTemplateAdmin from '../base/BaseTemplateAdmin'
import SimpleButton from '../common/SimpleButton'
import appConstants from '../../constants/AppConstants'
import {Link} from 'react-router-dom'
import * as bankAccountActions from '../../redux/actions/bankAccountActions'
import StringUtils from '../../helpers/StringUtils'
import Moment from 'react-moment';
import BankAccountAddEdit from './BankAccountAddEdit'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap'
import sleeper from '../../helpers/Sleeper'
import Alert from 'react-s-alert'
import BackgroundPlaceholderMarker from '../base/BackgroundPlaceholderMarker'

class BankAccount extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            render: false,
            bankAccounts: null,
            modalConfirmDelete: false,
            deleteId: 0
        }
    }

    toggleConfirmDeleteModal() {
        if (this.state.modalConfirmDelete) {
            this.setState({...this.state, modalConfirmDelete: false});
        } else {
            this.setState({...this.state, modalConfirmDelete: true});
        }
    }

    openConfirmDeleteModal(id) {
        this.setState({...this.state, modalConfirmDelete: true, deleteId: id});
    }

    componentDidMount() {
        this.loadBankAccounts();
    }

    loadBankAccounts() {
        sleeper(300).then(() => {
            bankAccountActions.list().then((ret) => {
                this.setState({...this.state, render: true, bankAccounts: ret.response})
            })
        })
    }

    delete() {
        this.refs.buttonDelete.setLoadingOn('Processando...');
        sleeper(1200).then(() => {
           bankAccountActions.remove(this.state.deleteId).then((ret) => {
               this.refs.buttonDelete.setLoadingOff();
               this.toggleConfirmDeleteModal();
               Alert.success(ret.message);
               bankAccountActions.list().then((ret) => {
                   this.setState({...this.state, render: true, bankAccounts: ret.response})
               })
           }).catch((ret) => {
               Alert.error(ret.message);
               this.refs.buttonDelete.setLoadingOff();
           })
        })
    }

    render() {
        let bankAccountLines = null;
        if (this.state.bankAccounts != null && this.state.bankAccounts.length > 0) {
            bankAccountLines = this.state.bankAccounts.map((b, i) => {
                return (<tr key={i}>
                        <td>
                            <div className="float-left margin-top-10">
                                <img width="50"
                                     src={b.bank.logo}
                                     alt={b.bank.name} className="img-thumbnail rounded-circle"/>
                            </div>
                            <div style={{marginLeft: "10px"}} className="float-left">
                                <span><strong>{b.bank.name}</strong></span>
                                <span className="block description">
                                    CC <strong>{b.accountNumber}
                                    -{b.accountNumberDigit}</strong> AG <strong>{b.agency}</strong>
                                </span>
                                <span className="block description">
                                    {StringUtils.getHumanNameBankAccountType(b.type)}
                                </span>
                                <span className="block description">
                                    {b.documentType} <strong>{(b.documentType == 'CPF') ? StringUtils.toCpf(b.documentNumber) : StringUtils.toCnpj(b.documentNumber)}</strong>
                                </span>
                            </div>
                        </td>
                        <td>
                            <strong className="font-14">
                                <Moment fromNow date={b.created}/>
                            </strong>
                        </td>
                        <td>
                            <div>
                                <SimpleButton key={b.idbankAccount} onClick={() => {this.refs.bankAccountAddEditComponent.wrappedInstance.toggleEditBankAccount(b.idbankAccount)}} className="btn btn-primary">
                                    Editar
                                </SimpleButton>
                            </div>
                            <div className="margin-top-10">
                                <SimpleButton key={b.idbankAccount} onClick={() => {this.openConfirmDeleteModal(b.idbankAccount)}} className="btn btn-danger">
                                    Excluir
                                </SimpleButton>
                            </div>
                        </td>
                    </tr>
                );
            })
        } else {
            bankAccountLines = <tr>
                <td colSpan={3}>
                    <div className="no-results">
                        <span>
                            <img width={110} className="img-fluid margin-top-30" alt="no results"
                                 src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/no-result.svg"/>
                        </span>
                        <h3 className="margin-top-20">Ops, não foram encontradas contas bancárias</h3>
                        <span className="description">
                            Não foram encontrado registros, talvez você queira adicionar uma <a href="#"
                                                                                                onClick={(e) => {
                                                                                                    e.preventDefault();
                                                                                                    this.refs.bankAccountAddEditComponent.wrappedInstance.toggleNewBankAccount()
                                                                                                }}>Conta Bancária</a>
                        </span>
                    </div>
                </td>
            </tr>
        }

        if (this.state.render)
            return (
                <div>
                    <div className="animated fadeIn">
                        <nav aria-label="breadcrumb">
                            <ol className="breadcrumb">
                                <li className="breadcrumb-item">
                                    <Link to={appConstants.routes.dashboard}>Visão Geral</Link>
                                </li>
                                <li className="breadcrumb-item active" aria-current="page">
                                    Minhas Contas Bancárias
                                </li>
                            </ol>
                        </nav>
                        <div className="row margin-top-30">
                            <div className="col-md-12">
                                <div className="header-title">
                                    <div className="float-left">
                                    <span className="title">
                                        Minhas Contas Bancárias ({this.state.bankAccounts.length})
                                    </span>
                                        <div className="line"></div>
                                    </div>
                                    <span className="page-actions float-right">
                                    <SimpleButton onClick={() => {
                                        this.refs.bankAccountAddEditComponent.wrappedInstance.toggleNewBankAccount()
                                    }} className="btn btn-primary">
                                        Adicionar Conta Bancária
                                    </SimpleButton>
                                </span>
                                    <div className="dropdown page-mobile-actions float-right">
                                        <button className="btn btn-primary dropdown-toggle" type="button"
                                                id="dropdownMenuButton"
                                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <i className="fas fa-ellipsis-v"></i>
                                        </button>
                                        <div className="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                            <a onClick={(e) => {
                                                e.preventDefault();
                                                this.refs.bankAccountAddEditComponent.wrappedInstance.toggleNewBankAccount()
                                            }} className="dropdown-item" href="#">Adicionar Conta Bancária</a>
                                        </div>
                                    </div>
                                    <div className="clearfix"></div>
                                </div>
                                <div className="widget margin-top-30">
                                    <div className="body">
                                        <div className="clearfix"></div>
                                        <div className="table-responsive">
                                            <table className="table">
                                                <thead>
                                                <tr>
                                                    <th>
                                                        Conta
                                                    </th>
                                                    <th>
                                                        Criado
                                                    </th>
                                                    <th>
                                                        Ações
                                                    </th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                {bankAccountLines}
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <BankAccountAddEdit key="bankAccountAddEditComponent" onSuccess={(bankAccount) => {
                        this.setState({...this.state,render:false},() => {
                            this.loadBankAccounts()
                        })
                    }} ref="bankAccountAddEditComponent"/>
                    <Modal isOpen={this.state.modalConfirmDelete} toggle={this.toggleConfirmDeleteModal.bind(this)}>
                        <ModalBody>
                            <h5>Você deseja realmente excluir está conta bancária?</h5>
                        </ModalBody>
                        <ModalFooter>
                            <SimpleButton onClick={this.delete.bind(this)} ref="buttonDelete"
                                          className="btn btn-danger">
                                Excluir
                            </SimpleButton>
                            <Button color="secondary"
                                    onClick={this.toggleConfirmDeleteModal.bind(this)}>Cancelar</Button>
                        </ModalFooter>
                    </Modal>
                </div>
            ); else return <BackgroundPlaceholderMarker/>;
    }
}

export default BankAccount;