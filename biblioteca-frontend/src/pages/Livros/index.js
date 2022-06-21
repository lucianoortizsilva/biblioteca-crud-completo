import Carregando from '../../components/Carregando'
import Cabecalho from '../../components/Cabecalho'
import { useContext, useState, useEffect, useMemo } from 'react';
import { ApiBackend } from '../../services/api'
import { FaEdit, FaTrashAlt } from 'react-icons/fa';
import { FiList } from "react-icons/fi";
import { CgSearchLoading } from "react-icons/cg"
import { AutenticacaoContext } from '../../contexts/autenticacao';
import { Table, Button } from 'react-bootstrap';
import { Link, useNavigate  } from 'react-router-dom';
import { toast } from 'react-toastify';
import Titulo from '../../components/Titulo';
import './livros.css'

function Livros() {

  const [exibirBotaoVejaMais, setExibirBotaoVejaMais] = useState(true);
  const [paginaProxima, setPaginaProxima] = useState(0);
  const [loading, setLoading] = useState(true);
  const [livros, setLivros] = useState([]);
  
  const {token, deslogar} = useContext(AutenticacaoContext);
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
      }, 2000)
    })      
  }, [])
  


  function buscarMaisLivros(){
    setLoading(true);
    setTimeout(()=> {
      buscarLivros();
    }, 2000)
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
    
    
    
  async function deletar(id) {
    await ApiBackend.delete(`/livros/${id}`,{headers:{"Authorization": token}})
    .then(function(response) {
      console.log(JSON.stringify(response));
      setLivros(livros.filter(item => item.id !== id));
      toast.success('Removido com sucesso!');
    })
    .catch(function(error) {
      if(error.code === 'ERR_NETWORK'){
        toast.error('Indisponível! Tente mais tarde');
      } else if(401 === error.response.data.status){
        toast.warn(error.response.data.mensagem);
        deslogar();
      } else if(error.response.data.status >= 400 && error.response.data.status <= 500) {
        toast.error(error.response.data.mensagem);
      } else {
        toast.error('Erro inesperado!');
      }
    });
  }



  function cadastrar(e){
    e.preventDefault();   
    navigate("/livro",{ replace: true });
  }
 

  
  return(
  <>
    <Cabecalho/>
    <Titulo descricao="Livros">
      <FiList size={25}/>
    </Titulo>
    <div className='botoes'>
      <Button variant="primary" size="sm" onClick={cadastrar}>NOVO</Button>
    </div>
    {livros.length === 0 ? (        
      <>
        {loading ? (
                    <>
                      <Carregando carregando={loading}/>
                    </>
                    ) : (
                    <>
                    <h3>SEM REGISTROS</h3>
                    {exibirBotaoVejaMais && 
                      <div className='carregar'>
                        <Button variant="primary" disabled={loading} onClick={loading ? null : buscarMaisLivros}>
                            <CgSearchLoading size={20}/> 
                            <span>
                              {loading ? 'Loading…' : 'Veja mais'}
                            </span>
                        </Button>
                      </div>  
                    }        
                    </>
                    )
        }
          
      </>) : (
      <>
       <Table striped bordered hover>
        <thead>
            <tr>
                <th>ISBN</th>
                <th>DESCRIÇÃO</th>
                <th>AUTOR</th>
                <th>OPÇÕES</th>                
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
                            <Link className='btn-edit' to={`/livro/${livro.id}`}>
                                <FaEdit key={livro.id} title="Editar"/>
                            </Link>
                            <a className='btn-delete' onClick={()=> deletar(livro.id)}>
                              <FaTrashAlt key={livro.id} title="Deletar"/>
                            </a>
                        </td>
                    </tr>                            
                )
            })
        }                
        </tbody>
    </Table>        
    {exibirBotaoVejaMais && 
      <div className='carregar'>
        <Button variant="primary" disabled={loading} onClick={loading ? null : buscarMaisLivros}>
            <CgSearchLoading size={20}/> 
            <span>
              {loading ? 'Loading…' : 'Veja mais'}
            </span>
        </Button>
      </div>  
    }
    {loading && <Carregando carregando={loading}/>}
  </>
  )}
  </>)

}

export default Livros;
