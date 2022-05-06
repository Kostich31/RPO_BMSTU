import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import BackendService from "../services/BackendService";
import Utils from "../Utils/Utils";
import { connect, useDispatch } from "react-redux";
import { userActions } from "../Utils/Rdx";

const Login = () => {
    const [formData, setFormData] = useState({
        username: "",
        password: ""
    });
    const [loggingIn, setLoggingIn] = useState(false);
    const [submitted, setSubmitted] = useState(false);
    // const [ errorMessage, setErrorMessage ] = useState(null);
    const nav = useNavigate();
    const dispatch = useDispatch();

    const handleSubmit = (e) => {
        e.preventDefault();
        setLoggingIn(true);
        setSubmitted(true);
        // setErrorMessage(null);
        const { username, password } = formData;
        BackendService.login(username, password)
            .then(resp => {
                console.log(resp.data);
                dispatch(userActions.login(resp.data));
                Utils.saveUser(resp.data);
                setLoggingIn(false);
                nav("/home");
            })
            .catch(err => {
                // if (err.response && err.response.status === 401)
                //     setErrorMessage("Ошибка авторизации");
                // else setErrorMessage(err.message);
                // setLoggingIn(false);
            })
    }

    return (
        <div className="col-md-6 me-0">
            {/*{*/}
            {/*    errorMessage &&*/}
            {/*    <div className="alert alert-danger mt-1 me-0 ms-0">{errorMessage}</div>*/}
            {/*}*/}
            <h2>Вход</h2>
            <form name="form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="username">Логин</label>
                    <input type="text" className={'form-control' + (submitted && !formData.username ? ' is-invalid' : '')}
                        name="username" value={formData.username}
                        onChange={(e) => setFormData({ ...formData, username: e.target.value })} />
                    {submitted && !formData.username && <div className="help-block text-danger">Введите имя пользователя</div>}
                </div>
                <div className="form-group">
                    <label htmlFor="password">Пароль</label>
                    <input type="password" className={'form-control' + (submitted && !formData.password ? ' is-invalid' : '')}
                        name="password" value={formData.password}
                        onChange={(e) => setFormData({ ...formData, password: e.target.value })} />
                    {submitted && !formData.password &&
                        <div className="help-block text-danger">Введите пароль</div>
                    }
                </div>
                <div className="form-group mt-2">
                    <button className="btn btn-primary">
                        {loggingIn && <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"> </span>}
                        Вход
                    </button>
                </div>
            </form>
        </div>
    );
};

export default connect()(Login);