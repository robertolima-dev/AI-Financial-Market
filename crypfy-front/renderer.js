import React from 'react'
import ReactDOM from 'react-dom'
import AppCrypfy from './src/components/AppCrypfy'
import thunkMiddleware from 'redux-thunk'
import {applyMiddleware, createStore} from 'redux'
import {Provider} from 'react-redux'
import {browserHistory, Route, Router, Switch} from 'react-router'
import {createBrowserHistory} from 'history';
import {routerMiddleware} from 'react-router-redux'
import reducers from './src/redux/reducers'
import Login from './src/components/login/Login'
import Signup from './src/components/login/Signup'
import EmailConfirmation from './src/components/login/EmailConfirmation'
import Dashboard from './src/components/dashboard/Dashboard'
import Withdraw from "./src/components/withdrawl-brl/Withdraw";
import Deposit from "./src/components/deposit-brl/Deposit";
import DepositAdd from "./src/components/deposit-brl/DepositAdd";
import WithdrawAdd from "./src/components/withdrawl-brl/WithdrawAdd";
import BankAccount from "./src/components/bank-account/BankAccount";
import Plan from './src/components/plan/Plan'
import PlanAdd from './src/components/plan/PlanAdd'
import PlanTakeProfit from './src/components/plan-take-profit/PlanTakeProfit'
import {sessionService} from 'redux-react-session';
import Alert from 'react-s-alert';
import Moment from 'react-moment';
import {autoRehydrate, persistReducer, persistStore} from 'redux-persist'
import storage from 'redux-persist/lib/storage'
import {PersistGate} from 'redux-persist/integration/react'
import GlobalMarketData from './src/components/globalmarketdata/GlobalMarketData'
import BaseTemplateAdmin from './src/components/base/BaseTemplateAdmin'

const history = createBrowserHistory();
const routeMiddleware = routerMiddleware(history);
Moment.globalLocale = 'pt-BR';

const persistConfig = {
    key: 'root',
    storage,
}

const persistedReducer = persistReducer(persistConfig, reducers)

const store = createStore(persistedReducer, applyMiddleware(thunkMiddleware, routeMiddleware));
sessionService.initSessionService(store);
let persistor = persistStore(store);

ReactDOM.render(<Provider store={store}>
    {/* Tell the Router to use our enhanced history */}
    <PersistGate loading={null} persistor={persistor}>
        <Router history={history}>
            <AppCrypfy>
                <Alert timeout={3500} offset={20} effect="jelly" stack={{limit: 1}} html={true} position="top"/>
                <Route on exact path="/" component={Login}/>
                <Route path="/signup" component={Signup}/>
                <Route path="/email-confirmation" component={EmailConfirmation}/>
                <Switch>
                    <Route exact path="/secured/dashboard" component={Dashboard}/>
                    <Route exact path="/secured/plans" component={Plan}/>
                    <Route exact path="/secured/plans/add" component={PlanAdd}/>
                    <Route exact path="/secured/deposits-brl" component={Deposit}/>
                    <Route exact path="/secured/withdraws-brl" component={Withdraw}/>
                    <Route exact path="/secured/withdraws-brl/add" component={WithdrawAdd}/>
                    <Route exact path="/secured/plan-take-profits" component={PlanTakeProfit}/>
                    <Route path="/secured/deposits-brl/add" component={DepositAdd}/>
                    <Route exact path="/secured/bank-accounts" component={BankAccount}/>
                    <Route exact path="/secured/global-market-data" component={GlobalMarketData}/>
                </Switch>
            </AppCrypfy>
        </Router>
    </PersistGate>
</Provider>, document.getElementById('app'))
