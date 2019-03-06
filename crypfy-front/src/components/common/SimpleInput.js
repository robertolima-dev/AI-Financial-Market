import React from 'react'

export default class SimpleInput extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: this.props.value,
            label: this.props.label,
            errorMessage: "",
            successMessage: "",
            required: (this.props.required) ? true : false,
            disabled: (this.props.disabled) ? true : false
        }

        this.newProps = {...this.props};

        delete this.newProps.onlyNumbers;
        delete this.newProps.label;
        delete this.newProps.className;
        delete this.newProps.required;
        delete this.newProps.onChange;
        delete this.newProps.value;
        delete this.newProps.disabled;
    }

    setError(message) {
        this.setState({...this.state, errorMessage: message});
    }

    setSuccess(message) {
        this.setState({...this.state, successMessage: message});
    }

    clearError() {
        this.setState({...this.state, errorMessage: ""});
    }

    onChange(e) {
        if(this.props.onlyNumbers && isNaN(e.target.value))
            return;
        this.setState({...this.state, value: e.target.value});
        this.props.onChange(e)
    }

    setValue(value) {
        this.setState({...this.state, value: value});
    }

    setDisable(disable) {
        this.setState({...this.state, disabled: disable})
    }

    render() {

        const error = (this.state.errorMessage != "") ? <div className="invalid-feedback">
            {this.state.errorMessage}
        </div> : null;

        const success = (this.state.successMessage != "") ?
            <div className="valid-feedback">{this.state.successMessage}</div> : null;

        let errorClass = (this.state.errorMessage != "") ? "is-invalid" : null;
        errorClass = (this.state.successMessage != "") ? errorClass + " is-valid" : errorClass;

        let labelClass = (this.state.errorMessage != "") ? "red" : "";
        labelClass = (this.state.successMessage != "") ? labelClass + " green" : labelClass;

        return (
            <div className="form-group">
                <label className={labelClass}>{this.state.label} {(this.state.required) ?
                    <span className="blue">*</span> : ''}</label>
                <input disabled={this.state.disabled} value={this.state.value} onChange={this.onChange.bind(this)}
                       className={this.props.className + " " + errorClass} {...this.newProps}/>
                {error}
                {success}
            </div>
        );
    }
}