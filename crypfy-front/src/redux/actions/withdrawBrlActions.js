import fetch from 'isomorphic-fetch'
import AppConstants from '../../constants/AppConstants'
import {sessionService} from 'redux-react-session';

export const listWithdrawsBrl = () => {
    let promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.withdrawBrl.list, {
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

export const add = (idbankAccount,amount) => {
    let promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.withdrawBrl.add, {
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

export const cancel = (id) => {
    let promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.withdrawBrl.cancel.replace("{id}",id), {
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
            })
        })
    })
    return promise;
}


export const del = (id) => {
    let promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.withdrawBrl.delete.replace("{id}",id), {
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
            })
        })
    })
    return promise;
}