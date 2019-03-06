import React from 'react'
import {connect} from 'react-redux';
import {withRouter} from 'react-router'
import BaseTemplateAdmin from './base/BaseTemplateAdmin'

class AppCrypfy extends React.Component {
    constructor(props) {
        super(props);

        this.baseTemplate = null;
        this.props.history.listen((location, action) => {
            if(this.baseTemplate != null)
                this.baseTemplate.getWrappedInstance().setActiveMenu(location.pathname)
        });

        this.nonBaseTemplateContent = ['/','/signup','/email-confirmation'];
    }

    render() {
        if (this.nonBaseTemplateContent.indexOf(this.props.location.pathname) > -1)
            return <div>{this.props.children}</div>
        else
            return (
                <BaseTemplateAdmin activeMenu={this.props.location.pathname} ref={ref => {
                    this.baseTemplate = ref
                }}>
                    <div>
                        {this.props.children}
                    </div>
                </BaseTemplateAdmin>
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