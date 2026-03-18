import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const publicApi = {
    getStudentByRollNumber: (rollNumber) => api.get(`/public/students/${rollNumber}`),
    getStudentAttendance: (studentId) => api.get(`/public/attendance/student/${studentId}`),
    getMessages: () => api.get('/public/messages'),
    getDocuments: () => api.get('/public/documents'),
};

export default api;
