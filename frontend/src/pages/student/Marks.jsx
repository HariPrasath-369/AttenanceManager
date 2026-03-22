import React from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import GradeBadge from '../../components/oem/GradeBadge';
import { Printer, Download } from 'lucide-react';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
// import jsPDF from "jspdf";
// import autoTable from "jspdf-autotable";

const StudentMarks = () => {
    const { data: marks, loading, error } = useFetch(api.student.getMarks);

    const handlePrint = () => {
        window.print();
    };

    const handleDownloadPDF = () => {
        const doc = new jsPDF();
        doc.setFontSize(18);
        doc.text('Semester Result', 14, 22);
        doc.setFontSize(12);
        doc.text(`Student: ${marks?.studentName || 'N/A'}`, 14, 32);
        doc.text(`Roll Number: ${marks?.rollNumber || 'N/A'}`, 14, 38);
        doc.text(`Class: ${marks?.className || 'N/A'}`, 14, 44);
        doc.text(`Semester: ${marks?.semester || 'N/A'}`, 14, 50);

        const tableData = marks?.subjectMarks?.map((sub) => [
            sub.subject,
            sub.assessment,
            sub.practical,
            sub.semester,
            sub.total,
            sub.grade,
        ]) || [];

        autoTable(doc, {
            startY: 60,
            head: [['Subject', 'Assessment', 'Practical', 'Semester', 'Total', 'Grade']],
            body: tableData,
            theme: 'striped',
            headStyles: { fillColor: [59, 130, 246] },
        });

        const finalY = doc.lastAutoTable.finalY + 10;
        doc.text(`GPA: ${marks?.gpa || 'N/A'}`, 14, finalY);
        doc.text(`Total Marks Obtained: ${marks?.totalObtained || 0} / ${marks?.totalMax || 0}`, 14, finalY + 6);
        doc.text(`Percentage: ${marks?.percentage?.toFixed(2) || 0}%`, 14, finalY + 12);

        doc.save('semester_result.pdf');
    };

    if (loading) return <div className="text-center py-10">Loading semester result...</div>;
    if (error) return <div className="text-center py-10 text-red-600">Failed to load marks.</div>;
    if (!marks || !marks.subjectMarks?.length) return <div className="text-center py-10">No marks available for this semester.</div>;

    return (
        <div className="space-y-6">
            <div className="flex justify-between items-center">
                <h1 className="text-2xl font-bold">Semester Result</h1>
                <div className="flex gap-2 print:hidden">
                    <button
                        onClick={handlePrint}
                        className="btn-secondary flex items-center gap-2"
                    >
                        <Printer className="w-4 h-4" /> Print
                    </button>
                    <button
                        onClick={handleDownloadPDF}
                        className="btn-primary flex items-center gap-2"
                    >
                        <Download className="w-4 h-4" /> Download PDF
                    </button>
                </div>
            </div>

            <div className="bg-white rounded-lg shadow overflow-hidden print:shadow-none">
                {/* Header Info */}
                <div className="p-6 border-b border-gray-200 bg-gray-50 print:bg-white">
                    <h2 className="text-xl font-semibold">Academic Performance</h2>
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-4 text-sm">
                        <div>
                            <span className="text-gray-500">Student Name:</span>
                            <span className="ml-2 font-medium">{marks.studentName}</span>
                        </div>
                        <div>
                            <span className="text-gray-500">Roll Number:</span>
                            <span className="ml-2 font-medium">{marks.rollNumber}</span>
                        </div>
                        <div>
                            <span className="text-gray-500">Class:</span>
                            <span className="ml-2 font-medium">{marks.className}</span>
                        </div>
                        <div>
                            <span className="text-gray-500">Semester:</span>
                            <span className="ml-2 font-medium">{marks.semester}</span>
                        </div>
                    </div>
                </div>

                {/* Marks Table */}
                <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                            <tr>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Subject</th>
                                <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">Assessment (20)</th>
                                <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">Practical (20)</th>
                                <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">Semester (60)</th>
                                <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">Total (100)</th>
                                <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">Grade</th>
                            </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                            {marks.subjectMarks.map((subject, idx) => (
                                <tr key={idx} className="hover:bg-gray-50">
                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{subject.subject}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-center text-sm text-gray-500">{subject.assessment}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-center text-sm text-gray-500">{subject.practical}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-center text-sm text-gray-500">{subject.semester}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-center text-sm font-semibold text-gray-900">{subject.total}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-center">
                                        <GradeBadge total={subject.total} maxMarks={100} />
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                {/* Summary Section */}
                <div className="p-6 bg-gray-50 print:bg-white border-t border-gray-200">
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                        <div>
                            <p className="text-gray-500 text-sm">GPA</p>
                            <p className="text-2xl font-bold text-primary-600">{marks.gpa || 'N/A'}</p>
                        </div>
                        <div>
                            <p className="text-gray-500 text-sm">Total Marks</p>
                            <p className="text-2xl font-bold">{marks.totalObtained} / {marks.totalMax}</p>
                        </div>
                        <div>
                            <p className="text-gray-500 text-sm">Percentage</p>
                            <p className="text-2xl font-bold">{marks.percentage?.toFixed(2)}%</p>
                        </div>
                    </div>
                    {marks.result && (
                        <div className="mt-4 p-4 rounded-lg border border-gray-200 bg-white print:bg-white">
                            <p className="text-sm text-gray-600">{marks.result}</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default StudentMarks;