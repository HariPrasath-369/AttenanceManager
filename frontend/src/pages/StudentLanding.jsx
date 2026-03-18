import React, { useState } from 'react';
import { publicApi } from '../services/api';
import StudentDashboard from './StudentDashboard';
import { motion } from 'framer-motion';

const StudentLanding = () => {
    const [rollNumber, setRollNumber] = useState('');
    const [studentData, setStudentData] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!rollNumber.trim()) return;

        setLoading(true);
        setError('');
        try {
            const response = await publicApi.getStudentByRollNumber(rollNumber);
            setStudentData(response.data);
        } catch (err) {
            setError(err.response?.data?.error || 'Student not found. Please check your roll number.');
            setStudentData(null);
        } finally {
            setLoading(false);
        }
    };

    if (studentData) {
        return <StudentDashboard student={studentData} onBack={() => setStudentData(null)} />;
    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100">
            <motion.div 
                initial={{ opacity: 0, scale: 0.95 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ duration: 0.4 }}
                className="bg-white p-10 rounded-2xl shadow-xl w-full max-w-md text-center"
            >
                <h1 className="text-3xl font-extrabold text-gray-800 mb-2">Smart Attendance</h1>
                <p className="text-gray-500 mb-8">Enter your roll number to view your dashboard</p>

                <form onSubmit={handleSearch} className="space-y-6">
                    <div>
                        <input
                            type="text"
                            value={rollNumber}
                            onChange={(e) => setRollNumber(e.target.value)}
                            placeholder="e.g. 101"
                            className="w-full text-center px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none text-lg transition-all"
                            required
                        />
                    </div>
                    {error && <p className="text-red-500 text-sm">{error}</p>}
                    <button
                        type="submit"
                        disabled={loading}
                        className={`w-full py-3 rounded-lg text-white font-semibold text-lg transition-all ${
                            loading ? 'bg-blue-300 cursor-not-allowed' : 'bg-blue-600 hover:bg-blue-700 hover:shadow-lg transform hover:-translate-y-0.5'
                        }`}
                    >
                        {loading ? 'Searching...' : 'View Dashboard'}
                    </button>
                </form>

                <div className="mt-8 text-sm text-gray-400">
                    <p>Teacher login? <a href="/login" className="text-blue-500 hover:underline">Click here</a></p>
                </div>
            </motion.div>
        </div>
    );
};

export default StudentLanding;
