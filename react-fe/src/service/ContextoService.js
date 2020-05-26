import api from './Api';
 
export const getContextos = () => {
    return api.get('/context');
};

export const getContextoById = (id) => {
    return api.get('/context/' + id);
};
 
export const saveContexto = (contexto) => {

    return api.post('/context', {
        ...contexto
    } ,   {
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Headers': '*'
        }
    }  );
};

export const updateContexto = (contexto) => {
    return api.put('/context', {
        ...contexto
    },   {
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Methods': '*'
        }
    }  );
}

// Requisições referentes a contexto -> CRUD

