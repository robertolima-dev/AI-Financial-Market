import React from 'react'
import WrapperAbsolute from '../common/WrapperAbsolute'
import {Link} from 'react-router-dom'
import appConstants from '../../constants/AppConstants'


class BaseTemplateAdmin extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <WrapperAbsolute style={{backgroundColor: "#fff"}}>
                {this.props.children}
            </WrapperAbsolute>
        );
    }
}

export default BaseTemplateAdmin;