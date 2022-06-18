import Carregando from '../../components/Carregando'
import {useContext, useState, useEffect} from 'react';
import { ApiBackend } from '../../services/api'
import { FaEdit, FaTrashAlt } from 'react-icons/fa';
import { AutenticacaoContext } from '../../contexts/autenticacao';
import { Table } from 'react-bootstrap';
import { Link  } from 'react-router-dom';
import { toast } from 'react-toastify';
import '../../pages/Livros/style.css'

function Livros(){

  const [loading, setLoading] = useState(true);
  const [livros, setLivros] = useState([]);
  const { token, deslogar } = useContext(AutenticacaoContext);


  useEffect(()=> {
    buscarLivros();
  }, []);



  async function buscarLivros() {      
    await ApiBackend.get(`/livros/pageable`)
      .then(function(response) {
        console.log(JSON.stringify(response.data.content));
        setLivros(response.data.content);
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



  const TABLE_LIVROS  = (
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
  );



  if(loading) {
    return(<Carregando descricao="Livros" carregando={loading}/>)
  } else if(livros.length === 0){
    return(
          <div>
            <h3>SEM REGISTROS</h3>
          </div>
          )  
  } else {
    return(<div>{TABLE_LIVROS}</div>)
  }

}

export default Livros;

