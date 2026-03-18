import React, { useState, useEffect } from 'react';
import Sidebar from '../components/Sidebar';
import api from '../services/api';
import { motion } from 'framer-motion';

const Settings = () => {
    const [settings, setSettings] = useState({ academicYear: '', semester: '', className: '' });
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        fetchSettings();
    }, []);

    const fetchSettings = async () => {
        try {
            const res = await api.get('/settings');
            if (res.data) setSettings(res.data);
            setLoading(false);
        } catch (err) {
            console.error("Error fetching settings");
            setLoading(false);
        }
    };

    const handleSave = async (e) => {
        e.preventDefault();
        setSaving(true);
        try {
            await api.post('/settings', settings);
            alert("Settings updated successfully!");
        } catch (err) {
            alert("Failed to save settings");
        } finally {
            setSaving(false);
        }
    };

    if (loading) return <div className="min-h-screen flex items-center justify-center bg-gray-50">Loading...</div>;

    return (
        <div className="flex bg-gray-50 min-h-screen">
            <Sidebar currentPath="/settings" />
            <div className="flex-1 ml-64 p-8">
                <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className="max-w-2xl">
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">General Settings</h1>
                    <p className="text-gray-500 mb-8">Configure global classroom parameters</p>

                    <div className="bg-white p-8 rounded-2xl shadow-sm border border-gray-100">
                        <form onSubmit={handleSave} className="space-y-6">
                            <div>
                                <label className="block text-sm font-semibold text-gray-700 mb-2">Class Name</label>
                                <input
                                    type="text" value={settings.className}
                                    onChange={(e) => setSettings({...settings, className: e.target.value})}
                                    className="w-full px-4 py-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all"
                                    placeholder="e.g. Computer Science - Year 3"
                                />
                            </div>
                            <div className="grid grid-cols-2 gap-6">
                                <div>
                                    <label className="block text-sm font-semibold text-gray-700 mb-2">Academic Year</label>
                                    <input
                                        type="text" value={settings.academicYear}
                                        onChange={(e) => setSettings({...settings, academicYear: e.target.value})}
                                        className="w-full px-4 py-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all"
                                        placeholder="2023-2024"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold text-gray-700 mb-2">Semester</label>
                                    <input
                                        type="text" value={settings.semester}
                                        onChange={(e) => setSettings({...settings, semester: e.target.value})}
                                        className="w-full px-4 py-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all"
                                        placeholder="Spring / Fall"
                                    />
                                </div>
                            </div>

                            <div className="pt-4">
                                <button
                                    type="submit"
                                    disabled={saving}
                                    className={`w-full py-3 rounded-xl text-white font-bold transition-all shadow-lg ${
                                        saving ? 'bg-blue-400 cursor-not-allowed' : 'bg-blue-600 hover:bg-blue-700'
                                    }`}
                                >
                                    {saving ? 'Saving...' : 'Save Configuration'}
                                </button>
                            </div>
                        </form>
                    </div>
                </motion.div>
            </div>
        </div>
    );
};

export default Settings;
