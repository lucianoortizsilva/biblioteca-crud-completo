import { Spinner } from 'react-bootstrap';
import './carregando.css'

function Carregando({descricao, carregando}){
    return (
        <div className='carregando'>
            <Spinner animation='grow' className={carregando ? 'spinner' : 'visually-hidden'} variant="danger"/>
            <Spinner animation='grow' className={carregando ? 'spinner' : 'visually-hidden'} variant="danger"/>
            <Spinner animation='grow' className={carregando ? 'spinner' : 'visually-hidden'} variant="danger"/>
            <Spinner animation='grow' className={carregando ? 'spinner' : 'visually-hidden'} variant="danger"/>
            <Spinner animation='grow' className={carregando ? 'spinner' : 'visually-hidden'} variant="danger"/>
            <span className={carregando ? '' : 'visually-hidden'}>Carregando {descricao} ...</span>
        </div>
    )
}

export default Carregando;
