# Leveling — Senior vs Staff vs Principal

> Audience: a 10-year senior engineer (E5) preparing for Staff (E6).
> Goal: understand what the committee is actually grading, then present your work at the right altitude.

---

## Why This Matters in 2026

After two years of aggressive re-leveling and headcount freezes, companies are keeping bar deliberately high for Staff. Candidates who were auto-promoted in 2021 are being re-evaluated. The practical effect: interviewers are skeptical of "Staff" scope claims from candidates who spent the last two years heads-down on team-level work. You must prove scope, not just title.

---

## The Core Separating Factor: Scope of Impact

| Dimension | E5 Senior | E6 Staff | E7 Principal |
|-----------|-----------|----------|--------------|
| Scope | Team (6-12 engineers) | Org / multi-team (50-200 eng) | Company / division (200+ eng) |
| Owns | Features, services | Systems, platform decisions | Technical strategy |
| Ambiguity handled | Requirements are roughly defined | Problem space is defined, solution is not | Neither is defined |
| Primary influence | Code review, 1:1s | Design docs, cross-team alignment | Roadmap, hiring bar, external credibility |
| Measures success | Team ships | Org ships faster / with less waste | Company bets pay off |
| Typical time horizon | 1-3 months | 6-18 months | 1-3 years |
| Who is the "customer" | Engineering manager, PMs | Directors, other Staff engineers | VPs, external partners |

The single hardest thing for most seniors making the Staff jump: accepting that writing code is no longer the primary proof of impact. Code you write is evidence; what you *enabled others to build* is the impact.

---

## What "Ambiguity" Actually Means at Each Level

- **E5**: "Build a rate-limiter for the API." You clarify requirements, make design choices, ship it.
- **E6**: "Our infrastructure costs are 3x competitors'. Fix it." No spec exists. You scope the problem, align stakeholders, sequence work across teams, and drive the outcome over 12 months.
- **E7**: "We're losing enterprise deals because of compliance gaps." You diagnose which gaps matter, propose a multi-year roadmap, build the team/org structure to execute it, and create the external narrative.

If every project you've led had a well-defined spec handed to you, that is an E5 signal — even if the code was complex.

---

## The Four Staff Archetypes

From [StaffEng.com](https://staffeng.com/guides/staff-archetypes) — know which one you are:

| Archetype | What They Do | Where They Spend Time |
|-----------|-------------|----------------------|
| **Tech Lead** | Sets technical direction for an eng org, often embedded with a product team | Design reviews, architectural decisions, unblocking engineers |
| **Architect** | Owns the technical strategy for a domain (infra, data, security) | Cross-team design, standards, migration plans |
| **Solver** | Dropped into the org's hardest problems; moves on after resolution | Deep investigation, prototype, write-up, teach |
| **Right Hand** | Extends a senior leader's (VP+) capacity on technical judgment | Staff meetings, strategy docs, organizational credibility |

You do not need to fit perfectly into one box, but you must be able to explain which archetype you lean toward and why. Interviewers use this to calibrate fit.

**Script to use when asked "what kind of Staff engineer are you?":**
> "I lean toward the Tech Lead archetype. For the past two years I've been the technical anchor for our platform org — I set the migration strategy, drove cross-team alignment on the service mesh rollout, and mentored three seniors through their first systems-design ownership moments. I'm less interested in being the singular solver parachuted in; I want to build the culture that doesn't need a single hero."

---

## Framing Your "Staff Project"

Every strong Staff candidate has at least one project that reads at E6 scope. If you don't have one, you need to reframe existing work or deliberately create one before the interview loop.

### Checklist for a qualifying Staff project

- [ ] Affected multiple teams (not just your own)
- [ ] You identified the problem — it was not handed to you
- [ ] You drove alignment among people who did not report to you
- [ ] The project ran for 6+ months
- [ ] You can quantify the outcome (latency, cost, eng-hours saved, revenue, reliability)
- [ ] You made at least one significant technical decision with real tradeoffs

### The SBAR framing (Situation / Background / Action / Result) for Staff narratives

Most people tell the *what* and skip the *why you specifically*. Interviewers at Staff level want to hear how you diagnosed ambiguity and built alignment, not just what you built.

**Weak framing (E5 signal):**
> "I led the migration of our monolith to microservices. I designed the service interfaces, coordinated with three teams, and we cut P99 latency by 40%."

**Strong framing (E6 signal):**
> "In early 2024 our deploy cycle was blocking three product teams from shipping independently. No one owned the problem because it lived at the intersection of platform, product, and DevEx. I wrote a two-pager scoping the blast radius, convinced the VP of Engineering it was a blocker for our Q3 OKR, and designed an incremental strangler-fig migration that let us ship value every sprint. I ran the cross-team design review, wrote the runbooks, and coached two seniors to lead individual service extractions. Eighteen months later deploy frequency went from weekly to daily per team, and we avoided the rewrite that two of the teams had been lobbying for."

The difference: you named the ambiguity, showed you owned finding the problem, demonstrated influence without authority, and quantified organizational impact.

---

## Presenting Scope Evidence: The "Sphere of Influence" Grid

Draw this mentally before every behavioral interview and reference it explicitly:

| Who was affected | How you influenced them | Outcome |
|-----------------|------------------------|---------|
| Your own team | Code, reviews, pairing | Shipped feature X |
| Adjacent team (payments) | Design doc, async alignment | Adopted shared auth library |
| Org (3 teams) | RFC, tech talk, working group | Standardized on gRPC; deprecated 4 custom RPC layers |
| Company (all backend) | Proposal to VPE, hiring bar change | New grad interview rubric now includes systems design |

At E6 you need credible rows in the "Org" tier. At E7 you need the "Company" row.

---

## The 2026 Downleveling Trend — How to Avoid It

### Why it happens

Committees downlevel when:
1. Scope claims cannot be verified — you say "org-wide" but can only name your team.
2. Impact reads as execution, not problem-finding — you built what you were told to build very well.
3. Technical depth is thin — you can describe the system but cannot defend design tradeoffs under pressure.
4. No evidence of mentorship multiplying output — you are a solo hero, not a force multiplier.
5. You center the narrative on yourself rather than the org outcome.

### Concrete mitigations

**Before the interview:**
- Write out three projects with the E6 framing above. Have a colleague play skeptic.
- Prepare a "scope ladder" for each project: team -> org -> company. Know where your project sits and say it explicitly.
- Be ready to name *who pushed back* on your design choices and how you resolved it. Influence without authority is only believable if you name the friction.

**During the interview:**
- When asked about a project, say the scope explicitly: "This crossed three teams and two org boundaries."
- When asked about impact, give numbers *and* explain what would have happened without your involvement.
- When asked about a decision you made, describe a specific alternative you rejected and why, naming the engineer or team that advocated for it.

**Script to pre-empt the downlevel:**
> "I want to give you context on scope before I describe the work. This project touched the platform team, the data team, and the ML infra team — all separate reporting chains. My manager was not involved in day-to-day execution after the first month; I was running weekly cross-team syncs and escalating blockers directly to the relevant directors."

---

## Influence Without Authority — The Staff Superpower

At Staff level you rarely have direct reports. Your influence levers are:

1. **Written artifacts** — Design docs, RFCs, postmortems that shift how others think.
2. **Institutional credibility** — Being the person others cite when defending a decision.
3. **Strategic storytelling** — Framing technical work in terms leadership cares about (reliability = revenue, tech debt = hiring risk).
4. **Meeting the other person's goals** — Before you can align someone, know what they are measured on.
5. **Saying no with data** — "Here is the failure mode and here is the probability" is more credible than "this feels wrong."

**Script for "Tell me about a time you influenced without authority":**
> "The mobile team was planning to build their own notification service, which would have fragmented our on-call surface and duplicated our infrastructure. I couldn't tell them not to — they had their own roadmap and director. Instead, I ran a lightweight spike that demonstrated our existing service could meet their latency requirements with two specific changes. I wrote a doc quantifying the operational cost of fragmentation and shared it with both directors. I also offered to personally own the two changes they needed. They adopted the shared service. That doc is now cited in our platform principles."

---

## Quick Reference: "What Level Is This Story?"

| Signal in the story | Reads as |
|--------------------|----|
| "My team shipped..." | E4/E5 |
| "I designed the system and the team shipped..." | E5 |
| "I identified the problem, aligned three teams, and we shipped..." | E6 |
| "I defined the technical direction for the org and we restructured the roadmap..." | E6/E7 |
| "I changed how the company thinks about X..." | E7 |

---

## Further Reading

- [StaffEng.com](https://staffeng.com) — Will Larson's collection of Staff/Principal engineering stories and guides. Read at least 5 story profiles from people at companies similar to your target.
- [staffeng.com/guides/what-do-staff-engineers-actually-do](https://staffeng.com/guides/what-do-staff-engineers-actually-do) — concrete breakdown of time allocation.
- [staffeng.com/guides/staff-archetypes](https://staffeng.com/guides/staff-archetypes) — the four archetype breakdown referenced above.

---

*Last updated: 2026-06-01*
