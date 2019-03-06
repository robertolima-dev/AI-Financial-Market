import fetch from 'isomorphic-fetch'
import AppConstants from '../../constants/AppConstants'
import {sessionService} from 'redux-react-session';
import base64 from 'base-64'
import utf8 from 'utf8'
import {push} from 'react-router-redux'
import * as totalBalanceActions from './totalBalanceActions'

export const login = (email, pass) => {
    return (dispatch) => {
        let promise = new Promise((resolve, reject) => {
            fetch(AppConstants.services.auth.loginResource, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({email: email, password: pass})
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success) {

                    const splitedToken = json.response.split(".");
                    const bytes = base64.decode(splitedToken[1]);
                    const decodedToken = utf8.decode(bytes);
                    let userInfo = JSON.parse(decodedToken);
                    userInfo = {...userInfo, user: JSON.parse(userInfo.user)}

                    const session = {
                        token: json.response,
                        user: userInfo
                    };

                    sessionService.deleteUser().then(() => {
                        sessionService.deleteSession().then(() => {
                            sessionService.saveSession(session).then(() => {
                                sessionService.saveUser(session).then(() => {
                                    dispatch(totalBalanceActions.updateTotalBalance());
                                    resolve();
                                })
                            })
                        })
                    })

                } else {
                    reject(json);
                }
            })
        })
        return promise;
    }
}

export const signup = (signup) => {
    return (dispatch) => {
        let promise = new Promise((resolve, reject) => {
            fetch(AppConstants.services.auth.signupResource, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(signup)
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success) {
                    resolve();
                } else {
                    reject(json);
                }
            })
        })
        return promise;
    }
}

export const emailConfirmation = (token) => {
    return (dispatch) => {
        let promise = new Promise((resolve, reject) => {
            fetch(AppConstants.services.auth.emailConfirmationResource + "?token=" + token, {
                method: 'PUT'
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success) {
                    const splitedToken = json.response.split(".");
                    const bytes = base64.decode(splitedToken[1]);
                    const decodedToken = utf8.decode(bytes);
                    let userInfo = JSON.parse(decodedToken);
                    userInfo = {...userInfo, user: JSON.parse(userInfo.user)}

                    const session = {
                        token: json.response,
                        user: userInfo
                    };

                    sessionService.saveSession(session).then(() => {
                        sessionService.saveUser(session).then(() => {
                            dispatch(totalBalanceActions.updateTotalBalance());
                            resolve();
                        })
                    })
                } else {
                    reject();
                }
            })
        })
        return promise;
    }
}

export const doSecurityFilter = () => {
    return (dispatch) => {
        let promise = new Promise((resolve, reject) => {
            sessionService.loadUser().then((session) => {
                if (session != null && session.token) {
                    fetch(AppConstants.services.auth.isValidSessionToken + session.token, {
                        method: 'GET',
                    }).then((response) => {
                        return response.json();
                    }).then((json) => {
                        if (json)
                            resolve();
                        else {
                            dispatch(push('/'))
                        }

                    })
                }
            })
        })
        return promise;
    }
}

export const logout = () => {
    return (dispatch) => {
        sessionService.deleteUser().then(() => {
            sessionService.deleteSession().then(() => {
                dispatch(push('/'));
            })
        })
    }
}


export const getToken = () => {
    const promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            resolve(session.token);
        });
    })
    return promise;
}


export const redefinePassword = (currentPassword, newPassword, confirmNewPassword) => {
    const promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.auth.redefinePassword, {
                method: 'PUT',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    currentPassword: currentPassword,
                    newPassword: newPassword,
                    confirmNewPassword: confirmNewPassword
                })
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success)
                    resolve(json)
                else
                    reject(json);
            })
        })
    })
    return promise;
}


export const getInfo = () => {
    const promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.auth.getInfo, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                }
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success)
                    resolve(json)
                else
                    reject(json);
            })
        })
    })
    return promise;
}


export const updateProfile = (user) => {
    const promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.auth.updateProfile, {
                method: 'PUT',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(user)
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success)
                    resolve(json)
                else
                    reject(json);
            })
        })
    })
    return promise;
}

export const uploadIdentityVerificationPhoto = (file) => {
    return (dispatch) => {
        let promise = new Promise((resolve, reject) => {
            sessionService.loadUser().then((session) => {
                const formData = new FormData();
                formData.append('file', file);
                fetch(AppConstants.services.auth.uploadIdentityVerificationPhoto, {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + session.token
                    },
                    body: formData
                }).then((response) => {
                    return response.json();
                }).then((json) => {
                    if (json.success) {
                        dispatch(totalBalanceActions.updateTotalBalance());
                        resolve(json)
                    }
                    else
                        reject(json);
                });
            })
        })
        return promise;
    }
}

export const uploadDocumentVerificationPhoto = (file) => {
    return (dispatch) => {
        let promise = new Promise((resolve, reject) => {
            sessionService.loadUser().then((session) => {
                const formData = new FormData();
                formData.append('file', file);
                fetch(AppConstants.services.auth.uploadDocumentVerificationPhoto, {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + session.token
                    },
                    body: formData
                }).then((response) => {
                    return response.json();
                }).then((json) => {
                    if (json.success) {
                        dispatch(totalBalanceActions.updateTotalBalance());
                        resolve(json)
                    }
                    else
                        reject(json);
                });
            })
        })
        return promise;
    }
}

export const uploadAvatar = (file) => {
    let promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            const formData = new FormData();
            formData.append('file', file);
            fetch(AppConstants.services.auth.uploadAvatar, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + session.token
                },
                body: formData
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success)
                    resolve(json)
                else
                    reject(json);
            });
        })
    })
    return promise;
}

export const cropAvatar = (imgCoords) => {
    let promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.auth.cropAvatar, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(imgCoords)
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success) {
                    //update session avatar
                    session = {...session, user: {...session.user, user: {...session.user.user, avatar: json.response}}}
                    sessionService.saveUser(session).then(() => {
                        resolve(json)
                    })
                }
                else
                    reject(json);
            });
        })
    })
    return promise;
}

export const resendEmailConfirmation = (email) => {
    let promise = new Promise((resolve, reject) => {
        fetch(AppConstants.services.auth.resendEmailConfirmation.replace('{email}',email), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then((response) => {
            return response.json();
        }).then((json) => {
            if (json.success)
                resolve(json);
            else
                reject(json);
        });
    })
    return promise;
}