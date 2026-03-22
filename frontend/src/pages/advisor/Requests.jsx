import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';
import toast from 'react-hot-toast';

const AdvisorRequests = () => {
    const { data: requests, loading, refetch } = useFetch(api.advisor.getPendingRequests);

    const handleApprove = async (id) => {
        try {
            await api.advisor.approveRequest(id, 'Approved');
            toast.success('Request approved');
            refetch();
        } catch (error) {
            toast.error('Failed to approve');
        }
    };

    const handleReject = async (id) => {
        try {
            await api.advisor.rejectRequest(id, 'Rejected');
            toast.success('Request rejected');
            refetch();
        } catch (error) {
            toast.error('Failed to reject');
        }
    };

    const columns = [
        { key: 'type', label: 'Type' },
        { key: 'description', label: 'Description' },
        { key: 'studentName', label: 'Student' },
        { key: 'studentRollNumber', label: 'Roll No' },
        { key: 'createdAt', label: 'Date', render: (row) => new Date(row.createdAt).toLocaleDateString() },
    ];

    const actions = (row) => (
        <div className="flex space-x-2">
            <button
                onClick={() => handleApprove(row.id)}
                className="text-green-600 hover:text-green-800"
            >
                Approve
            </button>
            <button
                onClick={() => handleReject(row.id)}
                className="text-red-600 hover:text-red-800"
            >
                Reject
            </button>
        </div>
    );

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <h1 className="text-2xl font-bold mb-6">Leave/OD Requests</h1>
            <DataTable columns={columns} data={requests || []} actions={actions} />
        </div>
    );
};

export default AdvisorRequests;