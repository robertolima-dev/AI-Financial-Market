import React from 'react'
import BaseTemplateAdmin from '../base/BaseTemplateAdmin'
import SimpleButton from '../common/SimpleButton'
import appConstants from '../../constants/AppConstants'
import {Link} from 'react-router-dom'
import PlanTakeProfitAdd from './add/PlanTakeProfitAdd'
import * as planTakeProfitActions from '../../redux/actions/planTakeProfitRequestActions'
import StringUtils from '../../helpers/StringUtils'
import PlanTakeProfitStatus from './PlanTakeProfitStatus'
import Moment from 'react-moment';
import PlanTakeProfitActionControl from './PlanTakeProfitActionControl'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader, Popover, PopoverBody, PopoverHeader} from 'reactstrap'
import sleeper from '../../helpers/Sleeper'
import Alert from 'react-s-alert'
import BackgroundPlaceholderMarker from '../base/BackgroundPlaceholderMarker'

class PlanTakeProfit extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            planTakeProfits: null,
            render: false,
            modalCancel: false,
            modalDelete: false,
            cancelId: 0,
            deleteId: 0,
            failedReasonPopovers : []
        }
    }

    componentDidMount() {
        this.loadPlanTakeProfits();
    }

    loadPlanTakeProfits() {
        planTakeProfitActions.list().then((ret) => {
            let failedReasonPopovers = [];
            ret.response.forEach((p, i) => {
                if (p.failedReason != null)
                    failedReasonPopovers[p.idplanTakeProfitRequest] = false;
            });
            this.setState({...this.state, planTakeProfits: ret.response, render: true, failedReasonPopovers : failedReasonPopovers});
        })
    }

    toggleCancel() {
        if (this.state.modalCancel)
            this.setState({...this.state, modalCancel: false})
        else
            this.setState({...this.state, modalCancel: true})
    }

    toggleDelete() {
        if (this.state.modalDelete)
            this.setState({...this.state, modalDelete: false})
        else
            this.setState({...this.state, modalDelete: true})
    }

    cancel() {
        this.refs.buttonCancel.setLoadingOn('Processando..');
        sleeper(1200).then(() => {
            planTakeProfitActions.changeStatusToCancelled(this.state.cancelId).then((ret) => {
                const sucessMessage = ret.message;
                //load request profit
                planTakeProfitActions.list().then((ret) => {

                    let failedReasonPopovers = [];
                    ret.response.forEach((p, i) => {
                        if (p.failedReason != null)
                            failedReasonPopovers[p.idplanTakeProfitRequest] = false;
                    });

                    Alert.success(sucessMessage);
                    this.refs.buttonCancel.setLoadingOff();
                    this.toggleCancel();
                    this.setState({...this.state, planTakeProfits: ret.response,failedReasonPopovers : failedReasonPopovers});
                })
            }).catch((ret) => {
                this.refs.buttonCancel.setLoadingOff();
                Alert.error(ret.message);
            })
        })
    }

    onCancel(id) {
        this.setState({...this.state, modalCancel: true, cancelId: id});
    }

    onDelete(id) {
        this.setState({...this.state, modalDelete: true, deleteId: id});
    }

    delete() {
        this.refs.buttonDelete.setLoadingOn('Processando..');
        sleeper(1200).then(() => {
            planTakeProfitActions.del(this.state.deleteId).then((ret) => {
                const sucessMessage = ret.message;
                //load request profit
                planTakeProfitActions.list().then((ret) => {

                    let failedReasonPopovers = [];
                    ret.response.forEach((p, i) => {
                        if (p.failedReason != null)
                            failedReasonPopovers[p.idplanTakeProfitRequest] = false;
                    });

                    Alert.success(sucessMessage);
                    this.refs.buttonDelete.setLoadingOff();
                    this.toggleDelete();
                    this.setState({...this.state, planTakeProfits: ret.response, failedReasonPopovers : failedReasonPopovers});
                })
            }).catch((ret) => {
                this.refs.buttonDelete.setLoadingOff();
                Alert.error(ret.message);
            })
        })
    }

    toggleFailedReasonPopover(id, e) {
        e.preventDefault();
        let failedReasonPopovers = this.state.failedReasonPopovers;
        if (failedReasonPopovers[id])
            failedReasonPopovers[id] = false;
        else
            failedReasonPopovers[id] = true;
        this.setState({...this.state, failedReasonPopovers: failedReasonPopovers});
    }

    render() {
        let planTakeProfitLines = null;
        if (this.state.planTakeProfits != null && this.state.planTakeProfits.length > 0) {
            planTakeProfitLines = this.state.planTakeProfits.map((p, i) => {
                let failedReason = (p.failedReason!= null) ? <span>
                    <p className="margin-top-10">
                                    <a className="denied-reason-link" href="#"
                                       id={"popover-" + p.idplanTakeProfitRequest}
                                       onClick={this.toggleFailedReasonPopover.bind(this, p.idplanTakeProfitRequest)}>
                                        Ver Motivo
                                    </a>
                                </p>
                                <Popover className="denied-reason" key={"popover-" + p.idplanTakeProfitRequest}
                                         placement="bottom"
                                         isOpen={this.state.failedReasonPopovers[p.idplanTakeProfitRequest]}
                                         target={"popover-" + p.idplanTakeProfitRequest}
                                         toggle={this.toggleFailedReasonPopover.bind(this, p.idplanTakeProfitRequest)}>
                                    <PopoverBody>
                                        {p.failedReason}
                                    </PopoverBody>
                                </Popover>
                    </span> : null;
                return (
                    <tr key={i}>
                        <td>
                            <div style={{position: "relative"}}>
                                <div className={"plan-avatar float-left " + p.status.toLowerCase().replace('_', '-')}>
                                    <span>
                                        <img width={30} alt={'plan ' + p.userPlan.plan.name}
                                             src={p.userPlan.plan.logo}/>
                                    </span>
                                </div>
                                <div style={{marginTop: "2px", marginLeft: "13px"}} className="float-left">
                                    <span className="plan-name">{p.userPlan.planName} <strong
                                        className="description-12">{p.userPlan.plan.name}
                                        ({p.userPlan.duration})</strong>
                                    </span>
                                    <div>
                                        <div>
                                            <span className="block label">Início Plano</span>
                                            <strong style={{position: "relative", top: "-5px"}} className="font-12">
                                                <Moment format="DD/MM/YYYY" date={p.userPlan.startDate}/>
                                            </strong>
                                        </div>
                                        <div style={{position: "relative", top: "-8px"}}>
                                            <span className="block label">Fim Plano</span>
                                            <strong style={{position: "relative", top: "-5px"}} className="font-12">
                                                <Moment format="DD/MM/YYYY" date={p.userPlan.endDate}/>
                                            </strong>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td>
                            R$ {StringUtils.toMoneyFormat(p.amount, 2, ',', '.')}
                        </td>
                        <td>
                            <PlanTakeProfitStatus status={p.status}/>
                            {failedReason}
                            <div className="margin-top-20">
                                <div>
                                    <span style={{fontSize: "12px", fontWeight: "700"}}>Criado <Moment fromNow
                                                                                                       date={p.created}/></span>
                                </div>
                                <div>
                                    <span style={{fontSize: "12px", fontWeight: "700"}}>Atualizado <Moment fromNow
                                                                                                           date={p.lastUpdated}/></span>
                                </div>
                            </div>
                        </td>
                        <td>
                            <PlanTakeProfitActionControl key={p.idplanTakeProfitRequest}
                                                         onDelete={this.onDelete.bind(this)}
                                                         onCancel={this.onCancel.bind(this)}
                                                         id={p.idplanTakeProfitRequest} status={p.status}/>
                        </td>
                    </tr>
                );
            })
        } else planTakeProfitLines = <tr>
            <td colSpan={4}>
                <div className="no-results">
                        <span>
                            <img width={110} className="img-fluid margin-top-30" alt="no results"
                                 src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/no-result.svg"/>
                        </span>
                    <h3 className="margin-top-20">Ops, não foram encontradas intenções de movimentação de lucro</h3>
                    <span className="description">
                            Não foram encontrado registros, talvez você queira adicionar uma <a href="#"
                                                                                                onClick={(e) => {
                                                                                                    e.preventDefault();
                                                                                                    this.refs.planTakeProfitAddComponent.toggle();
                                                                                                }}>intenção de movimentação de lucro</a>
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
                                <li className="breadcrumb-item active" aria-current="page">
                                    Movimentações de Lucro
                                </li>
                            </ol>
                        </nav>
                        <div className="row margin-top-30">
                            <div className="col-md-12">
                                <div className="header-title">
                                    <div className="float-left">
                                    <span className="title">
                                        Movimentações de Lucro ({this.state.planTakeProfits.length})
                                    </span>
                                        <div className="line"></div>
                                    </div>
                                    <span className="page-actions float-right">
                                    <SimpleButton onClick={() => {
                                        this.refs.planTakeProfitAddComponent.toggle();
                                    }} className="btn btn-primary">
                                        Adicionar Movimentação de Lucro
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
                                                this.refs.planTakeProfitAddComponent.toggle();
                                            }} className="dropdown-item" href="#">Adicionar Movimentação de Lucro</a>
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
                                                        Plano
                                                    </th>
                                                    <th>
                                                        Valor Solicitado
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
                                                {planTakeProfitLines}
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <PlanTakeProfitAdd onSucess={() => {
                        this.loadPlanTakeProfits()
                    }} ref="planTakeProfitAddComponent"/>
                    <Modal isOpen={this.state.modalCancel}
                           toggle={this.toggleCancel.bind(this)}>
                        <ModalHeader>
                            Deseja realmente cancelar está intenção de movimentação de lucro?
                        </ModalHeader>
                        <ModalFooter>
                            <SimpleButton key="buttonCancel" ref="buttonCancel" onClick={this.cancel.bind(this)}
                                          className="btn btn-danger">
                                Cancelar Intenção
                            </SimpleButton>
                            <Button color="secondary"
                                    onClick={this.toggleCancel.bind(this)}>Sair</Button>
                        </ModalFooter>
                    </Modal>
                    <Modal isOpen={this.state.modalDelete}
                           toggle={this.toggleDelete.bind(this)}>
                        <ModalHeader>
                            Deseja realmente excluir está intenção de movimentação de lucro?
                        </ModalHeader>
                        <ModalFooter>
                            <SimpleButton key="buttonDelete" ref="buttonDelete" onClick={this.delete.bind(this)}
                                          className="btn btn-danger">
                                Excluir Intenção
                            </SimpleButton>
                            <Button color="secondary"
                                    onClick={this.toggleDelete.bind(this)}>Sair</Button>
                        </ModalFooter>
                    </Modal>
                </div>
            ); else return <BackgroundPlaceholderMarker/>;
    }
}

export default PlanTakeProfit;