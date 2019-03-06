import React from 'react'
import InputMask from 'react-input-mask';

class SimpleInputMask extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            value : this.props.value,
            label : this.props.label,
            errorMessage : "",
            required : (this.props.required) ? true : false
        }

        this.newProps = {...this.props};

        delete this.newProps.label;
        delete this.newProps.className;
        delete this.newProps.required;
    }

    setError(message) {
        this.setState({...this.state,errorMessage:message});
    }

    clearError() {
        this.setState({...this.state,errorMessage:""});
    }

    render() {

        let error = (this.state.errorMessage != "") ? <div className="invalid-feedback">
            {this.state.errorMessage}
        </div> : null;
        let errorClass = (this.state.errorMessage != "") ? "is-invalid" : null;
        let labelClass = (this.state.errorMessage != "") ? "red" : null;
        const requiredSymbol = (this.state.required) ? <div className="required-symbol"></div> : null;

        return (
            <div className="form-group">
                <label className={labelClass}>{this.state.label}</label>
                <InputMask className={this.props.className+ " " + errorClass} {...this.newProps}/>
                {requiredSymbol}
                {error}
            </div>
        );
    }
}

export default SimpleInputMask;