import React, {useState, useEffect} from 'react';
import { Table } from 'react-bootstrap';
import { Link  } from 'react-router-dom';
import Carregando from '../../components/Carregando'
import {ApiBackend} from '../../services/api'
import { FaEdit } from 'react-icons/fa';
import '../../pages/Livros/style.css'

function Livros(){

  const [loading, setLoading] = useState(true);
  const [livros, setLivros] = useState([]);



  useEffect(()=> {
    buscarLivros();
  })



  async function buscarLivros() {      
    await ApiBackend.get(`/livros/pageable`)
      .then(function(response) {
        if (livros.length === 0) {
          console.log(response.data.content);
          setLivros(response.data.content);
          setLoading(false);
        }
      })
      .catch(function(error) {
        console.log(error);
        setLoading(false);
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
                            <Link to={`/livro/${livro.id}`}>
                                <FaEdit key={livro.id} title="Editar"/>
                            </Link>
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
  } else {
    return(
        <div className='mt-5'>
            {TABLE_LIVROS}
        </div>
    )
  }

}

export default Livros;

