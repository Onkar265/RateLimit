import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api/client'
import { useAuth } from '../context/useAuth'
import { ThemeToggle } from '../components/ThemeToggle'

export function UsagePage() {
  const [logs, setLogs] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')

  const { logout } = useAuth()

  useEffect(() => {
    let cancelled = false

    api.get('/api/usage')
      .then((data) => {
        if (!cancelled) setLogs(data)
      })
      .catch((err) => {
        if (!cancelled) setError(err.message)
      })
      .finally(() => {
        if (!cancelled) setIsLoading(false)
      })

    return () => {
      cancelled = true
    }
  }, [])

  const successCount = logs.filter((l) => l.statusCode < 400).length
  const rateLimitedCount = logs.filter((l) => l.statusCode === 429).length

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-950 transition-colors">
      <header className="border-b border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900">
        <div className="max-w-3xl mx-auto px-4 py-4 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="w-7 h-7 rounded-md bg-indigo-600 flex items-center justify-center">
              <span className="text-white font-bold text-xs">R</span>
            </div>
            <span className="font-semibold text-gray-900 dark:text-white text-sm">RateLimit</span>
          </div>

          <nav className="flex items-center gap-1">
            <Link
              to="/keys"
              className="px-3 py-1.5 text-sm font-medium text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white rounded-lg"
            >
              Keys
            </Link>
            <span className="px-3 py-1.5 text-sm font-medium text-indigo-600 dark:text-indigo-400 bg-indigo-50 dark:bg-indigo-950/50 rounded-lg">
              Usage
            </span>
            <div className="w-px h-5 bg-gray-200 dark:bg-gray-800 mx-2" />
            <ThemeToggle />
            <button
              onClick={logout}
              className="px-3 py-1.5 text-sm font-medium text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white rounded-lg"
            >
              Log out
            </button>
          </nav>
        </div>
      </header>

      <main className="max-w-3xl mx-auto px-4 py-8">
        <div className="mb-6">
          <h1 className="text-2xl font-semibold text-gray-900 dark:text-white">Usage</h1>
          <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
            Your most recent requests across all API keys.
          </p>
        </div>

        {error && (
          <div className="mb-4 text-sm text-red-700 dark:text-red-400 bg-red-50 dark:bg-red-950/50 border border-red-200 dark:border-red-900 rounded-lg px-3 py-2">
            {error}
          </div>
        )}

        {!isLoading && logs.length > 0 && (
          <div className="grid grid-cols-2 gap-4 mb-6">
            <div className="bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 rounded-xl p-4">
              <p className="text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wide">
                Successful
              </p>
              <p className="text-3xl font-semibold text-emerald-600 dark:text-emerald-400 mt-1">
                {successCount}
              </p>
            </div>
            <div className="bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 rounded-xl p-4">
              <p className="text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wide">
                Rate limited
              </p>
              <p className="text-3xl font-semibold text-red-600 dark:text-red-400 mt-1">
                {rateLimitedCount}
              </p>
            </div>
          </div>
        )}

        <div className="bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 rounded-xl divide-y divide-gray-100 dark:divide-gray-800 overflow-hidden">
          {isLoading ? (
            <p className="p-4 text-sm text-gray-500 dark:text-gray-400">Loading...</p>
          ) : logs.length === 0 ? (
            <p className="p-8 text-sm text-gray-500 dark:text-gray-400 text-center">
              No requests yet.
            </p>
          ) : (
            logs.map((log, i) => (
              <div key={i} className="p-3 flex items-center justify-between text-sm">
                <div className="flex items-center gap-2 min-w-0">
                  <span className="font-mono text-xs text-gray-500 dark:text-gray-400 bg-gray-100 dark:bg-gray-800 rounded px-1.5 py-0.5">
                    {log.method}
                  </span>
                  <span className="text-gray-800 dark:text-gray-200 truncate">{log.endpoint}</span>
                  <span className="text-xs text-gray-400 dark:text-gray-500 whitespace-nowrap">
                    via {log.keyLabel}
                  </span>
                </div>
                <div className="flex items-center gap-3 text-xs text-gray-500 dark:text-gray-400 whitespace-nowrap">
                  <span>{log.responseTimeMs}ms</span>
                  <span
                    className={
                      log.statusCode < 400
                        ? 'text-emerald-600 dark:text-emerald-400 font-medium'
                        : 'text-red-600 dark:text-red-400 font-medium'
                    }
                  >
                    {log.statusCode}
                  </span>
                  <span>{new Date(log.requestedAt).toLocaleTimeString()}</span>
                </div>
              </div>
            ))
          )}
        </div>
      </main>
    </div>
  )
}