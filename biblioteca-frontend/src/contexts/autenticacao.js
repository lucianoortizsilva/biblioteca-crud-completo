import { useState, createContext, useEffect } from 'react';
import { useCookies } from "react-cookie";
import { ApiBackend } from '../services/api'
import { toast } from 'react-toastify';
import jwt_decode from "jwt-decode";

export const AutenticacaoContext = createContext({});

function AutenticacaoProvider({ children }) {
  
  const headers = [{"Access-Control-Allow-Origin": "*"},{"Access-Control-Allow-Headers": "access-control-allow-origin, access-control-allow-headers"}]
  const [token, setToken] = useState(null);
  const [cookie, setCookie, removeCookie] = useCookies();
  const [logado, setLogado] = useState(true);
  const [autenticando, setAutenticando] = useState(false);



  useEffect(()=> {
    if(JSON.stringify(cookie.Authorization)){
      setToken(cookie.Authorization);
      setLogado(true);
    } else {
      setLogado(false);
    }
  })



  async function logar(email, senha) {
    setAutenticando(true);
    const body = {'username': email, 'password': senha};
    await ApiBackend.post(`/login`, JSON.stringify(body), headers)
    .then(response => {
      setLogado(true);
      setToken(response.headers.authorization);
      createCookie(response.headers.authorization);
    })
    .catch(error => {
      setLogado(false);    
      setToken(null);
      toast.error(error.response.data.mensagem);
    })
    .finally(()=>{
      setAutenticando(false);
    });
  }



  async function deslogar() {
    removeCookie('Authorization');
    removeCookie('Payload');
  }



  function createCookie(authorization) {
    let jwt = jwt_decode(authorization);
    let payload = JSON.stringify(jwt);
    setCookie('Authorization', authorization);
    setCookie('Payload', payload);
  }



  return(
    <AutenticacaoContext.Provider value={{logado, autenticando, token, logar, deslogar}}>
     {children}
    </AutenticacaoContext.Provider>
  )
}

export default AutenticacaoProvider;
