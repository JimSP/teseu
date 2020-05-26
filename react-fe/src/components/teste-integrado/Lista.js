import React, { Component } from 'react'

import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button'; 
import { Growl } from 'primereact/growl';
import { Dialog } from 'primereact/dialog';

import './Lista.scss';

import { getTeseuTestes } from '../../service/TeseuTesteService'
 
export default class ListaTesteIntegrado extends Component {

    constructor(props) {
        super(props);
        this.optionsTemplate = this.optionsTemplate.bind(this);
      
        this.filtrarPorNome = this.filtrarPorNome.bind(this);
    }

    state = {
        busca: '',     
        teseuTestes: [],
        teseuTestesFiltrados: [],
        exibirModalExclusao: false,
        displayDialogExc: true,
        position: 'center',
        teseuTesteExclusao: null  
    }           

    componentDidMount() {
        this.getTeseuTestesAsync();
     }

     getTeseuTestesAsync = () => {
        getTeseuTestes()
        .then(response => response.data)
        .then(teseuTestes => {
            this.setState({teseuTestes: teseuTestes.content , teseuTestesFiltrados: teseuTestes.content}); 
        })
        .catch(error => {
            this.showError(`Erro ao buscar testes: ${error.message}`);
        })
    }

    irParaCadastro = () => {
        this.props.history.push('/criar-teseu-teste');
    }
 
    deleteTeseuTeste = (teseuTeste) => {
        this.setState({ exibirModalExclusao: true, displayDialogExc: true, teseutTesteExclusao: teseuTeste });
    }

    editarTeseuTeste = (teseuTeste) => {
        const { history } = this.props;
        history.push(`/alterar-teseu-teste/${teseuTeste.id}`);
    }
 
    optionsTemplate(rowData) {
        return (
            <div>
                <i className="pi pi-eject" onClick={() => this.executarTeseuTeste(rowData)}></i>
                <i className="pi pi-pencil" onClick={() => this.editarTeseuTeste(rowData)}></i>
                <i className="pi pi-times" onClick={() => this.deleteTeseuTeste(rowData)}></i>
            </div>
        )
    }

    filtrarPorNome = async function(e) {
 
        await this.setState({ busca: String(e.target.value) });
  
        this.filtrarTeseuTestes().then(apis => 
            
            this.setState({ teseuTestesFiltrados: apis })
        );
 
    }
 
    filtrarNome = () => new Promise((resolve, reject) => {

        try {

            const teseuTestesFiltrados = this.state.teseuTestes.filter((teseuTeste) => {

                return teseuTeste.name.trim().toLowerCase().includes(this.state.busca.trim().toLowerCase() );                     
            });

            resolve(teseuTestesFiltrados);

        } catch (error) {
            reject(error);
        } 

    })
 
    filtrarTeseuTestes = () => new Promise((resolve, reject) => {
 
        try{ 
 
            this.filtrarNome()
                .then(teseuTestes => 
                                {                                
                                    resolve(teseuTestes);
                                })                                  
        } catch (error) {
            reject(error);
        } 

    })
   
    handleDelete = (e, name) => {

        e && e.preventDefault && e.preventDefault();
         
        this.showSuccess("Teste excluído com sucesso!");

        setTimeout(() => {
            this.getTeseuTestesAsync();
            this.onHide('displayDialogExc');
        }, 2000);
    }

    onHide = (name) => {
        this.setState({
            [`${name}`]: false
        });

        this.props.onClose && this.props.onClose();
    }
 
    renderFooter = (name) => {
        return (
            <div>
                <Button label="Salvar" icon="pi pi-check" onClick={(e) => this.handleDelete(e, name)} />
                <Button label="Cancelar" icon="pi pi-times" onClick={() => this.onHide(name)} className="p-button-secondary"/>
            </div>
        );
    }

    showSuccess = (detail) => {
 
        this.growl.show({severity: 'success', summary: 'Success Message', detail});
    }

    showError = (detail) => {
 
        this.growl.show({severity: 'error', summary: 'Error Message', detail}); 
    }

    render() {
  
        return (
            <div>
                <Growl ref={(el) => this.growl = el} />     
                <fieldset className="moldura">
                    <legend>Lista de testes regressivos</legend>
                    <div className={'container'}>
                        <div style={{width:'100%'}}>
                            <div style={{width:'800px'}}>
                                <fieldset className="filtro">
                                    <legend >Filtro</legend>
                                    <div className="p-grid">
                                        <div className="p-col-1 p-md-1">
                                            <label className="labelFiltro" htmlFor="filtroNome">Nome:</label>
                                        </div>
                                        <div className="p-col-1 p-md-4">
                                            <InputText id="filtroNome" value={this.state.busca} onChange={(e) => this.filtrarPorNome(e)} />
                                        </div>
                                    </div> 
                                </fieldset>
                            </div>
                            <div className="botaoCrud">
                                <Button label="Novo Teste" onClick={() => this.irParaCadastro()} />
                            </div>
                        </div>
                    </div>
                    {this.state.exibirModalExclusao && 
                        <Dialog header={`Teste integrado ${this.state.teseuTesteExclusao.name}`} 
                                        visible={this.state.displayDialogExc} 
                                        style={{width: '50vw'}} 
                                        onHide={() => this.onHide('displayDialogExc')} 
                                        footer={this.renderFooter('displayDialogExc')}
                        >            
                            <div>        
                                <Growl ref={(el) => this.growl = el} />
                                <p>{`Confirma a exclusão do teste integrado ${this.state.teseuTesteExclusao.name} ?`}</p>    
                            </div>  
                        </Dialog>   
                    }
                    <div className="dt-consulta">
                        <DataTable className="dt-consulta-teseu-teste" value={ this.state.teseuTestesFiltrados }> 
                            <Column style={{ width: "30%" }} field="name" header="Nome do Teste Regressivo" />              
                            <Column style={{ width: "10%" }} body={this.optionsTemplate} />
                        </DataTable>
                    </div>
                </fieldset>
            </div>
        )
    }
}
