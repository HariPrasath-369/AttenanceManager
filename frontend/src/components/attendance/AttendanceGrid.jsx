import React from 'react';

const AttendanceGrid = ({ students, attendance, onToggle, submitted, session }) => {
    return (
        <div className="overflow-x-auto">
            <table className="min-w-full bg-white border">
                <thead>
                    <tr className="bg-gray-50">
                        <th className="px-4 py-2 border">Roll No</th>
                        <th className="px-4 py-2 border">Name</th>
                        <th className="px-4 py-2 border">{session === 'MORNING' ? 'Morning' : 'Afternoon'}</th>
                    </tr>
                </thead>
                <tbody>
                    {students.map((student) => (
                        <tr key={student.id}>
                            <td className="px-4 py-2 border">{student.rollNumber}</td>
                            <td className="px-4 py-2 border">{student.fullName}</td>
                            <td className="px-4 py-2 border text-center">
                                <input
                                    type="checkbox"
                                    checked={attendance[student.id] || false}
                                    onChange={() => onToggle(student.id)}
                                    disabled={submitted}
                                    className="w-4 h-4"
                                />
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default AttendanceGrid;