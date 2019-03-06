import React from 'react'

class SimpleButton extends React.Component{
    constructor(props) {
        super(props);

        this.state = {
            loading:false,
            loadingLabel : ''
        }

        this.newProps = {...this.props};
        delete this.newProps.className;
    }

    setLoadingOn(loadingLabel) {
        this.setState({...this.state,loading:true,loadingLabel:loadingLabel});
    }

    setLoadingOff() {
        this.setState({...this.state,loading:false,loadingLabel:''});
    }

    render() {

        let value = (this.state.loading) ? <span><i className="fa fa-circle-o-notch fa-spin fa-fw"></i> {this.state.loadingLabel}</span> : this.props.children;
        let disabled = (this.state.loading) ? 'disabled' : '';

        return(
            <button {...this.newProps} className={this.props.className + ' ' + disabled}>
                {value}
            </button>
        );
    }
}

export default SimpleButton;