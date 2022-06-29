import React, { useState, useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrash, faEdit, faPlus } from '@fortawesome/free-solid-svg-icons';
import Alert from './Alert';
import BackendService, { retrieveAllMuseums, deleteMuseums } from '../services/BackendService';
import PaginationComponent from './PaginationComponent';

import { useNavigate } from 'react-router-dom';


const CountryListComponent = (props) => {
  const [message, setMessage] = useState();
  const [museums, setMuseums] = useState([]);
  const [selectedMuseums, setSelectedMuseums] = useState([]);
  const [show_alert, setShowAlert] = useState(false);
  const [checkedItems, setCheckedItems] = useState([]);
  const [hidden, setHidden] = useState(false);
  const [page, setPage] = useState(0);
  const [totalCount, setTotalCount] = useState(0);
  const limit = 3;
  const navigate = useNavigate();
  const setChecked = (v) => {
    setCheckedItems(Array(museums.length).fill(v));
  };
  const onPageChanged = (cp) => {
    refreshMuseums(cp - 1);
  };
  const handleCheckChange = (e) => {
    const idx = e.target.name;
    const isChecked = e.target.checked;
    let checkedCopy = [...checkedItems];
    checkedCopy[idx] = isChecked;
    setCheckedItems(checkedCopy);
  };
  const handleGroupCheckChange = (e) => {
    const isChecked = e.target.checked;
    setChecked(isChecked);
  };
  const deleteMuseumsClicked = () => {
    let x = [];
    museums.map((t, idx) => {
      if (checkedItems[idx]) {
        x.push(t);
      }
      return 0;
    });
    if (x.length > 0) {
      var msg;
      if (x.length > 1) {
        msg = 'Пожалуйста подтвердите удаление ' + x.length + ' стран';
      } else {
        msg = 'Пожалуйста подтвердите удаление музея ' + x[0].name;
      }
      setShowAlert(true);
      setSelectedMuseums(x);
      setMessage(msg);
    }
  };
  const refreshMuseums = (cp) => {
    console.log("CP", cp);
    retrieveAllMuseums(cp, limit)
      .then((resp) => {
        setMuseums(resp.data.content);
        setHidden(false);
        setTotalCount(resp.data.totalElements);
        setPage(cp);
      })
      .catch(() => {
        setHidden(true);
        setTotalCount(0);
      })
      .finally(() => setChecked(false));
  };
  useEffect(() => {
    refreshMuseums(0);
  }, []);
  const updateArtistClicked = (id) => {
    navigate(`/museums/${id}`);
  };
  const onDelete = () => {
    deleteMuseums(selectedMuseums)
      .then(() => refreshMuseums(0))
      .catch(() => {});
  };
  const closeAlert = () => {
    setShowAlert(false);
  };
  const addArtistClicked = () => {
    navigate(`/museums/-1`);
  };
  if (hidden) return null;


  // console.log(museums)

  return (
    <div className="m-4">
      <div className="row my-2">
        <h3>Музеи</h3>
        <div className="btn-toolbar">
          <div className="btn-group ms-auto">
            <button className="btn btn-outline-secondary" onClick={addArtistClicked}>
              <FontAwesomeIcon icon={faPlus} /> Добавить
            </button>
          </div>
          <div className="btn-group ms-2">
            <button className="btn btn-outline-secondary" onClick={deleteMuseumsClicked}>
              <FontAwesomeIcon icon={faTrash} /> Удалить
            </button>
          </div>
        </div>
      </div>
      <div className="row my-2 me-0">
        <PaginationComponent
          totalRecords={totalCount}
          pageLimit={limit}
          pageNeighbours={1}
          onPageChanged={onPageChanged}
        />
        <table className="table table-sm">
          <thead className="thead-light">
            <tr>
              <th>Название</th>
              <th>Расположение</th>
              <th>
                <div className="btn-toolbar pb-1">
                  <div className="btn-group ms-auto">
                    <input type="checkbox" onChange={handleGroupCheckChange} />
                  </div>
                </div>
              </th>
            </tr>
          </thead>
          <tbody>
            {museums &&
                
              museums.map((museum, index) => (
                <tr key={museum.id}>
                  <td>{museum.name}</td>
                  <td>{museum.location}</td>
                  <td>
                    <div className="btn-toolbar">
                      <div className="btn-group ms-auto">
                        <button
                          className="btn btn-outline-secondary btn-sm btn-toolbar"
                          onClick={() => updateArtistClicked(museum.id)}>
                          <FontAwesomeIcon icon={faEdit} fixedWidth />
                        </button>
                      </div>
                      <div className="btn-group ms-2 mt-1">
                        <input
                          type="checkbox"
                          name={index}
                          checked={checkedItems.length > index ? checkedItems[index] : false}
                          onChange={handleCheckChange}
                        />
                      </div>
                    </div>
                  </td>
                </tr>
              ))}
          </tbody>
        </table>
      </div>
      <Alert
        title="Удаление"
        message={message}
        ok={onDelete}
        close={closeAlert}
        modal={show_alert}
        cancelButton={true}
      />
    </div>
  );
};
export default CountryListComponent;
