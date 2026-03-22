import React, { useState } from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import DataTable from '../../components/tables/DataTable';
import Modal from '../../components/common/Modal';
import InputField from '../../components/forms/InputField';
import SelectField from '../../components/forms/SelectField';
import toast from 'react-hot-toast';

const TeacherMaterials = () => {
    const { data: materials, loading, refetch } = useFetch(api.material.getMyMaterials);
    const { data: classes } = useFetch(api.teacher.getClasses);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({ classId: '', subjectId: '', title: '', description: '', file: null });

    const handleFileChange = (e) => {
        setFormData({ ...formData, file: e.target.files[0] });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!formData.file) {
            toast.error('Please select a file');
            return;
        }
        const form = new FormData();
        form.append('classSubjectId', formData.classId); // assuming classSubjectId is needed
        form.append('title', formData.title);
        form.append('description', formData.description);
        form.append('file', formData.file);
        try {
            await api.material.upload(form);
            toast.success('Material uploaded');
            refetch();
            setIsModalOpen(false);
            setFormData({ classId: '', subjectId: '', title: '', description: '', file: null });
        } catch (error) {
            toast.error(error.response?.data?.message || 'Upload failed');
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure?')) {
            await api.material.delete(id);
            toast.success('Material deleted');
            refetch();
        }
    };

    const columns = [
        { key: 'title', label: 'Title' },
        { key: 'description', label: 'Description' },
        { key: 'className', label: 'Class' },
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

    const actions = (row) => (
        <button onClick={() => handleDelete(row.id)} className="text-red-600 hover:text-red-800">
            Delete
        </button>
    );

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Materials</h1>
                <button onClick={() => setIsModalOpen(true)} className="btn-primary">
                    Upload Material
                </button>
            </div>
            <DataTable columns={columns} data={materials || []} actions={actions} />
            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Upload Material">
                <form onSubmit={handleSubmit} className="space-y-4">
                    <SelectField
                        label="Class"
                        name="classId"
                        value={formData.classId}
                        onChange={(e) => setFormData({ ...formData, classId: e.target.value })}
                        options={classes?.map((c) => ({ value: c.id, label: c.name })) || []}
                        required
                    />
                    <SelectField
                        label="Subject"
                        name="subjectId"
                        value={formData.subjectId}
                        onChange={(e) => setFormData({ ...formData, subjectId: e.target.value })}
                        options={[]} // need to fetch subjects for the selected class
                        required
                    />
                    <InputField
                        label="Title"
                        name="title"
                        value={formData.title}
                        onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                        required
                    />
                    <InputField
                        label="Description"
                        name="description"
                        value={formData.description}
                        onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                    />
                    <InputField
                        label="File"
                        name="file"
                        type="file"
                        onChange={handleFileChange}
                        required
                    />
                    <button type="submit" className="btn-primary w-full">
                        Upload
                    </button>
                </form>
            </Modal>
        </div>
    );
};

export default TeacherMaterials;