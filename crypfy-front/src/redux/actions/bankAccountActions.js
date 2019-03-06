import fetch from 'isomorphic-fetch'
import AppConstants from '../../constants/AppConstants'
import {sessionService} from 'redux-react-session';


export const add = (bankAccount) => {
    let promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.bankAccounts.add, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(bankAccount)
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

export const update = (bankAccount) => {
    let promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.bankAccounts.update.replace("{id}",bankAccount.idbankAccount), {
                method: 'PUT',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(bankAccount)
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

export const remove = (id) => {
    let promise = new Promise((resolve, reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.bankAccounts.delete.replace("{id}",id), {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + session.token,
                    'Content-Type': 'application/json'
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

export const listCrypfyBankAccounts = () => {
    let promise = new Promise((resolve,reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.bankAccounts.listCrypfyBankAccounts, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + session.token
                },
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success) {
                    resolve(json.response)
                } else {
                    reject(json);
                }
            })
        })
    })
    return promise;
}

export const list = () => {
    let promise = new Promise((resolve,reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.bankAccounts.list, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + session.token
                },
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success) {
                    resolve(json)
                } else {
                    reject(json);
                }
            })
        })
    })
    return promise;
}

export const find = (id) => {
    let promise = new Promise((resolve,reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.bankAccounts.find.replace("{id}",id), {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + session.token
                },
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success) {
                    resolve(json)
                } else {
                    reject(json);
                }
            })
        })
    })
    return promise;
}

export const listBanks = () => {
    let promise = new Promise((resolve,reject) => {
        sessionService.loadUser().then((session) => {
            fetch(AppConstants.services.bank.listBanks, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + session.token
                },
            }).then((response) => {
                return response.json();
            }).then((json) => {
                if (json.success) {
                    resolve(json)
                } else {
                    reject(json);
                }
            })
        })
    })
    return promise;
}