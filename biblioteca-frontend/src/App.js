import { Fragment, useContext } from 'react';
import { AutenticacaoContext } from './contexts/autenticacao';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import Livros from './pages/Livros';
import Livro from './pages/Livro';
import Login from './pages/Login'
import Home from './pages/Home';
import Cabecalho from './components/Cabecalho';
import 'react-toastify/dist/ReactToastify.css';

function App() {

  const { logado } = useContext(AutenticacaoContext);


  return (
      <BrowserRouter>
        <Fragment>
          <ToastContainer autoClose={3000} />
          <Cabecalho logado={logado}/>
          <Routes>
            <Route path="/" element={<Login/>}/>
            <Route path='/home' element={logado ? <Home/> : <Navigate to="/"/>}/>
            <Route path='/livros' element={logado ? <Livros/> : <Navigate to="/"/>}/>
            <Route path='/livro/:id' element={logado ? <Livro/> : <Navigate to="/"/>}/>
          </Routes>
        </Fragment>
      </BrowserRouter>    
  );
  
}

export default App;
