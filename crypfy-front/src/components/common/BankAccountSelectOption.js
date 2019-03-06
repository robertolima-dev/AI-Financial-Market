import React from 'react'

class BankAccountSelectOption extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            label : this.props.option.label
        }
    }

    handleMouseDown (event) {
        event.preventDefault();
        event.stopPropagation();
        this.props.onSelect(this.props.option, event);
    }

    handleMouseEnter (event) {
        this.props.onFocus(this.props.option, event);
    }

    handleMouseMove (event) {
        if (this.props.isFocused) return;
        this.props.onFocus(this.props.option, event);
    }

    render() {
        return (
           <div onMouseDown={this.handleMouseDown.bind(this)} onMouseEnter={this.handleMouseEnter.bind(this)} onMouseMove={this.handleMouseMove.bind(this)} className={this.props.className + " bank-account-option"}>
               <img
                    src={this.props.option.bankImg}
                    alt="Bradesco" className="img-thumbnail rounded-circle"/>
               <span className="value">{this.props.option.label}</span>
           </div>
        );
    }
}

export default BankAccountSelectOption;