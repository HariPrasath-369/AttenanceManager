import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import StatsGrid from '../../components/dashboard/StatsGrid';
import { BookOpen, FileText, Calendar, Trophy } from 'lucide-react';

const TeacherDashboard = () => {
    const { data: dashboard, loading } = useFetch(api.teacher.getDashboard); // If teacher dashboard endpoint exists; fallback to classes count
    const { data: classes } = useFetch(api.teacher.getClasses);
    const { data: subjects } = useFetch(api.teacher.getSubjects);

    if (loading) return <div>Loading...</div>;

    const stats = [
        { title: 'Classes', value: classes?.length || 0, icon: BookOpen },
        { title: 'Subjects', value: subjects?.length || 0, icon: FileText },
        { title: 'Timetable Slots', value: dashboard?.timetableCount || 0, icon: Calendar },
        { title: 'Average Student Performance', value: `${dashboard?.avgPerformance || 0}%`, icon: Trophy },
    ];

    return (
        <div>
            <h1 className="text-2xl font-bold mb-6">Teacher Dashboard</h1>
            <StatsGrid stats={stats} />
            <div className="mt-8 bg-white p-6 rounded-lg shadow">
                <h2 className="text-lg font-semibold mb-4">Today's Classes</h2>
                {/* Could show today's timetable */}
                <p>No classes today.</p>
            </div>
        </div>
    );
};

export default TeacherDashboard;