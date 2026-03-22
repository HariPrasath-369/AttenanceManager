import React from 'react';

const FormWrapper = ({ title, onSubmit, children, submitText = 'Submit' }) => {
    return (
        <div className="bg-white p-6 rounded-lg shadow-md">
            {title && <h2 className="text-xl font-semibold mb-4">{title}</h2>}
            <form onSubmit={onSubmit}>
                {children}
                <button type="submit" className="btn-primary w-full mt-4">
                    {submitText}
                </button>
            </form>
        </div>
    );
};

export default FormWrapper;