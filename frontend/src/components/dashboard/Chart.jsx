import React from "react";
import { Bar, Line, Pie } from "react-chartjs-2";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    LineElement,
    PointElement,
    ArcElement,
    Title,
    Tooltip,
    Legend,
} from "chart.js";

// Register chart components
ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    LineElement,
    PointElement,
    ArcElement,
    Title,
    Tooltip,
    Legend
);

const Chart = ({ type = "bar", data, options }) => {
    const chartMap = {
        bar: Bar,
        line: Line,
        pie: Pie,
    };

    const Component = chartMap[type] || Bar;

    if (!data) return <p>Loading chart...</p>;

    return (
        <div className="w-full h-full">
            <Component data={data} options={options} />
        </div>
    );
};

export default Chart;