// src/api/api.js
import axios from 'axios';

// Configure axios instance
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const axiosInstance = axios.create({
    baseURL: API_BASE_URL,
    headers: { 'Content-Type': 'application/json' },
});

// Request interceptor to add JWT token
// Ensure the token is added correctly
axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Response interceptor for error handling
axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

// ==================== AUTH API ====================
export const authAPI = {
    login: (credentials) => axiosInstance.post('/auth/login', credentials),
    register: (userData) => axiosInstance.post('/auth/register', userData),
    logout: () => axiosInstance.post('/auth/logout'),
    me: () => axiosInstance.get('/auth/me'),
    changePassword: (data) => axiosInstance.post('/auth/change-password', data),
    refreshToken: (refreshToken) => axiosInstance.post('/auth/refresh-token', { refreshToken }),
};

// ==================== USER API ====================
export const userAPI = {
    getAll: () => axiosInstance.get('/users'),
    getById: (id) => axiosInstance.get(`/users/${id}`),
    create: (data) => axiosInstance.post('/users', data),
    update: (id, data) => axiosInstance.put(`/users/${id}`, data),
    delete: (id) => axiosInstance.delete(`/users/${id}`),
};

// ==================== PRINCIPAL API ====================
export const principalAPI = {
    // Departments
    getDepartments: () => axiosInstance.get('/principal/departments'),
    getDepartmentById: (id) => axiosInstance.get(`/principal/departments/${id}`),
    createDepartment: (data) => axiosInstance.post('/principal/departments', data),
    updateDepartment: (id, data) => axiosInstance.put(`/principal/departments/${id}`, data),
    deleteDepartment: (id) => axiosInstance.delete(`/principal/departments/${id}`),

    // HODs
    getHods: () => axiosInstance.get('/principal/hods'),
    getHodById: (id) => axiosInstance.get(`/principal/hods/${id}`),
    createHod: (data) => axiosInstance.post('/principal/hods', data),
    updateHod: (id, data) => axiosInstance.put(`/principal/hods/${id}`, data),
    deleteHod: (id) => axiosInstance.delete(`/principal/hods/${id}`),

    // Teachers & Performance
    getTeachersByDepartment: (deptId) => axiosInstance.get(`/principal/teachers?departmentId=${deptId}`),
    getPerformanceByDepartment: (deptId) => axiosInstance.get(`/principal/performance/department/${deptId}`),
    getPrincipalDashboard: () => axiosInstance.get('/principal/dashboard'),
};

// ==================== HOD API ====================
export const hodAPI = {
    // Teachers
    getTeachers: () => axiosInstance.get('/hod/teachers'),
    createTeacher: (data) => axiosInstance.post('/hod/teachers', data),
    updateTeacher: (id, data) => axiosInstance.put(`/hod/teachers/${id}`, data),
    deleteTeacher: (id) => axiosInstance.delete(`/hod/teachers/${id}`),

    // Classes
    getClasses: () => axiosInstance.get('/hod/classes'),
    createClass: (data) => axiosInstance.post('/hod/classes', data),
    assignClassAdvisor: (classId, teacherId) => axiosInstance.put(`/hod/classes/${classId}/advisor?teacherId=${teacherId}`),

    // Subjects
    getSubjects: () => axiosInstance.get('/hod/subjects'),
    createSubject: (data) => axiosInstance.post('/hod/subjects', data),

    // Class-Subject assignment
    assignTeacherToSubject: (data) => axiosInstance.post('/hod/class-subjects', data),

    // Timetable
    getTimetableForClass: (classId) => axiosInstance.get(`/hod/timetable/class/${classId}`),
    createTimetableEntry: (data) => axiosInstance.post('/hod/timetable', data),
    deleteTimetableEntry: (id) => axiosInstance.delete(`/hod/timetable/${id}`),

    // Semester requests
    getSemesterRequests: () => axiosInstance.get('/hod/semester-requests'),
    approveSemesterRequest: (requestId) => axiosInstance.put(`/hod/semester-requests/${requestId}/approve`),

    // Suggestions & Performance
    getSuggestions: () => axiosInstance.get('/hod/suggestions'),
    getPerformance: () => axiosInstance.get('/hod/performance'),
    getDashboard: () => axiosInstance.get('/hod/dashboard'),
};

// ==================== CLASS ADVISOR API ====================
export const advisorAPI = {
    // Students
    getStudents: () => axiosInstance.get('/advisor/students'),
    createStudent: (data) => axiosInstance.post('/advisor/students', data),
    updateStudent: (id, data) => axiosInstance.put(`/advisor/students/${id}`, data),
    deleteStudent: (id) => axiosInstance.delete(`/advisor/students/${id}`),

    // Attendance (advisor specific)
    takeAttendance: (data) => axiosInstance.post('/advisor/attendance', data), // reuses same endpoint but role-specific
    getAttendance: (classId, date, session) => axiosInstance.get(`/advisor/attendance/${classId}/${date}/${session}`),

    // Semester
    startSemester: (data) => axiosInstance.post('/advisor/semester/start', data),
    endSemester: (semesterId) => axiosInstance.post(`/advisor/semester/end/${semesterId}`),

    // Requests
    getPendingRequests: () => axiosInstance.get('/advisor/requests'),
    approveRequest: (id, comments) => axiosInstance.put(`/advisor/requests/${id}/approve?comments=${comments}`),
    rejectRequest: (id, comments) => axiosInstance.put(`/advisor/requests/${id}/reject?comments=${comments}`),

    // Performance & Dashboard
    getClassPerformance: () => axiosInstance.get('/advisor/performance'),
    getDashboard: () => axiosInstance.get('/advisor/dashboard'),
};

// ==================== TEACHER API ====================
export const teacherAPI = {
    getClasses: () => axiosInstance.get('/teacher/classes'),
    getSubjects: () => axiosInstance.get('/teacher/subjects'),
    getTimetable: () => axiosInstance.get('/teacher/timetable'),
};

// ==================== STUDENT API ====================
export const studentAPI = {
    getProfile: () => axiosInstance.get('/student/profile'),
    updateProfile: (data) => axiosInstance.put('/student/profile', data),
    getAttendance: (startDate, endDate) => axiosInstance.get(`/student/attendance?startDate=${startDate}&endDate=${endDate}`),
    getTimetable: () => axiosInstance.get('/student/timetable'),
    getMarks: () => axiosInstance.get('/student/marks'),
    getMaterials: () => axiosInstance.get('/student/materials'),
    submitRequest: (data) => axiosInstance.post('/student/requests', data),
    getRequests: () => axiosInstance.get('/student/requests'),
    submitSuggestion: (content) => axiosInstance.post('/student/suggestions', content),
};

// ==================== ATTENDANCE API (General) ====================
export const attendanceAPI = {
    takeAttendance: (data) => axiosInstance.post('/attendance', data),
    getAttendanceByDateAndSession: (classId, date, session) => axiosInstance.get(`/attendance/${classId}/${date}/${session}`),
    getStudentAttendance: (studentId, startDate, endDate) => axiosInstance.get(`/attendance/student/${studentId}`, { params: { startDate, endDate } }),
    getClassAttendanceRange: (classId, start, end) => axiosInstance.get(`/attendance/class/${classId}/range`, { params: { start, end } }),
    lockAttendance: (id) => axiosInstance.put(`/attendance/${id}/lock`),
};

// ==================== OEM / MARKS API ====================
export const oemAPI = {
    createSheet: (data) => axiosInstance.post('/marks/sheet', data),
    getSheetsForClass: (classId) => axiosInstance.get(`/marks/class/${classId}`),
    getStudentMarks: (studentId) => axiosInstance.get(`/marks/student/${studentId}`),
    updateMarks: (sheetId, data) => axiosInstance.put(`/marks/sheet/${sheetId}/marks`, data),
    submitSheet: (sheetId) => axiosInstance.put(`/marks/sheet/${sheetId}/submit`),
    lockSheet: (sheetId) => axiosInstance.put(`/marks/sheet/${sheetId}/lock`),
};

// ==================== TIMETABLE API ====================
export const timetableAPI = {
    getForClass: (classId) => axiosInstance.get(`/timetable/class/${classId}`),
    getForTeacher: (teacherId) => axiosInstance.get(`/timetable/teacher/${teacherId}`),
};

// ==================== MATERIALS API ====================
export const materialAPI = {
    upload: (formData) => axiosInstance.post('/materials/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
    }),
    getForClass: (classId) => axiosInstance.get(`/materials/class/${classId}`),
    getMyMaterials: () => axiosInstance.get('/materials/my'),
    delete: (id) => axiosInstance.delete(`/materials/${id}`),
};

// ==================== NOTIFICATIONS API ====================
export const notificationAPI = {
    getUnread: () => axiosInstance.get('/notifications/unread'),
    markAsRead: (id) => axiosInstance.put(`/notifications/${id}/read`),
    markAllAsRead: () => axiosInstance.put('/notifications/read-all'),
};

// ==================== ANALYTICS API ====================
export const analyticsAPI = {
    getDashboard: () => axiosInstance.get('/analytics/dashboard'),
};

// ==================== FILES API ====================
export const fileAPI = {
    upload: (formData) => axiosInstance.post('/files/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
    }),
    delete: (filename) => axiosInstance.delete(`/files/${filename}`),
};

// ==================== WORKFLOW / REQUESTS API ====================
export const requestAPI = {
    // Student submits
    createLeaveOrOd: (data) => axiosInstance.post('/student/requests', data),
    getMyRequests: () => axiosInstance.get('/student/requests'),
    // Advisor views and manages
    getPendingForAdvisor: () => axiosInstance.get('/advisor/requests'),
    approve: (id, comments) => axiosInstance.put(`/advisor/requests/${id}/approve?comments=${comments}`),
    reject: (id, comments) => axiosInstance.put(`/advisor/requests/${id}/reject?comments=${comments}`),
    // HOD semester requests
    getSemesterRequestsForHod: () => axiosInstance.get('/hod/semester-requests'),
    approveSemesterStart: (requestId) => axiosInstance.put(`/hod/semester-requests/${requestId}/approve`),
    // CA semester request
    requestSemesterStart: (data) => axiosInstance.post('/advisor/semester/start', data),
};

// Export all as a single default object for convenience
const api = {
    auth: authAPI,
    user: userAPI,
    principal: principalAPI,
    hod: hodAPI,
    advisor: advisorAPI,
    teacher: teacherAPI,
    student: studentAPI,
    attendance: attendanceAPI,
    oem: oemAPI,
    timetable: timetableAPI,
    material: materialAPI,
    notification: notificationAPI,
    analytics: analyticsAPI,
    file: fileAPI,
    request: requestAPI,
};

export default api;