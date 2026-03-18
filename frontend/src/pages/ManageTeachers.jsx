import React, { useState, useEffect } from 'react';
import Sidebar from '../components/Sidebar';
import api from '../services/api';
import { motion, AnimatePresence } from 'framer-motion';

const ManageTeachers = () => {
    const [teachers, setTeachers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({ name: '', email: '', password: '', role: 'TEACHER' });

    useEffect(() => {
        fetchTeachers();
    }, []);

    const fetchTeachers = async () => {
        try {
            const res = await api.get('/teachers');
            setTeachers(res.data);
            setLoading(false);
        } catch (err) {
            console.error("Error fetching teachers", err);
            setLoading(false);
        }
    };

    const handleOpenModal = () => {
        setFormData({ name: '', email: '', password: '', role: 'TEACHER' });
        setIsModalOpen(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post('/teachers', formData);
            setIsModalOpen(false);
            fetchTeachers();
        } catch (err) {
            alert(err.response?.data?.error || "Error adding teacher");
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Remove this teacher access?')) {
            try {
                await api.delete(`/teachers/${id}`);
                fetchTeachers();
            } catch (err) {
                console.error("Error removing teacher", err);
            }
        }
    };

    if (loading) return <div className="min-h-screen flex items-center justify-center bg-gray-50">Loading...</div>;

    return (
        <div className="flex bg-gray-50 min-h-screen">
            <Sidebar currentPath="/manage-teachers" />
            <div className="flex-1 ml-64 p-8">
                <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}>
                    <div className="flex justify-between items-center mb-8">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900">Manage Teachers</h1>
                            <p className="text-gray-500 mt-1">Control access for subordinate teachers</p>
                        </div>
                        <button 
                            onClick={handleOpenModal}
                            className="bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-2.5 rounded-xl font-semibold shadow-md transition-all flex items-center"
                        >
                            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                            Invite Teacher
                        </button>
                    </div>

                    <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="border-b border-gray-100 text-gray-500 text-sm bg-gray-50/50">
                                    <th className="py-4 px-6 font-medium">Name</th>
                                    <th className="py-4 px-6 font-medium">Email</th>
                                    <th className="py-4 px-6 font-medium">Role</th>
                                    <th className="py-4 px-6 font-medium text-right">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {teachers.map((teacher) => (
                                    <tr key={teacher.id} className="border-b border-gray-50 hover:bg-gray-50 transition-colors">
                                        <td className="py-4 px-6 text-gray-800 font-semibold">{teacher.name}</td>
                                        <td className="py-4 px-6 text-gray-600">{teacher.email}</td>
                                        <td className="py-4 px-6">
                                            <span className={`px-2.5 py-1 rounded-full text-xs font-bold ${teacher.role === 'MAIN' ? 'bg-purple-100 text-purple-600' : 'bg-gray-100 text-gray-600'}`}>
                                                {teacher.role}
                                            </span>
                                        </td>
                                        <td className="py-4 px-6 text-right">
                                            {teacher.role !== 'MAIN' && (
                                                <button onClick={() => handleDelete(teacher.id)} className="text-red-400 hover:text-red-600 p-2 hover:bg-red-50 rounded-lg transition-colors">
                                                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path></svg>
                                                </button>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </motion.div>
            </div>

            <AnimatePresence>
                {isModalOpen && (
                    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
                        <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} onClick={() => setIsModalOpen(false)} className="absolute inset-0 bg-black/40 backdrop-blur-sm" />
                        <motion.div 
                            initial={{ scale: 0.9, opacity: 0 }} animate={{ scale: 1, opacity: 1 }} exit={{ scale: 0.9, opacity: 0 }}
                            className="bg-white rounded-2xl shadow-2xl w-full max-w-md p-8 relative z-10"
                        >
                            <h2 className="text-xl font-bold text-gray-900 mb-6">Invite New Teacher</h2>
                            <form onSubmit={handleSubmit} className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Full Name</label>
                                    <input
                                        type="text" required value={formData.name}
                                        onChange={(e) => setFormData({...formData, name: e.target.value})}
                                        className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                                        placeholder="Dr. Jane Smith"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Email Address</label>
                                    <input
                                        type="email" required value={formData.email}
                                        onChange={(e) => setFormData({...formData, email: e.target.value})}
                                        className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                                        placeholder="jane.smith@school.com"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Initial Password</label>
                                    <input
                                        type="password" required value={formData.password}
                                        onChange={(e) => setFormData({...formData, password: e.target.value})}
                                        className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                                        placeholder="••••••••"
                                    />
                                </div>
                                <div className="flex space-x-3 pt-4">
                                    <button type="button" onClick={() => setIsModalOpen(false)} className="flex-1 px-4 py-2 border border-gray-200 rounded-lg font-semibold text-gray-600 hover:bg-gray-50 transition-colors">Cancel</button>
                                    <button type="submit" className="flex-1 px-4 py-2 bg-indigo-600 text-white rounded-lg font-semibold hover:bg-indigo-700 shadow-md transition-colors">Create Account</button>
                                </div>
                            </form>
                        </motion.div>
                    </div>
                )}
            </AnimatePresence>
        </div>
    );
};

export default ManageTeachers;
