import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import Chart from '../../components/dashboard/Chart';

const ClassAnalytics = () => {
    const { data: performance } = useFetch(api.advisor.getClassPerformance); // example

    const chartData = {
        labels: performance?.map((p) => p.studentName) || [],
        datasets: [
            {
                label: 'Attendance %',
                data: performance?.map((p) => p.attendancePercentage) || [],
                backgroundColor: 'rgba(59, 130, 246, 0.5)',
            },
            {
                label: 'Marks %',
                data: performance?.map((p) => p.avgMarks) || [],
                backgroundColor: 'rgba(16, 185, 129, 0.5)',
            },
        ],
    };

    return (
        <div>
            <h1 className="text-2xl font-bold mb-6">Class Analytics</h1>
            <div className="bg-white p-6 rounded-lg shadow">
                <Chart type="bar" data={chartData} options={{ responsive: true }} />
            </div>
        </div>
    );
};

export default ClassAnalytics;