import React from 'react'
import InputMask from 'react-input-mask';

class SimpleInputMask extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            value : this.props.value,
            label : this.props.label,
            errorMessage : "",
            required : (this.props.required) ? true : false,
            disabled: (this.props.disabled) ? true : false
        }

        this.newProps = {...this.props};

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

    onChange(e) {
        this.setState({...this.state,value:e.target.value});
        this.props.onChange(e);
    }

    setValue(value) {
        this.setState({...this.state,value:value});
    }

    setDisable(disable) {
        this.setState({...this.state,disabled:disable})
    }

    render() {

        let error = (this.state.errorMessage != "") ? <div className="invalid-feedback">
            {this.state.errorMessage}
        </div> : null;
        let errorClass = (this.state.errorMessage != "") ? "is-invalid" : null;
        let labelClass = (this.state.errorMessage != "") ? "red" : null;

        return (
            <div className="form-group">
                <label className={labelClass}>{this.state.label} {(this.state.required) ? <span className="blue">*</span> : ''}</label>
                <InputMask disabled={this.state.disabled} value={this.state.value} onChange={this.onChange.bind(this)} className={this.props.className+ " " + errorClass} {...this.newProps}/>
                {error}
            </div>
        );
    }
}

export default SimpleInputMask;