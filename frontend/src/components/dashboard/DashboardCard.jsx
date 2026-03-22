import React from 'react';

const DashboardCard = ({ title, value, icon: Icon, trend, trendType, description }) => {
  const trendColors = {
    positive: 'text-green-600 bg-green-50',
    negative: 'text-red-600 bg-red-50',
    neutral: 'text-gray-600 bg-gray-50',
  };

  return (
    <div className="bg-white rounded-lg shadow p-6 hover:shadow-lg transition-shadow">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm text-gray-500">{title}</p>
          <p className="text-2xl font-bold mt-1">{value}</p>
          {description && <p className="text-xs text-gray-400 mt-1">{description}</p>}
        </div>
        <div className={`p-3 rounded-full ${trend ? trendColors[trendType] : 'bg-primary-50'}`}>
          <Icon className="w-6 h-6 text-primary-600" />
        </div>
      </div>
      {trend && <p className={`text-xs font-medium mt-2 ${trendColors[trendType]}`}>{trend}</p>}
    </div>
  );
};

export default DashboardCard;