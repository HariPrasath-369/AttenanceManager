import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import StudentLanding from './pages/StudentLanding';
import Attendance from './pages/Attendance';
import StudentManagement from './pages/StudentManagement';
import ManageTeachers from './pages/ManageTeachers';
import Messages from './pages/Messages';
import Documents from './pages/Documents';
import Settings from './pages/Settings';

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="min-h-screen bg-gray-50 text-gray-900">
          <Routes>
            {/* Public Routes */}
            <Route path="/" element={<StudentLanding />} />
            <Route path="/login" element={<Login />} />
            
            {/* Protected Routes (Teacher) */}
            <Route path="/dashboard" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
            <Route path="/students" element={<PrivateRoute><StudentManagement /></PrivateRoute>} />
            <Route path="/attendance" element={<PrivateRoute><Attendance /></PrivateRoute>} />
            <Route path="/messages" element={<PrivateRoute><Messages /></PrivateRoute>} />
            <Route path="/documents" element={<PrivateRoute><Documents /></PrivateRoute>} />
            <Route path="/settings" element={<PrivateRoute><Settings /></PrivateRoute>} />
            
            {/* Main Admin Only Route */}
            <Route path="/manage-teachers" element={<PrivateRoute requiredRole="MAIN"><ManageTeachers /></PrivateRoute>} />

            {/* Fallback */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
