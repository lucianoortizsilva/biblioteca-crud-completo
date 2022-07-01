import './modalConfirma.css';
const { Button, Modal } = require("react-bootstrap");

function ModalConfirma({show, onHide, onConfirme}) {

    return (
        <Modal show={show} size="lg" aria-labelledby="contained-modal-title-vcenter" centered>
            <Modal.Header>
                <Modal.Title id="contained-modal-title-vcenter">
                    Atenção!
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>Confirmar Exclusão ?</p>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={onConfirme} variant="success">Confirmar</Button>
                <Button onClick={onHide} variant="danger">Cancelar</Button>
            </Modal.Footer>
        </Modal>
    )
}

export default ModalConfirma;
