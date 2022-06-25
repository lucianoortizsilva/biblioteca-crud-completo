import './modalConfirma.css';
const { Button, Modal } = require("react-bootstrap");

function ModalConfirma(props) {

    return (
        <Modal {...props}size="lg" aria-labelledby="contained-modal-title-vcenter" centered>
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    Atenção!
                </Modal.Title>
            </Modal.Header>        
            <Modal.Body>
                <p>Confirmar Exclusão ?</p>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={props.onConfirme} variant="success">Confirmar</Button>
                <Button onClick={props.onHide} variant="danger">Cancelar</Button>
            </Modal.Footer>
        </Modal>
    )
}

export default ModalConfirma;
