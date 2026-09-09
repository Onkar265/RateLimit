import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/useAuth'
import { ThemeToggle } from '../components/ThemeToggle'

export function TryPage() {
  const [apiKey, setApiKey] = useState('')
  const [profile, setProfile] = useState(null)
  const [projects, setProjects] = useState(null)
  const [skills, setSkills] = useState(null)
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  const { token, logout } = useAuth()
  const navigate = useNavigate()

  async function fetchPortfolio(path, headers) {
    const response = await fetch(`https://ratelimit-backend-v3l5.onrender.com${path}`, { headers })
    if (response.status === 401) {
      logout()
      navigate('/login')
      throw new Error('Session expired — please log in again')
    }
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
    setProfile(null)
    setProjects(null)
    setSkills(null)

    const headers = {
      'X-API-Key': apiKey,
      'Authorization': `Bearer ${token}`,
    }

    try {
      const [profileData, projectsData, skillsData] = await Promise.all([
        fetchPortfolio('/api/portfolio/profile', headers),
        fetchPortfolio('/api/portfolio/projects', headers),
        fetchPortfolio('/api/portfolio/skills', headers),
      ])
      setProfile(profileData)
      setProjects(projectsData)
      setSkills(skillsData)
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
          <div className="mb-6 text-sm text-red-700 dark:text-red-400 bg-red-50 dark:bg-red-950/50 border border-red-200 dark:border-red-900 rounded-lg px-3 py-2">
            {error}
          </div>
        )}

        {profile && (
          <div className="mb-6 bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 rounded-xl p-6 text-center">
            <div className="w-16 h-16 rounded-2xl bg-linear-to-br from-indigo-500 to-violet-600 mx-auto mb-4 flex items-center justify-center">
              <span className="text-white font-bold text-lg">
                {profile.name.split(' ').map((n) => n[0]).join('')}
              </span>
            </div>
            <h2 className="text-lg font-semibold text-gray-900 dark:text-white">{profile.name}</h2>
            <p className="text-sm text-indigo-600 dark:text-indigo-400 mt-0.5">{profile.title}</p>
            <p className="text-sm text-gray-600 dark:text-gray-300 mt-3 max-w-md mx-auto leading-relaxed">
              {profile.bio}
            </p>
            <div className="flex items-center justify-center gap-3 mt-5">
              <a
                href={profile.github}
                target="_blank"
                rel="noreferrer"
                className="px-3 py-1.5 text-xs font-medium bg-gray-900 dark:bg-white text-white dark:text-gray-900 rounded-lg hover:opacity-90 transition-opacity"
              >
                GitHub
              </a>
              <a
                href={profile.linkedin}
                target="_blank"
                rel="noreferrer"
                className="px-3 py-1.5 text-xs font-medium border border-gray-300 dark:border-gray-700 text-gray-700 dark:text-gray-300 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
              >
                LinkedIn
              </a>
            </div>
          </div>
        )}

        {projects && (
          <div className="mb-6 space-y-3">
            {projects.map((project, i) => (
              <div
                key={i}
                className="bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 rounded-xl p-4"
              >
                <div className="flex items-center justify-between gap-4">
                  <h3 className="text-sm font-semibold text-gray-900 dark:text-white">
                    {project.name}
                  </h3>
                  {project.url && (
                    <a
                      href={project.url}
                      target="_blank"
                      rel="noreferrer"
                      className="text-xs font-medium text-indigo-600 dark:text-indigo-400 hover:underline whitespace-nowrap"
                    >
                      View repo →
                    </a>
                  )}
                </div>
                <p className="text-sm text-gray-600 dark:text-gray-300 mt-1">{project.description}</p>
                <div className="flex flex-wrap gap-1.5 mt-3">
                  {project.techStack.map((tech) => (
                    <span
                      key={tech}
                      className="text-xs bg-gray-100 dark:bg-gray-800 text-gray-600 dark:text-gray-300 rounded-full px-2 py-0.5"
                    >
                      {tech}
                    </span>
                  ))}
                </div>
              </div>
            ))}
          </div>
        )}

        {skills && (
          <div className="bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 rounded-xl p-4">
            <p className="text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wide mb-2">
              Skills
            </p>
            <div className="flex flex-wrap gap-1.5">
              {skills.map((skill) => (
                <span
                  key={skill}
                  className="text-xs bg-indigo-50 dark:bg-indigo-950/50 text-indigo-700 dark:text-indigo-400 rounded-full px-2 py-0.5"
                >
                  {skill}
                </span>
              ))}
            </div>
          </div>
        )}
      </main>
    </div>
  )
}