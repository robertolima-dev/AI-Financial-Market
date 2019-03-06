import React from 'react'
import SimpleButton from '../common/SimpleButton'
import {push} from 'react-router-redux'
import {connect} from 'react-redux';
import * as depositBrlActions from '../../redux/actions/depositBrlActions'
import appConstants from '../../constants/AppConstants'
import Moment from 'react-moment';
import StringUtils from '../../helpers/StringUtils'
import DepositBrlStatusActionControl from './DepositBrlStatusActionControl'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader, Popover, PopoverBody, PopoverHeader} from 'reactstrap'
import sleeper from '../../helpers/Sleeper'
import Alert from 'react-s-alert';
import {Link} from 'react-router-dom'
import DepositStatus from './DepositStatus'
import BackgroundPlaceholderMarker from '../base/BackgroundPlaceholderMarker'

class Deposit extends React.Component {
    constructor(props) {
        super(props);

        document.body.style.backgroundColor = "rgb(244, 244, 244)";

        this.state = {
            render: false,
            deposits: [],
            modalDepositVoucherUpload: false,
            selectedDepositId: 0,
            confirmedUploadControl: false,
            modalDepositVoucherUploadCancel: false,
            modalDepositDelete: false,
            depositDeleteId: 0,
            depositCancelId: 0,
            deniedReasonPopovers: []
        }
        sleeper(300).then(() => {
            depositBrlActions.listDepositsBrl().then((ret) => {
                let deniedReasonPopovers = [];
                ret.response.forEach((d, i) => {
                    if (d.deniedReason != null)
                        deniedReasonPopovers[d.iddepositWithdrawRequestBrl] = false;
                });
                this.setState({
                    ...this.state,
                    render: true,
                    deposits: ret.response,
                    deniedReasonPopovers: deniedReasonPopovers
                });
            }).catch((ret) => {
                //redirect server error
            })
        })

        this.buttonUpload = null;
        this.buttonFileUpload = null;
    }

    preCancelDeposit(id) {
        this.setState({...this.state, depositCancelId: id}, () => {
            this.toggleDepositVoucherUploadCancelModal();
        });
    }

    preDeleteDeposit(id) {
        this.setState({...this.state, depositDeleteId: id, modalDepositDelete: true});
    }

    deleteDeposit() {
        this.refs.buttonDelete.setLoadingOn("processando..");
        sleeper(1000).then(() => {
            depositBrlActions.del(this.state.depositDeleteId).then((ret) => {
                let retMessage = ret.message;
                depositBrlActions.listDepositsBrl().then((ret) => {
                    let deniedReasonPopovers = [];
                    ret.response.forEach((d, i) => {
                        if (d.deniedReason != null)
                            deniedReasonPopovers[d.iddepositWithdrawRequestBrl] = false;
                    });
                    this.setState({...this.state, modalDepositDelete: false, deposits: ret.response,deniedReasonPopovers:deniedReasonPopovers}, () => {
                        Alert.success(retMessage);
                        this.refs.buttonDelete.setLoadingOff();
                    });
                })
            }).catch((ret) => {
                this.refs.buttonDelete.setLoadingOff();
                Alert.error(ret.message);
            })
        })
    }

    cancelDeposit() {
        this.refs.buttonCancel.setLoadingOn("processando..");
        sleeper(1000).then(() => {
            depositBrlActions.cancelDeposit(this.state.depositCancelId).then((ret) => {
                depositBrlActions.listDepositsBrl().then((ret) => {
                    let deniedReasonPopovers = [];
                    ret.response.forEach((d, i) => {
                        if (d.deniedReason != null)
                            deniedReasonPopovers[d.iddepositWithdrawRequestBrl] = false;
                    });
                    this.setState({...this.state, confirmedUploadControl: true, deposits: ret.response,deniedReasonPopovers:deniedReasonPopovers}, () => {
                        Alert.success("Intenção de Depósito cancelada com sucesso!");
                        this.refs.buttonCancel.setLoadingOff();
                        this.toggleDepositVoucherUploadCancelModal();
                    });
                })
            })
        })
    }

    toggleDepositVoucherUploadCancelModal() {
        if (this.state.modalDepositVoucherUploadCancel)
            this.setState({...this.state, modalDepositVoucherUploadCancel: false})
        else
            this.setState({...this.state, modalDepositVoucherUploadCancel: true})
    }

    toggleDepositDeleteModal() {
        if (this.state.modalDepositDelete)
            this.setState({...this.state, modalDepositDelete: false})
        else
            this.setState({...this.state, modalDepositDelete: true})
    }

    toggleDepositVoucherUploadModal() {
        if (this.state.modalDepositVoucherUpload)
            this.setState({...this.state, modalDepositVoucherUpload: false})
        else
            this.setState({...this.state, modalDepositVoucherUpload: true})
    }

    openDepositVoucherUploadModal(id) {
        this.setState({...this.state, modalDepositVoucherUpload: true, selectedDepositId: id});
    }

    chooseFileUpload() {
        this.buttonFileUpload.click(this);
    }

    onClickVoucherUpload(id) {
        this.openDepositVoucherUploadModal(id);
    }

    onChangeFileupload(e) {
        this.buttonUpload.setLoadingOn("carregando foto..");
        depositBrlActions.uploadDepositVoucher(e.target.files[0], this.state.selectedDepositId).then((ret) => {
            depositBrlActions.listDepositsBrl().then((ret) => {
                this.buttonUpload.setLoadingOff();
                this.setState({...this.state, confirmedUploadControl: true, deposits: ret.response});
            })
        })
    }

    toggleDeniedReasonPopover(id, e) {
        e.preventDefault();
        let deniedReasonPopovers = this.state.deniedReasonPopovers;
        if (deniedReasonPopovers[id])
            deniedReasonPopovers[id] = false;
        else
            deniedReasonPopovers[id] = true;
        this.setState({...this.state, deniedReasonPopovers: deniedReasonPopovers});
    }

    render() {
        let depositLines = null;
        if (this.state.deposits && this.state.deposits.length > 0)
            depositLines = this.state.deposits.map((d, i) => {
                let deniedReason = (d.deniedReason != null) ? <span>
                    <p className="margin-top-10">
                                    <a className="denied-reason-link" href="#"
                                       id={"popover-" + d.iddepositWithdrawRequestBrl}
                                       onClick={this.toggleDeniedReasonPopover.bind(this, d.iddepositWithdrawRequestBrl)}>
                                        Ver Motivo
                                    </a>
                                </p>
                                <Popover className="denied-reason" key={"popover-" + d.iddepositWithdrawRequestBrl}
                                         placement="bottom"
                                         isOpen={this.state.deniedReasonPopovers[d.iddepositWithdrawRequestBrl]}
                                         target={"popover-" + d.iddepositWithdrawRequestBrl}
                                         toggle={this.toggleDeniedReasonPopover.bind(this, d.iddepositWithdrawRequestBrl)}>
                                    <PopoverBody>
                                        {d.deniedReason}
                                    </PopoverBody>
                                </Popover>
                    </span> : null;
                return (
                    <tr key={i}>
                        <td>
                            <div className="float-left margin-top-10">
                                <img width="50"
                                     src={d.bankLogo}
                                     alt={d.bankName} className="img-thumbnail rounded-circle"/>
                            </div>
                            <div style={{marginLeft: "10px"}} className="float-left">
                                <span><strong>{d.bankName}</strong></span>
                                <span className="block description">
                                    CC <strong>{d.bankAccountNumber}-{d.bankAccountNumberDigit}</strong> AG <strong>{d.bankAgency}</strong>
                                </span>
                                <span className="block description">
                                    {StringUtils.getHumanNameBankAccountType(d.bankAccountType)}
                                </span>
                                <span className="block description">
                                    {d.bankAccountDocumentType} <strong>{(d.bankAccountDocumentType == 'CPF') ? StringUtils.toCpf(d.bankAccountDocumentNumber) : StringUtils.toCnpj(d.bankAccountDocumentNumber)}</strong>
                                </span>
                            </div>
                        </td>
                        <td>{StringUtils.toMoneyFormat(d.amount, 2, ',', '.')}</td>
                        <td>
                            <DepositStatus status={d.status}/>
                            {deniedReason}
                            <div className="margin-top-20">
                                <div>
                                <span style={{fontSize: "12px", fontWeight: "700"}}>Criado <Moment fromNow
                                                                                                   date={d.created}/></span>
                                </div>
                                <div>
                                <span style={{fontSize: "12px", fontWeight: "700"}}>Atualizado <Moment fromNow
                                                                                                       date={d.lastUpdated}/></span>
                                </div>
                            </div>
                        </td>
                        <td>
                            <DepositBrlStatusActionControl key={d.iddepositWithdrawRequestBrl}
                                                           id={d.iddepositWithdrawRequestBrl}
                                                           onClickCancelRequest={this.preCancelDeposit.bind(this)}
                                                           onClickDeleteRequest={this.preDeleteDeposit.bind(this)}
                                                           onClickVoucherUpload={this.onClickVoucherUpload.bind(this)}
                                                           status={d.status}/>
                        </td>
                    </tr>);
            })
        else
            depositLines = <tr>
                <td colSpan={4}>
                    <div className="no-results">
                        <span>
                            <img width={110} className="img-fluid margin-top-30" alt="no results"
                                 src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/no-result.svg"/>
                        </span>
                        <h3 className="margin-top-20">Ops, não foram encontradas intenções de depósito</h3>
                        <span className="description">
                            Não foram encontrado registros, talvez você queira adicionar uma <a href="#"
                                                                                                onClick={(e) => {
                                                                                                    e.preventDefault();
                                                                                                    this.props.toDepositsAdd();
                                                                                                }}>intenção de depósito</a>
                        </span>
                    </div>
                </td>
            </tr>


        let uploadModalControl = (!this.state.confirmedUploadControl) ? <div>
                <ModalBody>
                    <div className="text-center">
                                <span>
                                    <img className="margin-top-20"
                                         src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/deposit-voucher-upload.png"
                                         width={200} alt="Upload Deposit Voucher"/>
                                </span>
                        <h5 className="margin-top-20">Upload de Comprovante de Depósito</h5>
                        <p className="margin-top-20 description">
                            O comprovante de depósito deve seguir o formato <strong>PNG</strong>, <strong>JPG</strong> ou o
                            formato <strong>PDF</strong>. O Arquivo não deve
                            exceder <strong>20MB</strong> de tamanho.
                        </p>
                    </div>
                </ModalBody>
                <ModalFooter>
                    <input ref={input => this.buttonFileUpload = input} className="hide" type="file"
                           onChange={this.onChangeFileupload.bind(this)}/>
                    <SimpleButton ref={(button) => this.buttonUpload = button} onClick={this.chooseFileUpload.bind(this)}
                                  className="btn btn-primary">
                        Carregar Foto
                    </SimpleButton>
                    <Button color="secondary"
                            onClick={this.toggleDepositVoucherUploadModal.bind(this)}>Cancelar</Button>
                </ModalFooter>
            </div> :
            <div>
                <ModalBody>
                    <div className="text-center">
                                <span>
                                    <img className="margin-top-20"
                                         src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/deposit-voucher-upload-confirmation.png"
                                         width={200} alt="Upload Deposit Voucher Confirmation"/>
                                </span>
                        <h5 className="margin-top-20">Comprovante Armazenado com Sucesso!</h5>
                        <p className="margin-top-20">Seu comprovante de depósito foi armazenado com sucesso e em até
                            <strong>{appConstants.labels.maxDaysDepositVoucherAnalysis}</strong> dias úteis iremos
                            análisa-lo!</p>
                    </div>
                </ModalBody>
                <ModalFooter>
                    <SimpleButton onClick={this.toggleDepositVoucherUploadModal.bind(this)} className="btn btn-primary">
                        Ok, Entendi
                    </SimpleButton>
                </ModalFooter>
            </div>

        if (this.state.render)
            return (
                <div className="animated fadeIn">
                    <nav aria-label="breadcrumb">
                        <ol className="breadcrumb">
                            <li className="breadcrumb-item">
                                <Link to={appConstants.routes.dashboard}>Visão Geral</Link>
                            </li>
                            <li className="breadcrumb-item active" aria-current="page">
                                Depósitos BRL
                            </li>
                        </ol>
                    </nav>
                    <div className="row margin-top-30">
                        <div className="col-md-12">
                            <div className="header-title">
                                <div className="float-left">
                                    <span className="title">
                                        Depósitos BRL ({this.state.deposits.length})
                                    </span>
                                    <div className="line"></div>
                                </div>
                                <span className="page-actions float-right">
                                    <SimpleButton onClick={() => {
                                        this.props.toDepositsAdd()
                                    }} className="btn btn-primary">
                                        Adicionar Intenção de Depósito
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
                                            this.props.toDepositsAdd()
                                        }} className="dropdown-item" href="#">Adicionar Intenção de Depósito</a>
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
                                                    Conta Depósito
                                                </th>
                                                <th>
                                                    Valor
                                                </th>

                                                <th>
                                                    Status
                                                </th>
                                                <th>
                                                    Ações
                                                </th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            {depositLines}
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <Modal isOpen={this.state.modalDepositVoucherUpload}
                           toggle={this.toggleDepositVoucherUploadModal.bind(this)}>
                        {uploadModalControl}
                    </Modal>

                    <Modal isOpen={this.state.modalDepositVoucherUploadCancel}
                           toggle={this.toggleDepositVoucherUploadCancelModal.bind(this)}>
                        <ModalHeader>
                            Deseja realmente cancelar está intenção de depósito?
                        </ModalHeader>
                        <ModalFooter>
                            <SimpleButton ref="buttonCancel" onClick={this.cancelDeposit.bind(this)}
                                          className="btn btn-danger">
                                Cancelar Intenção
                            </SimpleButton>
                            <Button color="secondary"
                                    onClick={this.toggleDepositVoucherUploadCancelModal.bind(this)}>Sair</Button>
                        </ModalFooter>
                    </Modal>
                    <Modal isOpen={this.state.modalDepositDelete}
                           toggle={this.toggleDepositDeleteModal.bind(this)}>
                        <ModalHeader>
                            Deseja realmente excluir está intenção de depósito?
                        </ModalHeader>
                        <ModalFooter>
                            <SimpleButton ref="buttonDelete" onClick={this.deleteDeposit.bind(this)}
                                          className="btn btn-danger">
                                Excluir Intenção
                            </SimpleButton>
                            <Button color="secondary"
                                    onClick={this.toggleDepositDeleteModal.bind(this)}>Sair</Button>
                        </ModalFooter>
                    </Modal>
                </div>
            )
        else
            return <BackgroundPlaceholderMarker/>;

    }
}

const mapStateToProps = (state, ownProps) => {
    return {}
};

const mapDispatchToProps = (dispatch) => {
    return {
        toDepositsAdd: () => dispatch(push(appConstants.routes.depositsBrlAdd))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Deposit);