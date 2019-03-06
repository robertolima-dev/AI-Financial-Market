import React from 'react'
import WrapperAbsolute from '../common/WrapperAbsolute'
import queryString from 'query-string'
import sleeper from '../../helpers/Sleeper'
import {connect} from 'react-redux';
import * as userActions from '../../redux/actions/userActions'
import {push} from 'react-router-redux'
import appConstants from "../../constants/AppConstants";

class EmailConfirmation extends React.Component {
    constructor(props) {
        super(props);
        const parsedParams = queryString.parse(location.search);

        document.body.style.backgroundColor = "#416fba";

        this.state = {
            counterToRedirect: 5,
            isEmailConfirmed: null
        }

        if (parsedParams.token !== undefined)
            this.props.emailConfirmation(parsedParams.token).then(() => {
                this.setState({...this.state, isEmailConfirmed: true}, () => {
                    this.startCounterToRedirect();
                })
            }).catch(() => {
                this.setState({...this.state, isEmailConfirmed: false})
            });
        else
            this.state = {
                counterToRedirect: 5,
                isEmailConfirmed: false
            }
    }

    startCounterToRedirect() {
        if (this.state.counterToRedirect > 0)
            sleeper(1000).then(() => {
                this.setState({...this.state, counterToRedirect: this.state.counterToRedirect - 1}, () => {
                    this.startCounterToRedirect();
                });
            })
        else {
            this.props.toDashboard();
        }
    }

    render() {
        if (this.state.isEmailConfirmed == null)
            return null;
        else {
            const emailConfirmedBox = (this.state.isEmailConfirmed) ?
                <div>
                    <div className="email-confirmation-box text-center">
                    <span>
                        <img width={230} alt="proof of stake"
                             src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/proof-of-stake.svg"/>
                    </span>
                        <h5 >
                            Sua conta foi confirmada com sucesso!
                        </h5>
                        <h3 className="description">
                            Sua conta foi confirmada com sucesso e voce será redirecionado em
                            <strong> {this.state.counterToRedirect}</strong> segundos
                        </h3>
                    </div>
                </div> :
                <div className="email-confirmation-box text-center">
                <span>
                    <img width={150} alt="malware"
                         src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/bitcoin-encryption.svg"/>
                </span>
                    <h5 className="margin-top-30">
                        Link Inválido!
                    </h5>
                    <h3 className="description">
                        Link de confirmação de conta inválido ou expirado, verifique se copiou o mesmo corretamente
                    </h3>
                </div>;

            return (
                <WrapperAbsolute style={{"backgroundColor": "#416fba"}} className="email-confirmation-wrapper">
                    <img className="logo crypfy"
                         src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/full-logo-crypfy.svg"/>
                    {emailConfirmedBox}
                </WrapperAbsolute>
            );
        }
    }
}

const mapStateToProps = (state, ownProps) => {
    return {
        session: state.session
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        emailConfirmation: (token) => dispatch(userActions.emailConfirmation(token)),
        toDashboard: () => dispatch(push(appConstants.routes.dashboard)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(EmailConfirmation);