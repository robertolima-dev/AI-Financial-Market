const appConstantsDev = {
    services:{
        auth: {
            loginResource:'http://localhost:8083/signin',
            signupResource:'http://localhost:8083/signup',
            emailConfirmationResource: 'http://localhost:8083/email-confirmation',
            isValidSessionToken: 'http://localhost:8083/auth/is-valid-token?token=',
            redefinePassword: 'http://localhost:8083/user/redefine-password',
            getInfo: 'http://localhost:8083/user/get-info',
            updateProfile: 'http://localhost:8083/user',
            uploadIdentityVerificationPhoto: 'http://localhost:8083/user/upload-identity-verification',
            uploadDocumentVerificationPhoto: 'http://localhost:8083/user/upload-document-verification',
            uploadAvatar: 'http://localhost:8083/user/avatar-upload',
            cropAvatar: 'http://localhost:8083/user/crop-avatar',
            resendEmailConfirmation: "http://localhost:8083/user/{email}/resend-email-confirmation"
        },
        bankAccounts: {
            listCrypfyBankAccounts: "http://localhost:8083/bank-accounts/crypfy-accounts",
            list: "http://localhost:8083/bank-accounts",
            find: "http://localhost:8083/bank-accounts/{id}",
            add: "http://localhost:8083/bank-accounts",
            update: "http://localhost:8083/bank-accounts/{id}",
            delete: "http://localhost:8083/bank-accounts/{id}"
        },
        bank : {
            listBanks:"http://localhost:8083/banks"
        },
        depositBrl: {
            add: "http://localhost:8083/deposits-brl",
            list: "http://localhost:8083/deposits-brl",
            uploadVoucher: "http://localhost:8083/deposits-brl/{id}/upload-voucher",
            cancel: "http://localhost:8083/deposits-brl/{id}/change-status-to-cancelled",
            delete: "http://localhost:8083/deposits-brl/{id}"
        },
        withdrawBrl: {
            add: "http://localhost:8083/withdraws-brl",
            list: "http://localhost:8083/withdraws-brl",
            cancel: "http://localhost:8083/withdraws-brl/{id}/change-status-to-cancelled",
            delete: "http://localhost:8083/withdraws-brl/{id}"
        },
        dashboard: {
            data: "http://localhost:8083/dashboard/data",
            tickers: "http://localhost:8083/dashboard/tickers"
        },
        plan: {
            list: "http://localhost:8083/plans",
            planPerfomance: "http://localhost:8083/plans/{id}/perfomance",
            add: "http://localhost:8083/plans"
        },
        planTakeProfitRequest: {
            add: "http://localhost:8083/plan-take-profit-requests",
            list: "http://localhost:8083/plan-take-profit-requests",
            changeStatusToCancelled: "http://localhost:8083/plan-take-profit-requests/{id}/change-status-to-cancelled",
            delete: "http://localhost:8083/plan-take-profit-requests/{id}"
        },
        balance: {
            data: "http://localhost:8083/balance/data"
        }
    },
    routes: {
        login : "/",
        signup: "/signup",
        emailConfirmation: "/email-confirmation",
        dashboard: "/secured/dashboard",
        plans: "/secured/plans",
        addPlans: "/secured/plans/add",
        depositsBrl: "/secured/deposits-brl",
        depositsBrlAdd: "/secured/deposits-brl/add",
        withdrawsBrl: "/secured/withdraws-brl",
        withdrawsBrlAdd: "/secured/withdraws-brl/add",
        globalMarketData : "/secured/global-market-data",
        planTakeProfit: "/secured/plan-take-profits",
        bankAccount: "/secured/bank-accounts"
    },
    labels: {
        maxDaysDepositVoucherAnalysis : 2,
        maxDaysWithdrawBrlAnalysis : 2,
        depositBrlFee: 1
    }
}

const appConstantsProd = {
    services:{
        auth: {
            loginResource:'https://api.crypfy.com/signin',
            signupResource:'https://api.crypfy.com/signup',
            emailConfirmationResource: 'https://api.crypfy.com/email-confirmation',
            isValidSessionToken: 'https://api.crypfy.com/auth/is-valid-token?token=',
            redefinePassword: 'https://api.crypfy.com/user/redefine-password',
            getInfo: 'https://api.crypfy.com/user/get-info',
            updateProfile: 'https://api.crypfy.com/user',
            uploadIdentityVerificationPhoto: 'https://api.crypfy.com/user/upload-identity-verification',
            uploadDocumentVerificationPhoto: 'https://api.crypfy.com/user/upload-document-verification',
            uploadAvatar: 'https://api.crypfy.com/user/avatar-upload',
            cropAvatar: 'https://api.crypfy.com/user/crop-avatar',
            resendEmailConfirmation: "https://api.crypfy.com/user/{email}/resend-email-confirmation"
        },
        bankAccounts: {
            listCrypfyBankAccounts: "https://api.crypfy.com/bank-accounts/crypfy-accounts",
            list: "https://api.crypfy.com/bank-accounts",
            find: "https://api.crypfy.com/bank-accounts/{id}",
            add: "https://api.crypfy.com/bank-accounts",
            update: "https://api.crypfy.com/bank-accounts/{id}",
            delete: "https://api.crypfy.com/bank-accounts/{id}"
        },
        bank : {
            listBanks:"https://api.crypfy.com/banks"
        },
        depositBrl: {
            add: "https://api.crypfy.com/deposits-brl",
            list: "https://api.crypfy.com/deposits-brl",
            uploadVoucher: "https://api.crypfy.com/deposits-brl/{id}/upload-voucher",
            cancel: "https://api.crypfy.com/deposits-brl/{id}/change-status-to-cancelled",
            delete: "https://api.crypfy.com/deposits-brl/{id}"
        },
        withdrawBrl: {
            add: "https://api.crypfy.com/withdraws-brl",
            list: "https://api.crypfy.com/withdraws-brl",
            cancel: "https://api.crypfy.com/withdraws-brl/{id}/change-status-to-cancelled",
            delete: "https://api.crypfy.com/withdraws-brl/{id}"
        },
        dashboard: {
            data: "https://api.crypfy.com/dashboard/data",
            tickers: "https://api.crypfy.com/dashboard/tickers"
        },
        plan: {
            list: "https://api.crypfy.com/plans",
            planPerfomance: "https://api.crypfy.com/plans/{id}/perfomance",
            add: "https://api.crypfy.com/plans"
        },
        planTakeProfitRequest: {
            add: "https://api.crypfy.com/plan-take-profit-requests",
            list: "https://api.crypfy.com/plan-take-profit-requests",
            changeStatusToCancelled: "https://api.crypfy.com/plan-take-profit-requests/{id}/change-status-to-cancelled",
            delete: "https://api.crypfy.com/plan-take-profit-requests/{id}"
        },
        balance: {
            data: "https://api.crypfy.com/balance/data"
        }
    },
    routes: {
        login : "/",
        signup: "/signup",
        emailConfirmation: "/email-confirmation",
        dashboard: "/secured/dashboard",
        plans: "/secured/plans",
        addPlans: "/secured/plans/add",
        depositsBrl: "/secured/deposits-brl",
        depositsBrlAdd: "/secured/deposits-brl/add",
        withdrawsBrl: "/secured/withdraws-brl",
        withdrawsBrlAdd: "/secured/withdraws-brl/add",
        globalMarketData : "/secured/global-market-data",
        planTakeProfit: "/secured/plan-take-profits",
        bankAccount: "/secured/bank-accounts"
    },
    labels: {
        maxDaysDepositVoucherAnalysis : 2,
        maxDaysWithdrawBrlAnalysis : 2,
        depositBrlFee: 1
    }
}

let appConstants = null

if(process.env.NODE_ENV === "production"){
    appConstants = appConstantsProd;
} else {
    appConstants = appConstantsDev;
}

export default appConstants;
