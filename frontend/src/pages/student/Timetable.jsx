import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';

const days = {
    1: 'Monday',
    2: 'Tuesday',
    3: 'Wednesday',
    4: 'Thursday',
    5: 'Friday',
    6: 'Saturday',
    7: 'Sunday',
};

const StudentTimetable = () => {
    const { data: timetable, loading } = useFetch(api.student.getTimetable);

    const columns = [
        { key: 'dayOfWeek', label: 'Day', render: (row) => days[row.dayOfWeek] },
        { key: 'period', label: 'Period' },
        { key: 'subjectName', label: 'Subject' },
        { key: 'teacherName', label: 'Teacher' },
        { key: 'venue', label: 'Venue' },
    ];

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <h1 className="text-2xl font-bold mb-6">My Timetable</h1>
            <DataTable columns={columns} data={timetable || []} />
        </div>
    );
};

export default StudentTimetable;