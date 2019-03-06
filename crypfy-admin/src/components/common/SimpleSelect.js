import React from 'react'
import Select from 'react-select';

class SimpleSelect extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            errorMessage : "",
            label: this.props.label,
            value : this.props.value
        }

        this.newProps = {...this.props};

        delete this.newProps.label;
        delete this.newProps.value;
        delete this.newProps.onChange;
    }

    setError(message) {
        this.setState({...this.state,errorMessage:message});
    }

    clearError() {
        this.setState({...this.state,errorMessage:""});
    }

    onChange(value) {
        this.setState({...this.state,value:value});
        this.props.onChange(value);
    }

    render() {
        return (
            <div className="form-group">
                <label>{this.state.label}</label>
                <Select onChange={this.onChange.bind(this)} value={this.state.value} {...this.newProps}/>
            </div>
        )
    }
}

export default SimpleSelect;