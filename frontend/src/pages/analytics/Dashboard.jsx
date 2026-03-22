import React from 'react';
import { useAuth } from '../../context/AuthContext';
import StudentAnalytics from './StudentAnalytics';
import ClassAnalytics from './ClassAnalytics';

const AnalyticsDashboard = () => {
    const { user } = useAuth();
    const role = user?.roles?.[0];

    if (role === 'PRINCIPAL' || role === 'HOD') {
        return <ClassAnalytics />;
    } else if (role === 'CLASS_ADVISOR' || role === 'TEACHER') {
        return <ClassAnalytics />;
    } else if (role === 'STUDENT') {
        return <StudentAnalytics />;
    }
    return <div>Analytics not available for your role.</div>;
};

export default AnalyticsDashboard;