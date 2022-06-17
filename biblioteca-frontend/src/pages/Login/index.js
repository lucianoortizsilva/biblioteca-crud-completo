import { AutenticacaoContext } from '../../contexts/autenticacao';
import { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';
import logo from '../../assets/logo.png';
import './style.css';


function Login() {
  
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const { logar, logado, autenticando } = useContext(AutenticacaoContext);
  

  
  useEffect(()=>{
    if (logado) {
      navigate('/home');
    }
  })



  function submitLogin(e){
    e.preventDefault();    
    if (email !== '' && senha !== '') {
      logar(email, senha);
    }
  }



  return (
    <div className="container-center">
      <div className="login">
        <div className="login-area">
          <img src={logo} alt="Sistema Logo" />
        </div>
        <form onSubmit={submitLogin}>
          <h1>Entrar</h1>
          <input type="text" placeholder="email@email.com" value={email} onChange={ (e) => setEmail(e.target.value) } required/>
          <input type="password" placeholder="*******" value={senha} onChange={(e) => setSenha(e.target.value) } required/>
          <button type="submit">{autenticando ? 'Carregando...' : 'Acessar'}</button>
        </form>  
        <Link to="#">Criar uma conta</Link>
      </div>
    </div>
  );
}

export default Login;
