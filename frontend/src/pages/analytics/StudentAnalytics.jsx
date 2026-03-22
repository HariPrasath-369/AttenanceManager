import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import Chart from '../../components/dashboard/Chart';

const StudentAnalytics = () => {
    const { data: marks } = useFetch(api.student.getMarks);
    const { data: attendance } = useFetch(() =>
        api.student.getAttendance(
            new Date(new Date().setMonth(new Date().getMonth() - 3)).toISOString().slice(0, 10),
            new Date().toISOString().slice(0, 10)
        )
    );

    const marksChartData = {
        labels: marks?.subjectMarks?.map((s) => s.subject) || [],
        datasets: [
            {
                label: 'Marks',
                data: marks?.subjectMarks?.map((s) => s.marks) || [],
                backgroundColor: 'rgba(59, 130, 246, 0.5)',
            },
        ],
    };

    const attendanceChartData = {
        labels: attendance?.dailyAttendance?.map((d) => d.date) || [],
        datasets: [
            {
                label: 'Attendance %',
                data: attendance?.dailyAttendance?.map((d) => d.present ? 100 : 0) || [],
                backgroundColor: 'rgba(16, 185, 129, 0.5)',
            },
        ],
    };

    return (
        <div>
            <h1 className="text-2xl font-bold mb-6">My Analytics</h1>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-lg font-semibold mb-4">Subject-wise Marks</h2>
                    <Chart type="bar" data={marksChartData} options={{ responsive: true }} />
                </div>
                <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-lg font-semibold mb-4">Attendance Trend</h2>
                    <Chart type="line" data={attendanceChartData} options={{ responsive: true }} />
                </div>
            </div>
        </div>
    );
};

export default StudentAnalytics;