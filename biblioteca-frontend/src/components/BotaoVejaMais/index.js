import { CgSearchLoading } from "react-icons/cg"

function BotaoVejaMais({desabilitar, handleClick}) {
    return(
        <a variant="primary" 
          disabled={desabilitar} 
           onClick={handleClick}
             title='Buscar mais'>
            <CgSearchLoading size={40}/> 
            <span>
            {desabilitar ? 'Carregando' : 'Veja mais'}
            </span>
        </a>
    )
}

export default BotaoVejaMais;
