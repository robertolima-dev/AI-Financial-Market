import React from 'react'
import Select from 'react-select';

class SimpleSelect extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            errorMessage: "",
            label: this.props.label,
            value: this.props.value,
            required: (this.props.required) ? true : false,
            disabled: (this.props.disabled) ? true : false,
            options: this.props.options
        }

        this.newProps = {...this.props};

        delete this.newProps.required;
        delete this.newProps.options;
        delete this.newProps.label;
        delete this.newProps.value;
        delete this.newProps.onChange;
        delete this.newProps.className;
        delete this.newProps.disabled;
    }

    setError(message) {
        this.setState({...this.state, errorMessage: message});
    }

    clearError() {
        this.setState({...this.state, errorMessage: ""});
    }

    onChange(value) {
        this.setState({...this.state, value: value});
        this.props.onChange(value);
    }

    setDisable(disable) {
        this.setState({...this.state,disabled:disable})
    }

    onClickAdditionalLink(e) {
        e.preventDefault();
        this.props.onClickAdditionalLink();
    }

    setValue(newValue) {
        this.setState({...this.state,value:newValue});
    }

    addNewItem(newItem) {
        let newOptions = this.state.options;
        newOptions.push(newItem);
        this.setState({...this.state,options:newOptions});
    }

    render() {

        const error = (this.state.errorMessage != "") ? <div style={{display: "block"}} className="invalid-feedback">
            {this.state.errorMessage}
        </div> : null;
        const errorClass = (this.state.errorMessage != "") ? "is-invalid" : "";
        const labelClass = (this.state.errorMessage != "") ? "red" : "";
        const additionalLink = (this.props.additionalLink) ? <a onClick={this.onClickAdditionalLink.bind(this)} className="margin-left-5" href="#">{this.props.additionalLink}</a> : null;
        return (
            <div className="form-group">
                <label className={labelClass}>{this.state.label} {(this.state.required) ?
                    <span className="blue">*</span> : ''}
                    {additionalLink}
                </label>
                <Select options={this.state.options} disabled={this.state.disabled} className={(this.props.className) ? this.props.className + " " + errorClass : errorClass}
                        onChange={this.onChange.bind(this)} value={this.state.value} {...this.newProps}/>
                {error}
            </div>
        )
    }
}

export default SimpleSelect;