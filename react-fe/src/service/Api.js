import axios from 'axios';

const baseURL = 'http://localhost:8080'

const api = axios.create({
    baseURL: baseURL,
    timeout: 50000
});


export default api;