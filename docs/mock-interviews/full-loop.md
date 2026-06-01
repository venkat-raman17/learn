# Full-loop simulation (a virtual onsite)

A one-day dress rehearsal that condenses a real 5-6 hour onsite into a single sitting. Run this once — ideally in the final week before the real thing — so nothing on the day feels unfamiliar.

---

## Why bother with a full loop

Individual round drills build skills. A full loop builds stamina and transitions. The hidden difficulty of a real onsite is not any single round; it is doing round five well when you are already tired and slightly rattled from round three. One complete simulation surfaces that gap early enough to fix it.

Realistic timeline before a real onsite is 5-6 weeks. See [../../STUDY-PLAN.md](../../STUDY-PLAN.md) for the week-by-week breakdown and when to schedule this simulation.

---

## Sample schedule (5 h 30 min total)

| Block | Duration | Activity |
|---|---|---|
| 0:00 | 10 min | Recruiter recap warm-up |
| 0:10 | 45 min | Coding round 1 |
| 0:55 | 10 min | Break + reset |
| 1:05 | 45 min | Coding round 2 |
| 1:50 | 20 min | Break + snack + move |
| 2:10 | 50 min | System design (HLD) |
| 3:00 | 10 min | Break + reset |
| 3:10 | 45 min | Low-level design (LLD) |
| 3:55 | 15 min | Break + move |
| 4:10 | 40 min | Behavioral |
| 4:50 | 40 min | Debrief (fill the template below) |

Total sim time: ~4 h 50 min active, ~5 h 30 min elapsed.

---

## Round-by-round setup

### Recruiter recap (10 min)

This is not a real round. Use it to load the context you would normally hear from the recruiter before you enter the building: the team, the stack, what they said they care about. Read your notes from [../interview-process/recruiter-screen.md](../interview-process/recruiter-screen.md). Then say out loud in 2-3 sentences why you are excited about the role. This primes your narrative for behavioral and keeps motivation high going into coding.

### Coding rounds (45 min each)

- Pull a problem you have not seen. Use a blind draw from your weak-pattern list. Pattern notes live in [../dsa/patterns/](../dsa/patterns/).
- Set a 45-minute timer. No pausing.
- Talk through your thinking as if someone is listening. Narrate the brute force, then the optimization. Silence is a red flag in real rounds.
- Aim: working solution with at least 5 minutes left for test cases and complexity analysis.
- After each round, resist the urge to look up the editorial immediately. Write your own post-mortem first (see debrief template).

Common failure modes to watch for:
- Jumping to code without clarifying constraints
- Going silent when stuck
- Skipping edge cases until the last minute
- Complexity analysis that is vague ("it's kind of O(n)")

See [../rubrics.md](../rubrics.md) for the scoring signals interviewers use.

### System design / HLD (50 min)

- Pick one case study from [../hld/case-studies/](../hld/case-studies/) you have not fully memorized.
- Use the framework in [../hld/framework.md](../hld/framework.md): requirements, capacity estimation, high-level diagram, deep-dives.
- Draw on paper or a whiteboard. Do not skip the diagram.
- Timebox each section explicitly: 5 min requirements, 5 min estimation, 15 min diagram, 25 min deep-dives.
- After the timer, check core concepts against [../hld/core-concepts.md](../hld/core-concepts.md).

Staff-level signals to hit: you must drive the conversation, propose trade-offs proactively, and know when to go deep vs. when to stay high. An interviewer who has to drag you through the structure is a negative signal.

### Low-level design / LLD (45 min)

- Pick a prompt not in your solved list (parking lot, library system, rate limiter, etc.).
- Use the framework in [../lld/framework.md](../lld/framework.md).
- Apply SOLID principles from [../lld/solid.md](../lld/solid.md) consciously, not just by habit.
- Check design patterns from [../lld/design-patterns.md](../lld/design-patterns.md) before starting — know which patterns are likely relevant so you can reach for them.
- Write real code or detailed pseudocode. Vague boxes on a diagram are not enough.

### Behavioral (40 min)

- Pick 4 questions from [../behavioral/question-banks.md](../behavioral/question-banks.md): one leadership, one conflict, one failure, one ambiguity.
- Answer each out loud using a story from [../behavioral/story-bank.md](../behavioral/story-bank.md). Aim for 2.5-3 minutes per answer.
- Record yourself on your phone. Play back one answer. The thing that sounds fine in your head often sounds rambling on playback.
- Check your answers against the staff-level signals in [../behavioral/staff-signals.md](../behavioral/staff-signals.md). The bar is: did you talk about system-level impact, not just personal cleverness?

---

## Energy and time management

**Between rounds:** 10 minutes is enough to fully reset if you use it intentionally. Step away from the screen. Drink water. Take 10 deep breaths. Do not review what just happened — that processing belongs in the debrief. Your job during breaks is to arrive at the next round with a clean slate.

**The long break at 1:50:** This is the hinge point of the day. Eat something real. Walk for 10 minutes if possible. The system design round requires sustained attention; going in with a blood-sugar dip kills it.

**If you are running out of time in a round:** Stop coding, narrate what you would do next, and show test cases. Partial credit with clear communication beats frantic silence with a broken solution.

**Pacing within rounds:**

| Round type | First 10 min | Middle | Final 5 min |
|---|---|---|---|
| Coding | Clarify, brute-force sketch | Code optimal | Test cases, complexity |
| System design | Requirements + estimation | Diagram + deep-dives | Trade-off summary |
| LLD | Use cases, interface | Class structure, code | Extension points |
| Behavioral | Set context | Narrative | Outcome + learning |

---

## Communication across all rounds

Communication is not a soft skill in this loop. It is the primary signal for Staff level. In every round:

- Say what you are about to do before you do it.
- Say what you are uncertain about before you guess.
- Say the trade-off before you make the choice.
- Never go silent for more than 30 seconds without narrating your thinking.

Interviewers at Staff level are specifically listening for whether you can think out loud at scale. A candidate who codes in silence and produces a correct answer reads as Senior, not Staff.

---

## Debrief template

Fill this within 30 minutes of finishing the simulation while it is fresh. Be blunt with yourself.

```
Date:
Target company / role:

CODING ROUND 1
  Problem:
  Time to working solution (min):
  Optimal on first try? Y/N
  Communication score (1-5, where 5 = continuous narration):
  What I'd do differently:

CODING ROUND 2
  Problem:
  Time to working solution (min):
  Optimal on first try? Y/N
  Communication score (1-5):
  What I'd do differently:

SYSTEM DESIGN
  Problem:
  Did I drive structure without prompting? Y/N
  Trade-offs stated proactively? Y/N
  Weakest section (requirements / estimation / diagram / deep-dive):
  What I'd do differently:

LLD
  Problem:
  SOLID principles applied explicitly? Y/N
  Code quality (1-5):
  What I'd do differently:

BEHAVIORAL
  Questions answered:
  Answers felt staff-level? Y/N
  Story I want to polish before the real thing:

OVERALL
  Energy level at round 4 vs round 1 (1-5 each):
  Biggest gap to close before real onsite:
  One thing I will drill this week specifically:
```

---

## Checklist before you start

- [ ] Block 6 hours on your calendar with no interruptions
- [ ] Phone on do-not-disturb
- [ ] Problems pre-selected but not opened yet (blind draw at round time)
- [ ] Water and snacks staged for the long break
- [ ] Timer app ready (or use a phone timer)
- [ ] Paper and pen for HLD diagram
- [ ] Debrief template open and ready

---

## One-line summary

A real onsite is 5-6 hours of sustained performance under social pressure. One full simulation in a single day — even without the social pressure — is worth more than a week of isolated drills because it trains the thing that drills cannot: showing up well in round five.
