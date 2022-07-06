import { getUser } from '../../utils/utils';
import { LOGIN, LOGOUT } from '../actionCreators';

let user = getUser();
const initialState = user ? { user } : {};

function authentication(state = initialState, action) {
  console.log('authentication');
  switch (action.type) {
    case LOGIN:
      return { user: action.user };
    case LOGOUT:
      return {};
    default:
      return state;
  }
}

export default authentication;
