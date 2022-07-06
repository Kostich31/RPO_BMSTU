import React, { useEffect, useState } from 'react';
import {retrieveMuseum, createMuseum, updateMuseum} from '../services/BackendService';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronLeft, faSave } from '@fortawesome/free-solid-svg-icons';
import { alertActions } from '../redux/actionCreators';
import { connect } from 'react-redux';
import { Form } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';

const MuseumComponent = (props) => {
  const params = useParams();
  const [id, setId] = useState(params.id);
  const [name, setName] = useState('');
  const [location, setLocation] = useState('');
  const [hidden, setHidden] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    if (parseInt(id) !== -1) {
      retrieveMuseum(id)
        .then((resp) => {
          setName(resp.data.name);
        })
        .catch(() => setHidden(true));
    }
  }, []);

  const onSubmit = (event) => {
    event.preventDefault();
    event.stopPropagation();
    let err = null;
    if (!name) err = 'Название музея должно быть указано';
    if (err) props.dispatch(alertActions.error(err));
    let museum = { id, name, location };

    if (parseInt(museum.id) === -1) {
      createMuseum(museum)
        .then(() => navigate(`/museums`))
        .catch(() => {});
    } else {
      updateMuseum(museum)
        .then(() => navigate(`/museums`))
        .catch(() => {});
    }
  };

  if (hidden) return null;
  return (
    <div className="m-4">
      <div className=" row my-2 mr-0">
        <h3>Музей</h3>
        <button
          className="btn btn-outline-secondary ml-auto"
          onClick={() => navigate(`/museums`)}>
          <FontAwesomeIcon icon={faChevronLeft} /> Назад
        </button>
      </div>
      <Form onSubmit={onSubmit}>
        <Form.Group>
          <Form.Label>Имя</Form.Label>
          <Form.Control
            type="text"
            placeholder="Введите название музея"
            onChange={(e) => {
              setName(e.target.value);
            }}
            value={name}
            name="name"
            autoComplete="off"
          />
     
          <Form.Label>Расположение</Form.Label>
          <Form.Control
            type="text"
            placeholder="Введите расположение музея"
            onChange={(e) => {
              setLocation(e.target.value);
            }}
            value={location}
            name="countryId"
            autoComplete="off"
          />
        </Form.Group>
        <button className="btn btn-outline-secondary" type="submit">
          <FontAwesomeIcon icon={faSave} /> Сохранить
        </button>
      </Form>
    </div>
  );
};

export default connect()(MuseumComponent);
