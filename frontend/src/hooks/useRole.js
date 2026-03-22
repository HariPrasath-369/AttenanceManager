import useAuth from './useAuth';

export const useRole = () => {
    const { user } = useAuth();
    if (!user) return null;
    return user.roles?.[0] || 'STUDENT';
};