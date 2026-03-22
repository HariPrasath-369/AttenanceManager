import React, { useState } from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';
import Modal from '../../components/common/Modal';
import InputField from '../../components/forms/InputField';
import SelectField from '../../components/forms/SelectField';
import toast from 'react-hot-toast';

const Hods = () => {
    // Fetch HODs, departments, and all users
    const { data: hods, loading: hodsLoading, refetch } = useFetch(api.principal.getHods);
    const { data: departments, loading: deptsLoading } = useFetch(api.principal.getDepartments);
    const { data: users, loading: usersLoading } = useFetch(api.user.getAll);

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({ userId: '', departmentId: '', joiningDate: '' });
    const [editingId, setEditingId] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingId) {
                await api.principal.updateHod(editingId, formData);
                toast.success('HOD updated');
            } else {
                await api.principal.createHod(formData);
                toast.success('HOD created');
            }
            refetch();
            setIsModalOpen(false);
            setFormData({ userId: '', departmentId: '', joiningDate: '' });
            setEditingId(null);
        } catch (error) {
            console.error('Create/update HOD error:', error.response?.data);
            toast.error(error.response?.data?.message || 'Operation failed');
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure?')) {
            try {
                await api.principal.deleteHod(id);
                toast.success('HOD deleted');
                refetch();
            } catch (error) {
                toast.error(error.response?.data?.message || 'Delete failed');
            }
        }
    };

    // Prepare options for user select
    const userOptions = users?.map(user => ({
        value: user.id,
        label: user.fullName || user.username
    })) || [];

    const departmentOptions = departments?.map(dept => ({
        value: dept.id,
        label: dept.name
    })) || [];

    const columns = [
        { key: 'userName', label: 'Name' },
        { key: 'departmentName', label: 'Department' },
        { key: 'joiningDate', label: 'Joining Date' },
    ];

    const actions = (row) => (
        <div className="flex space-x-2">
            <button
                onClick={() => {
                    setEditingId(row.id);
                    setFormData({
                        userId: row.userId,
                        departmentId: row.departmentId,
                        joiningDate: row.joiningDate
                    });
                    setIsModalOpen(true);
                }}
                className="text-blue-600 hover:text-blue-800"
            >
                Edit
            </button>
            <button onClick={() => handleDelete(row.id)} className="text-red-600 hover:text-red-800">
                Delete
            </button>
        </div>
    );

    if (hodsLoading || deptsLoading || usersLoading) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">HODs</h1>
                <button onClick={() => setIsModalOpen(true)} className="btn-primary">
                    Add HOD
                </button>
            </div>
            <DataTable columns={columns} data={hods || []} actions={actions} />
            <Modal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                title={editingId ? 'Edit HOD' : 'Add HOD'}
            >
                <form onSubmit={handleSubmit} className="space-y-4">
                    <SelectField
                        label="User"
                        name="userId"
                        value={formData.userId}
                        onChange={(e) => setFormData({ ...formData, userId: e.target.value })}
                        options={userOptions}
                        required
                    />
                    <SelectField
                        label="Department"
                        name="departmentId"
                        value={formData.departmentId}
                        onChange={(e) => setFormData({ ...formData, departmentId: e.target.value })}
                        options={departmentOptions}
                        required
                    />
                    <InputField
                        label="Joining Date"
                        name="joiningDate"
                        type="date"
                        value={formData.joiningDate}
                        onChange={(e) => setFormData({ ...formData, joiningDate: e.target.value })}
                        required
                    />
                    <button type="submit" className="btn-primary w-full">
                        {editingId ? 'Update' : 'Create'}
                    </button>
                </form>
            </Modal>
        </div>
    );
};

export default Hods;