import AppConstants from '../../constants/AppConstants'
import {sessionService} from 'redux-react-session';

export const add = (planTakeProfitRequest) => {
    let promise = new Promise((resolve,reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.planTakeProfitRequest.add, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(planTakeProfitRequest)
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success)
                    resolve(json);
                else
                    reject(json);
            })
        })
    });
    return promise;
}

export const list = () => {
    let promise = new Promise((resolve,reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.planTakeProfitRequest.list, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                    'Content-Type': 'application/json'
                }
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success)
                    resolve(json);
                else
                    reject(json);
            })
        })
    });
    return promise;
}

export const changeStatusToCancelled = (id) => {
    let promise = new Promise((resolve,reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.planTakeProfitRequest.changeStatusToCancelled.replace("{id}",id), {
                method: 'PUT',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                    'Content-Type': 'application/json'
                }
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success)
                    resolve(json);
                else
                    reject(json);
            })
        })
    });
    return promise;
}

export const del = (id) => {
    let promise = new Promise((resolve,reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.planTakeProfitRequest.delete.replace("{id}",id), {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                    'Content-Type': 'application/json'
                }
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success)
                    resolve(json);
                else
                    reject(json);
            })
        })
    });
    return promise;
}