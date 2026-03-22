import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import StatsGrid from '../../components/dashboard/StatsGrid';
import { Users, ClipboardCheck, MessageSquare, Trophy } from 'lucide-react';

const AdvisorDashboard = () => {
  const { data: dashboard, loading } = useFetch(api.advisor.getDashboard);

  if (loading) return <div>Loading...</div>;

  const stats = [
    { title: 'Total Students', value: dashboard?.totalStudents || 0, icon: Users },
    { title: 'Today Attendance', value: `${dashboard?.todayAttendance || 0}%`, icon: ClipboardCheck },
    { title: 'Pending Requests', value: dashboard?.pendingRequests || 0, icon: MessageSquare, trend: 'Action Required', trendType: 'negative' },
    { title: 'Class Performance', value: `${dashboard?.avgPerformance || 0}%`, icon: Trophy, trend: '+2%', trendType: 'positive' },
  ];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Advisor Dashboard</h1>
      <StatsGrid stats={stats} />
      <div className="mt-8 grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-lg font-semibold mb-4">Recent Requests</h2>
          {/* List of recent requests */}
          <p>No pending requests.</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-lg font-semibold mb-4">Attendance Alerts</h2>
          {/* Low attendance alerts */}
          <p>No alerts.</p>
        </div>
      </div>
    </div>
  );
};

export default AdvisorDashboard;