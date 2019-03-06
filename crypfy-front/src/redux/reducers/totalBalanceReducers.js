export default (state = [], action) => {
    switch (action.type) {
        case 'TOTAL_BALANCE_UPDATE' :
            return {...state,
                balance: action.balance
            };
        default:
            return state;
    }
}