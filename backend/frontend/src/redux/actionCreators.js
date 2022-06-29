import { saveUser, removeUser } from '../utils/utils';


export const LOGIN = 'USER_LOGIN';
export const LOGOUT = 'USER_LOGOUT';
export const ERROR = 'ERROR';
export const CLEAR = 'CLEAR';

export const userActions = {
  login,
  logout,
};

function login(user) {
  saveUser(user);
  return { type: LOGIN, user };
}
function logout() {
  removeUser();
  return { type: LOGOUT };
}


export const alertActions = {
  error,
  clear
 };

function error(msg) {
  return { type: ERROR, msg }
 }
 function clear() {
  return { type: CLEAR }
 }
