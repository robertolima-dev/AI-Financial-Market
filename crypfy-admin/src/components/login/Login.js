import React from 'react'
import SimpleInput from '../common/SimpleInput'
import SimpleButton from '../common/SimpleButton'
import {Link} from 'react-router-dom'
import {connect} from 'react-redux';
import {push} from 'react-router-redux'
import sleeper from '../../helpers/Sleeper'
import WrapperAbsolute from '../common/WrapperAbsolute'
import appConstants from "../../constants/AppConstants";

class Login extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <WrapperAbsolute style={{"backgroundColor": "#416fba"}} className="login-wrapper">
                <div>sasasa</div>dssddsddds
            </WrapperAbsolute>
        );
    }
}

export default Login;