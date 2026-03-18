import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const PrivateRoute = ({ children, requiredRole }) => {
    const { user } = useAuth();

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    if (requiredRole && user.role !== requiredRole && user.role !== 'MAIN') {
        // MAIN teacher can access everything
        return <Navigate to="/dashboard" replace />;
    }

    return children;
};

export default PrivateRoute;
