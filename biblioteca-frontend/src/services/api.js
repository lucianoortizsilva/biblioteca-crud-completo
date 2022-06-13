import axios from 'axios';

const ApiLivros = axios.create({
  baseURL: process.env.REACT_APP_API_LIVROS
});

export {ApiLivros};