import React, { useState } from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';
import Modal from '../../components/common/Modal';
import InputField from '../../components/forms/InputField';
import SelectField from '../../components/forms/SelectField';
import toast from 'react-hot-toast';

const HodTeachers = () => {
    const { data: teachers, loading, refetch } = useFetch(api.hod.getTeachers);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({ userId: '', joiningDate: '' });
    const [editingId, setEditingId] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingId) {
                await api.hod.updateTeacher(editingId, formData);
                toast.success('Teacher updated');
            } else {
                await api.hod.createTeacher(formData);
                toast.success('Teacher created');
            }
            refetch();
            setIsModalOpen(false);
            setFormData({ userId: '', joiningDate: '' });
            setEditingId(null);
        } catch (error) {
            toast.error(error.response?.data?.message || 'Operation failed');
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure?')) {
            await api.hod.deleteTeacher(id);
            toast.success('Teacher deleted');
            refetch();
        }
    };

    const columns = [
        { key: 'fullName', label: 'Name' },
        { key: 'email', label: 'Email' },
        { key: 'departmentName', label: 'Department' },
        { key: 'advisorClassName', label: 'Advisor For' },
        { key: 'joiningDate', label: 'Joining Date' },
    ];

    const actions = (row) => (
        <div className="flex space-x-2">
            <button
                onClick={() => {
                    setEditingId(row.id);
                    setFormData({ userId: row.userId, joiningDate: row.joiningDate });
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

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Teachers</h1>
                <button onClick={() => setIsModalOpen(true)} className="btn-primary">
                    Add Teacher
                </button>
            </div>
            <DataTable columns={columns} data={teachers || []} actions={actions} />
            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title={editingId ? 'Edit Teacher' : 'Add Teacher'}>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <SelectField
                        label="User"
                        name="userId"
                        value={formData.userId}
                        onChange={(e) => setFormData({ ...formData, userId: e.target.value })}
                        options={[]} // Need user list
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

export default HodTeachers;