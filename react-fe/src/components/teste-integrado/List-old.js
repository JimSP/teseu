import React, { Component } from 'react';
import { DragDropContext } from 'react-beautiful-dnd';
import Column from './Column';

 

export default class List extends Component {

  constructor(props) {
    super(props);
    this.state = {

        items: [],
        selected: [] 
    };

     
}

componentDidMount() {
      
      
      this.setState({ items: this.props.listaApis});
       
       

      console.log('CDM this.props.itens', this.props)



      console.log('CDM this.props.itens', this.props.listaApis)


      console.log('CDM this.state.itens', this.state.items)

      console.log('CDM this.state.selected', this.state.selected)


    }

 

    render() {

 

      return (
        <div style={{
          display: 'flex',
          justifyContent: 'space-between',
          width: '1000px',
        }}
        >
          <DragDropContext onDragEnd={this.onDragEnd}>
            <spam>Lista de apis</spam>
            <Column droppableId="droppable" data={this.props.listaApis} {...this.props} />
            <spam>Ordem das apis no teste</spam>
            <Column droppableId="droppable2" data={this.state.selected} {...this.props} />
          </DragDropContext>
        </div>
      );
    }
}
 