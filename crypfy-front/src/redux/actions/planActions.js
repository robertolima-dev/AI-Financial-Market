import fetch from 'isomorphic-fetch'
import AppConstants from '../../constants/AppConstants'
import {sessionService} from 'redux-react-session';
import * as totalBalanceActions from './totalBalanceActions'

export const list = (status) => {
    const promise = new Promise((resolve, reject) => {
        const queryParam = (status) ? "?status="+status : "";
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.plan.list+queryParam, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                    'Content-Type': 'application/json'
                },
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success)
                    resolve(json)
                else
                    reject(json);
            })
        });
    })
    return promise;
}

export const getPerfomance = (idplan) => {
    const promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.plan.planPerfomance.replace("{id}", idplan), {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                    'Content-Type': 'application/json'
                },
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success)
                    resolve(json)
                else
                    reject(json);
            })
        });
    })
    return promise;
}

export const add = (idplan, planName, initialBalance, duration) => {
    return (dispatch) => {
        let promise = new Promise((resolve, reject) => {
            sessionService.loadUser().then((session) => {

                const json = {
                    idplan: idplan,
                    initialBalance: initialBalance,
                    duration: duration,
                    planName: planName
                }

                fetch(AppConstants.services.plan.add, {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + session.token,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(json)
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