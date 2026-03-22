import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';

const HodSuggestions = () => {
    const { data: suggestions, loading } = useFetch(api.hod.getSuggestions);

    const columns = [
        { key: 'message', label: 'Message' },
        { key: 'fromUser', label: 'From' },
        { key: 'createdAt', label: 'Date', render: (row) => new Date(row.createdAt).toLocaleDateString() },
    ];

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <h1 className="text-2xl font-bold mb-6">Suggestions</h1>
            <DataTable columns={columns} data={suggestions || []} />
        </div>
    );
};

export default HodSuggestions;