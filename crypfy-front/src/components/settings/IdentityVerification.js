import React from 'react'
import * as userActions from '../../redux/actions/userActions'
import SimpleButton from '../common/SimpleButton'
import Alert from 'react-s-alert'
import {connect} from 'react-redux'

class IdentityVerification extends React.Component {
    constructor(props) {
        super(props);
    }

    chooseFileUpload() {
        this.refs.buttonFileUpload.click();
    }

    onChangeFileupload(e) {
        this.refs.buttonUpload.setLoadingOn("carregando foto..");
        this.props.uploadIdentityVerificationPhoto(e.target.files[0]).then((ret) => {
            Alert.success(ret.message);
            this.refs.buttonUpload.setLoadingOff();
            this.props.loadVerification();
        }).catch((ret) => {
            Alert.error(ret.message);
            this.refs.buttonUpload.setLoadingOff();
        })
    }

    render() {
        switch (this.props.status) {
            case 'UNVERIFIED' :
                return (
                    <div className="alert alert-danger">
                        <div className="row">
                            <div className="col-md-3 col-sm-3 text-center">
                            <span>
                                <img alt="identity unverified"
                                     src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/unverified-identity.png"
                                     width={50}/>
                            </span>
                            </div>
                            <div className="col-md-9 col-sm-9">
                                <strong>
                                    Identidade não verificada
                                </strong>
                                <p className="font-14 margin-top-10">
                                    Para verificar sua identidade envie uma selfie segurando um documento de identificação com foto (ex: CNH, RG).
                                </p>
                                <SimpleButton onClick={this.chooseFileUpload.bind(this)} ref="buttonUpload" className="btn btn-danger">Enviar Foto</SimpleButton>
                                <input ref="buttonFileUpload" className="hide" type="file"
                                       onChange={this.onChangeFileupload.bind(this)}/>
                            </div>
                        </div>
                    </div>
                );
            case 'VERIFIED' :
                return (
                    <div className="alert alert-success">
                        <div className="row">
                            <div className="col-md-3 col-sm-3 text-center">
                            <span>
                                <img alt="identity verified"
                                     src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/verified-identity.png"
                                     width={50}/>
                            </span>
                            </div>
                            <div className="col-md-9 col-sm-9">
                                <strong>
                                    Identidade Verificada
                                </strong>
                                <p className="font-14 margin-top-10">
                                    Identidade verificada e validada
                                </p>
                            </div>
                        </div>
                    </div>
                );
            case 'WAITING_APPROVAL' :
                return (
                    <div className="alert alert-warning">
                        <div className="row">
                            <div className="col-md-3 col-sm-3 text-center">
                            <span>
                                <img alt="account unverified"
                                     src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/unverified-identity.png"
                                     width={50}/>
                            </span>
                            </div>
                            <div className="col-md-9 col-sm-9">
                                <strong>
                                    Identidade em Análise
                                </strong>
                                <p className="font-14 margin-top-10">
                                    Identidade em processo de Análise
                                </p>
                            </div>
                        </div>
                    </div>
                );
            case 'DENIED' :
                return (
                    <div className="alert alert-danger">
                        <div className="row">
                            <div className="col-md-3 col-sm-3 text-center">
                            <span>
                                <img alt="identity not verified"
                                     src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/unverified-identity.png"
                                     width={50}/>
                            </span>
                            </div>
                            <div className="col-md-9 col-sm-9">
                                <strong>
                                    Identidade Negada
                                </strong>
                                <p className="font-14 margin-top-10">
                                    Identidade Negada! Isso pode ter acontecido por várias razões, por favor
                                    envie novamente uma selfie segurando um documento de identificação com foto (ex: CNH, RG).
                                </p>
                                <SimpleButton onClick={this.chooseFileUpload.bind(this)} ref="buttonUpload" className="btn btn-danger">Enviar Foto</SimpleButton>
                                <input ref="buttonFileUpload" className="hide" type="file"
                                       onChange={this.onChangeFileupload.bind(this)}/>
                            </div>
                        </div>
                    </div>
                );
        }
    }
}

const mapStateToProps = (state, ownProps) => {
    return {}
};

const mapDispatchToProps = (dispatch) => {
    return {
        uploadIdentityVerificationPhoto: (file) => dispatch(userActions.uploadIdentityVerificationPhoto(file))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(IdentityVerification);