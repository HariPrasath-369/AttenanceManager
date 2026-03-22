import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';
import Chart from '../../components/dashboard/Chart';

const AdvisorPerformance = () => {
    const { data: performance, loading } = useFetch(api.advisor.getClassPerformance);

    const columns = [
        { key: 'studentName', label: 'Student' },
        { key: 'attendancePercentage', label: 'Attendance %' },
        { key: 'avgMarks', label: 'Average Marks' },
        { key: 'grade', label: 'Grade' },
    ];

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

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <h1 className="text-2xl font-bold mb-6">Class Performance</h1>
            <div className="mb-8 bg-white p-6 rounded-lg shadow">
                <Chart type="bar" data={chartData} options={{ responsive: true }} />
            </div>
            <DataTable columns={columns} data={performance || []} />
        </div>
    );
};

export default AdvisorPerformance;