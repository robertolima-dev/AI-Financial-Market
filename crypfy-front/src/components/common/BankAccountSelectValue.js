import React from 'react'

class BankAccountSelectValue extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="bank-account-value">
                <img
                    src={this.props.value.bankImg}
                    alt="Bradesco" className="img-thumbnail rounded-circle"/>
                <span className="value">{this.props.value.label}</span>
            </div>
        );
    }
}

export default BankAccountSelectValue;