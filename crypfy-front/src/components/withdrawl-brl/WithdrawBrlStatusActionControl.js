import React from 'react'
import SimpleButton from '../common/SimpleButton'

class WithdrawBrlStatusActionControl extends React.Component {
    constructor(props) {
        super(props);
    }

    onClickCancelRequest() {
        this.props.onClickCancelRequest(this.props.id);
    }

    onClickDeleteRequest() {
        this.props.onClickDeleteRequest(this.props.id);
    }

    render() {
        let actions = null;
        if (this.props.status == 'WAITING_APPROVAL')
            actions = <div>
                <div className="margin-top-10">
                    <SimpleButton key={'cancel'+this.props.id+this.props.status} onClick={this.onClickCancelRequest.bind(this)} className="btn btn-danger">
                        Cancelar Intenção
                    </SimpleButton>
                </div>
            </div>;
        if (this.props.status == 'CANCELLED')
            actions = <div>
                <div className="margin-top-10">
                    <SimpleButton key={'delete'+this.props.id+this.props.status} onClick={this.onClickDeleteRequest.bind(this)} className="btn btn-danger">
                        Excluir
                    </SimpleButton>
                </div>
            </div>;
        if (this.props.status == 'DENIED')
            actions = <div>
                <div className="margin-top-10">
                    <SimpleButton key={'denied'+this.props.id+this.props.status} onClick={this.onClickDeleteRequest.bind(this)} className="btn btn-danger">
                        Excluir
                    </SimpleButton>
                </div>
            </div>;
        return (
            <div>
                {actions}
            </div>
        );
    }
}

export default WithdrawBrlStatusActionControl;