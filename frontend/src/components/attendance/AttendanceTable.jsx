import React from 'react';

const AttendanceTable = ({ data }) => {
    return (
        <div className="overflow-x-auto">
            <table className="min-w-full bg-white border">
                <thead>
                    <tr className="bg-gray-50">
                        <th className="px-4 py-2 border">Date</th>
                        <th className="px-4 py-2 border">Student</th>
                        <th className="px-4 py-2 border">Morning</th>
                        <th className="px-4 py-2 border">Afternoon</th>
                    </tr>
                </thead>
                <tbody>
                    {data.map((row, idx) => (
                        <tr key={idx}>
                            <td className="px-4 py-2 border">{row.date}</td>
                            <td className="px-4 py-2 border">{row.studentName}</td>
                            <td className="px-4 py-2 border text-center">{row.morningPresent ? '✓' : '✗'}</td>
                            <td className="px-4 py-2 border text-center">{row.afternoonPresent ? '✓' : '✗'}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default AttendanceTable;