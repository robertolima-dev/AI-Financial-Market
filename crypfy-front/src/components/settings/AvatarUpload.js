import React from 'react'
import {Modal, ModalHeader, ModalBody, ModalFooter} from 'reactstrap'
import SimpleButton from '../common/SimpleButton'
import Alert from 'react-s-alert'
import * as userActions from '../../redux/actions/userActions'
import ReactCrop, {makeAspectCrop} from 'react-image-crop'

class AvatarUpload extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            modal: false,
            crop: {
                x: 0,
                y: 0,
                width: 50,
                height: 50,
                aspect: 1 / 1
            },
            uploadedPhoto: null
        }
    }

    toggleModal() {
        if (this.state.modal)
            this.setState({...this.state, modal: false, uploadedPhoto: null})
        else
            this.setState({...this.state, modal: true, uploadedPhoto: null})
    }

    onChangeCrop(crop) {
        this.setState({...this.state, crop: crop});
    }

    onImageLoaded(image) {
        this.setState({
            crop: makeAspectCrop({
                x: 0,
                y: 0,
                aspect: 1 / 1,
                width: image.width,
            }, image.width / image.height),
        });
    }

    chooseFileUpload() {
        this.refs.buttonFileUpload.click();
    }

    onChangeFileupload(e) {
        this.refs.buttonUpload.setLoadingOn("carregando foto..");
        userActions.uploadAvatar(e.target.files[0]).then((ret) => {
            Alert.success(ret.message);
            this.refs.buttonUpload.setLoadingOff();
            this.setState({...this.state, uploadedPhoto: ret.response})
        }).catch((ret) => {
            Alert.error(ret.message);
            this.refs.buttonUpload.setLoadingOff();
        })
    }

    cropImage() {
        this.refs.buttonCrop.setLoadingOn("carregando..");
        const imgCoords = {
            x : this.state.crop.x,
            y : this.state.crop.y,
            h : this.state.crop.height,
            w : this.state.crop.width,
            image : this.state.uploadedPhoto
        };
        userActions.cropAvatar(imgCoords).then((ret) => {
            Alert.success(ret.message);
            this.refs.buttonCrop.setLoadingOff();
            this.props.setAvatar(ret.response);
            this.setState({...this.state,modal: false});
        }).catch((ret) => {
            Alert.error(ret.message);
            this.refs.buttonUpload.setLoadingOff();
        })
    }

    render() {
        const reactCrop = (this.state.uploadedPhoto != null) ? <ModalBody>
            <div className="container-fluid">
                <div className="row">
                    <div className="col-md-12">
                        <ReactCrop minHeight={50} minWidth={50} onImageLoaded={this.onImageLoaded.bind(this)} crop={this.state.crop}
                                   imageStyle={{width: "100%", height: "100%"}}
                                   onChange={this.onChangeCrop.bind(this)}
                                   src={this.state.uploadedPhoto}/>
                    </div>
                </div>
            </div>
        </ModalBody> : null;

        const actionButton = (this.state.uploadedPhoto != null) ?
            <SimpleButton key="buttonCrop" onClick={this.cropImage.bind(this)} ref="buttonCrop"
                          className="btn btn-primary">Cortar e Salvar</SimpleButton> :
            <SimpleButton key="buttonUpload" onClick={this.chooseFileUpload.bind(this)} ref="buttonUpload"
                          className="btn btn-primary">Escolher Foto</SimpleButton>

        return (
            <Modal isOpen={this.state.modal}
                   toggle={this.toggleModal.bind(this)}>
                <ModalHeader toggle={this.toggle}>Alterar Avatar</ModalHeader>
                {reactCrop}
                <ModalFooter style={{border:"none"}}>
                    {actionButton}
                    <input ref="buttonFileUpload" className="hide" type="file"
                           onChange={this.onChangeFileupload.bind(this)}/>
                    <button type="button" className="btn btn-secondary" onClick={this.toggleModal.bind(this)}>Cancelar
                    </button>
                </ModalFooter>
            </Modal>
        );
    }
}

export default AvatarUpload;