import Carregando from '../../components/Carregando'
import Cabecalho from '../../components/Cabecalho'
import SemRegistro from '../../components/SemRegistro'
import { useState, useEffect, useMemo } from 'react';
import { ApiBackend } from '../../services/api'
import { GrView, GrAddCircle } from 'react-icons/gr';
import { FiList } from "react-icons/fi";
import { Table } from 'react-bootstrap';
import { Link, useNavigate  } from 'react-router-dom';
import { toast } from 'react-toastify';
import Titulo from '../../components/Titulo';
import './livros.css'
import BotaoVejaMais from '../../components/BotaoVejaMais';

function Livros() {

  const [exibirBotaoVejaMais, setExibirBotaoVejaMais] = useState(true);
  const [paginaProxima, setPaginaProxima] = useState(0);
  const [loading, setLoading] = useState(true);
  const [livros, setLivros] = useState([]);
    const paginaParaPesquisar = useMemo(() => paginaProxima, [paginaProxima]);
  const navigate = useNavigate();
  
  /**
   * 
   * Load apenas ao carregar a tela pela 1@ vez
   * 
   */
  useEffect(()=> {
    return(()=> {
      setTimeout(()=> {
        buscarLivros();
      }, 1000)
    })      
  }, [])
  


  function buscarMaisLivros(){
    setLoading(true);
    setTimeout(()=> {
      buscarLivros();
    }, 1000)
  }
  


  async function buscarLivros() {
    console.log('Buscando livros da página: ' + paginaParaPesquisar );
    await ApiBackend.get(`/livros/pageable?page=${paginaParaPesquisar}`)
    .then(function(response) {
      let encontrouLivros = response.data.content.length > 0;
      if(encontrouLivros) {
        //console.log(JSON.stringify(response.data));
        //console.log(JSON.stringify(response.data.content));
        setPaginaProxima(response.data.number + 1);
          response.data.content.forEach(livro => {
            livros.push({
              id: livro.id,
              isbn: livro.isbn,
              autor: livro.autor,
              descricao: livro.descricao,
              dtLancamento: livro.dtLancamento
            })
          });
          setExibirBotaoVejaMais(!response.data.last);          
      }
    })
    .catch(function(error) {
      console.log(JSON.stringify(error));
      if(error.code === 'ERR_NETWORK'){
        toast.error('Indisponível! Tente mais tarde');
      } else if(error.response.data.status >= 400 && error.response.data.status <= 500) {
        toast.error(error.response.data.mensagem);
      } else {
        toast.error('Erro inesperado!');
      }
    })
    .finally(()=>{
      setLoading(false);
    });
  }
    
    
    
  function cadastrar(e){
    e.preventDefault();   
    navigate("/livro",{ replace: true });
  }
 

  
  return(
  <>
    <Cabecalho/>
    <Titulo descricao="LIVROS">
      <FiList size={25}/>
    </Titulo>
    <div className='acoes'>
      <a onClick={cadastrar}>
        <GrAddCircle size={25}/>
        <span>CADASTRAR NOVO</span>
      </a>
    </div>
    {livros.length === 0 ? (        
      <>
        {loading ? 
          (<><Carregando carregando={loading}/></>) : 
          (<><SemRegistro/></>)
        }
          
      </>) : (
      <>
       <Table striped bordered hover>
        <thead>
            <tr>
                <th>ISBN</th>
                <th>DESCRIÇÃO</th>
                <th>AUTOR</th>
                <th></th>                
            </tr>
        </thead>            
        <tbody>
        {
            livros.map((livro) => {
                return(
                    <tr key={livro.id}>
                        <td>{livro.isbn}</td>
                        <td>{livro.descricao}</td>
                        <td>{livro.autor}</td>                        
                        <td>
                          <div>
                            <Link className='btn-edit' to={`/livro/${livro.id}`}>
                                <GrView key={livro.id} title="Detalhes"/>
                            </Link>
                          </div>
                        </td>
                    </tr>                            
                )
            })
        }                
        </tbody>
    </Table>        

    <div className='footer'>
      {exibirBotaoVejaMais && <BotaoVejaMais desabilitar={loading} handleClick={loading ? null : buscarMaisLivros}/>}
    </div>    

    {loading && <Carregando carregando={loading}/>}
  </>
  )}
  </>)

}

export default Livros;

