import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import Cabecalho from './components/Cabecalho'
import Home from './pages/Home';
import Erro from './pages/Erro';
import Livro from './pages/Livro';
import Livros from './pages/Livros';

function RoutesApp(){
  return(
    <BrowserRouter>
      <Cabecalho/>
      <Container>
        <Routes>
          <Route path="/" element={ <Home/> } />
          <Route path="*" element={ <Erro/> } />
          <Route path="/livros" element={ <Livros/> } />
          <Route path="/livro/:id" element={ <Livro/> } />
        </Routes>
      </Container>
    </BrowserRouter>
  )
}

export default RoutesApp; 
