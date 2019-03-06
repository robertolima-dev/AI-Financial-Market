import React from 'react'

class SimpleButton extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            disabled: this.props.disabled,
            value: this.props.children
        }

        this.newProps = {...this.props};
        delete this.newProps.className;
        delete this.newProps.disabled;
        delete this.newProps.onClick;
    }

    setLoadingOn(loadingLabel) {
        this.setState({...this.state, disabled: true, value: loadingLabel});
    }

    setLoadingOff() {
        this.setState({...this.state, disabled: false, value: this.props.children});
    }

    setDisabled(disabled) {
        this.setState({...this.state, disabled: disabled})
    }

    onClick(e) {
        if (this.state.disabled)
            return;
        else {
            if (this.props.onClick)
                this.props.onClick();
        }

    }

    render() {
        const disabled = (this.state.disabled) ? 'disabled' : '';
        return (
            <button disabled={this.state.disabled} onClick={this.onClick.bind(this)} {...this.newProps}
                    className={this.props.className + ' ' + disabled}>
                {this.state.value}
            </button>
        );
    }
}

export default SimpleButton;