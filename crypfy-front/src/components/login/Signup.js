import React from 'react'
import {connect} from 'react-redux';
import SimpleInput from "../common/SimpleInput";
import SimpleInputMask from "../common/SimpleInputMask";
import SimpleButton from "../common/SimpleButton";
import * as userActions from '../../redux/actions/userActions'
import WrapperAbsolute from '../common/WrapperAbsolute'
import sleeper from '../../helpers/Sleeper'
import {Link} from 'react-router-dom'
import {Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap'
import TermsOfService from './TermsOfService'


class Signup extends React.Component {
    constructor(props) {
        super(props);

        document.body.style.backgroundColor = "#416fba";

        this.state = {
            user: {
                email: '',
                name: '',
                documentType: 'CPF',
                documentNumber: '',
                password: '',
                confirmPassword: '',
                phone: ''
            },
            emailConfirmationBox: false,
            resendEmailConfirmation: false,
            emailConfirmation: null,
            termsOfServiceModal: false
        }
    }

    toggleTermsOfServiceModal() {
        if(this.state.termsOfServiceModal)
            this.setState({...this.state,termsOfServiceModal:false})
        else
            this.setState({...this.state,termsOfServiceModal:true})
    }

    onSubmit(e) {
        e.preventDefault();
        this.refs.buttonSubmit.setLoadingOn('processando...');
        this.clearForm();
        sleeper(1000).then(() => {
            this.props.signup(this.state.user).then(() => {
                this.setState({...this.state,emailConfirmationBox:true});
            }).catch((ret) => {
                ret.response.forEach((e) => {
                    if (e.code == 1000 || e.code == 1001 || e.code == 1002 || e.code == 1003 || e.code == 1004)
                        this.refs.inputEmail.setError(e.message);
                    if (e.code == 2000 || e.code == 2001)
                        this.refs.inputName.setError(e.message);
                    if (e.code == 3000 || e.code == 3003 || e.code == 3002)
                        this.refs.inputPassword.setError(e.message);
                    if (e.code == 3001)
                        this.refs.inputConfirmPassword.setError(e.message);
                    if (e.code == 4001 || e.code == 4002)
                        this.refs.inputDocumentNumber.setError(e.message);
                    if (e.code == 9000)
                        this.refs.inputEmail.setError(e.message);
                    if (e.code == 5001)
                        this.refs.inputPhone.setError(e.message);
                    if(e.code == 1005) {
                        this.refs.inputEmail.setError(<span>{e.message}<a href="#" onClick={this.resendEmailConfirmation.bind(this)}> Reenviar Email para {this.state.user.email}</a></span>);
                        this.setState({...this.state,resendEmailConfirmation:true,emailConfirmation:this.state.user.email});
                    }
                })
                this.refs.buttonSubmit.setLoadingOff();
            })
        })
    }

    resendEmailConfirmation(e) {
        e.preventDefault();
        userActions.resendEmailConfirmation(this.state.emailConfirmation).then(() => {
            this.refs.inputEmail.clearError();
            this.refs.inputEmail.setSuccess("Email reenviado com sucesso!");
        }).catch((e) => {
            this.refs.inputEmail.setError(e.message);
        })
    }

    clearForm() {
        this.refs.inputEmail.clearError();
        this.refs.inputName.clearError();
        this.refs.inputDocumentNumber.clearError();
        this.refs.inputPassword.clearError();
        this.refs.inputConfirmPassword.clearError();
        this.refs.inputPhone.clearError();
    }

    render() {

        const emailConfirmation = <div className="text-center signup-confirmation-box">
            <span>
                <img width={150} alt="email confirmation" src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/email-confirmation.svg"/>
            </span>
            <h5 className="margin-top-30">Conta Cadastrada com sucesso!</h5>
            <h3 className="description">
                Sua conta crypfy foi cadastrada com sucesso, foi enviado um <strong>email</strong> de confirmação para <strong>{this.state.user.email}</strong>
            </h3>
            <div className="margin-top-30">
                <Link to="/">Voltar para o Login</Link>
            </div>
        </div>

        const formSignup = <form onSubmit={this.onSubmit.bind(this)}>
            <div className="row">
                <div className="col-md-12">
                    <SimpleInput value={this.state.user.email} onChange={(e) => {
                        this.setState({...this.state,user:{...this.state.user,email:e.target.value}})
                    }} required="true" ref="inputEmail" className="form-control" type="text"
                                 label="Email"
                                 placeholder="Digite seu email"/>
                </div>
            </div>
            <div className="row">
                <div className="col-md-12">
                    <SimpleInput value={this.state.user.name} onChange={(e) => {
                        this.setState({...this.state, user:{...this.state.user,name:e.target.value}})
                    }} required="true" ref="inputName" className="form-control" type="text"
                                 label="Nome"
                                 placeholder="Digite seu nome"/>
                </div>
            </div>
            <div className="row">
                <div className="col-md-12">
                    <SimpleInputMask value={this.state.user.documentNumber} onChange={(e) => {
                        this.setState({...this.state, user:{...this.state.user,documentNumber:e.target.value}})
                    }} required="true" ref="inputDocumentNumber" mask="999.999.999.99"
                                     className="form-control" type="text"
                                     label="CPF" placeholder="Digite seu CPF"/>
                </div>
            </div>
            <div className="row">
                <div className="col-md-12">
                    <SimpleInputMask value={this.state.user.phone} onChange={(e) => {
                        this.setState({...this.state, user:{...this.state.user,phone:e.target.value.replaceAll('_', '')}})
                    }} ref="inputPhone" mask="(99) 9999-99999"
                                     className="form-control" type="text"
                                     label="Telefone" placeholder="Digite seu Telefone"/>
                </div>
            </div>
            <div className="row">
                <div className="col-md-12">
                    <SimpleInput value={this.state.user.password} onChange={(e) => {
                        this.setState({...this.state, user:{...this.state.user,password:e.target.value}})
                    }} required="true" ref="inputPassword" className="form-control"
                                 type="password" label="Senha"
                                 placeholder="Digite sua senha"/>
                </div>
            </div>
            <div className="row">
                <div className="col-md-12">
                    <SimpleInput value={this.state.user.confirmPassword} onChange={(e) => {
                        this.setState({...this.state, user:{...this.state.user,confirmPassword:e.target.value}})
                    }} required="true" ref="inputConfirmPassword" className="form-control"
                                 type="password" label="Confirmar Senha"
                                 placeholder="Confirme sua senha"/>
                </div>
            </div>
            <div className="row">
                <div className="col-md-12">
                    <span className="font-13">Ao registrar-se você concorda com os <a href="#" onClick={(e) => {e.preventDefault();this.toggleTermsOfServiceModal()}}>Termos de Serviço</a></span>
                </div>
            </div>
            <div className="row margin-top-20">
                <div className="col-md-12">
                    <SimpleButton ref="buttonSubmit" type="submit"
                                  className="btn btn-primary btn-lg btn-block margin-top-20">
                        Criar Conta
                    </SimpleButton>
                </div>
            </div>
        </form>

        const currentBox = (this.state.emailConfirmationBox) ? emailConfirmation : formSignup;

        return (
            <WrapperAbsolute style={{backgroundColor: "#416fba", padding: "40px 0 40px 0"}} className="signup-wrapper">
                <div>
                    <div className="signup-message text-center">
                        <h1 className="first-message">
                            Futuro do seu investimento <strong>crypto</strong>
                        </h1>
                        <h3 className="second-message">
                            Comece aqui o gerenciamento de seus ativos de forma segura e inteligente
                        </h3>
                    </div>
                    <div className="signup-box">
                        {currentBox}
                    </div>
                </div>
                <Modal isOpen={this.state.termsOfServiceModal}
                       toggle={this.toggleTermsOfServiceModal.bind(this)}>
                    <ModalHeader>
                        Termo de Serviço Crypfy
                    </ModalHeader>
                    <ModalBody style={{height:"400px",overflow:"auto"}}>
                        <TermsOfService/>
                    </ModalBody>
                    <ModalFooter>
                        <SimpleButton onClick={this.toggleTermsOfServiceModal.bind(this)}
                                      className="btn btn-primary">
                            Ok, Entendi
                        </SimpleButton>
                    </ModalFooter>
                </Modal>
            </WrapperAbsolute>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    return {}
};

const mapDispatchToProps = (dispatch) => {
    return {
        signup: (signup) => dispatch(userActions.signup(signup))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Signup);