import React from 'react'

class PlanTakeProfitStatus extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        if(this.props.status == 'WAITING')
            return (<span className="badge badge-info">AGUARDANDO PROCESSAMENTO</span>);
        if(this.props.status == 'PROCESSING')
            return (<span className="badge badge-primary">PROCESSANDO</span>);
        if(this.props.status == 'DONE')
            return (<span className="badge badge-success">CONFIRMADO</span>);
        if(this.props.status == 'FAILED')
            return (<span className="badge badge-danger">FALHA</span>);
        if(this.props.status == 'CANCELLED')
            return (<span className="badge badge-danger">CANCELADO</span>);
    }
}

export default PlanTakeProfitStatus;