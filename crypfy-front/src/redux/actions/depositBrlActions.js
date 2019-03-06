import fetch from 'isomorphic-fetch'
import AppConstants from '../../constants/AppConstants'
import {sessionService} from 'redux-react-session';

export const addDepositBrl = (amount, idbankAccount) => {
    let promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.depositBrl.add, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({idbankAccount: idbankAccount, amount: amount})
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

export const listDepositsBrl = () => {
    let promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.depositBrl.list, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + session.token
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

export const uploadDepositVoucher = (file,id) => {
    let promise = new Promise((resolve,reject) => {
        sessionService.loadUser().then((session) => {
            const formData = new FormData();
            formData.append('file',file);
            fetch(AppConstants.services.depositBrl.uploadVoucher.replace('{id}',id), {
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

export const cancelDeposit = (id) => {
    let promise = new Promise((resolve,reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.depositBrl.cancel.replace('{id}',id), {
                method: 'PUT',
                headers: {
                    'Authorization': 'Bearer ' + session.token
                }
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

export const del = (id) => {
    let promise = new Promise((resolve,reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.depositBrl.delete.replace('{id}',id), {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + session.token
                }
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