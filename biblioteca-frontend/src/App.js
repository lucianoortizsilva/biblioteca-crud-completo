import { useContext } from 'react';
import { AutenticacaoContext } from './contexts/autenticacao';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import Livros from './pages/Livros';
import Livro from './pages/Livro';
import Login from './pages/Login'
import Erro from './pages/Erro';
import Home from './pages/Home';
import 'react-toastify/dist/ReactToastify.css';
import GlobalStyle from './styles/global';

function App() {

  const { logado } = useContext(AutenticacaoContext);

  return (
      <BrowserRouter>
        <GlobalStyle/>
        <ToastContainer autoClose={3000} />
        <Routes>
          <Route exact path="/" element={<Login/>}/>
          <Route exact path='/home' element={logado ? <Home/> : <Navigate to="/"/>}/>
          <Route exact path='/livros' element={logado ? <Livros/> : <Navigate to="/"/>}/>
          <Route exact path='/livro' element={logado ? <Livro/> : <Navigate to="/"/>}/>
          <Route exact path='/livro/:id' element={logado ? <Livro/> : <Navigate to="/"/>}/>
          <Route exact path='*' element={<Erro/>}/>
        </Routes>
      </BrowserRouter>    
  );
  
}

export default App;
