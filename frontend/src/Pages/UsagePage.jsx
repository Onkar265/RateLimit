import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api/client'

export function UsagePage() {
  const [logs, setLogs] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')

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
    <div className="min-h-screen bg-gray-50 px-4 py-8">
      <div className="max-w-2xl mx-auto">
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-xl font-bold text-gray-800">Usage</h1>
          <Link to="/keys" className="text-sm text-blue-600 hover:underline">
            Back to keys
          </Link>
        </div>

        {error && (
          <div className="mb-4 text-sm text-red-600 bg-red-50 border border-red-200 rounded px-3 py-2">
            {error}
          </div>
        )}

        {!isLoading && logs.length > 0 && (
          <div className="grid grid-cols-2 gap-4 mb-6">
            <div className="bg-white rounded-lg shadow p-4">
              <p className="text-xs text-gray-500">Successful requests</p>
              <p className="text-2xl font-bold text-green-600">{successCount}</p>
            </div>
            <div className="bg-white rounded-lg shadow p-4">
              <p className="text-xs text-gray-500">Rate limited</p>
              <p className="text-2xl font-bold text-red-600">{rateLimitedCount}</p>
            </div>
          </div>
        )}

        <div className="bg-white rounded-lg shadow divide-y divide-gray-100">
          {isLoading ? (
            <p className="p-4 text-sm text-gray-500">Loading...</p>
          ) : logs.length === 0 ? (
            <p className="p-4 text-sm text-gray-500">No requests yet.</p>
          ) : (
            logs.map((log, i) => (
              <div key={i} className="p-3 flex items-center justify-between text-sm">
                <div>
                  <span className="font-mono text-gray-700">{log.method}</span>{' '}
                  <span className="text-gray-800">{log.endpoint}</span>
                </div>
                <div className="flex items-center gap-3 text-xs text-gray-500">
                  <span>{log.responseTimeMs}ms</span>
                  <span
                    className={
                      log.statusCode < 400
                        ? 'text-green-600 font-medium'
                        : 'text-red-600 font-medium'
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
      </div>
    </div>
  )
}