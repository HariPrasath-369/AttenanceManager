import React from 'react';

const AttendanceSummary = ({ total, present, absent, other }) => {
    return (
        <div className="grid grid-cols-4 gap-4 mb-6">
            <div className="bg-white p-4 rounded shadow text-center">
                <p className="text-sm text-gray-500">Total</p>
                <p className="text-2xl font-bold">{total}</p>
            </div>
            <div className="bg-white p-4 rounded shadow text-center">
                <p className="text-sm text-gray-500">Present</p>
                <p className="text-2xl font-bold text-green-600">{present}</p>
            </div>
            <div className="bg-white p-4 rounded shadow text-center">
                <p className="text-sm text-gray-500">Absent</p>
                <p className="text-2xl font-bold text-red-600">{absent}</p>
            </div>
            <div className="bg-white p-4 rounded shadow text-center">
                <p className="text-sm text-gray-500">OD/Leave</p>
                <p className="text-2xl font-bold text-yellow-600">{other}</p>
            </div>
        </div>
    );
};

export default AttendanceSummary;