import { AutenticacaoContext } from '../../contexts/autenticacao';
import { Navbar, Nav } from 'react-bootstrap';
import { useContext, useEffect, useState } from 'react';
import logo from '../../assets/logo.png'
import '../Cabecalho/style.css'

function Cabecalho() {
        
         const { logado, deslogar } = useContext(AutenticacaoContext);
         const [hiddenMenu, setHiddenMenu] = useState(true);
         
         useEffect(() => {
          setHiddenMenu(!logado);
        });



        function submitSair(e) {
            e.preventDefault();    
            deslogar();
        }


        
        return (
            <div>
                <Navbar bg="dark" variant="dark" hidden={hiddenMenu}>
                    <Navbar.Brand>
                        <img src={logo} className="d-inline-block align-top" height="40" width="45"/>
                    </Navbar.Brand>
                    <Navbar.Text className="mt-1"><h5>BIBLIOTECA</h5></Navbar.Text>
                    <Nav className="me-auto">
                        <Nav.Item>
                            <Nav.Link href="/home">HOME</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link href="/livros">LIVROS</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link onClick={submitSair}>sair</Nav.Link>
                        </Nav.Item>
                    </Nav>
                </Navbar>                
            </div>
        )
}

export default Cabecalho;
