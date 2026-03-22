import axiosInstance from './axiosConfig';

export const userApi = {
  getAll: () => axiosInstance.get('/users'),
  getById: (id) => axiosInstance.get(`/users/${id}`),
  create: (data) => axiosInstance.post('/users', data),
  update: (id, data) => axiosInstance.put(`/users/${id}`, data),
  delete: (id) => axiosInstance.delete(`/users/${id}`),
};

export const academicApi = {
  // Principal actions
  getHods: () => axiosInstance.get('/principal/hods'),
  getDepartments: () => axiosInstance.get('/principal/departments'),
  getTeachers: () => axiosInstance.get('/principal/teachers'),
  getPerformance: (deptId) => axiosInstance.get(`/principal/performance/department/${deptId}`),
  getPrincipalDashboard: () => axiosInstance.get('/principal/dashboard'),

  // HOD actions
  getHodTeachers: () => axiosInstance.get('/hod/teachers'),
  getHodClasses: () => axiosInstance.get('/hod/classes'),
  getHodSubjects: () => axiosInstance.get('/hod/subjects'),
  getHodTimetable: (classId) => axiosInstance.get(`/hod/timetable/class/${classId}`),
  getHodDashboard: () => axiosInstance.get('/hod/dashboard'),

  // Teacher actions
  getTeacherTimetable: () => axiosInstance.get('/teacher/timetable'),
  getTeacherSubjects: () => axiosInstance.get('/teacher/subjects'),
  getTeacherClasses: () => axiosInstance.get('/teacher/classes'),
};

export const oemApi = {
  getMarksByClass: (classId) => axiosInstance.get(`/marks/class/${classId}`),
  getMarksByStudent: (studentId) => axiosInstance.get(`/marks/student/${studentId}`),
  saveMarks: (data) => axiosInstance.put('/marks/sheet/marks', data),
  submitSheet: (sheetId) => axiosInstance.put(`/marks/sheet/${sheetId}/submit`),
  lockSheet: (sheetId) => axiosInstance.put(`/marks/sheet/${sheetId}/lock`),
};

export const attendanceApi = {
  submitAttendance: (data) => axiosInstance.post('/attendance', data),
  getAttendance: (classId, date, session) => axiosInstance.get(`/attendance/${classId}/${date}/${session}`),
  getStudentAttendance: (studentId) => axiosInstance.get(`/attendance/student/${studentId}`),
  lockAttendance: (id) => axiosInstance.put(`/attendance/${id}/lock`),
};

export const documentApi = {
  uploadMaterial: (formData) => axiosInstance.post('/materials/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }),
  getMyMaterials: () => axiosInstance.get('/materials/my'),
  getClassMaterials: (classId) => axiosInstance.get(`/materials/class/${classId}`),
};

export const requestApi = {
  getStudentRequests: () => axiosInstance.get('/student/requests'),
  submitRequest: (data) => axiosInstance.post('/student/requests', data),
  getAdvisorRequests: () => axiosInstance.get('/advisor/requests'),
  approveRequest: (id) => axiosInstance.put(`/advisor/requests/${id}/approve`),
  rejectRequest: (id) => axiosInstance.put(`/advisor/requests/${id}/reject`),
};
