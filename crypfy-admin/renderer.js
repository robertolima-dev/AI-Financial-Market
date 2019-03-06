import React from 'react'
import ReactDOM from 'react-dom'
import AppCrypfy from './src/components/AppCrypfy'
import thunkMiddleware from 'redux-thunk'
import {createStore, applyMiddleware} from 'redux'
import {Provider} from 'react-redux'
import {Router, Route, Switch, browserHistory} from 'react-router'
import {createBrowserHistory} from 'history';
import {syncHistoryWithStore, routerMiddleware} from 'react-router-redux'
import reducers from './src/redux/reducers/index'
import { sessionService } from 'redux-react-session';
import Login from './src/components/login/Login'

const history = createBrowserHistory();
const routeMiddleware = routerMiddleware(history);

const store = createStore(reducers, applyMiddleware(thunkMiddleware,routeMiddleware));
sessionService.initSessionService(store);

ReactDOM.render(<Provider store={store}>
    {/* Tell the Router to use our enhanced history */}
    <Router history={history}>
        <AppCrypfy>
            <Switch>
                <Route on exact path="/" component={Login}/>
            </Switch>
        </AppCrypfy>
    </Router>
</Provider>, document.getElementById('app'))
