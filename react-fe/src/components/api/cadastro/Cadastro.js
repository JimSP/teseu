import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';

import { Growl } from 'primereact/growl';
import { InputText } from 'primereact/inputtext'; 
import { Dropdown } from 'primereact/dropdown'; 
import { Button } from 'primereact/button'; 
import { TabView, TabPanel } from 'primereact/tabview';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { AutoComplete } from 'primereact/autocomplete';
import { InputTextarea } from 'primereact/inputtextarea';
import { Dialog } from 'primereact/dialog';
 
import './Api.scss'
import getMetodosHttp from '../../metodo/metodosHttp';

import { getContextos } from '../../../service/ContextoService';
import { getTeseuApiById } from '../../../service/TeseuApiService';
import { saveTeseuApi } from '../../../service/TeseuApiService';
import { updateTeseuApi } from '../../../service/TeseuApiService';
  
const opcoesMetodosHttp = getMetodosHttp();
 
class CadastroApi extends Component {

    state = {
        id: null,
        name: '',
        httpMethodValue: '',
        httpMethod: '',
        host: '',  
        path: '',
        paramsApi: [],
        headersApi: [],
        body: '',
        activeIndex: -1,       
        contextos: [],
        filteredContextsSingle: null, 
        idParam: null, 
        contexto: null, 
        valueParam: '',
        idHeader: null,
        contextoHeader: null,
        valueHeader: '',
        exibirModalExclusaoParam: false,
        exibirModalExclusaoHeader: false,
        displayDialogExc: true,
        position: 'center',
        paramExclusao: null 
    }

    constructor(props) {
        super(props); 
        this.filterContextsSingle = this.filterContextsSingle.bind(this);
        this.optionsTemplate = this.optionsTemplate.bind(this);
    }

    componentDidMount() {
  
        this.getContextosAsync();

        if (this.props.match && this.props.match.params && this.props.match.params.id) {
            const id = this.props.match.params.id;
            this.getApiAsync(id); 
        } else {
            this.setState({ activeIndex: 0 });
        }        
    }

    getContextosAsync = () => {
        getContextos()
        .then(response => response.data)
        .then(contextos => {

            this.setState({contextos})
        })
        .catch(error => {
            this.showError(error.message);
        })
    }

    getApiAsync = (id) => {
      
        getTeseuApiById(id)
        .then(response => response.data)
        .then(api => { 
          
            this.setState({ ...api , activeIndex: 0 });
        })
        .catch(error => {
            this.showError(error.message);
        })
    }

    showSuccess = (detail) => {
 
        this.growl.show({severity: 'success', summary: 'Success Message', detail});
    }

    showError = (detail) => {
 
        this.growl.show({severity: 'error', summary: 'Error Message', detail}); 
    }
 
    validarNome = () => {

        if (!this.state.name || !this.state.name.trim()) {
            this.showError("Nome da Api é obrigatório !");
            return false;
        }
        return true;
    }
 
    validarMetodoHttp = () => {

        if (!this.state.httpMethod) {
            this.showError("Método http é obrigatório !");
            return false;
        }
        return true;
    }
  
    validarHost = () => {

        if (!this.state.host || !this.state.host.trim()) {
            this.showError("Host é obrigatório !");
            return false;
        }
        return true;
    }
 
    validarPath = () => {

        if (!this.state.path || !this.state.path.trim()) {
            this.showError("Path é obrigatório !");
            return false;
        }
        return true;
    }

    validarAbas = () => {

        if ((!this.state.paramsApi || this.state.paramsApi.length == 0)  
           && !this.state.body) {
               this.showError("Parâmetros ou body é obrigatório!");
               return false;
        }
        return true;
    }
 
    validaDadosOk = () => {

        if (!this.validarNome()) {
            return false;
        } 
 
        if (!this.validarMetodoHttp()) {
            return false;
        } 
 
        if (!this.validarHost()) {
            return false;
        } 
 
        if (!this.validarPath()) {
            return false;
        }

        if (!this.validarAbas()) {
            return false;
        }
 
        return true;

    }
 
    cancelar = () => {

        const { history } = this.props;
        if (history) {
            history.push('/api');
        }
    }

    handleSubmit = (e) => {

        e && e.preventDefault && e.preventDefault();
 
        let { id, name, httpMethod, host, path, headersApi, paramsApi, body } = this.state;
    
        if (this.validaDadosOk()) { 
            if (body) {
                body = JSON.stringify(body);
            }
            const dados = {
                id,
                name,   
                host, 
                httpMethod,
                path,
                headersApi,
                paramsApi,
                body             
            };
            if (id == null) {
                saveTeseuApi(dados)
                    .then(response => response.data)
                    .then(teseuApi => {
                         
                        this.showSuccess("Api criada com sucesso!");

                        const { history } = this.props;
                    
                        window.setTimeout(function() {
                            history.push(`/api`);
                        }, 2000);
                })
                .catch(error => { 
                    this.showError(`Erro ao criada nova api: ${error.message}` )
                }); 
            } else {
                updateTeseuApi(dados)
                    .then(response => response.data)
                    .then(teseuApi => {
                        
                        this.showSuccess("Api atualizada com sucesso!");

                        const { history } = this.props;
                    
                        window.setTimeout(function() {
                            history.push(`/api`);
                        }, 2000);
                })
                .catch(error => {  
                    this.showError(`Erro ao atualizar api: ${error.message}` )
                });
            }    
        }
    }

    editarParam = (param) => {  
        this.setState({ idParam: param.id, contexto: param.name, valueParam: param.value, paramAlteracao: param });
    }

    deleteParam = (param) => {
        this.setState({ exibirModalExclusaoParam: true, displayDialogExc: true, paramExclusao: param });
    }

    editarHeader = (header) => { 
        this.setState({ idHeader: header.id, contextoHeader: header.name, valueHeader: header.value, headerAlteracao: header });
    }

    deleteHeader = (header) => { 
        let headersApi = this.state.headersApi;
        headersApi.splice(headersApi.indexOf(header.name), 1);
        this.setState({headersApi});
    } 

    optionsTemplate(rowData) {
        return (
            <div>
                <i className="pi pi-pencil" onClick={() => this.editarParam(rowData)}></i>
                <i className="pi pi-times" onClick={() => this.deleteParam(rowData)}></i>
            </div>
        )
    }

    optionsTemplateHeader(rowData) {
        return (
            <div>
                <i className="pi pi-pencil" onClick={() => this.editarHeader(rowData)}></i>
                <i className="pi pi-times" onClick={() => this.deleteHeader(rowData)}></i>
            </div>
        )
    }

    filterContextsSingle(event) {
        setTimeout(() => {
            var results = this.state.contextos.filter((contexto) => {
                return contexto.variable.toLowerCase().startsWith(event.query.toLowerCase());
            });

            this.setState({ filteredContextsSingle: results });

        }, 150);
    }

    onChangeChaveParam = (e) => {
 
        let value = "";
        let valueParam = this.state.valueParam;
        
        if (typeof(e.value)==='object') {
            let objeto = e.value;
            value = objeto.variable;
            valueParam = objeto.value;
        } else {
            value = e.value;
        }

        this.setState({contexto: value, valueParam})
    }

    validarParamsOk = () => {
 
        if (!this.state.contexto) {
            this.showError("A chave do parâmetro é obrigatória!");
            return false;
        }
 
        let index = this.state.paramsApi.findIndex(i => i.name === this.state.contexto.trim().toLowerCase());
        if (index != -1 && !this.state.paramAlteracao) {
            this.showError("Esse parâmetro já foi informado!");
            return false; 
        }

        if (!this.state.valueParam) {
            this.showError("O valor do parâmetro é obrigatório!");
            return false;
        }

        return true;
 
    }

    saveParam = () => {
                      
        if (!this.validarParamsOk()) {
            return;
        }

        let paramsApi = this.state.paramsApi;
        let paramApi = {
            id: this.state.idParam,
            name: this.state.contexto,
            value: this.state.valueParam
        }
  
        let index = paramsApi.findIndex(i => i.name === this.state.contexto);
 
        if (index != -1) { 
            paramsApi[index] = paramApi;
        } else {
            paramsApi.push(paramApi)
        }
 
        this.setState({paramsApi, idParam:null, contexto:"", valueParam:"", paramAlteracao:null});
    }

    onChangeChaveHeader = (e) => {

        let value = "";
        let valueHeader = this.state.valueHeader;
        
        if (typeof(e.value)==='object') {
            let objeto = e.value;
            value = objeto.variable;
            valueHeader = objeto.value;
        } else {
            value = e.value;
        }

        this.setState({contextoHeader: value, valueHeader})
    }

    validarHeadersOk = () => {
 
        if (!this.state.contextoHeader) {
            this.showError("A chave do header é obrigatória!");
            return false;
        }
 
        let index = this.state.headersApi.findIndex(i => i.name === this.state.contextoHeader.trim().toLowerCase());
        if (index != -1 && !this.state.headerAlteracao) {
            this.showError("Esse header já foi informado!");
            return false; 
        }

        if (!this.state.valueHeader) {
            this.showError("O valor do header é obrigatório!");
            return false;
        }

        return true;
 
    }

    saveHeader = () => {
                      
        if (!this.validarHeadersOk()) {
            return;
        }

        let headersApi = this.state.headersApi;
        let headerApi = {
            id: this.state.idHeader,
            name: this.state.contextoHeader,
            value: this.state.valueHeader
        }

        let index = headersApi.findIndex(i => i.name === this.state.contextoHeader);
        if (index != -1) { 
            headersApi[index] = headerApi;
        } else {
            headersApi.push(headerApi)
        }
 
        this.setState({headersApi, idHeader:null, contextoHeader:"", valueHeader:"", headerAlteracao:null});
    }

    handleDeleteParam = (e, name) => {

        e && e.preventDefault && e.preventDefault();
        
        let paramsApi = this.state.paramsApi;
        paramsApi.splice(paramsApi.indexOf(name), 1);
        this.setState({paramsApi});

        this.showSuccess("Parâmetro excluído com sucesso!");

        setTimeout(() => { 
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
                <Button label="Salvar" icon="pi pi-check" onClick={(e) => this.handleDeleteParam(e, name)} />
                <Button label="Cancelar" icon="pi pi-times" onClick={() => this.onHide(name)} className="p-button-secondary"/>
            </div>
        );
    }

    render() {

        return (
            <div>
                <Growl ref={(el) => this.growl = el} />
                <div className="card card-w-title">
                    <h1>Api {this.state.id} </h1>
                    <div className="p-grid p-fluid">                            
                        <div className="p-col-2">
                            <label htmlFor="nomeAPI">Nome da API:</label>
                        </div>
                        <div className="p-col-4">
                            <InputText id="nomeAPI" 
                                    onChange={(e) => this.setState({ name: e.target.value })}  
                                    value={this.state.name} />
                        </div>
                        <div className="p-col-2" style={{padding:'.75em'}}>
                            <label htmlFor="metodoHttp">Método HTTP:</label>
                        </div>
                        <div className="p-col-4" style={{padding:'.5em'}}>
                            <Dropdown id="metodoHttp"
                                      value={this.state.httpMethod}
                                      options={opcoesMetodosHttp}                                     
                                      onChange={(e) => this.setState({ httpMethod: e.value })}
                                      placeholder="Selecione o método http" />                    
                        </div>
                        <div className="p-col-2">
                            <label htmlFor="host">Host:</label>
                        </div>
                        <div className="p-col-4">
                            <InputText id="host" 
                                       onChange={(e) => this.setState({ host: e.target.value })}  
                                       value={this.state.host} />
                        </div>
                        <div className="p-col-2">
                            <label htmlFor="pathAPI">Path da API:</label>
                        </div>
                        <div className="p-col-4">
                            <InputText id="pathAPI" 
                                       onChange={(e) => this.setState({ path: e.target.value })}  
                                       value={this.state.path} />
                        </div>
                    </div>
                    <hr className="divisor" />
                    <div> 
                        <TabView renderActiveOnly={true} activeIndex={this.state.activeIndex} onTabChange={(e) => this.setState({activeIndex: e.index})}>
                            <TabPanel header="Params" leftIcon="pi pi-calendar">
                                <div>
                                    <div className="row" style={{ paddingLeft: "20px" }} >
                                        <div className="col col-md-5">
                                            <div className="form-group" >
                                                <label className="control-label">Chave</label>
                                                <AutoComplete value={this.state.contexto} id="inputContextParam"
                                                              suggestions={this.state.filteredContextsSingle} 
                                                              completeMethod={this.filterContextsSingle} 
                                                              field="variable" name="variableParam" 
                                                              size={30} placeholder="Chave" minLength={1} 
                                                              onChange={(e) => this.onChangeChaveParam(e) } />
                                            </div>
                                        </div>                                   
                                        <div className="col col-md-5">                           
                                            <div className="form-group">
                                                <label className="control-label value-param">Valor</label>
                                                <InputText id="inputValueParam"
                                                           name="valueParam"
                                                           size={30} placeholder="Valor" minLength={1} 
                                                           value={this.state.valueParam}
                                                           onChange={e => {
                                                                const valueParam = e.target.value;
                                                                this.setState({ valueParam });
                                                           }} />
                                            </div>
                                        </div>
                                        <div className="col col-md-1">
                                            <Button icon="pi pi-check" 
                                                    iconPos="right" 
                                                    onClick={() => this.saveParam()} />
                                        </div>
                                    </div>  
 
                                    {this.state.exibirModalExclusaoParam && 
                                        <Dialog header={`Parâmetro ${this.state.paramExclusao.name}`} 
                                                        visible={this.state.displayDialogExc} 
                                                        style={{width: '50vw'}} 
                                                        onHide={() => this.onHide('displayDialogExc')} 
                                                        footer={this.renderFooter('displayDialogExc')}
                                        >            
                                            <div>        
                                                <Growl ref={(el) => this.growl = el} />
                                                <p>{`Confirma a exclusão do parâmetro ${this.state.paramExclusao.name} ?`}</p>    
                                            </div>  
                                        </Dialog>   
                                    }
 
                                    <div className="row" style={{ paddingLeft: "2.5em" , width: "92%"  }}>
                                        <DataTable value={this.state.paramsApi} 
                                                   paginator={true} rows={4} 
                                        >              
                                            <Column field="name" header="Chave"  style={{width: '20em', height: '1.5em'}}/>
                                            <Column field="value" header="Valor"  style={{width: '20em', height: '1.5em'}}/>                                        
                                            <Column style={{ width: "10%" }} body={this.optionsTemplate} />
                                        </DataTable>
                                    </div>
                                </div>
                            </TabPanel>
                            <TabPanel header="Header" leftIcon="pi pi-user">
                                <div>
                                    <div className="row" style={{ paddingLeft: "20px" }} >
                                        <div className="col col-md-5">
                                            <div className="form-group" >
                                                <label className="control-label">Chave</label>
                                                <AutoComplete value={this.state.contextoHeader} 
                                                              suggestions={this.state.filteredContextsSingle} 
                                                              completeMethod={this.filterContextsSingle} 
                                                              field="variable" name="variableHeader" 
                                                              size={30} placeholder="Chave" minLength={1} 
                                                              onChange={(e) => this.onChangeChaveHeader(e) }  />
                                            </div>
                                        </div>                                   
                                        <div className="col col-md-5">                           
                                            <div className="form-group">
                                                <label className="control-label value-header">Valor</label>
                                                <InputText id="inputValueHeader"
                                                           name="valueHeader"
                                                           size={30} placeholder="Valor" minLength={1} 
                                                           value={this.state.valueHeader}
                                                           onChange={e => {
                                                                const valueHeader = e.target.value;
                                                                this.setState({ valueHeader });
                                                           }} />
                                            </div>
                                        </div>                          
                                        <div className="col col-md-1">
                                            <Button icon="pi pi-check" 
                                                    iconPos="right" 
                                                    onClick={() => this.saveHeader()} />
                                        </div>
                                    </div>  
                                    <div className="row" style={{ paddingLeft: "2.5em" , width: "92%"  }} >
                                        <DataTable value={this.state.headersApi} 
                                                   paginator={true} rows={4} 
                                        >              
                                            <Column field="name" header="Chave"  style={{width: '20em', height: '1.5em'}}/>
                                            <Column field="value" header="Valor"  style={{width: '20em', height: '1.5em'}}/>  
                                            <Column style={{ width: "10%" }} body={this.optionsTemplate} />
                                        </DataTable>
                                    </div>
                                </div>                                    
                            </TabPanel>
                            <TabPanel header="Body">
                                <div>
                                    <div className="row" style={{ paddingLeft: "20px" }} >
                                        <div className="col col-md-12">
                                            <div className="form-group" >
                                                <label className="control-label">Expressão</label>
                                                <InputTextarea 
                                                        value={this.state.body} 
                                                        onChange={(e) => this.setState({ body: e.target.value })} 
                                                        rows={12} cols={106} />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </TabPanel>
                        </TabView>
                    </div>     
                    <hr className="divisor" />
                    <div className="p-grid">
                        <div className="p-col-12 p-md-8"></div>
                        <div className="p-col-12 p-md-2">
                            <Button label="Salvar" className="botao-crud" onClick={() => this.handleSubmit()} />
                        </div>
                        <div className="p-col-12 p-md-2">
                            <Button label="Cancelar" className="botao-crud" onClick={() => this.cancelar()} />
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default withRouter(CadastroApi);