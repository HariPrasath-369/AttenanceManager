import React, { useState } from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import Chart from '../../components/dashboard/Chart';
import SelectField from '../../components/forms/SelectField';

const Performance = () => {
    const { data: departments } = useFetch(api.principal.getDepartments);
    const [selectedDept, setSelectedDept] = useState('');
    const { data: performance, loading } = useFetch(
        () => api.principal.getPerformanceByDepartment(selectedDept),
        [selectedDept]
    );

    const chartData = {
        labels: performance?.map((p) => p.className) || [],
        datasets: [
            {
                label: 'Average Marks',
                data: performance?.map((p) => p.avgMarks) || [],
                backgroundColor: 'rgba(59, 130, 246, 0.5)',
            },
            {
                label: 'Attendance %',
                data: performance?.map((p) => p.attendance) || [],
                backgroundColor: 'rgba(16, 185, 129, 0.5)',
            },
        ],
    };

    return (
        <div>
            <h1 className="text-2xl font-bold mb-6">Department Performance</h1>
            <div className="mb-6 w-64">
                <SelectField
                    label="Select Department"
                    name="department"
                    value={selectedDept}
                    onChange={(e) => setSelectedDept(e.target.value)}
                    options={departments?.map((d) => ({ value: d.id, label: d.name })) || []}
                />
            </div>
            {loading && <div>Loading...</div>}
            {performance && performance.length > 0 && (
                <div className="bg-white p-6 rounded-lg shadow">
                    <Chart type="bar" data={chartData} options={{ responsive: true }} />
                </div>
            )}
            {selectedDept && performance?.length === 0 && !loading && (
                <p>No performance data for this department.</p>
            )}
        </div>
    );
};

export default Performance;