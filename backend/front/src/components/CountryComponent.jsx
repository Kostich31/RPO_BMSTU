import React, { Component, useEffect, useState } from 'react';
import BackendService from '../services/BackendService';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronLeft, faSave } from '@fortawesome/free-solid-svg-icons';
import { alertActions } from "../Utils/Rdx";
import { connect } from "react-redux";
import { Form } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";

const CountryComponent = props => {
    const params = useParams();
    const [id, setId] = useState(params.id);
    const [name, setName] = useState("");
    const [hidden, setHidden] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (parseInt(id) !== -1) {
            BackendService.retrieveCountry(id)
                .then((resp) => {
                    setName(resp.data.name)
                })
                .catch(() => setHidden(true))
        }
    }, []); // [] нужны для вызова useEffect только один раз при инициализации компонента
    // это нужно для того, чтобы в состояние name каждый раз не записывалось значение из БД

    const onSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        let err = null;
        if (!name) err = "Название страны должно быть указано";
        if (err) props.dispatch(alertActions.error(err));
        let country = { id, name };

        if (parseInt(country.id) === -1) {
            BackendService.createCountry(country)
                .then(() => navigate(`/countries`))
                .catch(() => {
                })
        } else {
            BackendService.updateCountry(country)
                .then(() => navigate(`/countries`))
                .catch(() => {
                })
        }
    }

    if (hidden)
        return null;
    return (
        <div className="m-4">
            <div className=" row my-2 mr-0">
                <h3>Страна</h3>
                <button className="btn btn-outline-secondary ml-auto"
                    onClick={() => navigate(`/countries`)}
                ><FontAwesomeIcon icon={faChevronLeft} />{' '}Назад</button>
            </div>
            <Form onSubmit={onSubmit}>
                <Form.Group>
                    <Form.Label>Название</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Введите название страны"
                        onChange={(e) => { setName(e.target.value) }}
                        value={name}
                        name="name"
                        autoComplete="off"
                    />
                </Form.Group>
                <button className="btn btn-outline-secondary" type="submit">
                    <FontAwesomeIcon icon={faSave} />{' '}
                    Сохранить
                </button>
            </Form>
        </div>
    )
}

export default connect()(CountryComponent);
