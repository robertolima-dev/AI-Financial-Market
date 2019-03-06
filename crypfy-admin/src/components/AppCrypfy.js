import React from 'react'
import {connect} from 'react-redux';
import {withRouter} from 'react-router'

class AppCrypfy extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            this.props.children
        )
    }
}

const mapStateToProps = (state, ownProps) => {
    return {}
};

const mapDispatchToProps = (dispatch) => {
    return {}
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(AppCrypfy));