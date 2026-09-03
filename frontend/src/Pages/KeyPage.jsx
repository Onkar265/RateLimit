import { useState, useEffect } from 'react'
import { api } from '../api/client'
import { useAuth } from '../context/useAuth'

export function KeysPage() {
  const [keys, setKeys] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')

  const [label, setLabel] = useState('')
  const [isCreating, setIsCreating] = useState(false)
  const [newRawKey, setNewRawKey] = useState(null)

  const { logout } = useAuth()

  useEffect(() => {
    loadKeys()
  }, [])

  async function loadKeys() {
    setIsLoading(true)
    setError('')
    try {
      const data = await api.get('/api/keys')
      setKeys(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setIsLoading(false)
    }
  }

  async function handleCreate(e) {
    e.preventDefault()
    setError('')
    setIsCreating(true)

    try {
      const response = await api.post('/api/keys', { label })
      setNewRawKey(response.rawKey)
      setLabel('')
      await loadKeys()
    } catch (err) {
      setError(err.message)
    } finally {
      setIsCreating(false)
    }
  }

  async function handleRevoke(id) {
    if (!confirm('Revoke this key? This cannot be undone.')) return

    try {
      await api.delete(`/api/keys/${id}`)
      await loadKeys()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="min-h-screen bg-gray-50 px-4 py-8">
      <div className="max-w-2xl mx-auto">
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-xl font-bold text-gray-800">API Keys</h1>
          <button
            onClick={logout}
            className="text-sm text-gray-600 hover:text-gray-800"
          >
            Log out
          </button>
        </div>

        {error && (
          <div className="mb-4 text-sm text-red-600 bg-red-50 border border-red-200 rounded px-3 py-2">
            {error}
          </div>
        )}

        {newRawKey && (
          <div className="mb-6 bg-yellow-50 border border-yellow-300 rounded p-4">
            <p className="text-sm font-medium text-yellow-800 mb-1">
              Copy this key now — it won't be shown again
            </p>
            <code className="block bg-white border border-yellow-200 rounded px-3 py-2 text-sm break-all">
              {newRawKey}
            </code>
            <button
              onClick={() => setNewRawKey(null)}
              className="mt-2 text-sm text-yellow-800 underline"
            >
              Dismiss
            </button>
          </div>
        )}

        <form onSubmit={handleCreate} className="mb-6 bg-white rounded-lg shadow p-4 flex gap-2">
          <input
            type="text"
            value={label}
            onChange={(e) => setLabel(e.target.value)}
            placeholder="Key label, e.g. 'prod-server'"
            required
            className="flex-1 border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <button
            type="submit"
            disabled={isCreating}
            className="bg-blue-600 text-white rounded px-4 py-2 text-sm font-medium hover:bg-blue-700 disabled:opacity-50"
          >
            {isCreating ? 'Creating...' : 'Create key'}
          </button>
        </form>

        <div className="bg-white rounded-lg shadow divide-y divide-gray-100">
          {isLoading ? (
            <p className="p-4 text-sm text-gray-500">Loading...</p>
          ) : keys.length === 0 ? (
            <p className="p-4 text-sm text-gray-500">No keys yet.</p>
          ) : (
            keys.map((key) => (
              <div key={key.id} className="p-4 flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-800">{key.label}</p>
                  <p className="text-xs text-gray-500">
                    Created {new Date(key.createdAt).toLocaleDateString()} ·{' '}
                    {key.active ? (
                      <span className="text-green-600">Active</span>
                    ) : (
                      <span className="text-red-600">Revoked</span>
                    )}
                  </p>
                </div>
                {key.active && (
                  <button
                    onClick={() => handleRevoke(key.id)}
                    className="text-sm text-red-600 hover:underline"
                  >
                    Revoke
                  </button>
                )}
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  )
}