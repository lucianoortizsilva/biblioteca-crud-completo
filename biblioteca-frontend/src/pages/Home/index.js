import React, {useState, useEffect} from 'react';
import '../Home/style.css';
import {ApiLivros} from '../../services/api'

function Home() {
    
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
            {
                livros.map((livro) => {
                    return(<h3 key={livro.id}>{livro.descricao}</h3>)
                })
            }                
        </div>
    )

}

export default Home;
