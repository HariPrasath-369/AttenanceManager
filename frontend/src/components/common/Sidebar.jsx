import React from 'react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import {
  LayoutDashboard,
  Users,
  BookOpen,
  Calendar,
  ClipboardCheck,
  FileText,
  Trophy,
  Building2,
  MessageSquare,
  GraduationCap,
} from 'lucide-react';

const menuItems = {
  PRINCIPAL: [
    { to: '/principal/dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/principal/departments', label: 'Departments', icon: Building2 },
    { to: '/principal/hods', label: 'HODs', icon: Users },
    { to: '/principal/performance', label: 'Performance', icon: Trophy },
  ],
  HOD: [
    { to: '/hod/dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/hod/teachers', label: 'Teachers', icon: Users },
    { to: '/hod/classes', label: 'Classes', icon: BookOpen },
    { to: '/hod/subjects', label: 'Subjects', icon: FileText },
    { to: '/hod/timetable', label: 'Timetable', icon: Calendar },
    { to: '/hod/suggestions', label: 'Suggestions', icon: MessageSquare },
  ],
  CLASS_ADVISOR: [
    { to: '/advisor/dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/advisor/students', label: 'Students', icon: Users },
    { to: '/advisor/attendance', label: 'Attendance', icon: ClipboardCheck },
    { to: '/advisor/requests', label: 'Requests', icon: MessageSquare },
    { to: '/advisor/performance', label: 'Performance', icon: Trophy },
  ],
  TEACHER: [
    { to: '/teacher/dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/teacher/classes', label: 'Classes', icon: BookOpen },
    { to: '/teacher/materials', label: 'Materials', icon: FileText },
    { to: '/teacher/oem', label: 'OEM Board', icon: ClipboardCheck },
    { to: '/teacher/timetable', label: 'Timetable', icon: Calendar },
  ],
  STUDENT: [
    { to: '/student/dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/student/attendance', label: 'Attendance', icon: ClipboardCheck },
    { to: '/student/marks', label: 'Marks', icon: Trophy },
    { to: '/student/materials', label: 'Materials', icon: FileText },
    { to: '/student/requests', label: 'Requests', icon: MessageSquare },
    { to: '/student/timetable', label: 'Timetable', icon: Calendar },
  ],
};

const Sidebar = () => {
  const { user } = useAuth();
  const role = user?.roles?.[0] || 'STUDENT';
  const items = menuItems[role] || [];

  return (
    <aside className="w-64 bg-gray-900 text-white flex flex-col h-screen sticky top-0">
      <div className="p-6 text-2xl font-bold border-b border-gray-800">SMS</div>
      <nav className="flex-1 mt-6">
        {items.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) =>
              `flex items-center px-6 py-3 text-sm transition-colors ${isActive ? 'bg-gray-800 text-white' : 'text-gray-300 hover:bg-gray-800 hover:text-white'
              }`
            }
          >
            <item.icon className="w-5 h-5 mr-3" />
            {item.label}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
};

export default Sidebar;