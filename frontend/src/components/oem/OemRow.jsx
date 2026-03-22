import React from 'react';
import GradeBadge from './GradeBadge';

const OemRow = ({ student, marks, maxMarks, onChange, disabled }) => {
    const total = marks?.assessment + marks?.practical + marks?.semester;
    const grade = total ? GradeBadge({ total, maxMarks: maxMarks * 3 }) : null;

    return (
        <tr>
            <td className="px-4 py-2 border">{student.rollNumber}</td>
            <td className="px-4 py-2 border">{student.fullName}</td>
            <td className="px-4 py-2 border">
                <input
                    type="number"
                    value={marks?.assessment || 0}
                    onChange={(e) => onChange(student.id, 'assessment', e.target.value)}
                    disabled={disabled}
                    className="w-20 p-1 border rounded"
                />
            </td>
            <td className="px-4 py-2 border">
                <input
                    type="number"
                    value={marks?.practical || 0}
                    onChange={(e) => onChange(student.id, 'practical', e.target.value)}
                    disabled={disabled}
                    className="w-20 p-1 border rounded"
                />
            </td>
            <td className="px-4 py-2 border">
                <input
                    type="number"
                    value={marks?.semester || 0}
                    onChange={(e) => onChange(student.id, 'semester', e.target.value)}
                    disabled={disabled}
                    className="w-20 p-1 border rounded"
                />
            </td>
            <td className="px-4 py-2 border text-center font-bold">{total || 0}</td>
            <td className="px-4 py-2 border text-center">{grade}</td>
        </tr>
    );
};

export default OemRow;