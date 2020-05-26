import React, { Component } from 'react'; 
import { withRouter } from 'react-router-dom';

import { InputText } from 'primereact/inputtext'; 
import { Button } from 'primereact/button';
import { Growl } from 'primereact/growl';

import 'primereact/resources/themes/nova-light/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

import { DragDropContext } from 'react-beautiful-dnd';
import Column from './Column';
import './Cadastro.scss';
 
import { getTeseuApis } from '../../service/TeseuApiService';
import { getTeseuTesteById } from '../../service/TeseuTesteService';
import { saveTeseuTesteApi } from '../../service/TeseuTesteService';
import { updateTeseuTestApi } from '../../service/TeseuTesteService';

class CadastroTesteIntegrado extends Component {
 
    state = {
        id: null,
        name: "",
        items: [],
        selected: []
    };

    constructor(props) {
      super(props); 
       
    }

    componentDidMount() {
    
        this.getTeseuApisAsync()
            .then(() => this.getTeseuTesteSeEdicao());

    }

    getTeseuApisAsync = () => new Promise((resolve, reject) => {

        try { 

            let items = []
      
            getTeseuApis()
              .then(response => response.data)
              .then(data => data.content)
              .then(teseuApis => {
                  teseuApis.forEach((teseuAPI) => { 
                      const item = {
                          "id": String(teseuAPI.id), 
                          "content": this.getHttpApi(teseuAPI)
                      }
                      items.push(item);
                  });
                  this.setState({items});

                  resolve();
                  
            })
            .catch(error => { 
                this.showError(`Erro ao buscar APIs: ${error.message}` );
                reject(error);
            })

        } catch (error) {
          this.showError(`Erro ao buscar APIs: ${error.message}` );
          reject(error);
        } 
    })

    getTeseuTesteSeEdicao = () => {
        if (this.props.match && this.props.match.params && this.props.match.params.id) {
            const id = this.props.match.params.id;
            this.getTeseuTesteAsync(id); 
        } 
    }

    getTeseuTesteAsync = (id) => {

        let apis = []
        let items = this.state.items;
 
        getTeseuTesteById(id)
            .then(response => response.data) 
            .then(teseuTeste => {

                teseuTeste.sequenceOfApis.forEach((teseuAPI) => {
 
                    const api = {
                        "id": String(teseuAPI.idRequestApi),
                        "content": this.getHttpApi(teseuAPI)
                    }
                    apis.push(api);         
               
                    let index = items.findIndex(i => i.id == teseuAPI.idRequestApi);
          
                    if (index != -1) {
                        items.splice(index, 1);
                    }
                })
 
                this.setState({selected: apis, items, id: teseuTeste.id, name: teseuTeste.name}); 
        })
        .catch(error => {
            this.showError(`Erro ao buscar Teste Integrado: ${error.message}` );
        })
    }

    getHttpApi = (teseuAPI) => {
        return `${teseuAPI.name}: (${teseuAPI.httpMethod}) http://${teseuAPI.host}/${teseuAPI.path}`
    }

    showSuccess = (detail) => {
        this.growl.show({severity: 'success', summary: 'Success Message', detail});
    }

    showError = (detail) => {
        this.growl.show({severity: 'error', summary: 'Success Message', detail}); 
    }

    validarNome = () => {
        if (!this.state.name || !this.state.name.trim()) {
            this.showError("Nome do teste integrado é obrigatório !");
            return false;
        }
        return true;
    }

    validarApis = () => {
        if (!this.state.selected || this.state.selected.length == 0) {
            this.showError("Pelo menos uma api deve ser selecionada !");
            return false;
        } 
        return true;
    }

    validaDadosOk = () => {
        if (!this.validarNome()) {
            return false;
        } 
        if (!this.validarApis()) {
            return false;
        } 
        return true;
    }

    handleSubmit = (e) => {

        e && e.preventDefault && e.preventDefault();
      
        const { id, name } = this.state;

        if (this.validaDadosOk()) { 
            let executionOrders = [];
            let indice = 1;       
  
            for (let value of  this.state.selected) {           
                const executionOrder = {
                  "id": null,
                  "executionOrder" : indice++,
                  "idRequestApi" : value.id
                };
                executionOrders.push(executionOrder);
            }
            const regressiveTest = {
                "id": id,
                "name" : name, 
                "sequenceOfApis" : executionOrders
            }
            if (id == null) {
                saveTeseuTesteApi(regressiveTest)
                    .then(response => {
                        this.showSuccess("Teste integrado criado com sucesso!");
                        const { history } = this.props;
                        window.setTimeout(function() {
                            history.push(`/teseu-teste`);
                        }, 2000);
                })
                .catch(error => {
                    console.log('Erro: ', error.message); 
                    this.showError(`Erro ao criar teste integrado: ${error.message}` );
                });
            } else {
                updateTeseuTestApi(regressiveTest)
                    .then(response => {
                        this.showSuccess("Teste integrado alterado com sucesso!");
                        const { history } = this.props;                    
                        window.setTimeout(function() {
                            history.push(`/teseu-teste`);
                        }, 2000);
                })
                .catch(error => {  
                    this.showError(`Erro ao atualizar teste integrado: ${error.message}` );
                });
            }
        }    
    }

    reorder = (list, startIndex, endIndex) => {
        const result = Array.from(list);
        const [removed] = result.splice(startIndex, 1);
        result.splice(endIndex, 0, removed);      
        return result;
    };
  
    move =  (source, destination, droppableSource, droppableDestination) => {
        const sourceClone = Array.from(source);
        const destClone = Array.from(destination);
        const [removed] = sourceClone.splice(droppableSource.index, 1);
      
        destClone.splice(droppableDestination.index, 0, removed);
      
        const result = {};
        result[droppableSource.droppableId] = sourceClone;
        result[droppableDestination.droppableId] = destClone;
      
        return result;
    };
  
    /**
     * A semi-generic way to handle multiple lists. Matches
     * the IDs of the droppable container to the names of the
     * source arrays stored in the state.
     */
    id2List = {
      droppable: 'items',
      droppable2: 'selected',
    };

    getList = id => this.state[this.id2List[id]];

    onDragEnd = (res) => {
      const { source, destination } = res;
  
      // dropped outside the list
      if (!destination) {
        return;
      }
        
      if (source.droppableId === destination.droppableId) {
          const items = this.reorder(
          this.getList(source.droppableId),
          source.index,
          destination.index,
        );

        let state = { items };

        if (source.droppableId === 'droppable2') {
          state = { selected: items };
        }

        this.setState(state);
      } else {
        const result = this.move(
          this.getList(source.droppableId),
          this.getList(destination.droppableId),
          source,
          destination,
        );

        this.setState({
          items: result.droppable,
          selected: result.droppable2,
        });
      }
    };
  
    render() {

        return (
            <div>
                <Growl ref={(el) => this.growl = el} />            
                <div className="card card-w-title">
                    <h1>Teste integrado {this.state.id} </h1>
                    <div className="p-grid p-fluid">                            
                        <div className="p-col-2">
                            <label htmlFor="nomeTeste">Nome do teste integrado:</label>
                        </div>
                        <div className="p-col-10">
                            <InputText id="nomeTeste" 
                                    onChange={(e) => this.setState({ name: e.target.value })}  
                                    value={this.state.name} />
                        </div>
                    </div>
                    <fieldset className="border p-2"> 
                        <legend  className="w-auto">Composição do Teste Integrado</legend>
                        <label>Lista de APIs</label>
                        <label className="labelDireito">Ordem das APIs no teste</label>
                        <div style={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            width: '950px',
                            }}
                        >
                            <DragDropContext onDragEnd={this.onDragEnd}>             
                                <Column droppableId="droppable" data={this.state.items} {...this.props} />                                
                                <Column droppableId="droppable2" data={this.state.selected} {...this.props} />                          
                            </DragDropContext>
                        </div> 
                    </fieldset>
                </div>
                <Button label="Salvar" icon="pi pi-check" onClick={(e) => this.handleSubmit(e)}/>
            </div>
        );
    }
}

export default withRouter(CadastroTesteIntegrado);