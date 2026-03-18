import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import api from '../services/api';
import { motion } from 'framer-motion';

const Dashboard = () => {
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStudents = async () => {
            try {
                const res = await api.get('/students');
                setStudents(res.data);
            } catch (err) {
                console.error("Failed to load dashboard data");
            } finally {
                setLoading(false);
            }
        };
        fetchStudents();
    }, []);

    if (loading) return <div className="min-h-screen flex items-center justify-center bg-gray-50">Loading...</div>;

    return (
        <div className="flex bg-gray-50 min-h-screen">
            <Sidebar currentPath="/dashboard" />
            <div className="flex-1 ml-64 p-8">
                <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}>
                    <h1 className="text-3xl font-bold text-gray-900 mb-8">Dashboard Overview</h1>
                    
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex items-center">
                            <div className="bg-blue-100 p-4 rounded-xl text-blue-600 mr-4">
                                <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg>
                            </div>
                            <div>
                                <p className="text-sm text-gray-500 font-medium">Total Students</p>
                                <p className="text-3xl font-bold text-gray-800">{students.length}</p>
                            </div>
                        </div>
                        {/* More metric cards can go here */}
                    </div>

                    <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
                        <h2 className="text-xl font-semibold text-gray-800 mb-4">Class Directory</h2>
                        <div className="overflow-x-auto">
                            <table className="w-full text-left border-collapse">
                                <thead>
                                    <tr className="border-b border-gray-100 text-gray-500 text-sm">
                                        <th className="pb-3 font-medium px-4">Roll No</th>
                                        <th className="pb-3 font-medium px-4">Name</th>
                                        <th className="pb-3 font-medium px-4">Mobile</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {students.map(student => (
                                        <tr key={student.id} className="border-b border-gray-50 hover:bg-gray-50 transition-colors">
                                            <td className="py-4 px-4 text-gray-800 font-medium">{student.rollNumber}</td>
                                            <td className="py-4 px-4 text-gray-600">{student.name}</td>
                                            <td className="py-4 px-4 text-gray-600">{student.mobileNumber}</td>
                                        </tr>
                                    ))}
                                    {students.length === 0 && (
                                        <tr><td colSpan="3" className="py-8 text-center text-gray-500">No students found. Add some!</td></tr>
                                    )}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </motion.div>
            </div>
        </div>
    );
};

export default Dashboard;
