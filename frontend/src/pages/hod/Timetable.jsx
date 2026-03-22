import React, { useState } from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';
import Modal from '../../components/common/Modal';
import InputField from '../../components/forms/InputField';
import SelectField from '../../components/forms/SelectField';
import toast from 'react-hot-toast';

const days = [
    { value: 1, label: 'Monday' },
    { value: 2, label: 'Tuesday' },
    { value: 3, label: 'Wednesday' },
    { value: 4, label: 'Thursday' },
    { value: 5, label: 'Friday' },
    { value: 6, label: 'Saturday' },
    { value: 7, label: 'Sunday' },
];

const HodTimetable = () => {
    const [selectedClass, setSelectedClass] = useState(null);
    const { data: classes } = useFetch(api.hod.getClasses);
    const { data: timetable, refetch } = useFetch(
        () => (selectedClass ? api.hod.getTimetableForClass(selectedClass.id) : Promise.resolve({ data: [] })),
        [selectedClass]
    );
    const { data: subjects } = useFetch(api.hod.getSubjects);
    const { data: teachers } = useFetch(api.hod.getTeachers);

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({ dayOfWeek: 1, period: 1, subjectId: '', teacherId: '', venue: '' });

    const handleCreate = async (e) => {
        e.preventDefault();
        if (!selectedClass) return;
        try {
            await api.hod.createTimetableEntry({ ...formData, classId: selectedClass.id });
            toast.success('Timetable entry added');
            refetch();
            setIsModalOpen(false);
            setFormData({ dayOfWeek: 1, period: 1, subjectId: '', teacherId: '', venue: '' });
        } catch (error) {
            toast.error(error.response?.data?.message || 'Failed to add entry');
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure?')) {
            await api.hod.deleteTimetableEntry(id);
            toast.success('Entry deleted');
            refetch();
        }
    };

    const columns = [
        {
            key: 'dayOfWeek',
            label: 'Day',
            render: (row) => days.find((d) => d.value === row.dayOfWeek)?.label,
        },
        { key: 'period', label: 'Period' },
        { key: 'subjectName', label: 'Subject' },
        { key: 'teacherName', label: 'Teacher' },
        { key: 'venue', label: 'Venue' },
    ];

    const actions = (row) => (
        <button onClick={() => handleDelete(row.id)} className="text-red-600 hover:text-red-800">
            Delete
        </button>
    );

    return (
        <div>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Timetable</h1>
                <select
                    onChange={(e) => setSelectedClass(classes?.find((c) => c.id === parseInt(e.target.value)))}
                    className="input-field w-64"
                >
                    <option value="">Select Class</option>
                    {classes?.map((c) => (
                        <option key={c.id} value={c.id}>
                            {c.name}
                        </option>
                    ))}
                </select>
            </div>

            {selectedClass && (
                <>
                    <div className="mb-4 flex justify-end">
                        <button onClick={() => setIsModalOpen(true)} className="btn-primary">
                            Add Entry
                        </button>
                    </div>
                    <DataTable columns={columns} data={timetable || []} actions={actions} />
                </>
            )}

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Add Timetable Entry">
                <form onSubmit={handleCreate} className="space-y-4">
                    <SelectField
                        label="Day"
                        name="dayOfWeek"
                        value={formData.dayOfWeek}
                        onChange={(e) => setFormData({ ...formData, dayOfWeek: parseInt(e.target.value) })}
                        options={days}
                        required
                    />
                    <InputField
                        label="Period"
                        name="period"
                        type="number"
                        value={formData.period}
                        onChange={(e) => setFormData({ ...formData, period: e.target.value })}
                        required
                    />
                    <SelectField
                        label="Subject"
                        name="subjectId"
                        value={formData.subjectId}
                        onChange={(e) => setFormData({ ...formData, subjectId: e.target.value })}
                        options={subjects?.map((s) => ({ value: s.id, label: s.name })) || []}
                        required
                    />
                    <SelectField
                        label="Teacher"
                        name="teacherId"
                        value={formData.teacherId}
                        onChange={(e) => setFormData({ ...formData, teacherId: e.target.value })}
                        options={teachers?.map((t) => ({ value: t.id, label: t.fullName })) || []}
                        required
                    />
                    <InputField
                        label="Venue"
                        name="venue"
                        value={formData.venue}
                        onChange={(e) => setFormData({ ...formData, venue: e.target.value })}
                        required
                    />
                    <button type="submit" className="btn-primary w-full">
                        Add
                    </button>
                </form>
            </Modal>
        </div>
    );
};

export default HodTimetable;