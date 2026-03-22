import React, { useState } from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';
import Modal from '../../components/common/Modal';
import InputField from '../../components/forms/InputField';
import toast from 'react-hot-toast';

const HodSubjects = () => {
    const { data: subjects, loading, refetch } = useFetch(api.hod.getSubjects);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({ name: '', code: '' });
    const [editingId, setEditingId] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingId) {
                await api.hod.updateSubject(editingId, formData);
                toast.success('Subject updated');
            } else {
                await api.hod.createSubject(formData);
                toast.success('Subject created');
            }
            refetch();
            setIsModalOpen(false);
            setFormData({ name: '', code: '' });
            setEditingId(null);
        } catch (error) {
            toast.error(error.response?.data?.message || 'Operation failed');
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure?')) {
            await api.hod.deleteSubject(id);
            toast.success('Subject deleted');
            refetch();
        }
    };

    const columns = [
        { key: 'name', label: 'Name' },
        { key: 'code', label: 'Code' },
    ];

    const actions = (row) => (
        <div className="flex space-x-2">
            <button
                onClick={() => {
                    setEditingId(row.id);
                    setFormData({ name: row.name, code: row.code });
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
                <h1 className="text-2xl font-bold">Subjects</h1>
                <button onClick={() => setIsModalOpen(true)} className="btn-primary">
                    Add Subject
                </button>
            </div>
            <DataTable columns={columns} data={subjects || []} actions={actions} />
            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title={editingId ? 'Edit Subject' : 'Add Subject'}>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <InputField
                        label="Name"
                        name="name"
                        value={formData.name}
                        onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                        required
                    />
                    <InputField
                        label="Code"
                        name="code"
                        value={formData.code}
                        onChange={(e) => setFormData({ ...formData, code: e.target.value })}
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

export default HodSubjects;