import { useEffect } from 'react';
import erro from '../../assets/erro.png'
import { Link, useNavigate  } from 'react-router-dom';
import '../Erro/style.css'


function Erro() {

    const navigate = useNavigate();

    useEffect(()=> {
      setTimeout(()=> {
        navigate("/", { replace: true });
      }, 5000)
    });

    return (
      <div className='pnl-erro'>
        <img src={erro} className="d-inline-block align-top"/>
        <h2>Pagina não encontrada!</h2>
        <Link to="/">HOME</Link>
      </div>
    );
  }
  
export default Erro;
