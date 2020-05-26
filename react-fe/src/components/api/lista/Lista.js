import React, { Component } from 'react'
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button'; 
import { Growl } from 'primereact/growl';
import { Dialog } from 'primereact/dialog';

import './Lista.scss';

import { getTeseuApis } from '../../../service/TeseuApiService'
 
export default class ListaApi extends Component {

    constructor(props) {
        super(props);
        this.optionsTemplate = this.optionsTemplate.bind(this);
        this.montarEndereco = this.montarEndereco.bind(this);
        this.filtrarPorNome = this.filtrarPorNome.bind(this);
    }

    state = {
        busca: '',     
        buscaEndereco: '', 
        apis: [],
        apisFiltradas: [],
        contextos: [],
        exibirModalExclusao: false,
        displayDialogExc: true,
        position: 'center',
        apiExclusao: null  
    }           

    componentDidMount() {
       
        this.getApisAsync();
    }

    getApisAsync = () => {

        getTeseuApis()
        .then(response => response.data)
        .then(apis => {
             
            this.setState({apis: apis.content , apisFiltradas: apis.content});
        })
        .catch(error => {
            this.showError(`Erro ao buscar apis: ${error.message}`);
        })
    }

    irParaCadastro = () => {
        this.props.history.push('/criar-api');
    }
  
    deleteApi = (api) => {
        this.setState({ exibirModalExclusao: true, displayDialogExc: true, apiExclusao: api });
    }

    editarApi = (api) => {
        const { history } = this.props;
        history.push(`/alterar-api/${api.id}`);
    }

    montarEndereco(api) {
        return (
            <div>
                { `http://${api.host}/${api.path}` }
            </div>
        )
    }
  
    optionsTemplate(rowData) {
        return (
            <div>
                <i className="pi pi-pencil" onClick={() => this.editarApi(rowData)}></i>
                <i className="pi pi-times" onClick={() => this.deleteApi(rowData)}></i>
            </div>
        )
    }

    filtrarPorNome = async function(e) {
 
        await this.setState({ busca: String(e.target.value) });
  
        this.filtrarApis().then(apis => 
            
            this.setState({ apisFiltradas: apis })
        );
 
    }

    filtrarPorEnderecoApi = async function(e) {
 
        await this.setState({ buscaEndereco: String(e.target.value) });
  
        this.filtrarApis().then(apis => 
            
            this.setState({ apisFiltradas: apis })
        );
 
    }

    filtrarNome = () => new Promise((resolve, reject) => {

        try {

            const apisFiltradas = this.state.apis.filter((api) => {

                return api.name.trim().toLowerCase().includes(this.state.busca.trim().toLowerCase() );                     
            });

            resolve(apisFiltradas);

        } catch (error) {
            reject(error);
        } 

    })

    filtrarEnderecoApi = (apis) => new Promise((resolve, reject) => {

        try {

            const apisFiltradas = apis.filter((api) => {
 
                return `http://${api.host}/${api.path}`.trim().toLowerCase().includes(this.state.buscaEndereco.trim().toLowerCase() );                     
            });

            resolve(apisFiltradas);

        } catch (error) {
            reject(error);
        } 

    })
   
    filtrarApis = () => new Promise((resolve, reject) => {
 
        try { 
            this.filtrarNome()
                .then(apis => 
                        this.filtrarEnderecoApi(apis)
                            .then(apis =>    
                                {                                
                                    resolve(apis);
                                })                                  
                            );
        } catch (error) {
            reject(error);
        } 
    })

    handleDelete = (e, name) => {

        e && e.preventDefault && e.preventDefault();
         
        this.showSuccess("Api excluída com sucesso!");

        setTimeout(() => {
            this.getApisAsync();
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
                    <legend>Lista de apis</legend>
                    <div className={'container'}>
                        <div style={{width:'100%'}}>
                            <div style={{width:'800px'}}>
                                <fieldset className="filtro">
                                <legend >Filtros</legend>
                                <div className="p-grid">
                                    <div className="p-col-1 p-md-1">
                                        <label className="labelFiltro" htmlFor="filtroNome">Nome:</label>
                                    </div>
                                    <div className="p-col-1 p-md-4">
                                        <InputText id="filtroNome" value={this.state.busca} onChange={(e) => this.filtrarPorNome(e)} />
                                    </div>
                                    <div className="p-col-1 p-md-1">
                                        <label className="labelFiltro" htmlFor="filtroEndereco">Endereço:</label>
                                    </div>
                                    <div className="p-col-1 p-md-4">
                                        <InputText id="filtroEndereco" 
                                                value={this.state.buscaEndereco} 
                                                onChange={(e) => this.filtrarPorEnderecoApi(e)} />
                                    </div>    
                                </div> 
                                </fieldset>
                            </div>
                            <div className="botaoNew">
                                <Button label="Nova Api" onClick={() => this.irParaCadastro()} />
                            </div>
                        </div>
                    </div>
                    {this.state.exibirModalExclusao && 
                        <Dialog header={`Api ${this.state.apiExclusao.name}`} 
                                        visible={this.state.displayDialogExc} 
                                        style={{width: '50vw'}} 
                                        onHide={() => this.onHide('displayDialogExc')} 
                                        footer={this.renderFooter('displayDialogExc')}
                        >            
                            <div>        
                                <Growl ref={(el) => this.growl = el} />
                                <p>{`Confirma a exclusão da api ${this.state.apiExclusao.name} ?`}</p>    
                            </div>  
                        </Dialog>   
                    }
                    <div className="dt-consulta">
                        <DataTable className="dt-consulta-api" value={ this.state.apisFiltradas }> 
                            <Column style={{ width: "30%" }} field="name" header="Nome" />              
                            <Column style={{ width: "60%" }} field="visivel" body={this.montarEndereco} header="Endereço" />
                            <Column style={{ width: "10%" }} body={this.optionsTemplate} />
                        </DataTable>
                    </div>
                </fieldset>
            </div>
        )
    }
}