import api from './Api';
 
export const getTeseuTestes = () => {
    return api.get('/teseu-test-api');
};

export const getTeseuTesteById = (id) => {
    return api.get('/teseu-test-api/' + id);
};
 
export const saveTeseuTesteApi = (teseuTest) => {
 
    return api.post('/teseu-test-api', {
        ...teseuTest
    } ,   {
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Methods': '*'
        }
    }  );
};

export const updateTeseuTestApi = (teseuTest) => {
    return api.put('/teseu-test-api', {
        ...teseuTest
    },   {
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Methods': '*'
        }
    }  );
}

export const deleteTeseuTesteApi = (id) => {
    return api.delete('/teseu-test-api/'+id);
}


// Requisições referentes a Teseu Tests -> CRUD

