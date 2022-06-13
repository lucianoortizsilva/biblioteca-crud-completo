import { Container, Navbar, Nav } from 'react-bootstrap';
import logo from '../../assets/logo.png'
import '../Cabecalho/style.css'

function Cabecalho(){
        return (
            <div>
                <Navbar bg="dark" variant="dark">
                <Container>
                    <Navbar.Brand>
                    <img src={logo} className="d-inline-block align-top" height="40" width="45"/>
                    </Navbar.Brand>
                    <Nav className="me-auto">
                        <Navbar.Text className="mt-1">
                            <h5>Biblioteca</h5>                            
                        </Navbar.Text>                    
                    </Nav>
                </Container>
                </Navbar>                
            </div>
        )
}

export default Cabecalho;
