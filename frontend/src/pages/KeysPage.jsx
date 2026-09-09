import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api/client'
import { useAuth } from '../context/useAuth'
import { ThemeToggle } from '../components/ThemeToggle'

export function KeysPage() {
  const [keys, setKeys] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')

  const [label, setLabel] = useState('')
  const [isCreating, setIsCreating] = useState(false)
  const [newRawKey, setNewRawKey] = useState(null)

  const { logout } = useAuth()

  useEffect(() => {
    let cancelled = false

    api.get('/api/keys')
      .then((data) => {
        if (!cancelled) setKeys(data)
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

  async function loadKeys() {
    try {
      const data = await api.get('/api/keys')
      setKeys(data)
    } catch (err) {
      setError(err.message)
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
            <span className="px-3 py-1.5 text-sm font-medium text-indigo-600 dark:text-indigo-400 bg-indigo-50 dark:bg-indigo-950/50 rounded-lg">
              Keys
            </span>
            <Link
              to="/usage"
              className="px-3 py-1.5 text-sm font-medium text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white rounded-lg"
            >
              Usage
            </Link>
            <Link
              to="/try"
              className="px-3 py-1.5 text-sm font-medium text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white rounded-lg"
            >
              Try it
            </Link>
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
          <h1 className="text-2xl font-semibold text-gray-900 dark:text-white">API Keys</h1>
          <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
            Manage the keys used to authenticate requests to your rate-limited API.
          </p>
        </div>

        {error && (
          <div className="mb-4 text-sm text-red-700 dark:text-red-400 bg-red-50 dark:bg-red-950/50 border border-red-200 dark:border-red-900 rounded-lg px-3 py-2">
            {error}
          </div>
        )}

        {newRawKey && (
          <div className="mb-6 bg-amber-50 dark:bg-amber-950/30 border border-amber-300 dark:border-amber-900 rounded-xl p-4">
            <p className="text-sm font-medium text-amber-900 dark:text-amber-300 mb-2">
              Copy this key now — it won't be shown again
            </p>
            <div className="flex items-center gap-2">
              <code className="flex-1 bg-white dark:bg-gray-900 border border-amber-200 dark:border-amber-900 rounded-lg px-3 py-2 text-sm text-gray-800 dark:text-gray-200 break-all">
                {newRawKey}
              </code>
            </div>
            <button
              onClick={() => setNewRawKey(null)}
              className="mt-3 text-sm text-amber-800 dark:text-amber-400 font-medium hover:underline"
            >
              Dismiss
            </button>
          </div>
        )}

        <form
          onSubmit={handleCreate}
          className="mb-6 bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 rounded-xl p-4 flex gap-2"
        >
          <input
            type="text"
            value={label}
            onChange={(e) => setLabel(e.target.value)}
            placeholder="Key label, e.g. 'prod-server'"
            required
            className="flex-1 bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg px-3 py-2 text-sm text-gray-900 dark:text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-shadow"
          />
          <button
            type="submit"
            disabled={isCreating}
            className="bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg px-4 py-2 text-sm font-medium transition-colors disabled:opacity-50 disabled:cursor-not-allowed whitespace-nowrap"
          >
            {isCreating ? 'Creating...' : 'Create key'}
          </button>
        </form>

        <div className="bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 rounded-xl divide-y divide-gray-100 dark:divide-gray-800 overflow-hidden">
          {isLoading ? (
            <p className="p-4 text-sm text-gray-500 dark:text-gray-400">Loading...</p>
          ) : keys.length === 0 ? (
            <p className="p-8 text-sm text-gray-500 dark:text-gray-400 text-center">
              No keys yet — create one above to get started.
            </p>
          ) : (
            keys.map((key) => (
              <div key={key.id} className="p-4 flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-900 dark:text-white">{key.label}</p>
                  <p className="text-xs text-gray-500 dark:text-gray-400 mt-0.5">
                    Created {new Date(key.createdAt).toLocaleDateString()}
                    <span className="mx-1.5">·</span>
                    {key.active ? (
                      <span className="inline-flex items-center gap-1 text-emerald-600 dark:text-emerald-400">
                        <span className="w-1.5 h-1.5 rounded-full bg-emerald-500" />
                        Active
                      </span>
                    ) : (
                      <span className="inline-flex items-center gap-1 text-gray-400 dark:text-gray-500">
                        <span className="w-1.5 h-1.5 rounded-full bg-gray-400 dark:bg-gray-600" />
                        Revoked
                      </span>
                    )}
                  </p>
                </div>
                {key.active && (
                  <button
                    onClick={() => handleRevoke(key.id)}
                    className="text-sm text-red-600 dark:text-red-400 font-medium hover:underline"
                  >
                    Revoke
                  </button>
                )}
              </div>
            ))
          )}
        </div>
      </main>
    </div>
  )
}