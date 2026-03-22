import React, { useState, useEffect } from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import OemSheetGrid from '../../components/oem/OemSheetGrid';
import OemRow from '../../components/oem/OemRow';
import toast from 'react-hot-toast';

const TeacherOemBoard = () => {
  const { data: classes } = useFetch(api.teacher.getClasses);
  const [selectedClass, setSelectedClass] = useState(null);
  const [selectedSubject, setSelectedSubject] = useState(null);
  const [selectedSheetType, setSelectedSheetType] = useState('Assessment');
  const [sheets, setSheets] = useState([]);
  const [currentSheet, setCurrentSheet] = useState(null);
  const [marks, setMarks] = useState({});
  const [students, setStudents] = useState([]);

  useEffect(() => {
    if (selectedClass && selectedSubject) {
      const fetchSheets = async () => {
        try {
          const res = await api.oem.getSheetsForClass(selectedClass.id);
          setSheets(res.data);
          const sheet = res.data.find((s) => s.sheetType === selectedSheetType);
          setCurrentSheet(sheet);
          if (sheet) {
            const map = {};
            sheet.records.forEach((rec) => {
              map[rec.studentId] = rec.marksObtained;
            });
            setMarks(map);
            // Also fetch students for this class if needed
            // setStudents(sheet.records.map(r => ({ id: r.studentId, fullName: r.studentName, rollNumber: r.rollNumber })));
          }
        } catch (error) {
          console.error(error);
        }
      };
      fetchSheets();
    }
  }, [selectedClass, selectedSubject, selectedSheetType]);

  const handleCreateSheet = async () => {
    if (!selectedClass || !selectedSubject) return;
    try {
      await api.oem.createSheet({
        classSubjectId: selectedSubject.id,
        sheetType: selectedSheetType,
        maxMarks: 100,
      });
      toast.success('Sheet created');
      // Refetch sheets
      const res = await api.oem.getSheetsForClass(selectedClass.id);
      setSheets(res.data);
    } catch (error) {
      toast.error('Failed to create sheet');
    }
  };

  const handleMarkChange = (studentId, field, value) => {
    if (currentSheet?.status !== 'DRAFT') return;
    const numValue = parseInt(value) || 0;
    setMarks((prev) => ({ ...prev, [studentId]: numValue }));
  };

  const handleSave = async () => {
    const records = Object.entries(marks).map(([studentId, marksObtained]) => ({
      studentId: parseInt(studentId),
      marksObtained,
    }));
    try {
      await api.oem.updateMarks(currentSheet.id, { marks: records });
      toast.success('Marks saved');
      // Refresh sheet data
      const res = await api.oem.getSheetsForClass(selectedClass.id);
      setSheets(res.data);
    } catch (error) {
      toast.error('Failed to save marks');
    }
  };

  const handleSubmit = async () => {
    try {
      await api.oem.submitSheet(currentSheet.id);
      toast.success('Sheet submitted');
      const res = await api.oem.getSheetsForClass(selectedClass.id);
      setSheets(res.data);
    } catch (error) {
      toast.error('Failed to submit');
    }
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">OEM Board</h1>
      <div className="flex space-x-4 mb-6">
        <select
          onChange={(e) => setSelectedClass(classes?.find((c) => c.id === parseInt(e.target.value)))}
          className="input-field w-auto"
        >
          <option value="">Select Class</option>
          {classes?.map((c) => (
            <option key={c.id} value={c.id}>
              {c.name}
            </option>
          ))}
        </select>
        {selectedClass && (
          <select
            onChange={(e) => setSelectedSubject({ id: parseInt(e.target.value) })}
            className="input-field w-auto"
          >
            <option value="">Select Subject</option>
            {selectedClass.subjects?.map((s) => (
              <option key={s.id} value={s.id}>
                {s.name}
              </option>
            ))}
          </select>
        )}
        {selectedSubject && (
          <select
            value={selectedSheetType}
            onChange={(e) => setSelectedSheetType(e.target.value)}
            className="input-field w-auto"
          >
            <option value="Assessment">Assessment</option>
            <option value="Practical">Practical</option>
            <option value="Semester">Semester</option>
          </select>
        )}
        {selectedSubject && !currentSheet && (
          <button onClick={handleCreateSheet} className="btn-primary">
            Create Sheet
          </button>
        )}
      </div>

      {currentSheet && (
        <div>
          <div className="mb-4 flex justify-between items-center">
            <h2 className="text-xl font-semibold">
              {currentSheet.sheetType} Sheet
            </h2>
            <span
              className={`px-2 py-1 rounded text-sm ${currentSheet.status === 'DRAFT'
                ? 'bg-yellow-100 text-yellow-800'
                : currentSheet.status === 'SUBMITTED'
                  ? 'bg-blue-100 text-blue-800'
                  : 'bg-green-100 text-green-800'
                }`}
            >
              {currentSheet.status}
            </span>
          </div>

          <div className="overflow-x-auto">
            <table className="min-w-full bg-white border">
              <thead>
                <tr>
                  <th className="px-4 py-2 border">Roll No</th>
                  <th className="px-4 py-2 border">Student</th>
                  <th className="px-4 py-2 border">Marks (Max {currentSheet.maxMarks})</th>
                </tr>
              </thead>
              <tbody>
                {currentSheet.records.map((rec) => (
                  <tr key={rec.studentId}>
                    <td className="px-4 py-2 border">{rec.rollNumber}</td>
                    <td className="px-4 py-2 border">{rec.studentName}</td>
                    <td className="px-4 py-2 border">
                      <input
                        type="number"
                        value={marks[rec.studentId] !== undefined ? marks[rec.studentId] : rec.marksObtained}
                        onChange={(e) => handleMarkChange(rec.studentId, 'marks', e.target.value)}
                        disabled={currentSheet.status !== 'DRAFT'}
                        className="w-24 p-1 border rounded"
                      />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {currentSheet.status === 'DRAFT' && (
            <div className="mt-4 space-x-2">
              <button onClick={handleSave} className="btn-secondary">
                Save Draft
              </button>
              <button onClick={handleSubmit} className="btn-primary">
                Submit
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default TeacherOemBoard;