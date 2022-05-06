import { combineReducers, applyMiddleware } from "redux";
import { configureStore } from "@reduxjs/toolkit";
import Utils from "./Utils";

/* ACTIONS */
const userConstants = {
    LOGIN: 'USER_LOGIN',
    LOGOUT: 'USER_LOGOUT',
};

const alertConstants = {
    ERROR: 'ERROR',
    CLEAR: 'CLEAR',
};

/* ACTION GENERATORS */
export const userActions = {
    login,
    logout
};

function login(user) {
    Utils.saveUser(user)
    return { type: userConstants.LOGIN, user }
}

function logout() {
    Utils.removeUser()
    return { type: userConstants.LOGOUT }
}

export const alertActions = {
    error,
    clear
}

function error(msg) {
    return { type: alertConstants.ERROR, msg }
}

function clear() {
    return { type: alertConstants.CLEAR }
}

/* REDUCERS */
let user = Utils.getUser()
const initialState = user ? { user } : {}
function authentication(state = initialState, action) {
    console.log("authentication")
    switch (action.type) {
        case userConstants.LOGIN:
            return { user: action.user };
        case userConstants.LOGOUT:
            return {};
        default:
            return state
    }
}


function alert(state = {}, action) {
    console.log("alert")
    switch (action.type) {
        case alertConstants.ERROR:
            return { msg: action.msg };
        case alertConstants.CLEAR:
            return {};
        default:
            return state
    }
}

/* STORE */

const rootReducer = combineReducers({
    authentication, alert
});

export const store = configureStore({ reducer: rootReducer });
