import { Navbar, Nav } from 'react-bootstrap';
import logo from '../../assets/logo.png'
import '../Cabecalho/style.css'

function Cabecalho(){
        return (
            <div>
                <Navbar bg="dark" variant="dark">
                    <Navbar.Brand>
                        <img src={logo} className="d-inline-block align-top" height="40" width="45"/>
                    </Navbar.Brand>
                    <Navbar.Text className="mt-1"><h5>BIBLIOTECA</h5></Navbar.Text>
                    <Nav className="me-auto">
                        <Nav.Item>
                            <Nav.Link href="/">HOME</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link href="/livros">LIVROS</Nav.Link>
                        </Nav.Item>
                    </Nav>
                </Navbar>                
            </div>
        )
}

export default Cabecalho;
