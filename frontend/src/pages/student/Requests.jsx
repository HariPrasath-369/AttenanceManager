import React, { useState } from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';
import Modal from '../../components/common/Modal';
import InputField from '../../components/forms/InputField';
import SelectField from '../../components/forms/SelectField';
import toast from 'react-hot-toast';

const StudentRequests = () => {
    const { data: requests, loading, refetch } = useFetch(api.student.getRequests);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({ type: 'LEAVE', description: '' });

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.student.submitRequest(formData);
            toast.success('Request submitted');
            setIsModalOpen(false);
            setFormData({ type: 'LEAVE', description: '' });
            refetch();
        } catch (error) {
            toast.error(error.response?.data?.message || 'Failed to submit');
        }
    };

    const columns = [
        { key: 'type', label: 'Type' },
        { key: 'description', label: 'Description' },
        {
            key: 'status',
            label: 'Status',
            render: (row) => (
                <span
                    className={`px-2 py-1 rounded text-xs ${row.status === 'APPROVED'
                            ? 'bg-green-100 text-green-800'
                            : row.status === 'REJECTED'
                                ? 'bg-red-100 text-red-800'
                                : 'bg-yellow-100 text-yellow-800'
                        }`}
                >
                    {row.status}
                </span>
            ),
        },
        { key: 'comments', label: 'Comments' },
        { key: 'createdAt', label: 'Submitted On', render: (row) => new Date(row.createdAt).toLocaleDateString() },
    ];

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">My Requests</h1>
                <button onClick={() => setIsModalOpen(true)} className="btn-primary">
                    New Request
                </button>
            </div>
            <DataTable columns={columns} data={requests || []} />
            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Submit Request">
                <form onSubmit={handleSubmit} className="space-y-4">
                    <SelectField
                        label="Type"
                        name="type"
                        value={formData.type}
                        onChange={(e) => setFormData({ ...formData, type: e.target.value })}
                        options={[
                            { value: 'LEAVE', label: 'Leave' },
                            { value: 'OD', label: 'OD' },
                        ]}
                        required
                    />
                    <InputField
                        label="Description"
                        name="description"
                        value={formData.description}
                        onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                        required
                    />
                    <button type="submit" className="btn-primary w-full">
                        Submit
                    </button>
                </form>
            </Modal>
        </div>
    );
};

export default StudentRequests;