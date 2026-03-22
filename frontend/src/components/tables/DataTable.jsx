import React, { useState } from 'react';
import Pagination from './Pagination';

const DataTable = ({ columns, data, actions, itemsPerPage = 10 }) => {
    const [currentPage, setCurrentPage] = useState(1);
    const start = (currentPage - 1) * itemsPerPage;
    const paginatedData = data.slice(start, start + itemsPerPage);

    return (
        <div>
            <div className="overflow-x-auto">
                <table className="min-w-full bg-white border">
                    <thead className="bg-gray-50">
                        <tr>
                            {columns.map((col, idx) => (
                                <th key={idx} className="px-4 py-2 border text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    {col.label}
                                </th>
                            ))}
                            {actions && <th className="px-4 py-2 border text-right">Actions</th>}
                        </tr>
                    </thead>
                    <tbody>
                        {paginatedData.map((row, idx) => (
                            <tr key={idx}>
                                {columns.map((col, colIdx) => (
                                    <td key={colIdx} className="px-4 py-2 border">
                                        {col.render ? col.render(row) : row[col.key]}
                                    </td>
                                ))}
                                {actions && <td className="px-4 py-2 border text-right">{actions(row)}</td>}
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            <Pagination
                currentPage={currentPage}
                totalItems={data.length}
                itemsPerPage={itemsPerPage}
                onPageChange={setCurrentPage}
            />
        </div>
    );
};

export default DataTable;