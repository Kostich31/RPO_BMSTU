export const saveUser = (user) => {
  localStorage.setItem('user', JSON.stringify(user));
};
export const removeUser = () => {
  localStorage.removeItem('user');
};
export const getToken = () => {
  let user = JSON.parse(localStorage.getItem('user'));
  return user && 'Bearer ' + user.token;
};
export const getUserName = () => {
  let user = JSON.parse(localStorage.getItem('user'));
  return user && user.login;
};
export const getUser = () => {
  return JSON.parse(localStorage.getItem('user'));
};
