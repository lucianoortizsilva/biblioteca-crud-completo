import './titulo.css'

function Titulo({children, descricao}){
    return (
        <div className="title">
        {children}
        <span>{descricao}</span>
      </div>
    )
}

export default Titulo;
