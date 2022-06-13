import React, {useState, useEffect} from 'react';
import { Table } from 'react-bootstrap';
import { Link  } from 'react-router-dom';

import {ApiLivros} from '../../services/api'

function Livros(){

  const [livros, setLivros] = useState([]);
        
  useEffect(()=> {
      async function loadLivros(){
          if(livros.length === 0){
              await ApiLivros.get(`/pageable`)
              .then(function(response) {
                  setLivros(response.data.content);
              })
              .catch(function(error){
                  console.log(error);
              });
          }
      }
      loadLivros();
  })

  
  return(
    <div className='mt-5'>
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
                            <td><Link to={`/livro/${livro.id}`}>editar</Link></td>
                        </tr>                            
                    )
                })
            }                
            </tbody>
        </Table>            
    </div>
)

}

export default Livros;
