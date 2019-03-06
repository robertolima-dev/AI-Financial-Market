import React from 'react'
import BaseTemplateAdmin from '../base/BaseTemplateAdmin'
import appConstants from '../../constants/AppConstants'
import {Link} from 'react-router-dom'
import {Radio, RadioGroup} from 'react-icheck'
import PlanAddLinea from './PlanAddLinea'
import PlanAddAudace from './PlanAddAudace'
import PlanAddCadenza from './PlanAddCadenza'

class PlanAdd extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            audace: false,
            linea: true,
            cadenza: false
        }
    }

    toggle(plan) {
        if (plan == 'linea')
            this.setState({...this.state, linea: true, audace: false, cadenza: false});
        if (plan == 'audace')
            this.setState({...this.state, linea: false, audace: true, cadenza: false});
        if (plan == 'cadenza')
            this.setState({...this.state, linea: false, audace: false, cadenza: true});
    }

    render() {
        let plan = null;

        if (this.state.audace)
            plan = <PlanAddAudace/>;
        if (this.state.linea)
            plan = <PlanAddLinea/>;
        if (this.state.cadenza)
            plan = <PlanAddCadenza/>;

        return (
            <div>
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item">
                            <Link to={appConstants.routes.dashboard}>Visão Geral</Link>
                        </li>
                        <li className="breadcrumb-item">
                            <Link to={appConstants.routes.plans}>Planos</Link>
                        </li>
                        <li className="breadcrumb-item active" aria-current="page">Contratar Plano</li>
                    </ol>
                </nav>
                <div>&nbsp;</div>
                <div className="header-title">
                    <div className="float-left">
                            <span className="title">
                                 Contratar Plano
                            </span>
                        <div className="line"></div>
                    </div>
                </div>
                <div className="clearfix"></div>
                <div className="container-fluid">
                    <div className="row margin-top-30">
                        <div className="widget col-md-12">
                            <div className="row">
                                <div className="col-md-4 col-sm-4 col-12">
                                    <div onClick={this.toggle.bind(this, 'linea')}
                                         className={(this.state.linea) ? 'plan-option text-center active linea' : 'plan-option text-center'}>
                                        <span>
                                            <img className="img-fluid" style={{maxWidth: "120px"}}
                                                 src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/plan-linea.svg"
                                                 alt="linea plan"/>
                                        </span>
                                        <h1 className="margin-top-10">Línea</h1>
                                    </div>
                                </div>
                                <div className="col-md-4 col-sm-4 col-12">
                                    <div onClick={this.toggle.bind(this, 'audace')}
                                         className={(this.state.audace) ? 'plan-option text-center active audace' : 'plan-option text-center'}>
                                        <span>
                                            <img className="img-fluid" style={{maxWidth: "120px"}}
                                                 src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/plan-audace.svg"
                                                 alt="audace plan"/>
                                        </span>
                                        <h1 className="margin-top-10">Audace</h1>
                                    </div>
                                </div>
                                <div className="col-md-4 col-sm-4 col-12">
                                    <div onClick={this.toggle.bind(this, 'cadenza')}
                                         className={(this.state.cadenza) ? 'plan-option text-center active cadenza' : 'plan-option text-center'}>
                                        <span>
                                            <img className="img-fluid" style={{maxWidth: "120px"}}
                                                 src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/plan-cadenza.svg"
                                                 alt="cadenza plan"/>
                                        </span>
                                        <h1 className="margin-top-10">Cadenza</h1>
                                    </div>
                                </div>
                            </div>
                            <div className="container-fluid">
                                {plan}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}


export default PlanAdd;