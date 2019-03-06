import React from 'react'
import SimpleInput from '../common/SimpleInput'
import SimpleSelect from '../common/SimpleSelect'
import * as userActions from '../../redux/actions/userActions'
import Alert from 'react-s-alert'
import sleeper from '../../helpers/Sleeper'
import SimpleButton from '../common/SimpleButton'
import SimpleInputMask from '../common/SimpleInputMask'
import DocumentVerification from './DocumentVerification'
import IdentityVerification from './IdentityVerification'

class Profile extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            currentPassword: "",
            newPassword: "",
            confirmNewPassword: "",
            render: false,
            user: null,
            disableDocumentNumber: false
        }
    }

    componentWillMount() {
        this.loadInfo();
    }

    loadInfo() {
        userActions.getInfo().then((ret) => {
            let disableDocumentNumber = false;
            if (ret.response.documentVerificationStatus == 'VERIFIED' || ret.response.documentVerificationStatus == 'WAITING_APPROVAL')
                disableDocumentNumber = true;

            if (ret.response.documentVerificationStatus == 'UNVERIFIED' || ret.response.identityVerificationStatus == 'UNVERIFIED' || ret.response.documentVerificationStatus == 'DENIED' || ret.response.identityVerificationStatus == 'DENIED')
                this.props.profileWarning(true);
            else
                this.props.profileWarning(false);

            this.setState({
                ...this.state,
                user: ret.response,
                render: true,
                disableDocumentNumber: disableDocumentNumber
            });
            
            this.props.setAvatar(ret.response.avatar);
        });
    }

    loadVerifications() {
        userActions.getInfo().then((ret) => {
            if (ret.response.documentVerificationStatus == 'VERIFIED' || ret.response.documentVerificationStatus == 'WAITING_APPROVAL') {
                this.refs.inputDocumentType.setDisable(true);
                this.refs.inputDocumentNumber.setDisable(true);
            }
            else {
                this.refs.inputDocumentNumber.setDisable(false);
                this.refs.inputDocumentType.setDisable(false);
            }

            if (ret.response.documentVerificationStatus == 'UNVERIFIED' || ret.response.identityVerificationStatus == 'UNVERIFIED' || ret.response.documentVerificationStatus == 'DENIED' || ret.response.identityVerificationStatus == 'DENIED')
                this.props.profileWarning(true);
            else
                this.props.profileWarning(false);

            this.setState({
                ...this.state,
                user: {
                    ...this.state.user,
                    identityVerificationStatus: ret.response.identityVerificationStatus,
                    documentVerificationStatus: ret.response.documentVerificationStatus
                }
            });
        });
    }

    redefinePasswordOnSubmit(e) {
        e.preventDefault();
        this.clearErrorsRedefinePasswordForm();
        this.refs.buttonRedefinePassword.setLoadingOn("processando..");
        sleeper(1000).then(() => {
            userActions.redefinePassword(this.state.currentPassword, this.state.newPassword, this.state.confirmNewPassword).then((ret) => {
                Alert.success(ret.message);
                this.clearRedefinePasswordForm();
                this.refs.buttonRedefinePassword.setLoadingOff();
            }).catch((ret) => {
                Alert.error(ret.message);
                this.refs.buttonRedefinePassword.setLoadingOff();
                ret.response.forEach((e) => {
                    if (e.code == 3004 || e.code == 3006)
                        this.refs.inputCurrentPassword.setError(e.message);
                    if (e.code == 3002 || e.code == 3003 || e.code == 3005 || e.code == 3000)
                        this.refs.inputNewPassword.setError(e.message);
                    if (e.code == 3002 || e.code == 3001)
                        this.refs.inputConfirmNewPassword.setError(e.message);
                })
            })
        })
    }

    updateProfileOnSubmit(e) {
        e.preventDefault();
        this.clearUpdateProfileForm();
        this.refs.inputButtonUpdateProfile.setLoadingOn("processando..");
        sleeper(1000).then(() => {
            userActions.updateProfile(this.state.user).then((ret) => {
                Alert.success(ret.message);
                this.refs.inputButtonUpdateProfile.setLoadingOff();
            }).catch((ret) => {
                this.refs.inputButtonUpdateProfile.setLoadingOff();
                Alert.error(ret.message);
                ret.response.forEach((e) => {
                    if (e.code == 2000 || e.code == 2001)
                        this.refs.inputName.setError(e.message);
                    if (e.code == 4001 || e.code == 4002)
                        this.refs.inputDocumentNumber.setError(e.message);
                    if (e.code == 5001)
                        this.refs.inputPhone.setError(e.message);
                })
            })
        })
    }

    clearErrorsRedefinePasswordForm() {
        this.refs.inputCurrentPassword.clearError();
        this.refs.inputNewPassword.clearError();
        this.refs.inputConfirmNewPassword.clearError();
    }

    clearRedefinePasswordForm() {
        this.setState({
            ...this.state,
            currentPassword: "",
            newPassword: "",
            confirmNewPassword: ""
        });

        this.refs.inputCurrentPassword.setValue("");
        this.refs.inputNewPassword.setValue("");
        this.refs.inputConfirmNewPassword.setValue("");
    }

    clearUpdateProfileForm() {
        this.refs.inputName.clearError();
        this.refs.inputDocumentNumber.clearError();
        this.refs.inputPhone.clearError();
    }

    onChangeDocumentType(val) {
        let newValue = (val != null) ? val.value : null;
        this.setState({...this.state, user: {...this.state.user, documentType: newValue, documentNumber: ''}}, () => {
            this.refs.inputDocumentNumber.setValue('');
        })
    }

    render() {

        let documentNumber = null;

        // input document number control
        if (this.state.user)
            documentNumber = (this.state.user && this.state.user.documentType == 'CPF') ?
                <SimpleInputMask
                    disabled={this.state.disableDocumentNumber}
                    key="inputDocumentNumberCPF" required={true} onChange={(e) => {
                    this.setState({
                        ...this.state,
                        user: {
                            ...this.state.user,
                            documentNumber: e.target.value.replaceAll('.', '').replaceAll('-', '').replaceAll('_', '')
                        }
                    })
                }} value={this.state.user.documentNumber} ref="inputDocumentNumber"
                    mask="999.999.999-99"
                    className="form-control test" type="text"
                    label="Documento CPF" placeholder="Digite seu Documento"/> :
                <SimpleInputMask
                    disabled={this.state.disableDocumentNumber}
                    key="inputDocumentNumberCNPJ" required={true} onChange={(e) => {
                    this.setState({
                        ...this.state,
                        user: {
                            ...this.state.user,
                            documentNumber: e.target.value.replaceAll('.', '').replaceAll('-', '').replaceAll('_', '').replaceAll('/', '')
                        }
                    })
                }} value={this.state.user.documentNumber} ref="inputDocumentNumber"
                    mask="99.999.999/9999-99"
                    className="form-control" type="text"
                    label="Documento CNPJ" placeholder="Digite seu Documento"/>


        if (this.state.render)
            return (
                <div>
                    <div className="row">
                        <div className="col-md-12 margin-top-20">
                            <div className="header-title">
                                    <span className="title">
                                        Meu Perfil
                                        <small>Informações de perfil</small>
                                    </span>
                                <div className="line"></div>
                            </div>
                        </div>
                    </div>
                    <form onSubmit={this.updateProfileOnSubmit.bind(this)}>
                        <div className="row margin-top-20">
                            <div className="col-md-5">
                                <SimpleInput ref="inputEmail" required={true} disabled={true}
                                             value={this.state.user.email}
                                             className="form-control"
                                             label="Email"
                                             type="text"/>
                            </div>

                            <div className="col-md-7">
                                <SimpleInput ref="inputName" required={true} onChange={(e) => {
                                    this.setState({...this.state, user: {...this.state.user, name: e.target.value}})
                                }} placeholder="Digite seu Nome" value={this.state.user.name}
                                             className="form-control"
                                             label="Nome" type="text"/>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-md-4">
                                <SimpleSelect
                                    disabled={this.state.user.documentVerificationStatus == 'VERIFIED' || this.state.user.documentVerificationStatus == 'WAITING_APPROVAL'}
                                    ref="inputDocumentType" required={true}
                                    onChange={this.onChangeDocumentType.bind(this)}
                                    value={this.state.user.documentType}
                                    options={[{value: 'CPF', label: "CPF"}]} label="Tipo Documento"/>
                            </div>
                            <div className="col-md-4">
                                {documentNumber}
                            </div>
                            <div className="col-md-4">
                                <SimpleInputMask onChange={(e) => {
                                    this.setState({...this.state, user: {...this.state.user, phone: e.target.value.replaceAll('_', '')}})
                                }} value={this.state.user.phone} ref="inputPhone" mask="(99) 9999-99999"
                                                 className="form-control" type="text"
                                                 label="Telefone" placeholder="Digite seu Telefone"/>
                            </div>
                        </div>
                        <div className="row margin-top-10">
                            <div className="col-md-12">
                                <SimpleButton ref="inputButtonUpdateProfile" type="submit"
                                              className="btn btn-primary float-right">Atualizar Perfil</SimpleButton>
                            </div>
                        </div>
                    </form>
                    <div className="row">
                        <div className="col-md-12 margin-top-10">
                            <div className="header-title">
                                            <span className="title">
                                                Verificações
                                                <small>Verificações de conta</small>
                                            </span>
                                <div className="line"></div>
                            </div>
                        </div>
                    </div>
                    <div className="row margin-top-20">
                        <div className="col-md-6">
                            <IdentityVerification loadVerification={this.loadVerifications.bind(this)}
                                                  status={this.state.user.identityVerificationStatus}/>
                        </div>
                        <div className="col-md-6">
                            <DocumentVerification loadVerification={this.loadVerifications.bind(this)}
                                                  status={this.state.user.documentVerificationStatus}/>
                        </div>
                    </div>
                    <div className="row margin-top-10">
                        <div className="col-md-12">
                            <div className="header-title">
                                <span className="title">
                                    Trocar Senha
                                </span>
                                <div className="line"></div>
                            </div>
                        </div>
                    </div>
                    <form onSubmit={this.redefinePasswordOnSubmit.bind(this)}>
                        <div className="row margin-top-20">
                            <div className="col-md-4">
                                <SimpleInput value={this.state.currentPassword} onChange={(e) => {
                                    this.setState({...this.state, currentPassword: e.target.value})
                                }} ref="inputCurrentPassword" className="form-control"
                                             label="Senha Atual" type="password"/>
                            </div>
                            <div className="col-md-4">
                                <SimpleInput value={this.state.newPassword} onChange={(e) => {
                                    this.setState({...this.state, newPassword: e.target.value})
                                }} ref="inputNewPassword" className="form-control"
                                             label="Nova Senha" type="password"/>
                            </div>
                            <div className="col-md-4">
                                <SimpleInput value={this.state.confirmNewPassword} onChange={(e) => {
                                    this.setState({...this.state, confirmNewPassword: e.target.value})
                                }} ref="inputConfirmNewPassword" className="form-control"
                                             label="Confirmar Nova Senha" type="password"/>
                            </div>
                        </div>
                        <div className="row margin-top-10">
                            <div className="col-md-12">
                                <SimpleButton ref="buttonRedefinePassword" type="submit"
                                              className="btn btn-primary float-right">
                                    Trocar Senha
                                </SimpleButton>
                            </div>
                        </div>
                        <div className="margin-top-20"></div>
                    </form>
                </div>
            );
        else
            return null;
    }
}

export default Profile;