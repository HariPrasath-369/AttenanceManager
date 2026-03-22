import React from 'react';
import DashboardCard from './DashboardCard';

const StatsGrid = ({ stats }) => {
    return (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {stats.map((stat, idx) => (
                <DashboardCard key={idx} {...stat} />
            ))}
        </div>
    );
};

export default StatsGrid;