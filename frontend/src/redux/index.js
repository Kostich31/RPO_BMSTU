import { createStore, compose, applyMiddleware } from 'redux';
import { configureStore } from '@reduxjs/toolkit';
import rootReducer from './reducers/index.js';
import { composeWithDevTools } from 'redux-devtools-extension';

// import { createLogger } from 'react-logger';

// const loggerMiddleware = createLogger();


const store = createStore(
    rootReducer,
    window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
  );
window.store = store;

export default store;
