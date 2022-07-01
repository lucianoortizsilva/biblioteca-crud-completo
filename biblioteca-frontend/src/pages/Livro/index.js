import { useParams, useNavigate } from 'react-router-dom';
import { useContext, useState, useEffect, useCallback } from 'react'; 
import { toast } from 'react-toastify';
import { ApiBackend } from '../../services/api'
import { AutenticacaoContext } from '../../contexts/autenticacao';
import { FiPlusCircle, FiSave, FiEdit} from "react-icons/fi";
import { FaTrashAlt } from 'react-icons/fa';
import { GrView } from 'react-icons/gr';
import { Nav, Container, Form, FormGroup } from 'react-bootstrap';
import DatePicker, { registerLocale } from "react-datepicker";
import Titulo from '../../components/Titulo';
import ModalConfirma from '../../components/ModalConfirma'
import pt from 'date-fns/locale/pt-BR';
import './livro.css'
import "react-datepicker/dist/react-datepicker.css";
import 'react-datepicker/dist/react-datepicker-cssmodules.css';
import Cabecalho from '../../components/Cabecalho'
registerLocale('pt-BR', pt)

function Livro() {
  
  const {id} = useParams();
  const [loading, setLoading] = useState(true);
  const [editando, setEditando] = useState(false);
  const [removido, setRemovido] = useState(false);
  const {token, deslogar } = useContext(AutenticacaoContext);  
  const [codigo, setCodigo] = useState('');
  const [isbn, setIsbn] = useState('');
  const [autor, setAutor] = useState('');
  const [descricao, setDescricao] = useState('');
  const [dtLancamento, setDtLancamento] = useState(new Date());
  const [exibirModal, setExibirModal] = useState(false);

  const navigate = useNavigate();
  
  useEffect(()=> {
    if (id) {
      setCodigo(id);
      buscarLivroPoId();
    } else {
      setEditando(true);
    }
  },[])



  async function buscarLivroPoId() {
    await ApiBackend.get(`/livros/${id}`,{headers:{"Authorization": token}})
      .then(function(response) {
        console.log(JSON.stringify(response.data));
        let livro = response.data;
        setIsbn(livro.isbn);
        setAutor(livro.autor)
        setDescricao(livro.descricao);
        let dtArray = livro.dtLancamento.toString().split('-');
        let dtFormatada = dtArray[0] + '/' + dtArray[1] + '/' + dtArray[2];
        setDtLancamento(new Date(dtFormatada));
      })
      .catch(function(error) {
        console.log(JSON.stringify(error));
        if(error.code === 'ERR_NETWORK'){
          toast.error('Indisponível! Tente mais tarde');
        } else if(401 === error.response.data.status){
          toast.warn(error.response.data.mensagem);
          deslogar();
        } else if(error.response.data.status >= 400 && error.response.data.status <= 500) {
          console.log('Opa: ' + JSON.stringify(error));
          toast.error(error.response.data.mensagem);
        } else {
          toast.error('Erro inesperado!');
        }
      })
      .finally(()=>{
        setLoading(false);
      });
  }



  function salvar(event) {
    event.preventDefault();        
    const body = {
      'isbn' : isbn,
      'descricao' : descricao,
      'autor' : autor,
      'dtLancamento' : dtLancamento
    };    
    const header = {headers: { 'Authorization': token,'Content-Type' : 'application/json'}};    
    if(id){
      update(body, header);
    } else {
      post(body, header);
    }    
  }
  


  async function post(body, header)  {
    await ApiBackend.post(`/livros`, JSON.stringify(body), header)
    .then(response => {
      console.log(JSON.stringify(response));
      toast.success('Criado com sucesso!');
      navigate("/livros",{ replace: true });        
    })
    .catch(error => {
      console.log(JSON.stringify(error));
      if(error.code === 'ERR_NETWORK'){
        toast.error('Indisponível! Tente mais tarde');
      } else if(401 === error.response.data.status){
        toast.warn(error.response.data.mensagem);
        deslogar();
      } else if(error.response.data.status >= 400 && error.response.data.status <= 500) {
        if(error.response.data.erros){
          error.response.data.erros.map(erro => {
            toast.error(erro.campo + ': ' + erro.mensagem);
          })
        } else{
          toast.error(error.response.data.mensagem);
        }
      } else {
        toast.error('Erro inesperado!');
      }
    });
  }



  async function update(body, header)  {
    await ApiBackend.put(`/livros/${id}`, JSON.stringify(body), header)
    .then(response => {
      console.log(JSON.stringify(response));
      toast.success('Atualizado com sucesso!');
      navigate("/livros",{ replace: true });        
    })
    .catch(error => {
      console.log(JSON.stringify(error));
      if(error.code === 'ERR_NETWORK'){
        toast.error('Indisponível! Tente mais tarde');
      } else if(401 === error.response.data.status){
        toast.warn(error.response.data.mensagem);
        deslogar();
      } else if(error.response.data.status >= 400 && error.response.data.status <= 500) {
        toast.error(error.response.data.mensagem);
      } else {
        toast.error('Erro inesperado!');
      }
    })
    .finally(()=>{
      setEditando(false);
    });
  }



  const deletar = useCallback((e)=> {
    e.preventDefault();
    
    async function remover() {
      try {
        setLoading(true);
        const response = await ApiBackend.delete(`/livros/${id}`,{headers:{"Authorization": token}});
        console.log(JSON.stringify(response));
        setRemovido(true);
        toast.success('Removido com sucesso!');
        navigate("/livros",{ replace: true });        
      } catch (error) {
        if(error.code === 'ERR_NETWORK'){
          toast.error('Indisponível! Tente mais tarde');
        } else if(401 === error.response.data.status){
          toast.warn(error.response.data.mensagem);
          deslogar();
        } else if(error.response.data.status >= 400 && error.response.data.status <= 500) {
          toast.error(error.response.data.mensagem);
        } else {
          toast.error('Erro inesperado!');
        }        
      } finally {
        setLoading(false);
      }      
    }

    remover();

  }, [])



  return(
    <div className='livro'>
      <Cabecalho/>
      <Titulo descricao={id ? 'LIVRO' : 'NOVO'}>
        {
           id && editando ? <FiEdit size={25}/> :
           id && !editando ? <GrView size={25}/> : 
           <FiPlusCircle size={25}/>
        }
      </Titulo>
      <Container className='mt-5'>
        <Form onSubmit={salvar}>
          <Form.Group controlId="id">
            <Form.Label className='form-label-id'>{codigo}</Form.Label>
          </Form.Group>
          
          <Form.Group controlId="dtLancamento">
            <Form.Label>DATA LANÇAMENTO</Form.Label>
            <DatePicker className='form-control' 
            locale={pt} 
            selected={dtLancamento} 
            onChange={(date) => setDtLancamento(date)} 
            dateFormat="dd/MM/yyyy" 
            disabled={!editando} 
            readOnly={!editando}
            required/>
          </Form.Group>

          <Form.Group controlId="isbn">
            <Form.Label>ISBN</Form.Label>
            <Form.Control type="text" 
            value={isbn} 
            onChange={e => setIsbn(e.target.value)} 
            disabled={!editando} 
            readOnly={!editando}
            required/>
          </Form.Group>

          <Form.Group controlId="descricao">
            <Form.Label>DESCRIÇÃO</Form.Label>
            <Form.Control type="text" 
            value={descricao} 
            onChange={e => setDescricao(e.target.value)} 
            disabled={!editando} 
            readOnly={!editando}
            required/>
          </Form.Group>

          <Form.Group controlId="autor">
            <Form.Label>AUTOR</Form.Label>
            <Form.Control type="text" 
            value={autor} 
            onChange={e => setAutor(e.target.value)} 
            disabled={!editando} 
            readOnly={!editando}
            required/>
          </Form.Group>

          <FormGroup controlId='botoes'>
            <Nav.Link id='btn-save' onClick={salvar} disabled={!editando || removido} readOnly={!editando || removido} hidden={!editando}>
              <FiSave title="Salvar" disabled={!editando || removido} readOnly={!editando || removido}/>
            </Nav.Link>
            <Nav.Link id='btn-edit' onClick={()=> setEditando(true)} disabled={editando || removido} readOnly={editando || removido} hidden={editando}>
              <FiEdit title="Editar" disabled={editando || removido} readOnly={editando || removido}/>              
            </Nav.Link>
            <Nav.Link id='btn-delete' onClick={()=> setExibirModal(true)} hidden={!id}>
              <FaTrashAlt title="Deletar"/>
            </Nav.Link>
          </FormGroup>
        </Form>
      </Container>

      <ModalConfirma show={exibirModal}
                     onConfirme={(e)=> deletar(e)}
                     onHide={()=> setExibirModal(false)}/>
                     
    </div>
  )
}

export default Livro; 
