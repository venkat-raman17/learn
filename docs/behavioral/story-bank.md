# Building your STARL story bank

A Staff interview is an altitude test. The panel is not checking whether you can code —
they already believe you can. They are checking whether you operate at the right level:
do you shape technical direction, move org-spanning problems, and grow the engineers
around you? Your story bank is the primary evidence you present.

This document helps you build 3-4 high-water-mark stories that you can recombine across
30+ distinct behavioral questions.

---

## The STARL structure

| Letter | What to cover | Common failure mode |
|--------|---------------|---------------------|
| **S** — Situation | Business/org context in 2-3 sentences. What was broken, risky, or ambiguous? | Too much history; interviewer loses the thread |
| **T** — Task | Your specific mandate. Why you specifically? What was the scope? | Sounds like the team's task, not yours |
| **A** — Action | 3-5 decisions YOU made. Foreground trade-offs, stakeholders influenced, alternatives rejected. | Lists activities instead of decisions; solo-coding story |
| **R** — Result | Quantified outcome, business impact, timeline. "We improved latency" is not a result. | Vague or hedged. Own the number. |
| **L** — Learning | What you would do differently and what it changed in how you operate since. | Skipped entirely; makes you sound overconfident |

### Altitude rules

Staff answers live at the **system + people** layer, not the implementation layer.

- Bad: "I rewrote the auth service in Rust and cut p99 by 40 ms."
- Good: "I owned the decision to decouple auth from the monolith, aligned three teams on
  a phased migration plan, and established the latency SLO that anchored the project.
  The outcome was a 40 ms p99 improvement and a template other teams reused."

The implementation detail is evidence, not the headline.

---

## How to choose your 3-4 stories

You need coverage across the themes below. Map each story you draft against this table.
Aim for no theme covered by fewer than two stories (so you have a backup if one story
falls flat or the interviewer already heard it).

| Theme | Why Staff panels ask about it |
|-------|-------------------------------|
| Technical direction / strategy | Can you set it, not just execute it? |
| Cross-team influence without authority | Staff scope requires this daily |
| Handling ambiguity / incomplete data | Senior ICs get clear specs; Staff shapes unclear ones |
| Navigating conflict (peer, stakeholder, manager) | You will block or be blocked constantly |
| Growing / sponsoring others | Multiplier vs. individual contributor |
| Trade-off decisions under pressure | Speed vs. correctness; build vs. buy; short vs. long term |
| Failure / recovery / post-mortem | Credibility and self-awareness |

A story that covers 4+ themes is a **high-water-mark** story. You want 3-4 of these.

### Selection heuristics

1. **Recent**: Ideally within the last 24 months (2024-2026). Older stories need explicit
   framing: "This was in 2021, but the pattern is identical to what I face today."
2. **Scope asymmetry**: Your impact exceeded your formal authority. You influenced N teams
   where N >= 2, or the decision outlasted your involvement.
3. **Stakes were real**: Production traffic, customer SLA, budget, org structure, or
   headcount was at risk. Toy projects and internal hackathons rarely qualify.
4. **You made a controversial or hard call**: Safe consensus stories do not distinguish
   Staff candidates. Find the moment you pushed back, reversed direction, or absorbed
   political cost.

---

## One fully worked example (realistic Staff scenario, 2025)

**Context**: Senior/Staff at a mid-size fintech, 2025. You are the technical lead for a
payments platform team. The company is mid-Series C, growing 3x YoY, and the legacy
settlement pipeline is becoming a production risk.

---

### Story: Decommissioning the legacy settlement pipeline

**Situation (3 sentences max)**

> "Our settlement pipeline was a 6-year-old Ruby monolith processing $2 B/month in
> transactions. By Q2 2025 it had a 99.1% success rate — which sounds fine until you
> realize 0.9% failure on $2 B is $18 M/month in manual reconciliation work and customer
> escalations. Three prior migration attempts had been abandoned because no one owned the
> cross-team coordination."

**Task**

> "My manager asked me to assess whether we could get to 99.95% by EOY. I self-scoped
> that into: own the architectural decision, secure alignment from Payments, Risk, and
> Finance Engineering, and drive execution without taking every team's roadmap hostage."

**Action (decisions, not activities)**

> "First, I ran a failure-mode audit across 18 months of incident data — solo, two days —
> because I needed the numbers before any stakeholder conversation. That gave me the data
> to reject the leading proposal (rewrite in Go) in favour of a strangler-fig migration
> that let us ship incrementally.
>
> Second, I wrote a two-pager that framed the risk in business terms ($18 M/month exposure)
> rather than technical terms. I walked it through the VP of Finance and the Head of Risk
> individually before the group review, so no one was surprised in the room. Both became
> sponsors.
>
> Third, I made the call to pause the feature roadmap for Payments Engineering for one
> quarter — a decision that required sign-off from two PMs and my manager's manager. I
> absorbed the pushback by committing to a written deprecation timeline with weekly
> status updates.
>
> Fourth, I designed the migration contracts (event schema, idempotency guarantees) and
> ran two days of design reviews across four teams rather than mandating my design.
> Three concrete changes came out of those reviews that I incorporated."

**Result**

> "We reached 99.94% success rate by November 2025, two months ahead of the original
> schedule. Reconciliation overhead dropped from 14 FTE-hours/week to under 2. The
> strangler-fig pattern and the event schema we defined became the internal standard
> adopted by two other platform teams. The VP of Finance cited it in the Q3 board update."

**Learning**

> "I underestimated how much the Finance team cared about auditability vs. latency — I
> assumed latency was the primary constraint, and I was wrong. I now treat constraint
> discovery as an explicit step before any design work, not something I infer. I also
> learned to over-communicate timelines to sponsors even when nothing is wrong; silence
> reads as risk."

---

**Themes this story covers**

| Theme | Evidence in the story |
|-------|-----------------------|
| Technical direction / strategy | Rejected rewrite, chose strangler-fig, owned architectural decision |
| Cross-team influence without authority | Aligned Payments, Risk, Finance, two PMs |
| Handling ambiguity | Scoped an open-ended assessment into a concrete plan |
| Navigating conflict | Paused feature roadmap; absorbed PM pushback |
| Trade-off decisions | Speed (rewrite) vs. safety (incremental); feature work vs. platform |
| Growing / sponsoring others | Design reviews that incorporated team feedback (implicit multiplier) |
| Failure / recovery | Learned from wrong constraint assumption |

---

## Blank fill-in template

Copy this for each story you draft. Fill in bullets, then convert to spoken prose.

```
### Story: [Working title — internal use only]

SITUATION
- Company/team context in one sentence:
- What was broken, at risk, or ambiguous:
- Why it mattered (business stakes, $ or %, or org risk):

TASK
- My explicit or self-scoped mandate:
- Why me specifically (role, leverage, context):
- Scope: how many teams / systems / people involved:

ACTION — decisions I made (aim for 3-5)
1. Decision / trade-off:
   - What I rejected and why:
   - Who I had to convince:
2.
3.
4.
5.

RESULT
- Primary metric (quantified):
- Secondary outcomes (adoption, team health, process change):
- Timeline vs. plan:

LEARNING
- What I got wrong or would change:
- How this changed how I operate today:
```

---

## Story-to-theme mapping table

Fill this in once you have drafted your 3-4 stories. Use it before each interview round
to pick the right story for a given question.

| Story (short name) | Tech direction | Cross-team influence | Ambiguity | Conflict | Growing others | Trade-off | Failure/recovery |
|--------------------|:--------------:|:--------------------:|:---------:|:--------:|:--------------:|:---------:|:----------------:|
| Settlement pipeline | Y | Y | Y | Y | partial | Y | Y |
| _Your story 2_ | | | | | | | |
| _Your story 3_ | | | | | | | |
| _Your story 4_ | | | | | | | |

A `Y` means the story has strong evidence for that theme. `partial` means you can stretch
it if needed but it is not the strongest angle.

---

## Example question-to-story routing

| Question | Best story to pull | Angle to foreground |
|----------|--------------------|---------------------|
| "Tell me about a time you influenced without authority." | Highest cross-team story | Stakeholder alignment moves, not technical merit |
| "Describe a technical decision you made under incomplete information." | Ambiguity-heavy story | The moment you decided to proceed and what you risked |
| "Tell me about a failure." | Story with clearest learning | Own it fully; do not hedge |
| "How do you set technical direction?" | Strategy story | The process: data gathering, stakeholder input, written artifact |
| "Tell me about a time you grew someone." | Any story with sponsorship thread | Pull out the individual; name what changed for them |
| "When did you push back on leadership?" | Conflict story | Name what you said, in what forum, and what happened |

---

## Delivery tips (the non-fluff version)

- **Time-box Situation + Task to 60 seconds combined.** Interviewers at Staff level cut you
  off if you over-narrate context. Practice out loud with a timer.
- **Say "I decided" not "we decided"** unless you are explicitly describing a collaborative
  decision process. Interviewers are calibrating your scope of ownership.
- **Name the dissenter.** "The Head of Risk initially disagreed because..." is ten times
  more credible than "there was some pushback."
- **Land the Result before the Learning.** A common error is burying the result inside the
  learning paragraph. State the number, then reflect.
- **Prepare a "compressed" version** (90 seconds) and an "expanded" version (4 minutes) of
  each story. Use compressed when the interviewer is moving fast; expand when they ask
  follow-up questions.

---

## Refresh schedule

Stories go stale. Review this bank:
- 2 weeks before each interview loop.
- Any time a significant project closes (add the story while details are fresh).
- After each interview debrief — note which stories landed and which fell flat, and update
  the mapping table accordingly.
