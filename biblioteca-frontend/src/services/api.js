import axios from 'axios';

const ApiBackend = axios.create({baseURL: process.env.REACT_APP_API_BACKEND});

export {ApiBackend};
