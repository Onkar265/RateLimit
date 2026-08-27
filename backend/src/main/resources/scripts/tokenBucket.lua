-- KEYS[1] = bucket key
-- ARGV[1] = capacity (max tokens the bucket can hold)
-- ARGV[2] = refill rate (tokens added per second)
-- ARGV[3] = now (current unix timestamp, in seconds)

local bucket = redis.call('HMGET', KEYS[1], 'tokens', 'timestamp')
local tokens = tonumber(bucket[1]) or tonumber(ARGV[1])
local last_refill = tonumber(bucket[2]) or tonumber(ARGV[3])

local elapsed = tonumber(ARGV[3]) - last_refill
local refill = elapsed * tonumber(ARGV[2])
tokens = math.min(tonumber(ARGV[1]), tokens + refill)

if tokens >= 1 then
    tokens = tokens - 1
    redis.call('HMSET', KEYS[1], 'tokens', tokens, 'timestamp', ARGV[3])
    redis.call('EXPIRE', KEYS[1], 3600)
    return 1
else
    redis.call('HMSET', KEYS[1], 'tokens', tokens, 'timestamp', ARGV[3])
    redis.call('EXPIRE', KEYS[1], 3600)
    return 0
end