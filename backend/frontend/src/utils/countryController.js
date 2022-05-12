export function retrieveAllCountries(page, limit) {
  return axios.get(`${API_URL}/countries`);
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
