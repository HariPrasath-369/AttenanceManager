import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import StatsGrid from '../../components/dashboard/StatsGrid';
import { Users, BookOpen, FileText, Trophy } from 'lucide-react';

const HodDashboard = () => {
  const { data: dashboard, loading } = useFetch(api.hod.getDashboard);

  if (loading) return <div>Loading...</div>;

  const stats = [
    { title: 'Teachers', value: dashboard?.totalTeachers || 0, icon: Users },
    { title: 'Classes', value: dashboard?.totalClasses || 0, icon: BookOpen },
    { title: 'Subjects', value: dashboard?.totalSubjects || 0, icon: FileText },
    { title: 'Avg Class Performance', value: `${dashboard?.avgPerformance || 0}%`, icon: Trophy },
  ];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">HOD Dashboard</h1>
      <StatsGrid stats={stats} />
      <div className="mt-8 grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-lg font-semibold mb-4">Pending Semester Requests</h2>
          {/* List of pending requests – would fetch from API */}
          <p>No pending requests.</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-lg font-semibold mb-4">Recent Suggestions</h2>
          {/* List of suggestions */}
          <p>No suggestions.</p>
        </div>
      </div>
    </div>
  );
};

export default HodDashboard;