import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import { ProtectedRoute } from './components/ProtectedRoute'
import { LoginPage } from './pages/LoginPage'
import { RegisterPage } from './pages/RegisterPage'
import { KeysPage } from './pages/KeysPage'
import { UsagePage } from './pages/UsagePage'

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route
            path="/keys"
            element={
              <ProtectedRoute>
                <KeysPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/usage"
            element={
              <ProtectedRoute>
                <UsagePage />
              </ProtectedRoute>
      }
    />
          <Route path="/" element={<Navigate to="/keys" replace />} />
          <Route path="*" element={<Navigate to="/keys" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App