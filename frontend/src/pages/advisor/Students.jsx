import React, { useState } from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';
import Modal from '../../components/common/Modal';
import InputField from '../../components/forms/InputField';
import toast from 'react-hot-toast';

const AdvisorStudents = () => {
    const { data: students, loading, refetch } = useFetch(api.advisor.getStudents);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({ rollNumber: '', fullName: '', email: '', dateOfBirth: '', parentContact: '', parentEmail: '' });
    const [editingId, setEditingId] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingId) {
                await api.advisor.updateStudent(editingId, formData);
                toast.success('Student updated');
            } else {
                await api.advisor.createStudent(formData);
                toast.success('Student created');
            }
            refetch();
            setIsModalOpen(false);
            setFormData({ rollNumber: '', fullName: '', email: '', dateOfBirth: '', parentContact: '', parentEmail: '' });
            setEditingId(null);
        } catch (error) {
            toast.error(error.response?.data?.message || 'Operation failed');
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure?')) {
            await api.advisor.deleteStudent(id);
            toast.success('Student deleted');
            refetch();
        }
    };

    const columns = [
        { key: 'rollNumber', label: 'Roll No' },
        { key: 'fullName', label: 'Name' },
        { key: 'email', label: 'Email' },
        { key: 'dateOfBirth', label: 'DOB' },
        { key: 'parentContact', label: 'Parent Contact' },
    ];

    const actions = (row) => (
        <div className="flex space-x-2">
            <button
                onClick={() => {
                    setEditingId(row.id);
                    setFormData({
                        rollNumber: row.rollNumber,
                        fullName: row.fullName,
                        email: row.email,
                        dateOfBirth: row.dateOfBirth,
                        parentContact: row.parentContact,
                        parentEmail: row.parentEmail,
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

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Students</h1>
                <button onClick={() => setIsModalOpen(true)} className="btn-primary">
                    Add Student
                </button>
            </div>
            <DataTable columns={columns} data={students || []} actions={actions} />
            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title={editingId ? 'Edit Student' : 'Add Student'}>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <InputField
                        label="Roll Number"
                        name="rollNumber"
                        value={formData.rollNumber}
                        onChange={(e) => setFormData({ ...formData, rollNumber: e.target.value })}
                        required
                    />
                    <InputField
                        label="Full Name"
                        name="fullName"
                        value={formData.fullName}
                        onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                        required
                    />
                    <InputField
                        label="Email"
                        name="email"
                        type="email"
                        value={formData.email}
                        onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                        required
                    />
                    <InputField
                        label="Date of Birth"
                        name="dateOfBirth"
                        type="date"
                        value={formData.dateOfBirth}
                        onChange={(e) => setFormData({ ...formData, dateOfBirth: e.target.value })}
                        required
                    />
                    <InputField
                        label="Parent Contact"
                        name="parentContact"
                        value={formData.parentContact}
                        onChange={(e) => setFormData({ ...formData, parentContact: e.target.value })}
                        required
                    />
                    <InputField
                        label="Parent Email"
                        name="parentEmail"
                        type="email"
                        value={formData.parentEmail}
                        onChange={(e) => setFormData({ ...formData, parentEmail: e.target.value })}
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

export default AdvisorStudents;