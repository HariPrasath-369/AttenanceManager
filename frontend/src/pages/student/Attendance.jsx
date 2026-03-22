import React, { useState } from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import AttendanceTable from '../../components/attendance/AttendanceTable';
import Chart from '../../components/dashboard/Chart';

const StudentAttendance = () => {
    const [startDate, setStartDate] = useState(new Date(new Date().setMonth(new Date().getMonth() - 1)).toISOString().slice(0, 10));
    const [endDate, setEndDate] = useState(new Date().toISOString().slice(0, 10));
    const { data: attendance, loading } = useFetch(() =>
        api.student.getAttendance(startDate, endDate),
        [startDate, endDate]
    );

    if (loading) return <div>Loading...</div>;

    const chartData = {
        labels: attendance?.dailyAttendance?.map((d) => d.date) || [],
        datasets: [
            {
                label: 'Attendance',
                data: attendance?.dailyAttendance?.map((d) => d.present ? 100 : 0) || [],
                backgroundColor: 'rgba(59, 130, 246, 0.5)',
            },
        ],
    };

    return (
        <div>
            <h1 className="text-2xl font-bold mb-6">My Attendance</h1>
            <div className="flex space-x-4 mb-6">
                <div>
                    <label className="block text-sm font-medium">From</label>
                    <input
                        type="date"
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                        className="input-field"
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium">To</label>
                    <input
                        type="date"
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                        className="input-field"
                    />
                </div>
            </div>
            <div className="bg-white p-6 rounded-lg shadow mb-6">
                <Chart type="bar" data={chartData} options={{ responsive: true }} />
            </div>
            <AttendanceTable data={attendance?.records || []} />
        </div>
    );
};

export default StudentAttendance;