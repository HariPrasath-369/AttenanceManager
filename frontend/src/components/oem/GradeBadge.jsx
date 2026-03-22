import React from 'react';

const GradeBadge = ({ total, maxMarks }) => {
    const percentage = (total / maxMarks) * 100;
    let grade = 'F';
    if (percentage >= 90) grade = 'A+';
    else if (percentage >= 80) grade = 'A';
    else if (percentage >= 70) grade = 'B+';
    else if (percentage >= 60) grade = 'B';
    else if (percentage >= 50) grade = 'C';
    else if (percentage >= 40) grade = 'D';

    const colorClass = {
        'A+': 'text-green-600',
        'A': 'text-green-600',
        'B+': 'text-blue-600',
        'B': 'text-blue-600',
        'C': 'text-yellow-600',
        'D': 'text-orange-600',
        'F': 'text-red-600',
    }[grade];

    return <span className={`font-bold ${colorClass}`}>{grade}</span>;
};

export default GradeBadge;