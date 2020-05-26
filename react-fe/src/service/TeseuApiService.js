import api from './Api';
 
export const getTeseuApis = () => {
    return api.get('/teseu-manager-api');
};

export const getTeseuApiById = (id) => {
    return api.get('/teseu-manager-api/' + id);
};
 
export const saveTeseuApi = (teseuApi) => {

    return api.post('/teseu-manager-api', {
        ...teseuApi
    } ,   {
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Methods': '*'
        }
    }  );
};

export const updateTeseuApi = (teseuApi) => {
    return api.put('/teseu-manager-api', {
        ...teseuApi
    },   {
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Methods': '*'
        }
    }  );
}

export const deleteTeseuApi = (id) => {
    return api.delete('/teseu-manager-api/'+id);
}


// Requisições referentes a Teseu Api -> CRUD

