import React from 'react';

const OemSheetGrid = ({ sheets, onSelect }) => {
    return (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
            {sheets.map((sheet) => (
                <button
                    key={sheet.id}
                    onClick={() => onSelect(sheet)}
                    className="p-4 bg-white rounded shadow hover:shadow-md transition"
                >
                    <h3 className="font-bold">{sheet.sheetType}</h3>
                    <p className="text-sm text-gray-500">Status: {sheet.status}</p>
                    <p className="text-sm">Max Marks: {sheet.maxMarks}</p>
                </button>
            ))}
        </div>
    );
};

export default OemSheetGrid;