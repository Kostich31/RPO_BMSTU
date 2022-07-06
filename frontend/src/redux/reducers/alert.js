import { ERROR, CLEAR } from '../actionCreators';

function alert(state = {}, action) {
  console.log('alert');
  switch (action.type) {
    case ERROR:
      return { msg: action.msg };
    case CLEAR:
      return {};
    default:
      return state;
  }
}

export default alert;
