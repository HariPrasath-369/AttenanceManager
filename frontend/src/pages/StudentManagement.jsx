import React, { useState, useEffect } from 'react';
import Sidebar from '../components/Sidebar';
import api from '../services/api';
import { motion, AnimatePresence } from 'framer-motion';

const StudentManagement = () => {
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingStudent, setEditingStudent] = useState(null);
    const [formData, setFormData] = useState({ name: '', rollNumber: '', mobileNumber: '' });

    useEffect(() => {
        fetchStudents();
    }, []);

    const fetchStudents = async () => {
        try {
            const res = await api.get('/students');
            setStudents(res.data);
            setLoading(false);
        } catch (err) {
            console.error("Error fetching students", err);
            setLoading(false);
        }
    };

    const handleOpenModal = (student = null) => {
        if (student) {
            setEditingStudent(student);
            setFormData({ name: student.name, rollNumber: student.rollNumber, mobileNumber: student.mobileNumber });
        } else {
            setEditingStudent(null);
            setFormData({ name: '', rollNumber: '', mobileNumber: '' });
        }
        setIsModalOpen(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingStudent) {
                await api.put(`/students/${editingStudent.id}`, formData);
            } else {
                await api.post('/students', formData);
            }
            setIsModalOpen(false);
            fetchStudents();
        } catch (err) {
            alert(err.response?.data?.error || "Error saving student");
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Delete this student and all their attendance records?')) {
            try {
                await api.delete(`/students/${id}`);
                fetchStudents();
            } catch (err) {
                console.error("Error deleting student", err);
            }
        }
    };

    if (loading) return <div className="min-h-screen flex items-center justify-center bg-gray-50">Loading...</div>;

    return (
        <div className="flex bg-gray-50 min-h-screen">
            <Sidebar currentPath="/students" />
            <div className="flex-1 ml-64 p-8">
                <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}>
                    <div className="flex justify-between items-center mb-8">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900">Student Management</h1>
                            <p className="text-gray-500 mt-1">Add or update student information</p>
                        </div>
                        <button 
                            onClick={() => handleOpenModal()}
                            className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2.5 rounded-xl font-semibold shadow-md transition-all flex items-center"
                        >
                            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path></svg>
                            Add Student
                        </button>
                    </div>

                    <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="border-b border-gray-100 text-gray-500 text-sm bg-gray-50/50">
                                    <th className="py-4 px-6 font-medium">Roll No</th>
                                    <th className="py-4 px-6 font-medium">Full Name</th>
                                    <th className="py-4 px-6 font-medium">Mobile Number</th>
                                    <th className="py-4 px-6 font-medium text-right">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {students.sort((a,b) => a.rollNumber.localeCompare(b.rollNumber)).map((student) => (
                                    <tr key={student.id} className="border-b border-gray-50 hover:bg-gray-50 transition-colors">
                                        <td className="py-4 px-6 text-gray-800 font-bold">{student.rollNumber}</td>
                                        <td className="py-4 px-6 text-gray-600 font-medium">{student.name}</td>
                                        <td className="py-4 px-6 text-gray-500">{student.mobileNumber}</td>
                                        <td className="py-4 px-6 text-right space-x-2">
                                            <button onClick={() => handleOpenModal(student)} className="text-blue-500 hover:text-blue-700 p-2 hover:bg-blue-50 rounded-lg transition-colors">
                                                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"></path></svg>
                                            </button>
                                            <button onClick={() => handleDelete(student.id)} className="text-red-400 hover:text-red-600 p-2 hover:bg-red-50 rounded-lg transition-colors">
                                                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path></svg>
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                                {students.length === 0 && (
                                    <tr><td colSpan="4" className="py-10 text-center text-gray-400">No students registered.</td></tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </motion.div>
            </div>

            {/* Modal */}
            <AnimatePresence>
                {isModalOpen && (
                    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
                        <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} onClick={() => setIsModalOpen(false)} className="absolute inset-0 bg-black/40 backdrop-blur-sm" />
                        <motion.div 
                            initial={{ scale: 0.9, opacity: 0 }} animate={{ scale: 1, opacity: 1 }} exit={{ scale: 0.9, opacity: 0 }}
                            className="bg-white rounded-2xl shadow-2xl w-full max-w-md p-8 relative z-10"
                        >
                            <h2 className="text-xl font-bold text-gray-900 mb-6">{editingStudent ? 'Edit Student' : 'Add New Student'}</h2>
                            <form onSubmit={handleSubmit} className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Roll Number</label>
                                    <input
                                        type="text"
                                        required
                                        value={formData.rollNumber}
                                        onChange={(e) => setFormData({...formData, rollNumber: e.target.value})}
                                        className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                        placeholder="e.g. 101"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Full Name</label>
                                    <input
                                        type="text"
                                        required
                                        value={formData.name}
                                        onChange={(e) => setFormData({...formData, name: e.target.value})}
                                        className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                        placeholder="John Doe"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Mobile Number</label>
                                    <input
                                        type="text"
                                        required
                                        value={formData.mobileNumber}
                                        onChange={(e) => setFormData({...formData, mobileNumber: e.target.value})}
                                        className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                        placeholder="1234567890"
                                    />
                                </div>
                                <div className="flex space-x-3 pt-4">
                                    <button type="button" onClick={() => setIsModalOpen(false)} className="flex-1 px-4 py-2 border border-gray-200 rounded-lg font-semibold text-gray-600 hover:bg-gray-50 transition-colors">Cancel</button>
                                    <button type="submit" className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg font-semibold hover:bg-blue-700 shadow-md transition-colors">Save Student</button>
                                </div>
                            </form>
                        </motion.div>
                    </div>
                )}
            </AnimatePresence>
        </div>
    );
};

export default StudentManagement;
