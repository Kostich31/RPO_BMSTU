import React from 'react';
import { Navbar, Nav } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars, faHome, faUser } from '@fortawesome/free-solid-svg-icons';
import { Link, useNavigate } from 'react-router-dom';
import { getUserName, getUser } from '../utils/utils';
import { logout } from '../services/BackendService';
import { useDispatch } from 'react-redux';
import { userActions } from '../redux/actionCreators';

function NavigationBar({toggleSideBar}) {
  const navigate = useNavigate();

  const goHome = () => {
    console.log('GOHOME');
    navigate('/home');
  };

  const userName = getUserName();
  const user = getUser();
  const dispatch = useDispatch();
  return (
    <Navbar bg="light" expand="lg">
      <button
        type="button"
        className="btn btn-outline-secondary mr-2"
        onClick={toggleSideBar}>
        <FontAwesomeIcon icon={faBars} />
      </button>
      <Navbar.Brand>My RPO</Navbar.Brand>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="me-auto">
          <Nav.Link as={Link} to="/home">
            Home
          </Nav.Link>
          <Nav.Link onClick={() => goHome()}>Link</Nav.Link>
        </Nav>
        <Navbar.Text>{userName}</Navbar.Text>
        {userName && (
          <Nav.Link
            onClick={() => {
              logout().then(() => {
                dispatch(userActions.logout());
                navigate('/login');
              });
            }}>
            <FontAwesomeIcon icon={faUser} fixedWidth />
            Выход
          </Nav.Link>
        )}
        {!userName && (
          <Nav.Link as={Link} to="/login">
            <FontAwesomeIcon icon={faUser} fixedWidth />
            Вход
          </Nav.Link>
        )}
      </Navbar.Collapse>
    </Navbar>
  );
}

export default NavigationBar;
