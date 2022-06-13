import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Cabecalho from './components/Cabecalho'
import Home from './pages/Home';
import Erro from './pages/Erro';

function RoutesApp(){
  return(
    <BrowserRouter>
      <Cabecalho/>
      <Routes>
        <Route path="/" element={ <Home/> } />
        <Route path="*" element={ <Erro/> } />
      </Routes>
    </BrowserRouter>
  )
}

export default RoutesApp; 
