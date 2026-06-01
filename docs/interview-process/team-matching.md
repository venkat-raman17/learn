# Team matching (Google / Meta style)

Team matching is the phase that happens **after** you pass the hiring committee (Google) or final loop (Meta). You have cleared the bar — the only remaining question is which team you join. This doc tells you how to run that process deliberately so you land somewhere you actually want to be.

---

## How team match works

### Google

1. Your recruiter marks you "hire" and moves the packet to the staffing team.
2. You are visible in an internal system (formerly "Hire") where hiring managers can express interest, or your recruiter seeds a shortlist of teams based on your stated preferences.
3. You hold 30–45 min "team match calls" with each manager — these are informal but consequential.
4. You pick a team, the manager confirms, HC ratifies the level/comp, offer is extended.
5. Typical window: **2–6 weeks**, sometimes longer at senior/staff levels where fewer teams have headcount.

### Meta

1. HC approval triggers an e-mail asking for your top three area preferences (Ads, Infra, Reality Labs, etc.).
2. A "team sourcer" or the recruiter arranges calls with candidate teams inside those areas.
3. Teams have to opt **in** to you; you have to opt **in** to them. Mutual selection.
4. Typical window: **1–3 weeks** after HC; the company has a hard offer-expiry clock once comp is sent.

### What both companies share

- You are **not** obligated to take the first team that expresses interest.
- You can have parallel conversations with multiple teams simultaneously.
- At staff level, teams are often more selective and calls run deeper into technical vision.
- Your recruiter is your ally here — push them to surface multiple options.

---

## The five dimensions to evaluate

Use this table as a scorecard. Rate each 1–5 during and after every call.

| Dimension | What to probe | Red-flag signal |
|---|---|---|
| **Manager** | Their eng background, how they describe failure, how often they skip 1:1s | "I trust the team to self-manage" (absentee) or micromanages every PR |
| **Roadmap** | Concrete H1/H2 bets, not "we're exploring opportunities" | No shipped product in 12 months; strategy pivots every quarter |
| **Tech** | Stack modernity, on-call load, test coverage culture, incident frequency | "We'll migrate off the monolith... eventually" for the 4th year running |
| **Growth** | Path to principal/distinguished, recent promo rate, whether IC6/L7 have an actual scope | Staff engineers doing L5 work because "we need the bandwidth" |
| **Work-life** | On-call rotation size, expected response SLA, meeting culture | "We do incident reviews every Monday because we have incidents every week" |

---

## Questions to ask team leads

Ask these verbatim or adapt them. A good manager will answer concretely and confidently. Vague or defensive answers are data.

### Manager signal

- "Tell me about the last time a project you sponsored got cancelled or significantly scoped down. What happened?"
- "How do you run career conversations — what's your cadence and what triggers a promo packet?"
- "What would make someone not succeed on this team in their first six months?"
- "Who was the last staff engineer you worked with, and what were they actually owning?"

### Roadmap signal

- "What's the team's primary bet for the next 12 months, and what would cause you to abandon it?"
- "What problem does this team uniquely solve that no other team at the company does?"
- "What was the team's biggest delivery in the last two quarters? What slipped?"

### Tech signal

- "What does your on-call rotation look like — how many people, what's the average page volume per week?"
- "Walk me through your last major incident. What was the blast radius and how long to resolution?"
- "What's the oldest piece of meaningful tech debt on the team, and what's the plan?"
- "What does a typical code review look like — who reviews, what's the SLA, how are disagreements resolved?"

### Growth signal

- "How many staff or principal engineers are on the team right now, and what's the ratio to senior?"
- "What's the scope I'd own from day one, and what would it look like in 18 months if things go well?"
- "Has anyone on the team been promoted in the last year? What did that process look like?"

### Work-life signal

- "What does a normal week look like for a staff engineer here — what meetings are non-negotiable?"
- "How do you handle urgent asks from partner teams or leadership — how often do they arrive, and who absorbs them?"
- "What does 'heads-down time' look like — do engineers realistically get 3–4 hours of uninterrupted focus per day?"

---

## Signals of a good team

- Manager can describe your specific scope without reading off a job req.
- Roadmap has a named outcome with a date, not just a direction.
- On-call rotation has >= 6 people and < 5 pages/week average.
- Recent staff-level promos exist and the manager can name what they were doing.
- Team shipped something in the last 90 days. Manager mentions the user or business impact unprompted.
- Manager proactively mentions what they are **not** doing to stay focused.
- Engineers on the team talk to you directly during the call — not just the manager narrating about them.

---

## Signals of a bad team

- Manager says "we wear a lot of hats" without being able to say what the primary hat is.
- Roadmap is dependent on another team's decision that has not been made yet.
- On-call is 3–4 people, escalations go to the same two engineers every time.
- "We just need someone to come in and own the tech strategy" — undefined scope sold as opportunity.
- The team has no staff engineers, or the existing ones are doing cross-org coordination with no eng depth.
- Pivoted direction more than once in the past 12 months without a clear external forcing function.
- Manager talks about headcount pressure constantly ("we've been stretched thin").

---

## Timeline and your leverage

You have more negotiating power than you think in team match.

| Situation | What you can do |
|---|---|
| Only one team has shown interest | Ask recruiter to actively surface 2–3 more before you decide. Be specific: "I want to see at least one infra team and one product-infra team." |
| Team you like has no headcount yet | Ask for a written commitment of next-cycle headcount. Google sometimes does provisional matches. |
| Offer expiry clock is ticking (Meta) | Ask for a 1-week extension. Most recruiters can grant one without escalation. |
| Two teams both want you | Tell both. A staff candidate with competing team interest is normal and expected — do not hide it. |
| Team seems right but manager is new | Ask to speak with the skip-level and one senior IC on the team before deciding. |

**Typical outer bounds:** Google will let you hold a "hire" status for up to ~3 months before it expires. Meta's offer clock is usually 2 weeks from when comp is sent. Use the time — a bad team match at staff is much harder to recover from than at senior, because your scope and trajectory are more tightly coupled to the specific organization.

---

## Preparing your preference statement

Before your first team match call, write a 3–5 sentence statement your recruiter can share:

> "I am most interested in distributed systems or developer infrastructure problems at scale. I want to own a technical domain end-to-end, not just contribute to one. I am looking for a team where staff-level scope means driving multi-quarter technical strategy, not just being the most senior IC on a feature squad. I am open to both product-infra and platform teams. I am not looking for greenfield ML/AI work at this stage."

Be specific about what you do **not** want. Recruiters use your negatives to filter quickly.

---

## Post-call scoring ritual

Within one hour of each team match call, fill in this template:

```
Team: [name]
Manager: [name], [years at company]
Date: [date]

Manager (1-5): __  Notes: 
Roadmap (1-5): __  Notes: 
Tech (1-5):    __  Notes: 
Growth (1-5):  __  Notes: 
Work-life (1-5): __ Notes: 

Total: __ / 25

One thing that excited me:
One thing that concerned me:
Follow-up question before deciding:
```

A score below 16/25 is a pass unless one dimension is a 1 (automatic disqualifier). Do not let recruiter urgency override a low score.

---

## Final decision checklist

Before you say yes to a team:

- [ ] I have spoken to the manager for at least 45 minutes total (one call is not enough).
- [ ] I have spoken to at least one staff or principal IC on the team, not arranged by the manager.
- [ ] I know what I will own in the first 90 days by name, not just category.
- [ ] I have read (or been shown) the team's public or internal roadmap doc.
- [ ] I have asked about on-call load and gotten a number, not a narrative.
- [ ] I am not choosing this team primarily because it responded first or the recruiter is pushing it.
- [ ] I could explain to a peer why this team is the right scope for a staff engineer and they would agree.
