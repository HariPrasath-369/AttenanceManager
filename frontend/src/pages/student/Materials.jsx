import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';

const StudentMaterials = () => {
    const { data: materials, loading } = useFetch(api.student.getMaterials);

    const columns = [
        { key: 'title', label: 'Title' },
        { key: 'description', label: 'Description' },
        { key: 'subjectName', label: 'Subject' },
        { key: 'uploadedAt', label: 'Date', render: (row) => new Date(row.uploadedAt).toLocaleDateString() },
        {
            key: 'fileUrl',
            label: 'File',
            render: (row) => (
                <a href={row.fileUrl} target="_blank" rel="noopener noreferrer" className="text-blue-600 underline">
                    Download
                </a>
            ),
        },
    ];

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <h1 className="text-2xl font-bold mb-6">Materials</h1>
            <DataTable columns={columns} data={materials || []} />
        </div>
    );
};

export default StudentMaterials;