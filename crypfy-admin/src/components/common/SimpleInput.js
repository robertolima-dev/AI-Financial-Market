import React from 'react'

export default class SimpleInput extends React.Component{
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

        const error = (this.state.errorMessage != "") ? <div className="invalid-feedback">
            {this.state.errorMessage}
        </div> : null;
        const errorClass = (this.state.errorMessage != "") ? "is-invalid" : null;
        const labelClass = (this.state.errorMessage != "") ? "red" : null;
        const requiredSymbol = (this.state.required) ? <div className="required-symbol"></div> : null;

        return (
            <div className="form-group">
                <label className={labelClass}>{this.state.label}</label>
                <input className={this.props.className+ " " + errorClass} {...this.newProps}/>
                {requiredSymbol}
                {error}
            </div>
        );
    }
}