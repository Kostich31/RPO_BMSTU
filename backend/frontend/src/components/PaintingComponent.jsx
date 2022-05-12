import React, { useEffect, useState } from 'react';
import {retrievePainting, createPainting, updatePainting} from '../services/BackendService';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronLeft, faSave } from '@fortawesome/free-solid-svg-icons';
import { alertActions } from '../redux/actionCreators';
import { connect } from 'react-redux';
import { Form } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';

const PaintingComponent = (props) => {
  const params = useParams();
  const [id, setId] = useState(params.id);
  const [name, setName] = useState('');
  const [artistid, setArtistid] = useState('');
  const [museumid, setMuseumid] = useState('');
  const [year, setYear] = useState('');
  const [hidden, setHidden] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    if (parseInt(id) !== -1) {
      retrievePainting(id)
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
    if (!name) err = 'Название картины должно быть указано';
    if (err) props.dispatch(alertActions.error(err));
    let painting = { id, name, artistid, museumid, year };

    if (parseInt(painting.id) === -1) {
      createPainting(painting)
        .then(() => navigate(`/paintings`))
        .catch(() => {});
    } else {
      updatePainting(painting)
        .then(() => navigate(`/paintings`))
        .catch(() => {});
    }
  };

  if (hidden) return null;
  return (
    <div className="m-4">
      <div className=" row my-2 mr-0">
        <h3>Картина</h3>
        <button
          className="btn btn-outline-secondary ml-auto"
          onClick={() => navigate(`/paintings`)}>
          <FontAwesomeIcon icon={faChevronLeft} /> Назад
        </button>
      </div>
      <Form onSubmit={onSubmit}>
        <Form.Group>
          <Form.Label>Имя</Form.Label>
          <Form.Control
            type="text"
            placeholder="Введите название картины"
            onChange={(e) => {
              setName(e.target.value);
            }}
            value={name}
            name="name"
            autoComplete="off"
          />
     
          <Form.Label>ID художника</Form.Label>
          <Form.Control
            type="text"
            placeholder="Введите ID художника картины"
            onChange={(e) => {
              setArtistid(e.target.value);
            }}
            value={artistid}
            name="countryId"
            autoComplete="off"
          />
          <Form.Label>ID музея</Form.Label>
          <Form.Control
            type="text"
            placeholder="Введите ID музея картины"
            onChange={(e) => {
              setMuseumid(e.target.value);
            }}
            value={museumid}
            name="countryId"
            autoComplete="off"
          />
          <Form.Label>Год</Form.Label>
          <Form.Control
            type="text"
            placeholder="Введите год написания картины"
            onChange={(e) => {
              setYear(e.target.value);
            }}
            value={year}
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

export default connect()(PaintingComponent);
