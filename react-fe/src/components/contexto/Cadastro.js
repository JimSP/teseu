import React, { Component } from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { Dropdown } from 'primereact/dropdown';
import { Button } from 'primereact/button';
import { Dialog } from 'primereact/dialog';


import { Growl } from 'primereact/growl';


import { getContextos } from '../../service/ContextoService';
import { getContextoById } from '../../service/ContextoService';
import { saveContexto } from '../../service/ContextoService';
import { updateContexto } from '../../service/ContextoService'; 



import 'primereact/resources/themes/nova-light/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

export default class CadastroContexto extends Component {

    constructor() {
        super();
        this.state = {
            id: null,
            variable: "",
            value: "",
            contextos: [] 
        };
  
        this.optionsTemplate = this.optionsTemplate.bind(this);
    }

    componentDidMount() {
        // this.contextoservice.getcontextosSmall().then(data => this.setState({contextos: data}));
 
       /* let contextos = [
            {id: 1,
             chave: 'usuario',
             valor: 'jane.pellim'
            },
            {id: 2,
             chave: 'senha',
             valor: 'abcg'
            },
            {id: 3,
             chave: 'numAcesso',
             valor: 1
            }
        ]  */

        this.getContextosAsync();
  
     }

    getContextosAsync = () => {
        getContextos()
        .then(response => response.data)
        .then(contextos => {

            this.setState({contextos})
 
        })
        .catch(error => {
            this.showError(error.message);
            console.log('Erro: ', error.message);
        })
    }
 

    showSuccess = (detail) => {
 
        this.growl.show({severity: 'success', summary: 'Success Message', detail});
    }

    showError = (detail) => {
 
        this.growl.show({severity: 'error', summary: 'Success Message', detail}); 
    }


    findSelectedContextoIndex = () => {
        return this.state.contextos.indexOf(this.state.contexto);
    }

    validarChave = () => {

        if (!this.state.variable || !this.state.variable.trim()) {
            this.showError("Chave da variável de contexto é obrigatória !");
            return false;
        }
        return true;
    }

    validarValor = () => {

        if (!this.state.value || !this.state.value.trim()) {
            this.showError("Valor da variável de contexto é obrigatório !");
            return false;
        }
        return true;
    }

    validaDadosOk = () => {

        if (!this.validarChave()) {
            return false;
        } 
 
        if (!this.validarValor()) {
            return false;
        } 
 
       
        return true;

    }

    handleSubmit = (e) => {

        e && e.preventDefault && e.preventDefault();
 
        let { id, variable, value } = this.state;
   
        const dados = {
            id,
            variable, 
            value              
        };
 
        if(this.state.id) {

            updateContexto(dados)
            .then(response => {

                    console.log('ver se tem o num da agenda gravada...'+response)

                          
                this.showSuccess("Variável de contexto atualizada com sucesso!");

                this.getContextosAsync();

                this.onHide('displayDialog');

               /* setTimeout(() => {
                    this.props && this.props.afterAction();
                    this.onHide('displayDialog');
                 }, 2000);  */

        })
        .catch(error => {
            console.log('Erro: ', error.message); 
            this.showError(`Erro ao atualizar variável de contexto: ${error.message}` )
        });





            // chama api de update
         /// contextos[this.findSelectedContextoIndex()] = this.state.contexto;
 
        } else { 
            if (this.validaDadosOk()) { 
                saveContexto(dados)
                    .then(response => {

                            console.log('ver se tem o num da agenda gravada...'+response)

                                  
                        this.showSuccess("Variável de contexto gravada com sucesso!");

                        this.getContextosAsync();

                        this.onHide('displayDialog');

                       /* setTimeout(() => {
                            this.props && this.props.afterAction();
                            this.onHide('displayDialog');
                         }, 2000);  */

                })
                .catch(error => {
                    console.log('Erro: ', error.message); 
                    this.showError(`Erro ao gravar nova variável de contexto: ${error.message}` )
                });
            }
        }
          
       
    } 

 
 
    editarContexto = (contexto) => {

        this.setState({
            ...contexto,
            displayDialog: true
        });

    }

    addNew = () => { 
    
        this.setState({
            id:null,
            variable: '',
            value: '',
            displayDialog: true
        });
    }
 
    optionsTemplate(rowData) {
        return (
            <div>
                <i className="pi pi-pencil" onClick={() => this.editarContexto(rowData)}></i>
                <i className="pi pi-times" onClick={() => this.deleteContexto(rowData)}></i>
            </div>
        )
    }


    onHide = (name) => {
        this.setState({
            [`${name}`]: false
        });

    }

    render() {


        let header = <div className="p-clearfix" style={{lineHeight:'1.87em'}}>CRUD de Variáveis de Contexto </div>;

        let footer = <div className="p-clearfix" style={{width:'100%'}}>
            <Button style={{float:'left'}} label="Add" icon="pi pi-plus" onClick={this.addNew}/>
        </div>;

        let dialogFooter = <div className="ui-dialog-buttonpane p-clearfix">


                {this.state.contexto && this.state.contexto.id &&
                    <Button label="Delete" icon="pi pi-times" onClick={this.delete}/>
                }        
             
                <Button label="Salvar" icon="pi pi-check" onClick={(e) => this.handleSubmit(e)} />
                <Button label="Cancelar" icon="pi pi-times" onClick={() => this.onHide('displayDialog')} className="p-button-secondary"/>
        



            </div>;



        return (
            <div>
           
                <div className="content-section implementation">
                    <Growl ref={(el) => this.growl = el} />
 
                    <DataTable value={this.state.contextos} 
                               paginator={true} 
                               rows={15}  
                               header={header} 
                               footer={footer}> 
 
                        <Column field="variable" header="Chave" style={{height: '3.5em'}}/>
                        <Column field="value" header="Valor" style={{height: '3.5em'}}/>
                       
                        <Column style={{ width: "10%" }} body={this.optionsTemplate} />
 
                    </DataTable>


 


                    <Dialog visible={this.state.displayDialog} 
                            style={{width: '50vw'}} 
                            header="Detalhes do Contexto" 
                            modal={true} 
                            footer={dialogFooter} 
                            onHide={() => this.setState({displayDialog: false})}>
                            <div className="p-grid p-fluid">
                                <div className="p-col-4" style={{padding:'.75em'}}><label htmlFor="chave">Chave</label></div>
                                <div className="p-col-8" style={{padding:'.5em'}}>
                                    <InputText id="chave" 
                                                onChange={(e) => this.setState({variable: e.target.value})} 
                                                value={this.state.variable}/>
                                </div>
                                <div className="p-col-4" style={{padding:'.75em'}}><label htmlFor="valor">Valor</label></div>
                                <div className="p-col-8" style={{padding:'.5em'}}>
                                    <InputText id="valor"
                                               onChange={(e) => this.setState({value: e.target.value})}  
                                               value={this.state.value}/>
                                </div>
                            </div>
                    </Dialog>
                </div>
            </div>
        );
    }
}