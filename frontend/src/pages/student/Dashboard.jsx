import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import StatsGrid from '../../components/dashboard/StatsGrid';
import Chart from '../../components/dashboard/Chart';
import { ClipboardCheck, Trophy, FileText, Calendar } from 'lucide-react';

const StudentDashboard = () => {
  const { data: profile, loading: profileLoading } = useFetch(api.student.getProfile);
  const { data: marks } = useFetch(api.student.getMarks);
  const { data: attendance, loading: attendanceLoading } = useFetch(() =>
    api.student.getAttendance(new Date(new Date().setMonth(new Date().getMonth() - 1)).toISOString().slice(0, 10), new Date().toISOString().slice(0, 10))
  );

  if (profileLoading || attendanceLoading) return <div>Loading...</div>;

  const stats = [
    { title: 'Attendance', value: `${attendance?.percentage || 0}%`, icon: ClipboardCheck },
    { title: 'GPA', value: marks?.gpa || 'N/A', icon: Trophy },
    { title: 'Materials', value: marks?.materialCount || 0, icon: FileText },
    { title: 'Upcoming Classes', value: '3', icon: Calendar }, // placeholder
  ];

  const chartData = {
    labels: marks?.subjectMarks?.map((s) => s.subject) || [],
    datasets: [
      {
        label: 'Marks',
        data: marks?.subjectMarks?.map((s) => s.marks) || [],
        backgroundColor: 'rgba(59, 130, 246, 0.5)',
      },
    ],
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Welcome, {profile?.fullName}</h1>
      <StatsGrid stats={stats} />
      <div className="mt-8 bg-white p-6 rounded-lg shadow">
        <h2 className="text-lg font-semibold mb-4">Recent Marks</h2>
        <Chart type="bar" data={chartData} options={{ responsive: true }} />
      </div>
    </div>
  );
};

export default StudentDashboard;