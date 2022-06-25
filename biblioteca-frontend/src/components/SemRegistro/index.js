import { Alert, Container } from "react-bootstrap";
import { FcEmptyFilter } from 'react-icons/fc';
import './semRegistro.css'

function SemRegistro({}) {
    return(
        <>
            <Alert className='semRegistro' variant='dark'>
                <FcEmptyFilter size={35}/>
                <span>SEM REGISTROS</span>
            </Alert>
        </>
    )
}

export default SemRegistro;
