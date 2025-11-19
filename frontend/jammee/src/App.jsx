import "./App.css";
import Topbar from "./assets/components/layout/TopBar";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import { useSelector } from "react-redux";
import { selectIsAuthenticated } from "./store/slices/authSlice";
import { BrowserRouter, Navigate, Routes, Route } from "react-router-dom";
import AuthLayout from "./assets/components/layout/AuthLayout";
import RegisterPage from "./pages/RegisterPage";
import LoginPage from "./pages/LoginPage";
import MainLayout from "./assets/components/layout/MainLayout";
import HomePage from "./pages/HomePage";

function App() {
    const isAuthenticated = useSelector(selectIsAuthenticated);

    return (
        <BrowserRouter>
            {isAuthenticated && <Topbar />}
            <Routes>
                {!isAuthenticated ? (
                    <>
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
                        <Route path="*" element={<Navigate to="/login" replace />} />
                    </>
                ) : (
                    <>
                        <Route path="/" element={<Navigate to="/home" replace />} />
                        <Route
                            path="*"
                            element={
                                <MainLayout>
                                    <div>Pagina non trovata</div>
                                </MainLayout>
                            }
                        />
                        <Route path="/register" element={<Navigate to="/home" replace />} />
                        <Route path="/login" element={<Navigate to="/home" replace />} />
                        <Route
                            path="/home"
                            element={
                                <MainLayout>
                                    <HomePage />
                                </MainLayout>
                            }
                        />
                    </>
                )}
            </Routes>
        </BrowserRouter>
    );
}

export default App;
