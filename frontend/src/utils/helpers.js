export const formatDate = (date) => {
    if (!date) return '';
    return new Date(date).toLocaleDateString();
};

export const formatDateTime = (date) => {
    if (!date) return '';
    return new Date(date).toLocaleString();
};

export const truncate = (str, n) => {
    return str?.length > n ? str.substr(0, n - 1) + '...' : str;
};