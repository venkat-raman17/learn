# Behavioral mock

Self-run, 45-minute solo practice kit. No interviewer required. Use this alongside
[../rubrics.md](../rubrics.md) (scoring) and [../../STUDY-PLAN.md](../../STUDY-PLAN.md)
(weekly schedule). Story inventory lives in [../behavioral/story-bank.md](../behavioral/story-bank.md).

---

## How to run a session (45 min)

| Phase | Time | What to do |
|-------|------|-----------|
| Warm-up | 0–3 min | Read today's question set (pick 4–5). Mentally tag which stories apply. |
| Answer block | 3–33 min | Record yourself on phone/laptop. One question at a time, 2–3 min per answer. No pausing the recording mid-answer. |
| Follow-up drill | 33–40 min | For each answer, speak the follow-ups listed below out loud. Keep camera on. |
| Self-score | 40–45 min | Replay the recording. Score each answer with the rubric in [../rubrics.md](../rubrics.md). Write the story tag used. |

### Recording rules

- Use voice memos, Loom, or `ffmpeg -f avfoundation` — format does not matter.
- Watch it back at 1.25× speed. Listen for filler words, hedging ("kind of", "sort of"), and missing impact statements.
- Keep a running log: `date | question | story tag | score | one fix`.

### STARL in 2–3 minutes

```
Situation  – 15 sec  context, stakes, your role
Task       – 10 sec  what you were responsible for (not the team)
Action     – 60 sec  what YOU did, step by step, with tradeoffs named
Result     – 20 sec  quantified outcome (latency, revenue, adoption, time saved)
Learning   – 15 sec  what changed in how you work or how the org works
```

If you hit 3 min and are still in Action, cut to Result immediately. Interviewers
disengage after 3 min 30 sec.

### Follow-up drills (run after every answer)

Speak each of these out loud after your main answer:

1. "What would you do differently if you had to do it again?"
2. "How did the key stakeholders react, and how did you handle pushback?"
3. "What was the hardest part, and why did you choose that approach over the alternatives?"
4. "How did this change the team or the system six months later?"
5. "What would have happened if you had done nothing?"

---

## Question bank (~20 questions, by Staff signal)

Reference the four signals from [../behavioral/staff-signals.md](../behavioral/staff-signals.md).

### Signal 1 — Technical direction

> Evaluates: setting scope, architectural trade-offs, long-horizon thinking.

1. Tell me about a multi-quarter technical initiative you owned from definition to delivery. How did you decide what was in scope?
2. Describe a time you killed a technically sound project because the org wasn't ready. How did you make that call?
3. You have two engineers who disagree on a fundamental architectural choice. Both arguments have merit. What do you do?
4. Walk me through a design decision you made that you later regretted. What were the early signals you missed?
5. How do you decide when to pay down tech debt versus ship new features?

### Signal 2 — Influence without authority

> Evaluates: cross-functional persuasion, building coalitions, written and verbal communication.

6. Give me an example of convincing a senior leader to change direction on something they felt strongly about.
7. Describe a time when you had to align two teams with conflicting priorities. What levers did you use?
8. Tell me about a time your written proposal (RFC, design doc, memo) changed the outcome. What made it effective?
9. You need buy-in from a team that doesn't report to you and has no incentive to help. Walk me through your approach.
10. How do you keep stakeholders aligned over a six-month project when requirements shift?

### Signal 3 — Ambiguity and judgment

> Evaluates: scoping vague problems, deciding with incomplete data, escalation judgment.

11. Describe the most ambiguous problem you've ever been handed. How did you figure out what "done" looked like?
12. Tell me about a time you had to ship a decision with 60% of the information you wanted. What happened?
13. A production incident is happening, root cause is unclear, and two different teams are pointing at each other. You are not on-call. What do you do?
14. You've been asked to evaluate whether the company should build, buy, or partner for a capability. Where do you start?
15. Walk me through a time you escalated — and a time you chose not to. What was your decision criteria?

### Signal 4 — Multiplier effect

> Evaluates: raising the floor of the team, sponsorship, feedback culture, onboarding.

16. Tell me about an engineer you significantly leveled up. What did you actually do, and how did you know it worked?
17. Describe a time you changed an engineering practice or process that outlasted your involvement in it.
18. Give me an example of public, specific feedback you gave to a peer that was hard to hear. How did it land?
19. How do you maintain team velocity and morale during a high-pressure delivery crunch?

### Classic / curveball questions

20. What is your biggest professional failure? (Probe: not a team failure — yours specifically.)
21. Tell me about a serious conflict with a senior engineer or engineering manager. Who was right, and how did it resolve?
22. Describe an unpopular decision you made that you still believe was correct.
23. You just joined a new team. In 90 days, how do you figure out what to work on?

---

## Self-scoring rubric snapshot

Full rubric: [../rubrics.md](../rubrics.md). Quick field reference:

| Score | Behavioral bar |
|-------|---------------|
| 4 — Exceeds | Org-level impact, names tradeoffs, shows learning, crisp STARL under 3 min |
| 3 — Meets | Team-level impact, clear ownership, result quantified, minor rambling |
| 2 — Developing | Vague ownership ("we"), result absent or anecdotal, answer > 4 min |
| 1 — Miss | No concrete action, theoretical framing, no result, no learning |

Target: 4 × score-4 answers and no score-1 answers before your loop interview.

---

## Story-to-question mapping worksheet

After each session, fill one row per question practiced. Use story tags from
[../behavioral/story-bank.md](../behavioral/story-bank.md).

| Question # | Story tag | Score | Gap / fix for next session |
|------------|-----------|-------|---------------------------|
| Q1 | | | |
| Q2 | | | |
| Q3 | | | |
| Q4 | | | |
| Q5 | | | |

Rotate stories across sessions. If the same story appears for more than two questions
in one session, you are over-relying on it — surface a new story.

---

## Common failure modes to watch on playback

- **The team narration trap** — "We decided..." with no I-statement in the action block. Fix: restate the question to yourself as "what did *you* do?"
- **Result-free endings** — closing with "it went well" or "the team was happy." Fix: always prepare a number before the session (latency reduction %, incident rate, adoption curve, time saved).
- **Over-hedging on conflict questions** — softening the conflict until it disappears. Interviewers need to see that you can hold a position.
- **Missing the learning** — stopping at Result. The L in STARL signals Staff-level self-awareness.
- **Failing the follow-up** — if you cannot answer "what would you do differently," your story is not fully processed. Go back to [../behavioral/story-bank.md](../behavioral/story-bank.md) and add a retrospective note.

---

## Weekly practice cadence (from [../../STUDY-PLAN.md](../../STUDY-PLAN.md))

- **2× per week** — one 45-min mock (this doc), one story refinement pass.
- **Before each mock** — review the question bank and pick a signal to focus on.
- **After each mock** — update the story-to-question table above; flag any story used
  that scores below 3 for a rewrite.
- **Day before interview** — do a compressed 20-min run: one question per signal, no
  recording, just speak aloud and time yourself.
