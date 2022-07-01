import { AutenticacaoContext } from '../../contexts/autenticacao';
import { Navbar, Nav } from 'react-bootstrap';
import { useContext } from 'react';
import { useCookies } from 'react-cookie';
import logo from '../../assets/logo.png'
import { AiOutlinePoweroff } from 'react-icons/ai'
import './cabecalho.css'

function Cabecalho() {
        
    const [cookie] = useCookies();
    const { deslogar } = useContext(AutenticacaoContext);

    function submitSair(e) {
        e.preventDefault();    
        deslogar();
    }


        
    return (
        <>
            <Navbar bg="dark" variant="dark">
                
                <Navbar.Brand>
                    <img src={logo} className="d-inline-block align-top" height="40" width="45" alt='logo'/>
                </Navbar.Brand>
                
                <Navbar.Text className="mt-2">
                    <h5>BIBLIOTECA</h5>
                </Navbar.Text>
                
                <Nav className="collapse navbar-collapse justify-content-start">
                    <Nav.Item>
                        <Nav.Link href="#">|</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link href="/home">HOME</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link href="#">|</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link href="/livros">LIVROS</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link href="#">|</Nav.Link>
                    </Nav.Item>
                </Nav>
                
                <Nav className="collapse navbar-collapse justify-content-end">
                    <Nav.Item>
                        <Nav.Link className='btn-sair' onClick={submitSair}> 
                            <AiOutlinePoweroff/>
                        </Nav.Link>
                    </Nav.Item>
                </Nav>

            </Navbar>   
            
            <div className='perfil'>
                <p>{cookie.Payload.username}</p>
            </div>             
        </>
    )
}

export default Cabecalho;
