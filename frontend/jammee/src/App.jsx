import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { useGetMusicianMeQuery } from "./store/slices/api/musicistaApi";
import { selectIsAuthenticated } from "./store/slices/authSlice";

import AuthLayout from "./assets/components/layout/AuthLayout";
import MainLayout from "./assets/components/layout/MainLayout";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import HomePage from "./pages/HomePage";
import MusicianFormPage from "./pages/MusicianFormPage";
import UserPage from "./pages/UserPage";
import InboxPage from "./pages/InboxPage";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import "./App.css";

const ProtectedRoute = ({ children }) => {
    const isAuthenticated = useSelector(selectIsAuthenticated);
    // eslint-disable-next-line no-unused-vars
    const { data: musician, error, isLoading } = useGetMusicianMeQuery(undefined, { skip: !isAuthenticated });

    if (!isAuthenticated) return <Navigate to="/login" replace />;

    if (error?.status === 404) return <Navigate to="/musician/register" replace />;
    if (isLoading) return <div>Caricamento...</div>;

    return children;
};

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route
                    path="/login"
                    element={
                        <AuthLayout>
                            <LoginPage />
                        </AuthLayout>
                    }
                />
                <Route
                    path="/register"
                    element={
                        <AuthLayout>
                            <RegisterPage />
                        </AuthLayout>
                    }
                />

                <Route
                    path="/musician/register"
                    element={
                        <AuthLayout>
                            <MusicianFormPage />
                        </AuthLayout>
                    }
                />

                <Route
                    path="/home"
                    element={
                        <ProtectedRoute>
                            <MainLayout>
                                <HomePage />
                            </MainLayout>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/profile"
                    element={
                        <ProtectedRoute>
                            <MainLayout>
                                <UserPage />
                            </MainLayout>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/musician/:id"
                    element={
                        <ProtectedRoute>
                            <MainLayout>
                                <UserPage />
                            </MainLayout>
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/inbox"
                    element={
                        <ProtectedRoute>
                            <MainLayout>
                                <InboxPage />
                            </MainLayout>
                        </ProtectedRoute>
                    }
                />

                <Route path="*" element={<Navigate to="/home" replace />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
