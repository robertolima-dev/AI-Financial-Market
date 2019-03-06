import { combineReducers } from 'redux'
import { routerReducer } from 'react-router-redux'
import { sessionReducer } from 'redux-react-session';
import TotalBalanceReducers from './totalBalanceReducers'

export default combineReducers({
    routing: routerReducer,
    session: sessionReducer,
    totalBalance: TotalBalanceReducers
})