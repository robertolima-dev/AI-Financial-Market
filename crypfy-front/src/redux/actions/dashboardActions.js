import fetch from 'isomorphic-fetch'
import AppConstants from '../../constants/AppConstants'
import {sessionService} from 'redux-react-session';


export const data = () => {
    const promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.dashboard.data, {
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

export const tickers = () => {
    const promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.dashboard.tickers, {
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