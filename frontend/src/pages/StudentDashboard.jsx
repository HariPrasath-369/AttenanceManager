import React, { useEffect, useState } from 'react';
import { publicApi } from '../services/api';
import { motion } from 'framer-motion';
import { format } from 'date-fns';

const StudentDashboard = ({ student, onBack }) => {
    const [attendance, setAttendance] = useState([]);
    const [messages, setMessages] = useState([]);
    const [documents, setDocuments] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchDashboardData = async () => {
            try {
                const [attRes, msgRes, docRes] = await Promise.all([
                    publicApi.getStudentAttendance(student.id),
                    publicApi.getMessages(),
                    publicApi.getDocuments()
                ]);
                setAttendance(attRes.data);
                setMessages(msgRes.data);
                setDocuments(docRes.data);
            } catch (err) {
                console.error("Error fetching dashboard data", err);
            } finally {
                setLoading(false);
            }
        };
        fetchDashboardData();
    }, [student.id]);

    const calculateAttendanceStats = () => {
        if (!attendance.length) return { present: 0, total: 0, percentage: 0 };
        const total = attendance.length;
        const present = attendance.filter(a => a.status === 'PRESENT' || a.status === 'OD').length;
        return { present, total, percentage: Math.round((present / total) * 100) };
    };

    const stats = calculateAttendanceStats();

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-gray-50">
                <div className="animate-spin rounded-full h-12 w-12 border-4 border-blue-500 border-t-transparent"></div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 py-8 px-4 sm:px-6 lg:px-8">
            <div className="max-w-7xl mx-auto space-y-8">
                {/* Header */}
                <motion.div 
                    initial={{ opacity: 0, y: -20 }} animate={{ opacity: 1, y: 0 }}
                    className="flex justify-between items-center bg-white p-6 rounded-2xl shadow-sm border border-gray-100"
                >
                    <div className="flex items-center space-x-4">
                        <button onClick={onBack} className="p-2 bg-gray-100 hover:bg-gray-200 rounded-full text-gray-600 transition-colors">
                            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 19l-7-7 7-7"></path></svg>
                        </button>
                        <div>
                            <h1 className="text-2xl font-bold text-gray-900">{student.name}</h1>
                            <p className="text-gray-500">Roll No: {student.rollNumber}</p>
                        </div>
                    </div>
                </motion.div>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* Left Column: Stats & Recent Messages */}
                    <div className="lg:col-span-1 space-y-8">
                        {/* Attendance Card */}
                        <motion.div initial={{ opacity: 0, x: -20 }} animate={{ opacity: 1, x: 0 }} transition={{ delay: 0.1 }} className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
                            <h2 className="text-lg font-semibold text-gray-800 mb-4">Overall Attendance</h2>
                            <div className="flex flex-col items-center justify-center">
                                <div className="relative w-32 h-32 flex items-center justify-center">
                                    <svg className="w-full h-full transform -rotate-90">
                                        <circle cx="64" cy="64" r="56" fill="none" className="stroke-gray-100 stroke-[12]"></circle>
                                        <circle cx="64" cy="64" r="56" fill="none" strokeDasharray={`${stats.percentage * 3.51} 351`} className={`stroke-[12] stroke-linecap-round ${stats.percentage >= 75 ? 'stroke-green-500' : 'stroke-red-500'}`}></circle>
                                    </svg>
                                    <div className="absolute flex flex-col items-center">
                                        <span className="text-2xl font-bold text-gray-800">{stats.percentage}%</span>
                                    </div>
                                </div>
                                <div className="mt-4 text-center">
                                    <p className="text-sm text-gray-500">{stats.present} / {stats.total} Sessions Attended</p>
                                </div>
                            </div>
                        </motion.div>

                        {/* Recent Messages */}
                        <motion.div initial={{ opacity: 0, x: -20 }} animate={{ opacity: 1, x: 0 }} transition={{ delay: 0.2 }} className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
                            <h2 className="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                                <span className="bg-blue-100 text-blue-600 p-2 rounded-lg mr-3">
                                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path></svg>
                                </span>
                                Class Announcements
                            </h2>
                            <div className="space-y-4 max-h-[400px] overflow-y-auto pr-2">
                                {messages.length === 0 ? (
                                    <p className="text-gray-500 text-sm italic">No recent announcements.</p>
                                ) : (
                                    messages.slice().reverse().map(msg => (
                                        <div key={msg.id} className="p-4 bg-gray-50 rounded-xl border border-gray-100">
                                            <p className="text-xs text-blue-600 font-medium mb-1">{format(new Date(msg.createdAt), 'MMM dd, yyyy - hh:mm a')}</p>
                                            <p className="text-gray-700 text-sm whitespace-pre-wrap">{msg.content}</p>
                                        </div>
                                    ))
                                )}
                            </div>
                        </motion.div>
                    </div>

                    {/* Right Column: Study Materials */}
                    <div className="lg:col-span-2 space-y-8">
                        {/* Documents */}
                        <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.3 }} className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
                            <h2 className="text-lg font-semibold text-gray-800 mb-6 flex items-center">
                                <span className="bg-indigo-100 text-indigo-600 p-2 rounded-lg mr-3">
                                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path></svg>
                                </span>
                                Study Materials & Resources
                            </h2>
                            {documents.length === 0 ? (
                                <div className="text-center py-10 bg-gray-50 rounded-xl border border-dashed border-gray-200">
                                    <svg className="mx-auto h-12 w-12 text-gray-300 mb-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path></svg>
                                    <p className="text-gray-500 text-sm">No materials uploaded yet.</p>
                                </div>
                            ) : (
                                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    {documents.map(doc => (
                                        <a 
                                            key={doc.id} 
                                            href={doc.fileUrl} 
                                            target="_blank" 
                                            rel="noopener noreferrer"
                                            className="group flex flex-col p-4 bg-white border border-gray-100 rounded-xl shadow-sm hover:shadow-md hover:border-indigo-100 transition-all text-left"
                                        >
                                            <div className="flex items-start justify-between">
                                                <div className="flex-1">
                                                    <h3 className="text-sm font-semibold text-gray-800 group-hover:text-indigo-600 transition-colors line-clamp-2">{doc.title}</h3>
                                                    <p className="text-xs text-gray-500 mt-2">Added on {format(new Date(doc.uploadedAt), 'MMM dd, yyyy')}</p>
                                                </div>
                                                <div className="bg-indigo-50 p-2 rounded-lg group-hover:bg-indigo-100 transition-colors ml-3">
                                                    <svg className="w-4 h-4 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path></svg>
                                                </div>
                                            </div>
                                        </a>
                                    ))}
                                </div>
                            )}
                        </motion.div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default StudentDashboard;
