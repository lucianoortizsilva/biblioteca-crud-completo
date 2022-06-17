import { useParams } from 'react-router-dom';

function Livro() {
  const { id } = useParams();

  return(
    <div>
      <h2>Livro</h2>
      <span>
        <h3>ID: {id}</h3>
      </span>
    </div>
  )
}

export default Livro; 
