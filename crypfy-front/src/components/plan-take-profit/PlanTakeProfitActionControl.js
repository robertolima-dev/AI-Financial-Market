import React from 'react'
import SimpleButton from '../common/SimpleButton'

class PlanTakeProfitActionControl extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            id: this.props.id
        }
    }

    render() {
        if (this.props.status == 'WAITING')
            return (
                <div>
                    <SimpleButton key={'cancel-button-'+this.props.id+this.props.status} onClick={this.props.onCancel.bind(this, this.state.id)} className="btn btn-danger">
                        Cancelar Intenção
                    </SimpleButton>
                </div>
            )
        if(this.props.status == 'CANCELLED' || this.props.status == 'FAILED')
            return(
              <div>
                  <SimpleButton key={'delete-button-'+this.props.id+this.props.status} onClick={this.props.onDelete.bind(this, this.state.id)} className="btn btn-danger">
                      Excluir
                  </SimpleButton>
              </div>
            );
        return null;
    }
}

export default PlanTakeProfitActionControl;