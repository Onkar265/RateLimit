import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/useAuth'
import { ThemeToggle } from '../components/ThemeToggle'

export function TryPage() {
  const [apiKey, setApiKey] = useState('')
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  const { token, logout } = useAuth()
  const navigate = useNavigate()

  async function fetchPortfolio(path, headers) {
    const response = await fetch(`http://localhost:8080${path}`, { headers })
    if (!response.ok) {
      const body = await response.json().catch(() => ({}))
      throw new Error(body.error || `Request to ${path} failed (${response.status})`)
    }
    return response.json()
  }

  async function handleTry(e) {
    e.preventDefault()
    setError('')
    setIsLoading(true)

    const headers = {
      'X-API-Key': apiKey,
      'Authorization': `Bearer ${token}`,
    }

    try {
      const [profile, projects, skills] = await Promise.all([
        fetchPortfolio('/api/portfolio/profile', headers),
        fetchPortfolio('/api/portfolio/projects', headers),
        fetchPortfolio('/api/portfolio/skills', headers),
      ])
      navigate('/portfolio-view', { state: { profile, projects, skills } })
    } catch (err) {
      setError(err.message)
    } finally {
      setIsLoading(false)
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
            <Link to="/keys" className="px-3 py-1.5 text-sm font-medium text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white rounded-lg">
              Keys
            </Link>
            <Link to="/usage" className="px-3 py-1.5 text-sm font-medium text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white rounded-lg">
              Usage
            </Link>
            <span className="px-3 py-1.5 text-sm font-medium text-indigo-600 dark:text-indigo-400 bg-indigo-50 dark:bg-indigo-950/50 rounded-lg">
              Try it
            </span>
            <div className="w-px h-5 bg-gray-200 dark:bg-gray-800 mx-2" />
            <ThemeToggle />
            <button onClick={logout} className="px-3 py-1.5 text-sm font-medium text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white rounded-lg">
              Log out
            </button>
          </nav>
        </div>
      </header>

      <main className="max-w-3xl mx-auto px-4 py-8">
        <div className="mb-6">
          <h1 className="text-2xl font-semibold text-gray-900 dark:text-white">Try the API</h1>
          <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
            Paste one of your active API keys below to fetch live portfolio data through the rate limiter.
          </p>
        </div>

        <form onSubmit={handleTry} className="mb-6 bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 rounded-xl p-4 flex gap-2">
          <input
            type="text"
            value={apiKey}
            onChange={(e) => setApiKey(e.target.value)}
            placeholder="sk_..."
            required
            className="flex-1 font-mono bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg px-3 py-2 text-sm text-gray-900 dark:text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
          />
          <button
            type="submit"
            disabled={isLoading}
            className="bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg px-4 py-2 text-sm font-medium transition-colors disabled:opacity-50 whitespace-nowrap"
          >
            {isLoading ? 'Fetching...' : 'Fetch'}
          </button>
        </form>

        {error && (
          <div className="text-sm text-red-700 dark:text-red-400 bg-red-50 dark:bg-red-950/50 border border-red-200 dark:border-red-900 rounded-lg px-3 py-2">
            {error}
          </div>
        )}
      </main>
    </div>
  )
}