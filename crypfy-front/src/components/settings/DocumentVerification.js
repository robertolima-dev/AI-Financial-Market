import React from 'react'
import SimpleButton from '../common/SimpleButton'
import Alert from 'react-s-alert'
import * as userActions from '../../redux/actions/userActions'
import {connect} from 'react-redux'

class DocumentVerification extends React.Component {
    constructor(props) {
        super(props);
    }

    chooseFileUpload() {
        this.refs.buttonFileUpload.click();
    }

    onChangeFileupload(e) {
        this.refs.buttonUpload.setLoadingOn("carregando foto..");
        this.props.uploadDocumentVerificationPhoto(e.target.files[0]).then((ret) => {
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
                                <img alt="account not verified"
                                     src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/unverified-document.png"
                                     width={50}/>
                            </span>
                            </div>
                            <div className="col-md-9 col-sm-9">
                                <strong>
                                    Documento não verificado
                                </strong>
                                <p className="font-14 margin-top-10">
                                    Para verificar seu documento envie uma foto de um documento que
                                    contenha seu CPF (ex: RG, CPF).
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
                                <img alt="account verified"
                                     src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/verified-document.png"
                                     width={50}/>
                            </span>
                            </div>
                            <div className="col-md-9 col-sm-9">
                                <strong>
                                    Documento Verificado
                                </strong>
                                <p className="font-14 margin-top-10">
                                    Documento verificado e validado
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
                                <img alt="account verified"
                                     src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/unverified-document.png"
                                     width={50}/>
                            </span>
                            </div>
                            <div className="col-md-9 col-sm-9">
                                <strong>
                                    Documento em Análise
                                </strong>
                                <p className="font-14 margin-top-10">
                                    Documento em processo de análise
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
                                <img alt="account not verified"
                                     src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/unverified-document.png"
                                     width={50}/>
                            </span>
                            </div>
                            <div className="col-md-9 col-sm-9">
                                <strong>
                                    Documento Negado
                                </strong>
                                <p className="font-14 margin-top-10">
                                    Documento negado! Isso pode ter acontecido por várias razões, por favor
                                    envie novamente um documento que contenha seu CPF (ex: RG, CPF).
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
        uploadDocumentVerificationPhoto: (file) => dispatch(userActions.uploadDocumentVerificationPhoto(file))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(DocumentVerification);