import { combineReducers } from 'redux'
import { routerReducer } from 'react-router-redux'
import { sessionReducer } from 'redux-react-session';

export default combineReducers({
    routing: routerReducer,
    session: sessionReducer
})