import React, { useState, useEffect } from 'react';
import { useFetch } from '../../hooks/useFetch';
import api from '../../api/api';
import AttendanceGrid from '../../components/attendance/AttendanceGrid';
import AttendanceSummary from '../../components/attendance/AttendanceSummary';
import toast from 'react-hot-toast';

const AdvisorAttendance = () => {
  const { data: students } = useFetch(api.advisor.getStudents);
  const [date, setDate] = useState(new Date().toISOString().slice(0, 10));
  const [session, setSession] = useState('MORNING');
  const [attendance, setAttendance] = useState({});
  const [submitted, setSubmitted] = useState(false);

  useEffect(() => {
    const fetchExisting = async () => {
      if (students?.length) {
        try {
          const res = await api.attendance.getAttendanceByDateAndSession(
            students[0].classId,
            date,
            session
          );
          const existing = res.data;
          if (existing.records) {
            const map = {};
            existing.records.forEach((rec) => {
              map[rec.studentId] = rec.present;
            });
            setAttendance(map);
            setSubmitted(existing.isSubmitted);
          } else {
            setAttendance({});
            setSubmitted(false);
          }
        } catch (error) {
          setAttendance({});
          setSubmitted(false);
        }
      }
    };
    fetchExisting();
  }, [date, session, students]);

  const toggleAttendance = (studentId) => {
    if (submitted) return;
    setAttendance((prev) => ({ ...prev, [studentId]: !prev[studentId] }));
  };

  const handleSubmit = async () => {
    if (!students?.length) return;
    const records = Object.entries(attendance).map(([studentId, present]) => ({
      studentId: parseInt(studentId),
      session,
      present,
    }));
    const payload = {
      classId: students[0].classId,
      date,
      records,
      isSubmitted: true,
    };
    try {
      await api.attendance.takeAttendance(payload);
      toast.success('Attendance submitted');
      setSubmitted(true);
    } catch (error) {
      toast.error('Failed to submit attendance');
    }
  };

  if (!students) return <div>Loading...</div>;

  const total = students.length;
  const present = Object.values(attendance).filter((v) => v === true).length;
  const absent = total - present;

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Take Attendance</h1>
      <div className="flex space-x-4 mb-6">
        <input
          type="date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
          className="input-field w-auto"
        />
        <select
          value={session}
          onChange={(e) => setSession(e.target.value)}
          className="input-field w-auto"
        >
          <option value="MORNING">Morning</option>
          <option value="AFTERNOON">Afternoon</option>
        </select>
        {!submitted && (
          <button onClick={handleSubmit} className="btn-primary">
            Submit Attendance
          </button>
        )}
        {submitted && (
          <span className="text-green-600">
            Attendance already submitted for this date and session.
          </span>
        )}
      </div>

      <AttendanceSummary total={total} present={present} absent={absent} other={0} />
      <AttendanceGrid
        students={students}
        attendance={attendance}
        onToggle={toggleAttendance}
        submitted={submitted}
        session={session}
      />
    </div>
  );
};

export default AdvisorAttendance;