import { combineReducers } from 'redux';
import authentication from './auth';
import alert from './alert';

const rootReducer = combineReducers({
  auth: authentication,
  alert
});

export default rootReducer;
