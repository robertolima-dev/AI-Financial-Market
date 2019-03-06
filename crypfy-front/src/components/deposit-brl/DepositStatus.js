import React from 'react'
import StringUtils from '../../helpers/StringUtils'

class DepositStatus extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        if(this.props.status == 'WAITING_PHOTO_UPLOAD')
            return <span className="badge badge-info">
                            {StringUtils.getHumanNameDepositWithdrawStatus(this.props.status)}
                        </span>
        if(this.props.status == 'CANCELLED')
            return <span className="badge badge-danger">
                            {StringUtils.getHumanNameDepositWithdrawStatus(this.props.status)}
                        </span>
        if(this.props.status == 'CONFIRMED')
            return <span className="badge badge-success">
                            {StringUtils.getHumanNameDepositWithdrawStatus(this.props.status)}
                        </span>
        if(this.props.status == "DENIED")
            return <span className="badge badge-danger">
                            {StringUtils.getHumanNameDepositWithdrawStatus(this.props.status)}
                        </span>
        if(this.props.status == 'WAITING_APPROVAL')
            return <span className="badge badge-primary">
                            {StringUtils.getHumanNameDepositWithdrawStatus(this.props.status)}
                        </span>
    }
}

export default DepositStatus;