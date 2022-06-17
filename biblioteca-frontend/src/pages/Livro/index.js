import { useParams } from 'react-router-dom';

function Livro() {
  const { id } = useParams();

  return(
    <div>
      <span>
        <h3>LIVRO COM ID: {id}</h3>
      </span>
    </div>
  )
}

export default Livro; 
