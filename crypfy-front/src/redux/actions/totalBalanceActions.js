import fetch from 'isomorphic-fetch'
import AppConstants from '../../constants/AppConstants'
import {sessionService} from 'redux-react-session';

export const updateTotalBalance = () => {
    return (dispatch) => {
        let promise = new Promise((resolve, reject) => {
            sessionService.loadUser().then((session) => {
                fetch(AppConstants.services.balance.data, {
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + session.token,
                        'Content-Type': 'application/json'
                    },
                }).then((response) => {
                    return response.json();
                }).then((json) => {
                    if (json.success){
                        dispatch({
                            type: 'TOTAL_BALANCE_UPDATE',
                            balance : json.response
                        });
                        resolve();
                    }
                    else
                        reject(json);
                })
            })
        })
        return promise;
    }
}