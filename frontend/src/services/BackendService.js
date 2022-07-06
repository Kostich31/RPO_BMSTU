import React from 'react';
import axios from 'axios';
import { getToken } from '../utils/utils';
import store from '../redux/index.js';
import { alertActions } from '../redux/actionCreators';

const API_URL = 'http://localhost:8081/api/v1';
const AUTH_URL = 'http://localhost:8081/auth';

export const login = (login, password) => {
  console.log('LOGGED IN');
  return axios.post(`${AUTH_URL}/login`, { login, password });
};
export const logout = () => {
  console.log('LOGOUT');
  return axios.get(
    `${AUTH_URL}/logout`,
    // , { headers: { Authorization: getToken() } }
  );
};

function showError(msg) {
  store.dispatch(alertActions.error(msg));
}
axios.interceptors.request.use(
  (config) => {
    store.dispatch(alertActions.clear());
    let token = getToken();
    if (token) config.headers.Authorization = token;
    return config;
  },
  (error) => {
    console.log('ERROR', error);
    showError(error.message);
    return Promise.reject(error);
  },
);
axios.interceptors.response.use(undefined, (error) => {
  if (error.response && error.response.status && [401, 403].indexOf(error.response.status) !== -1) {
    console.log('ERROR', error);
    showError('Ошибка авторизации');
  } else if (error.response && error.response.data && error.response.data.message) {
    console.log('ERROR', error);
    showError(error.response.data.message);
  } else {
    console.log('ERROR', error);
    showError(error.message);
  }
  return Promise.reject(error);
});

//Countries

export function retrieveAllCountries(page, limit) {
  return axios.get(`${API_URL}/countries?page=${page}&limit=${limit}`);
}
export function retrieveCountry(id) {
  return axios.get(`${API_URL}/countries/${id}`);
}
export function createCountry(country) {
  return axios.post(`${API_URL}/countries`, country);
}
export function updateCountry(country) {
  return axios.put(`${API_URL}/countries/${country.id}`, country);
}
export function deleteCountries(countries) {
  return axios.post(`${API_URL}/deletecountries`, countries);
}


//Artists

export function retrieveAllArtists(page, limit) {
  return axios.get(`${API_URL}/artists?page=${page}&limit=${limit}`);
}
export function retrieveArtist(id) {
  return axios.get(`${API_URL}/artists/${id}`);
}
export function createArtist(artist) {
  return axios.post(`${API_URL}/artists`, artist);
}
export function updateArtist(artist) {
  return axios.put(`${API_URL}/artists/${artist.id}`, artist);
}
export function deleteArtists(artists) {
  return axios.post(`${API_URL}/deleteartists`, artists);
}


//Museums

export function retrieveAllMuseums(page, limit) {
  return axios.get(`${API_URL}/museums?page=${page}&limit=${limit}`);
}
export function retrieveMuseum(id) {
  return axios.get(`${API_URL}/museums/${id}`);
}
export function createMuseum(museum) {
  return axios.post(`${API_URL}/museums`, museum);
}
export function updateMuseum(museum) {
  console.log("museu", museum)
  return axios.put(`${API_URL}/museums/${museum.id}`, museum);
}
export function deleteMuseums(museums) {
  return axios.post(`${API_URL}/deletemuseums`, museums);
}

//Paintings

export function retrieveAllPaintings(page, limit) {
  return axios.get(`${API_URL}/paintings?page=${page}&limit=${limit}`);
}
export function retrievePainting(id) {
  return axios.get(`${API_URL}/paintings/${id}`);
}
export function createPainting(painting) {
  return axios.post(`${API_URL}/paintings`, painting);
}
export function updatePainting(painting) {
  return axios.put(`${API_URL}/paintings/${painting.id}`, painting);
}
export function deletePaintings(paintings) {
  return axios.post(`${API_URL}/deletepaintings`, paintings);
}


//Users

export function retrieveAllUsers(page, limit) {
  return axios.get(`${API_URL}/users?page=${page}&limit=${limit}`);
}
export function retrieveUser(id) {
  return axios.get(`${API_URL}/users/${id}`);
}
export function createUser(user) {
  return axios.post(`${API_URL}/users`, user);
}
export function updateUser(user) {
  return axios.put(`${API_URL}/users/${user.id}`, user);
}
export function deleteUsers(users) {
  return axios.post(`${API_URL}/deleteusers`, users);
}
