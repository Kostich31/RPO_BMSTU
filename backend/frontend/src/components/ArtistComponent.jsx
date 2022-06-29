import React, { useEffect, useState } from 'react';
import {retrieveArtist, createArtist, updateArtist} from '../services/BackendService';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronLeft, faSave } from '@fortawesome/free-solid-svg-icons';
import { alertActions } from '../redux/actionCreators';
import { connect } from 'react-redux';
import { Form } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';

const ArtistComponent = (props) => {
  const params = useParams();
  const [id, setId] = useState(params.id);
  const [name, setName] = useState('');
  const [countryid, setCountryid] = useState('');
  const [hidden, setHidden] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    if (parseInt(id) !== -1) {
      retrieveArtist(id)
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
    if (!name) err = 'Имя художника должно быть указано';
    if (err) props.dispatch(alertActions.error(err));
    let artist = { id, name, countryid };

    if (parseInt(artist.id) === -1) {
      createArtist(artist)
        .then(() => navigate(`/artists`))
        .catch(() => {});
    } else {
      updateArtist(artist)
        .then(() => navigate(`/artists`))
        .catch(() => {});
    }
  };

  if (hidden) return null;
  return (
    <div className="m-4">
      <div className=" row my-2 mr-0">
        <h3>Художник</h3>
        <button
          className="btn btn-outline-secondary ml-auto"
          onClick={() => navigate(`/artists`)}>
          <FontAwesomeIcon icon={faChevronLeft} /> Назад
        </button>
      </div>
      <Form onSubmit={onSubmit}>
        <Form.Group>
          <Form.Label>Имя</Form.Label>
          <Form.Control
            type="text"
            placeholder="Введите имя художника"
            onChange={(e) => {
              setName(e.target.value);
            }}
            value={name}
            name="name"
            autoComplete="off"
          />
     
          <Form.Label>ID страны</Form.Label>
          <Form.Control
            type="text"
            placeholder="Введите ID страны"
            onChange={(e) => {
              setCountryid(e.target.value);
            }}
            value={countryid}
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

export default connect()(ArtistComponent);
