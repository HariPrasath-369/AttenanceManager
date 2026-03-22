import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import StatsGrid from '../../components/dashboard/StatsGrid';
import Chart from '../../components/dashboard/Chart';
import { Building2, Users, GraduationCap, Trophy } from 'lucide-react';

const PrincipalDashboard = () => {
  const { data: dashboard, loading } = useFetch(api.principal.getPrincipalDashboard);

  if (loading) return <div>Loading...</div>;

  const stats = [
    { title: 'Departments', value: dashboard?.totalDepartments || 0, icon: Building2 },
    { title: 'Teachers', value: dashboard?.totalTeachers || 0, icon: Users },
    { title: 'Students', value: dashboard?.totalStudents || 0, icon: GraduationCap },
    { title: 'Avg Performance', value: `${dashboard?.avgPerformance || 0}%`, icon: Trophy, trend: '+2%', trendType: 'positive' },
  ];

  const chartData = {
    labels: dashboard?.departmentNames || [],
    datasets: [
      {
        label: 'Average Marks',
        data: dashboard?.departmentAvgMarks || [],
        backgroundColor: 'rgba(59, 130, 246, 0.5)',
      },
    ],
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Principal Dashboard</h1>
      <StatsGrid stats={stats} />
      <div className="mt-8 bg-white p-6 rounded-lg shadow">
        <h2 className="text-lg font-semibold mb-4">Department Performance</h2>
        <Chart type="bar" data={chartData} options={{ responsive: true }} />
      </div>
    </div>
  );
};

export default PrincipalDashboard;