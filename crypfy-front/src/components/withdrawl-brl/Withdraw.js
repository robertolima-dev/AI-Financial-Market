import React from 'react'
import SimpleButton from '../common/SimpleButton'
import * as withdrawBrlActions from '../../redux/actions/withdrawBrlActions'
import StringUtils from '../../helpers/StringUtils'
import Moment from 'react-moment';
import {push} from 'react-router-redux'
import {connect} from 'react-redux';
import {Link} from 'react-router-dom'
import appConstants from '../../constants/AppConstants'
import WithdrawStatus from './WithdrawStatus'
import WithdrawBrlStatusActionControl from './WithdrawBrlStatusActionControl'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader, Popover, PopoverBody, PopoverHeader} from 'reactstrap'
import sleep from "../../helpers/Sleeper";
import Alert from 'react-s-alert'
import * as totalBalanceActions from '../../redux/actions/totalBalanceActions'
import BackgroundPlaceholderMarker from '../base/BackgroundPlaceholderMarker'

class Withdraw extends React.Component {
    constructor(props) {
        super(props);

        document.body.style.backgroundColor = "rgb(244, 244, 244)";

        this.state = {
            render: false,
            withdraws: [],
            modalDepositVoucherUpload: false,
            modalCancelWithdraw: false,
            modalDeleteWithdraw: false,
            cancelWithdrawId: 0,
            deleteWithdrawId: 0,
            deniedReasonPopovers: []
        }
        sleep(300).then(() => {
            withdrawBrlActions.listWithdrawsBrl().then((ret) => {
                let deniedReasonPopovers = [];
                ret.response.forEach((d, i) => {
                    if (d.deniedReason != null)
                        deniedReasonPopovers[d.iddepositWithdrawRequestBrl] = false;
                })
                this.setState({...this.state, render: true, withdraws: ret.response,deniedReasonPopovers: deniedReasonPopovers});
            }).catch((ret) => {
                //redirect server error
            })
        })
    }

    toggleModalCancelWithdraw() {
        if (this.state.modalCancelWithdraw)
            this.setState({...this.state, modalCancelWithdraw: false})
        else
            this.setState({...this.state, modalCancelWithdraw: true})
    }

    toggleModalDeleteWithdraw() {
        if (this.state.modalDeleteWithdraw)
            this.setState({...this.state, modalDeleteWithdraw: false})
        else
            this.setState({...this.state, modalDeleteWithdraw: true})
    }

    cancelWithdraw() {
        this.refs.buttonCancel.setLoadingOn("Processando...");
        sleep(1200).then(() => {
            withdrawBrlActions.cancel(this.state.cancelWithdrawId).then((ret) => {
                this.refs.buttonCancel.setLoadingOff();
                Alert.success(ret.message);
                this.props.updateTotalBalance();
                withdrawBrlActions.listWithdrawsBrl().then((ret) => {
                    let deniedReasonPopovers = [];
                    ret.response.forEach((d, i) => {
                        if (d.deniedReason != null)
                            deniedReasonPopovers[d.iddepositWithdrawRequestBrl] = false;
                    })
                    this.setState({...this.state, render: true,modalCancelWithdraw: false, withdraws: ret.response,deniedReasonPopovers: deniedReasonPopovers});
                })
            }).catch((ret) => {
                this.refs.buttonCancel.setLoadingOff();
                Alert.error(ret.message);
                this.toggleModalCancelWithdraw();
            })
        })
    }

    deleteWithdraw() {
        this.refs.buttonDelete.setLoadingOn("Processando...");
        sleep(1200).then(() => {
            withdrawBrlActions.del(this.state.deleteWithdrawId).then((ret) => {
                this.refs.buttonDelete.setLoadingOff();
                Alert.success(ret.message);
                withdrawBrlActions.listWithdrawsBrl().then((ret) => {
                    let deniedReasonPopovers = [];
                    ret.response.forEach((d, i) => {
                        if (d.deniedReason != null)
                            deniedReasonPopovers[d.iddepositWithdrawRequestBrl] = false;
                    })
                    this.setState({...this.state, render: true,modalDeleteWithdraw: false, withdraws: ret.response,deniedReasonPopovers: deniedReasonPopovers});
                })
            }).catch((ret) => {
                this.refs.buttonDelete.setLoadingOff();
                Alert.error(ret.message);
                this.toggleModalDeleteWithdraw();
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
        let withdrawLines = null;
        if (this.state.withdraws && this.state.withdraws.length > 0)
            withdrawLines = this.state.withdraws.map((d, i) => {
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
                        <td>
                            <strong>R$ {StringUtils.toMoneyFormat(d.amount - d.fee, 2, ',', '.')}</strong>
                            <div>
                                <span
                                    className="green font-13">R$ {StringUtils.toMoneyFormat(d.amount, 2, ',', '.')}</span>
                                - <span
                                className="red font-13">(taxa) R$ {StringUtils.toMoneyFormat(d.fee, 2, ',', '.')}</span>
                            </div>
                        </td>
                        <td>
                            <WithdrawStatus status={d.status}/>
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
                            <WithdrawBrlStatusActionControl key={d.iddepositWithdrawRequestBrl} status={d.status}
                                                            onClickDeleteRequest={(id) => {
                                                                this.setState({
                                                                    ...this.state,
                                                                    deleteWithdrawId: id,
                                                                    modalDeleteWithdraw: true
                                                                })
                                                            }} onClickCancelRequest={(id) => {
                                this.setState({...this.state, cancelWithdrawId: id, modalCancelWithdraw: true})
                            }} id={d.iddepositWithdrawRequestBrl}/>
                        </td>
                    </tr>);
            })
        else
            withdrawLines = <tr>
                <td colSpan={4}>
                    <div className="no-results">
                        <span>
                            <img width={110} className="img-fluid margin-top-30" alt="no results"
                                 src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/no-result.svg"/>
                        </span>
                        <h3 className="margin-top-20">Ops, não foram encontradas intenções de saque</h3>
                        <span className="description">
                            Não foram encontrado registros, talvez você queira adicionar uma <a onClick={(e) => {
                            e.preventDefault();
                            this.props.toWithdrawsAdd();
                        }} href="#">intenção de saque</a>
                        </span>
                    </div>
                </td>
            </tr>

        if (this.state.render)
            return (
                <div>
                    <div className="animated fadeIn">
                        <nav aria-label="breadcrumb">
                            <ol className="breadcrumb">
                                <li className="breadcrumb-item">
                                    <Link to={appConstants.routes.dashboard}>Visão Geral</Link>
                                </li>
                                <li className="breadcrumb-item active" aria-current="page">Saques BRL</li>
                            </ol>
                        </nav>
                        <div className="row margin-top-30">
                            <div className="col-md-12">
                                <div className="header-title">
                                    <div className="float-left">
                                    <span className="title">
                                        Saques BRL ({this.state.withdraws.length})
                                    </span>
                                        <div className="line"></div>
                                    </div>
                                    <span className="page-actions float-right">
                                    <SimpleButton onClick={() => {
                                        this.props.toWithdrawsAdd()
                                    }} className="btn btn-primary">
                                        Adicionar Intenção de Saque
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
                                                this.props.toWithdrawsAdd();
                                            }} className="dropdown-item" href="#">Adicionar Intenção de Saque</a>
                                        </div>
                                    </div>
                                    <div className="clearfix"></div>
                                </div>
                                <div className="widget margin-top-30">
                                    <div className="body">
                                        <div className="clearfix"></div>
                                        <div className="table-responsive">
                                            <table className="table margin-top-40">
                                                <thead>
                                                <tr>
                                                    <th>
                                                        Conta Saque
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
                                                {withdrawLines}
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <Modal isOpen={this.state.modalCancelWithdraw}
                           toggle={this.toggleModalCancelWithdraw.bind(this)}>
                        <ModalHeader>
                            Deseja realmente cancelar está intenção de saque?
                        </ModalHeader>
                        <ModalFooter>
                            <SimpleButton ref="buttonCancel" onClick={this.cancelWithdraw.bind(this)}
                                          className="btn btn-danger">
                                Cancelar Intenção
                            </SimpleButton>
                            <Button color="secondary"
                                    onClick={this.toggleModalCancelWithdraw.bind(this)}>Sair</Button>
                        </ModalFooter>
                    </Modal>
                    <Modal isOpen={this.state.modalDeleteWithdraw}
                           toggle={this.toggleModalDeleteWithdraw.bind(this)}>
                        <ModalHeader>
                            Deseja realmente excluir está intenção de saque?
                        </ModalHeader>
                        <ModalFooter>
                            <SimpleButton ref="buttonDelete" onClick={this.deleteWithdraw.bind(this)}
                                          className="btn btn-danger">
                                Excluir Intenção
                            </SimpleButton>
                            <Button color="secondary"
                                    onClick={this.toggleModalDeleteWithdraw.bind(this)}>Sair</Button>
                        </ModalFooter>
                    </Modal>
                </div>
            )
        else return <BackgroundPlaceholderMarker/>;
    }
}

const mapStateToProps = (state, ownProps) => {
    return {}
};

const mapDispatchToProps = (dispatch) => {
    return {
        toWithdrawsAdd: () => dispatch(push(appConstants.routes.withdrawsBrlAdd)),
        updateTotalBalance: () => dispatch(totalBalanceActions.updateTotalBalance())
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Withdraw);