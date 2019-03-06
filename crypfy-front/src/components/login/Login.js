import React from 'react'
import SimpleInput from '../common/SimpleInput'
import SimpleButton from '../common/SimpleButton'
import {Link} from 'react-router-dom'
import {connect} from 'react-redux';
import {push} from 'react-router-redux'
import * as userActions from '../../redux/actions/userActions'
import sleeper from '../../helpers/Sleeper'
import WrapperAbsolute from '../common/WrapperAbsolute'
import appConstants from "../../constants/AppConstants";
import MetaTags from 'react-meta-tags';

class Login extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            email: "",
            password: "",
            resendEmailConfirmation: false,
            emailConfirmation: null
        }
    }

    resendEmailConfirmation(e) {
        e.preventDefault();
        this.refs.inputEmail.setError(<span style={{color:"#999"}}>enviando..</span>);
        userActions.resendEmailConfirmation(this.state.emailConfirmation).then(() => {
            this.refs.inputEmail.clearError();
            this.refs.inputEmail.setSuccess("Email reenviado com sucesso!");
        }).catch((e) => {
            this.refs.inputEmail.setError(e.message);
        })
    }

    onSubmit(e) {
        e.preventDefault();
        this.refs.buttonLogin.setLoadingOn('processando...');
        sleeper(1000).then(() => {
            this.props.login(this.state.email, this.state.password).then((resolve) => {
                this.props.toDashboard();
            }).catch((error) => {
                //clear field errors
                this.refs.inputEmail.clearError();
                this.refs.inputPassword.clearError();
                //clear resend email confirmation message
                this.setState({...this.state,resendEmailConfirmation:false},() => {
                    error.response.forEach((e) => {
                        if (e.code == 1000 || e.code == 1003 || e.code == 9000)
                            this.refs.inputEmail.setError(e.message);
                        if (e.code == 1001)
                            this.refs.inputPassword.setError(e.message);
                        if(e.code == 1004) {
                            this.refs.inputEmail.setError(<span>{e.message}<a href="#" onClick={this.resendEmailConfirmation.bind(this)}> Reenviar Email para {this.state.email}</a></span>);
                            this.setState({...this.state,resendEmailConfirmation:true,emailConfirmation:this.state.email});
                        }
                    })
                    this.refs.buttonLogin.setLoadingOff();
                })
            })
        })
    }

    render() {
        return (
            <WrapperAbsolute style={{"position":"absolute","height": "100%","width":"100%"}} className="login-wrapper">
                <MetaTags>
                    <title>Crypfy Login - Plataforma de Investimento e Gerenciamento de Criptomoedas </title>
                    <meta name="description" content="Login Crypfy" />
                </MetaTags>
                <div>
                    <img className="logo crypfy"
                         src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/full-logo-crypfy.svg"/>
                    <div className="login-box">
                        <form onSubmit={this.onSubmit.bind(this)}>
                            <div className="row">
                                <div className="col-md-12">
                                    <SimpleInput value={this.state.email} ref="inputEmail" onChange={(e) => {
                                        this.setState({...this.state, email: e.target.value})
                                    }} className="form-control" label="Email" placeholder="Digite seu Email"
                                                 type="text"/>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-md-12">
                                    <SimpleInput value={this.state.password} ref="inputPassword" onChange={(e) => {
                                        this.setState({...this.state, password: e.target.value})
                                    }} className="form-control" label="Senha" placeholder="Digite sua senha"
                                                 type="password"
                                    />
                                </div>
                            </div>
                            <div className="row margin-top-20">
                                <div className="col-md-12">
                                    <SimpleButton ref="buttonLogin" type="submit"
                                                  className="btn btn-primary btn-lg btn-block">
                                        Login
                                    </SimpleButton>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div className="extra-links">
                        <Link to={appConstants.routes.signup}>Não possui uma conta ?</Link>
                    </div>
                </div>
            </WrapperAbsolute>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    return {
        session:state.session
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        toSignup: () => dispatch(appConstants.routes.signup),
        toDashboard: () => dispatch(push(appConstants.routes.dashboard)),
        login: (email, pass) => dispatch(userActions.login(email, pass))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Login);