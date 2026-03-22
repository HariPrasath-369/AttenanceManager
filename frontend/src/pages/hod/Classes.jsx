import React, { useState } from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';
import Modal from '../../components/common/Modal';
import InputField from '../../components/forms/InputField';
import toast from 'react-hot-toast';

const HodClasses = () => {
    const { data: classes, loading, refetch } = useFetch(api.hod.getClasses);
    const { data: teachers } = useFetch(api.hod.getTeachers);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({ name: '', year: '', semester: '', capacity: '' });
    const [assigning, setAssigning] = useState({ classId: null, teacherId: '' });

    const handleCreate = async (e) => {
        e.preventDefault();
        try {
            const payload = {
                name: formData.name,
                year: parseInt(formData.year, 10),
                semester: parseInt(formData.semester, 10),
                capacity: parseInt(formData.capacity, 10),
            };
            await api.hod.createClass(payload);
            toast.success('Class created');
            refetch();
            setIsModalOpen(false);
            setFormData({ name: '', year: '', semester: '', capacity: '' });
        } catch (error) {
            console.error('Create class error:', error.response?.data);
            const message = error.response?.data?.message || 'Failed to create class';
            toast.error(message);
        }
    };

    const handleAssignAdvisor = async (classId, teacherId) => {
        if (!teacherId) return;
        try {
            await api.hod.assignClassAdvisor(classId, teacherId);
            toast.success('Class advisor assigned');
            refetch();
            setAssigning({ classId: null, teacherId: '' });
        } catch (error) {
            toast.error(error.response?.data?.message || 'Assignment failed');
        }
    };

    const columns = [
        { key: 'name', label: 'Class Name' },
        { key: 'year', label: 'Year' },
        { key: 'semester', label: 'Semester' },
        { key: 'capacity', label: 'Capacity' },
        { key: 'classAdvisorName', label: 'Advisor' },
    ];

    const actions = (row) => (
        <div className="flex space-x-2">
            <select
                onChange={(e) => handleAssignAdvisor(row.id, e.target.value)}
                value={assigning.classId === row.id ? assigning.teacherId : row.classAdvisorId || ''}
                className="text-sm border rounded px-2 py-1"
            >
                <option value="">Assign Advisor</option>
                {teachers?.map((t) => (
                    <option key={t.id} value={t.id}>
                        {t.fullName}
                    </option>
                ))}
            </select>
        </div>
    );

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Classes</h1>
                <button onClick={() => setIsModalOpen(true)} className="btn-primary">
                    Add Class
                </button>
            </div>
            <DataTable columns={columns} data={classes || []} actions={actions} />
            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Create Class">
                <form onSubmit={handleCreate} className="space-y-4">
                    <InputField
                        label="Name"
                        name="name"
                        value={formData.name}
                        onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                        required
                    />
                    <InputField
                        label="Year"
                        name="year"
                        type="number"
                        value={formData.year}
                        onChange={(e) => setFormData({ ...formData, year: e.target.value })}
                        required
                    />
                    <InputField
                        label="Semester"
                        name="semester"
                        type="number"
                        value={formData.semester}
                        onChange={(e) => setFormData({ ...formData, semester: e.target.value })}
                        required
                    />
                    <InputField
                        label="Capacity"
                        name="capacity"
                        type="number"
                        value={formData.capacity}
                        onChange={(e) => setFormData({ ...formData, capacity: e.target.value })}
                        required
                    />
                    <button type="submit" className="btn-primary w-full">
                        Create
                    </button>
                </form>
            </Modal>
        </div>
    );
};

export default HodClasses;