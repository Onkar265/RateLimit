# RateLimit — A Rate-Limited API Platform

A full-stack platform that issues API keys, enforces per-key rate limits with an atomic Redis-backed token bucket, logs usage asynchronously, and exposes a React dashboard for managing it all. The protected API itself serves live portfolio data — so you can register, create a key, and see the whole system work end-to-end against real content.

**Try it yourself:** register an account, create an API key, and paste it into the "Try it" page to fetch live data through the rate limiter.

## Why this project

Most rate-limiting demos wrap a trivial "hello world" endpoint. This one is built the way real API platforms (Stripe, Twilio, RapidAPI) actually work: you're the provider, developers register for keys, and your infrastructure enforces fair usage per key — with the correctness guarantees that require.



**Request flow:** every request passes through `UsageLoggingFilter` (times and logs the outcome, success or failure) wrapping `RateLimitFilter` (checks the API key against a Redis-backed token bucket) before reaching Spring Security's JWT layer or the actual controller.

## Key design decisions

### Atomic rate limiting via Lua, not application-level locking
A naive `GET → check → INCR` rate limiter has a race condition: two concurrent requests can both read the same "under limit" state before either writes back, letting both through when only one should succeed. The token bucket logic (read current tokens, compute time-based refill, decrement if available) runs as a single Lua script executed atomically inside Redis — the entire check-and-decrement happens as one indivisible operation, closing the race window entirely. Verified with a concurrency test firing 50 simultaneous requests against a 10-token bucket and asserting exactly 10 succeed.

### Two independent auth mechanisms, deliberately
JWT authenticates *dashboard users* (people managing their own keys). API keys authenticate *API consumers* (calling the actual rate-limited data). These are separate concerns with separate filters — conflating them would mean either forcing API consumers to have dashboard logins, or forcing dashboard actions to be rate-limited by a key that doesn't exist yet at that point in the flow.

### Cache-aside for plan limits, not a query per request
Every request needs to know its key's daily limit, but hitting Postgres on every single request would defeat the purpose of a fast rate limiter. `PlanLimitResolver` checks Redis first (5-minute TTL); on a miss, it queries Postgres (with `JOIN FETCH` to eagerly load the user's plan in one round trip) and populates the cache. Revoking a key invalidates its cache entry immediately rather than waiting out the TTL.

### Async usage logging, sync rate-limit checks
Rate-limit decisions block the request (they have to — the caller needs an answer). Usage logging doesn't need to: it runs on a dedicated thread pool via `@Async`, so a slow database write never adds latency to the actual API response. Logging failures are caught and swallowed with a warning, since by the time the log write happens the real response has already been sent — there's nothing left to roll back.

### Portfolio endpoint requires both a JWT and an API key
`/api/portfolio/**` demonstrates the platform serving something real — this project's own portfolio data. It intentionally requires *both* forms of authentication: a valid dashboard login (JWT) and a valid, active, under-quota API key, exercising the full registration → login → key creation → protected request pipeline in one flow.

## Tech stack

**Backend:** Spring Boot 4, Spring Security, Spring Data JPA, Flyway, PostgreSQL, Redis, JWT (JJWT)
**Frontend:** React (Vite), React Router, Tailwind CSS
**Infra:** Docker Compose (Postgres + Redis)

## API overview

| Endpoint | Auth | Description |
|---|---|---|
| `POST /api/auth/register` | none | Create an account, returns a JWT |
| `POST /api/auth/login` | none | Log in, returns a JWT |
| `GET /api/keys` | JWT | List your API keys |
| `POST /api/keys` | JWT | Create a key (raw value shown once) |
| `DELETE /api/keys/{id}` | JWT | Revoke a key |
| `GET /api/usage` | JWT | Recent request history across your keys |
| `GET /api/data/ping` | API key | Simple rate-limited health check |
| `GET /api/portfolio/*` | JWT **and** API key | Live portfolio data, fully rate-limited |

## Running locally

```bash
# Start Postgres + Redis
docker compose up -d

# Backend (from /backend)
mvn spring-boot:run

# Frontend (from /frontend)
npm install
npm run dev
```

Backend runs on `localhost:8080`, frontend on `localhost:5173`.

## Known limitations / next steps

- No IP-based rate limiting on `/api/auth/**` — the token-bucket system protects the metered data API, not registration/login from brute-force or spam abuse. A separate, simpler IP-keyed limiter is a natural next addition.
- Single-region Redis/Postgres — no discussion of what horizontal scaling or Redis Cluster would require, since this is a portfolio-scale deployment.

