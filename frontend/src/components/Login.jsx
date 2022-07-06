import React, { useState } from 'react';
import { login, logout } from '../services/BackendService';
import { saveUser, removeUser, getToken, getUserName, getUser } from '../utils/utils';
import {useNavigate} from 'react-router-dom'
import { useDispatch } from 'react-redux';
import { userActions } from '../redux/actionCreators';


function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loggingIn, setLoggingIn] = useState(false);
  const [submitted, setSubmitted] = useState(false);
//   const [errorMessage, setErrorMessage] = useState('');

  const nav = useNavigate();
  const dispatch = useDispatch();

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('HANDESUBMIT');
    setSubmitted(true);
    setLoggingIn(true);
    login(username, password)
      .then((resp) => {
        console.log(resp.data);
        saveUser(resp.data);
        dispatch(userActions.login(resp.data))
        nav('/home');
      })
      .catch((err) => {
        // if (err.response && err.response.status === 401) {
        // //   setErrorMessage('Ошибка авторизации');
        //   setLoggingIn(false);
        // } else {
        // //   setErrorMessage(err.message);
        //   setLoggingIn(false);
        // }
      })

      .finally(() => setLoggingIn(false));
  };
  const handleChange = (e) => {
    if (e.target.name === 'username') {
      setUsername(e.target.value);
    } else {
      setPassword(e.target.value);
    }
  };

  return (
    <div className="col-md-6 me-0">
      <h2>Вход</h2>
      {/* {errorMessage && <div className="alert alert-danger mt-1 me-0 ms-0">{errorMessage}</div>} */}

      <form name="form" onSubmit={(e) => handleSubmit(e)}>
        <div className="form-group">
          <label htmlFor="username">Логин</label>
          <input
            type="text"
            className={'form-control' + (submitted && !username ? ' is-invalid' : '')}
            name="username"
            value={username}
            onChange={handleChange}
          />
          {submitted && !username && (
            <div className="help-block text-danger">Введите имя пользователя</div>
          )}
        </div>
        <div className="form-group">
          <label htmlFor="password">Пароль</label>
          <input
            type="password"
            className={'form-control' + (submitted && !password ? ' is-invalid' : '')}
            name="password"
            value={password}
            onChange={handleChange}
          />
          {submitted && !password && <div className="help-block text-danger">Введите пароль</div>}
        </div>
        <div className="form-group mt-2">
          <button className="btn btn-primary">
            {loggingIn && (
              <span
                className="spinner-border spinner-border-sm"
                role="status"
                aria-hidden="true"></span>
            )}
            Вход
          </button>
        </div>
      </form>
    </div>
  );
}

export default Login;
