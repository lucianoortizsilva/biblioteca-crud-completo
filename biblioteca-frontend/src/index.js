import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { CookiesProvider } from "react-cookie";
import AutenticacaoProvider from './contexts/autenticacao'
import './index.css';
import  'bootstrap/dist/css/bootstrap.min.css' ;

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <AutenticacaoProvider>
    <CookiesProvider>
      <React.StrictMode>
        <App />
      </React.StrictMode>
    </CookiesProvider>
  </AutenticacaoProvider>  
  
);