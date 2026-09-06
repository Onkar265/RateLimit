import { useLocation, Navigate, Link } from 'react-router-dom'
import { ThemeToggle } from '../components/ThemeToggle'

export function ShowcasePage() {
  const location = useLocation()
  const data = location.state

  if (!data) {
    return <Navigate to="/try" replace />
  }

  const { profile, projects, skills } = data

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-950 transition-colors">
      <header className="border-b border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900">
        <div className="max-w-4xl mx-auto px-4 py-4 flex items-center justify-between">
          <Link to="/try" className="text-sm text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white">
            ← Back
          </Link>
          <ThemeToggle />
        </div>
      </header>

      <main className="max-w-4xl mx-auto px-4 py-16">
        <div className="text-center mb-16">
          <div className="w-20 h-20 rounded-2xl bg-gradient-to-br from-indigo-500 to-violet-600 mx-auto mb-6 flex items-center justify-center">
            <span className="text-white font-bold text-2xl">
              {profile.name.split(' ').map((n) => n[0]).join('')}
            </span>
          </div>
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white">{profile.name}</h1>
          <p className="text-indigo-600 dark:text-indigo-400 font-medium mt-2">{profile.title}</p>
          <p className="text-gray-600 dark:text-gray-300 max-w-xl mx-auto mt-4 leading-relaxed">
            {profile.bio}
          </p>
          <div className="flex items-center justify-center gap-3 mt-6">
            <a
              href={profile.github}
              target="_blank"
              rel="noreferrer"
              className="px-4 py-2 text-sm font-medium bg-gray-900 dark:bg-white text-white dark:text-gray-900 rounded-lg hover:opacity-90 transition-opacity"
            >
              GitHub
            </a>
            <a
              href={profile.linkedin}
              target="_blank"
              rel="noreferrer"
              className="px-4 py-2 text-sm font-medium border border-gray-300 dark:border-gray-700 text-gray-700 dark:text-gray-300 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
            >
              LinkedIn
            </a>
          </div>
        </div>

        <div className="mb-16">
          <h2 className="text-xs font-semibold text-gray-400 dark:text-gray-500 uppercase tracking-widest text-center mb-8">
            Projects
          </h2>
          <div className="grid gap-4">
            {projects.map((project, i) => (
              <div
                key={i}
                className="bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 rounded-2xl p-6 hover:border-indigo-300 dark:hover:border-indigo-800 transition-colors"
              >
                <div className="flex items-start justify-between gap-4">
                  <h3 className="text-base font-semibold text-gray-900 dark:text-white">
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
                <p className="text-sm text-gray-600 dark:text-gray-300 mt-2 leading-relaxed">
                  {project.description}
                </p>
                <div className="flex flex-wrap gap-1.5 mt-4">
                  {project.techStack.map((tech) => (
                    <span
                      key={tech}
                      className="text-xs bg-gray-100 dark:bg-gray-800 text-gray-600 dark:text-gray-300 rounded-full px-2.5 py-1"
                    >
                      {tech}
                    </span>
                  ))}
                </div>
              </div>
            ))}
          </div>
        </div>

        <div>
          <h2 className="text-xs font-semibold text-gray-400 dark:text-gray-500 uppercase tracking-widest text-center mb-8">
            Skills
          </h2>
          <div className="flex flex-wrap justify-center gap-2 max-w-xl mx-auto">
            {skills.map((skill) => (
              <span
                key={skill}
                className="text-sm bg-indigo-50 dark:bg-indigo-950/50 text-indigo-700 dark:text-indigo-400 rounded-full px-3.5 py-1.5"
              >
                {skill}
              </span>
            ))}
          </div>
        </div>
      </main>
    </div>
  )
}