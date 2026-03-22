import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import ProtectedRoute from '../components/common/ProtectedRoute';
import MainLayout from '../layouts/MainLayout';
import AuthLayout from '../layouts/AuthLayout';

// Auth pages
import Login from '../pages/auth/Login';
import Register from '../pages/auth/Register';
import ChangePassword from '../pages/auth/ChangePassword';

// Principal
import PrincipalDashboard from '../pages/principal/Dashboard';
import Departments from '../pages/principal/Departments';
import Hods from '../pages/principal/Hods';
import Performance from '../pages/principal/Performance';

// HOD
import HodDashboard from '../pages/hod/Dashboard';
import HodTeachers from '../pages/hod/Teachers';
import HodClasses from '../pages/hod/Classes';
import HodSubjects from '../pages/hod/Subjects';
import HodTimetable from '../pages/hod/Timetable';
import HodSuggestions from '../pages/hod/Suggestions';

// Advisor
import AdvisorDashboard from '../pages/advisor/Dashboard';
import AdvisorStudents from '../pages/advisor/Students';
import AdvisorAttendance from '../pages/advisor/Attendance';
import AdvisorRequests from '../pages/advisor/Requests';
import AdvisorPerformance from '../pages/advisor/Performance';

// Teacher
import TeacherDashboard from '../pages/teacher/Dashboard';
import TeacherClasses from '../pages/teacher/Classes';
import TeacherMaterials from '../pages/teacher/Materials';
import TeacherOemBoard from '../pages/teacher/OemBoard';
import TeacherTimetable from '../pages/teacher/Timetable';

// Student
import StudentDashboard from '../pages/student/Dashboard';
import StudentAttendance from '../pages/student/Attendance';
import StudentMarks from '../pages/student/Marks';
import StudentMaterials from '../pages/student/Materials';
import StudentRequests from '../pages/student/Requests';
import StudentTimetable from '../pages/student/Timetable';

// Analytics
import AnalyticsDashboard from '../pages/analytics/Dashboard';

const AppRoutes = () => {
  return (
    <Routes>
      {/* Auth routes */}
      <Route element={<AuthLayout />}>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/change-password" element={<ChangePassword />} />
      </Route>

      {/* Protected routes */}
      <Route element={<ProtectedRoute />}>
        <Route element={<MainLayout />}>
          {/* Principal */}
          <Route element={<ProtectedRoute allowedRoles={['PRINCIPAL']} />}>
            <Route path="/principal/dashboard" element={<PrincipalDashboard />} />
            <Route path="/principal/departments" element={<Departments />} />
            <Route path="/principal/hods" element={<Hods />} />
            <Route path="/principal/performance" element={<Performance />} />
          </Route>

          {/* HOD */}
          <Route element={<ProtectedRoute allowedRoles={['HOD']} />}>
            <Route path="/hod/dashboard" element={<HodDashboard />} />
            <Route path="/hod/teachers" element={<HodTeachers />} />
            <Route path="/hod/classes" element={<HodClasses />} />
            <Route path="/hod/subjects" element={<HodSubjects />} />
            <Route path="/hod/timetable" element={<HodTimetable />} />
            <Route path="/hod/suggestions" element={<HodSuggestions />} />
          </Route>

          {/* Class Advisor */}
          <Route element={<ProtectedRoute allowedRoles={['CLASS_ADVISOR']} />}>
            <Route path="/advisor/dashboard" element={<AdvisorDashboard />} />
            <Route path="/advisor/students" element={<AdvisorStudents />} />
            <Route path="/advisor/attendance" element={<AdvisorAttendance />} />
            <Route path="/advisor/requests" element={<AdvisorRequests />} />
            <Route path="/advisor/performance" element={<AdvisorPerformance />} />
          </Route>

          {/* Teacher */}
          <Route element={<ProtectedRoute allowedRoles={['TEACHER']} />}>
            <Route path="/teacher/dashboard" element={<TeacherDashboard />} />
            <Route path="/teacher/classes" element={<TeacherClasses />} />
            <Route path="/teacher/materials" element={<TeacherMaterials />} />
            <Route path="/teacher/oem" element={<TeacherOemBoard />} />
            <Route path="/teacher/timetable" element={<TeacherTimetable />} />
          </Route>

          {/* Student */}
          <Route element={<ProtectedRoute allowedRoles={['STUDENT']} />}>
            <Route path="/student/dashboard" element={<StudentDashboard />} />
            <Route path="/student/attendance" element={<StudentAttendance />} />
            <Route path="/student/marks" element={<StudentMarks />} />
            <Route path="/student/materials" element={<StudentMaterials />} />
            <Route path="/student/requests" element={<StudentRequests />} />
            <Route path="/student/timetable" element={<StudentTimetable />} />
          </Route>

          {/* Analytics */}
          <Route path="/analytics" element={<AnalyticsDashboard />} />
        </Route>
      </Route>

      {/* Default */}
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
};

export default AppRoutes;