import { Spinner } from 'react-bootstrap';
import './carregando.css'

function Carregando({carregando}){
    return (
        <div className='carregando'>
            <Spinner animation='grow' className={carregando ? 'spinner' : 'visually-hidden'} variant="danger"/>
            <Spinner animation='grow' className={carregando ? 'spinner' : 'visually-hidden'} variant="danger"/>
            <Spinner animation='grow' className={carregando ? 'spinner' : 'visually-hidden'} variant="danger"/>
            <Spinner animation='grow' className={carregando ? 'spinner' : 'visually-hidden'} variant="danger"/>
            <Spinner animation='grow' className={carregando ? 'spinner' : 'visually-hidden'} variant="danger"/>
        </div>
    )
}

export default Carregando;
