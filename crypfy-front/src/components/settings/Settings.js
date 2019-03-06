import React from 'react'
import {Modal} from 'reactstrap'
import Profile from './Profile'
import {connect} from 'react-redux'
import AvatarUpload from './AvatarUpload'

class Settings extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            modal: false,
            profileWarning: false,
            avatar: 'https://s3-sa-east-1.amazonaws.com/static.crypfy/img/avatar.svg'
        }

    }

    setProfileWarning(warning) {
        this.setState({
            ...this.state,
            profileWarning: warning
        });
    }

    setAvatar(src) {
        this.setState({...this.state,avatar:src});
    }

    toggleModal() {
        if (this.state.modal)
            this.setState({...this.state, modal: false})
        else
            this.setState({...this.state, modal: true})
    }

    toggleAvatarUploadModal() {
        this.refs.avatarUploadComponent.toggleModal();
    }

    render() {
        return (
            <Modal className="modal-full" isOpen={this.state.modal}
                   toggle={this.toggleModal.bind(this)}>
                <div className="container-fluid">
                    <div className="row">
                        <div className="col-md-3 settings-sidebar">
                            <div className="text-center margin-top-30">
                                <span>
                                    <img src={this.state.avatar}
                                         width={70} className="img-thumbnail rounded-circle"/>
                                </span>
                                <div className="margin-top-10">
                                    <span onClick={this.toggleAvatarUploadModal.bind(this)} className="btn btn-outline-light btn-sm">Alterar Avatar</span>
                                </div>
                                <h1 className="profile-name">{(this.props.session.user.user) ? this.props.session.user.user.user.name : ""}</h1>
                                <h2 className="profile-email">{(this.props.session.user.user) ? this.props.session.user.user.user.email : ""}</h2>
                            </div>
                            <ul>
                                <li className="active">
                                    <a><i className="lnr lnr-users"></i> Perfil {(this.state.profileWarning) ? <span
                                        className="badge badge-danger">!</span> : ''}</a>
                                </li>
                            </ul>
                        </div>
                        <div className="col-md-9">
                            <Profile setAvatar={this.setAvatar.bind(this)} profileWarning={this.setProfileWarning.bind(this)}/>
                            <AvatarUpload setAvatar={this.setAvatar.bind(this)} ref="avatarUploadComponent"/>
                        </div>
                    </div>
                </div>
            </Modal>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    return {
        session: state.session
    }
};

const mapDispatchToProps = (dispatch) => {
    return {}
};

export default connect(mapStateToProps, mapDispatchToProps, null, {withRef: true})(Settings);