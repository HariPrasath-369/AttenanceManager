import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';

const TeacherClasses = () => {
    const { data: classes, loading } = useFetch(api.teacher.getClasses);

    const columns = [
        { key: 'name', label: 'Class Name' },
        { key: 'departmentName', label: 'Department' },
        { key: 'year', label: 'Year' },
        { key: 'semester', label: 'Semester' },
        { key: 'capacity', label: 'Capacity' },
    ];

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <h1 className="text-2xl font-bold mb-6">My Classes</h1>
            <DataTable columns={columns} data={classes || []} />
        </div>
    );
};

export default TeacherClasses;