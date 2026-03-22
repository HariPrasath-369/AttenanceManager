import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/api';
import FormWrapper from '../../components/forms/FormWrapper';
import InputField from '../../components/forms/InputField';
import SelectField from '../../components/forms/SelectField';
import toast from 'react-hot-toast';

const Register = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    fullName: '',
    roles: ['STUDENT'],
  });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();  // ✅ Critical to prevent page reload
    setLoading(true);
    try {
      await api.auth.register(formData);
      toast.success('Registration successful! Please login.');
      navigate('/login');
    } catch (error) {
      console.error('Registration error:', error.response?.data);
      toast.error(error.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <FormWrapper
      title="Create an Account"
      onSubmit={handleSubmit}
      submitText={loading ? 'Registering...' : 'Register'}
    >
      <InputField
        label="Username"
        name="username"
        value={formData.username}
        onChange={(e) => setFormData({ ...formData, username: e.target.value })}
        required
      />
      <InputField
        label="Full Name"
        name="fullName"
        value={formData.fullName}
        onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
        required
      />
      <InputField
        label="Email"
        name="email"
        type="email"
        value={formData.email}
        onChange={(e) => setFormData({ ...formData, email: e.target.value })}
        required
      />
      <InputField
        label="Password"
        name="password"
        type="password"
        value={formData.password}
        onChange={(e) => setFormData({ ...formData, password: e.target.value })}
        required
      />
      <SelectField
        label="Role"
        name="roles"
        value={formData.roles[0]}
        onChange={(e) => setFormData({ ...formData, roles: [e.target.value] })}
        options={[
          { value: 'STUDENT', label: 'Student' },
          { value: 'TEACHER', label: 'Teacher' },
          { value: 'CLASS_ADVISOR', label: 'Class Advisor' },
          { value: 'HOD', label: 'HOD' },
          { value: 'PRINCIPAL', label: 'Principal' },
        ]}
        required
      />
    </FormWrapper>
  );
};

export default Register;