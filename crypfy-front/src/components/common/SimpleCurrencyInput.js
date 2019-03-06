import React from 'react'
import CurrencyInput from 'react-currency-input';


class SimpleCurrencyInput extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            value : (this.props.value) ? this.props.value : null,
            label : this.props.label,
            errorMessage : "",
            required : (this.props.required) ? true : false,
            disabled : (this.props.disabled) ? true : false
        }

        this.newProps = {...this.props};

        delete this.newProps.id;
        delete this.newProps.label;
        delete this.newProps.className;
        delete this.newProps.required;
        delete this.newProps.onChange;
        delete this.newProps.value;
        delete this.newProps.disabled;
    }

    setError(message) {
        this.setState({...this.state,errorMessage:message});
    }

    clearError() {
        this.setState({...this.state,errorMessage:""});
    }

    setValue(value) {
        let promise = new Promise((resolve,reject) => {
            this.setState({...this.state, value: value},() => {
                resolve();
            });
        })
        return promise;
    }

    clearErrorValue(value) {
        this.setState({...this.state, value: value,errorMessage:""});
    }

    onChange(e) {
        this.setState({...this.state,value:e.target.value});
        this.props.onChange(e.target.value);
    }

    setDisabled(disabled) {
        this.setState({...this.state,disabled:disabled});
    }

    render() {
        const error = (this.state.errorMessage != "") ? <div style={{display:"block"}} className="invalid-feedback">
            {this.state.errorMessage}
        </div> : null;
        const errorClass = (this.state.errorMessage != "") ? "is-invalid" : "";
        const labelClass = (this.state.errorMessage != "") ? "red" : "";
        return (
            <div className="form-group">
                <label className={labelClass}>{this.state.label} {(this.state.required) ?
                    <span className="blue">*</span> : ''}</label>
                <CurrencyInput disabled={this.state.disabled} decimalSeparator="," thousandSeparator="." value={this.state.value} onChangeEvent={this.onChange.bind(this)} className={this.props.className+ " " + errorClass} {...this.newProps} />
                {error}
            </div>
        );
    }
}

export default SimpleCurrencyInput;