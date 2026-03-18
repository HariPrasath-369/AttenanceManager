import React, { useState, useEffect } from 'react';
import Sidebar from '../components/Sidebar';
import api from '../services/api';
import { format } from 'date-fns';
import { motion } from 'framer-motion';

const Attendance = () => {
    const [students, setStudents] = useState([]);
    const [date, setDate] = useState(format(new Date(), 'yyyy-MM-dd'));
    const [session, setSession] = useState('MORNING');
    const [attendanceData, setAttendanceData] = useState({});
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchStudents();
    }, []);

    useEffect(() => {
        if (students.length > 0) {
            fetchAttendance();
        }
    }, [date, session, students]);

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

    const fetchAttendance = async () => {
        try {
            const res = await api.get(`/attendance/date/${date}`);
            const sessionData = res.data.filter(a => a.session === session);
            
            const attendanceMap = {};
            sessionData.forEach(record => {
                attendanceMap[record.studentId] = record.status;
            });
            setAttendanceData(attendanceMap);
        } catch (err) {
            console.error("Error fetching attendance", err);
        }
    };

    const markAttendance = async (studentId, status) => {
        // Optimistic update
        setAttendanceData(prev => ({ ...prev, [studentId]: status }));
        try {
            await api.post('/attendance', {
                studentId,
                date,
                session,
                status
            });
        } catch (err) {
            console.error("Error marking attendance", err);
            // Revert on error
            fetchAttendance();
        }
    };

    const markAll = (status) => {
        students.forEach(student => {
            if (attendanceData[student.id] !== status) {
                markAttendance(student.id, status);
            }
        });
    };

    if (loading) return <div className="min-h-screen flex items-center justify-center bg-gray-50">Loading...</div>;

    return (
        <div className="flex bg-gray-50 min-h-screen">
            <Sidebar currentPath="/attendance" />
            <div className="flex-1 ml-64 p-8">
                <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}>
                    <div className="flex justify-between items-end mb-8">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900">Attendance Tracking</h1>
                            <p className="text-gray-500 mt-1">Mark daily session attendance</p>
                        </div>
                        <div className="flex space-x-4 bg-white p-2 rounded-xl border border-gray-100 shadow-sm">
                            <input 
                                type="date" 
                                value={date} 
                                onChange={(e) => setDate(e.target.value)} 
                                className="px-3 py-2 border-r border-gray-100 outline-none text-gray-700 bg-transparent font-medium"
                            />
                            <select 
                                value={session} 
                                onChange={(e) => setSession(e.target.value)}
                                className="px-3 py-2 outline-none text-gray-700 bg-transparent font-medium cursor-pointer"
                            >
                                <option value="MORNING">Morning</option>
                                <option value="NOON">Noon</option>
                                <option value="EVENING">Evening</option>
                            </select>
                        </div>
                    </div>

                    <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                        <div className="flex justify-between items-center p-6 border-b border-gray-100 bg-gray-50/50">
                            <h2 className="text-lg font-semibold text-gray-800">Student Roster</h2>
                            <div className="flex space-x-2">
                                <button onClick={() => markAll('PRESENT')} className="px-4 py-2 bg-green-50 text-green-600 rounded-lg text-sm font-semibold hover:bg-green-100 transition-colors">Mark All Present</button>
                                <button onClick={() => markAll('ABSENT')} className="px-4 py-2 bg-red-50 text-red-600 rounded-lg text-sm font-semibold hover:bg-red-100 transition-colors">Mark All Absent</button>
                            </div>
                        </div>
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="border-b border-gray-100 text-gray-500 text-sm">
                                    <th className="py-4 px-6 font-medium">Roll No</th>
                                    <th className="py-4 px-6 font-medium">Name</th>
                                    <th className="py-4 px-6 font-medium text-right">Status Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {students.map((student, index) => {
                                    const currentStatus = attendanceData[student.id];
                                    return (
                                        <motion.tr 
                                            initial={{ opacity: 0, y: 5 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: index * 0.05 }}
                                            key={student.id} 
                                            className="border-b border-gray-50 hover:bg-gray-50 transition-colors"
                                        >
                                            <td className="py-4 px-6 text-gray-800 font-medium">{student.rollNumber}</td>
                                            <td className="py-4 px-6 text-gray-600">{student.name}</td>
                                            <td className="py-4 px-6">
                                                <div className="flex justify-end space-x-2">
                                                    <button 
                                                        onClick={() => markAttendance(student.id, 'PRESENT')}
                                                        className={`px-4 py-1.5 rounded-full text-xs font-bold border transition-all ${currentStatus === 'PRESENT' ? 'bg-green-500 text-white border-green-500 shadow-md transform scale-105' : 'bg-transparent text-green-600 border-green-200 hover:bg-green-50'}`}
                                                    >
                                                        Present
                                                    </button>
                                                    <button 
                                                        onClick={() => markAttendance(student.id, 'ABSENT')}
                                                        className={`px-4 py-1.5 rounded-full text-xs font-bold border transition-all ${currentStatus === 'ABSENT' ? 'bg-red-500 text-white border-red-500 shadow-md transform scale-105' : 'bg-transparent text-red-600 border-red-200 hover:bg-red-50'}`}
                                                    >
                                                        Absent
                                                    </button>
                                                    <button 
                                                        onClick={() => markAttendance(student.id, 'OD')}
                                                        className={`px-4 py-1.5 rounded-full text-xs font-bold border transition-all ${currentStatus === 'OD' ? 'bg-indigo-500 text-white border-indigo-500 shadow-md transform scale-105' : 'bg-transparent text-indigo-600 border-indigo-200 hover:bg-indigo-50'}`}
                                                    >
                                                        OD (On Duty)
                                                    </button>
                                                </div>
                                            </td>
                                        </motion.tr>
                                    );
                                })}
                            </tbody>
                        </table>
                    </div>
                </motion.div>
            </div>
        </div>
    );
};

export default Attendance;
