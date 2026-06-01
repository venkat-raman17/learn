# Interview-day logistics & the loop

A 10-year senior engineer can lose a Staff offer to pure execution failures: wrong timezone for a virtual onsite, a dead whiteboard tab, a thank-you email that never landed. This doc is about removing those variables so your technical signal is all that decides.

---

## The typical Staff loop & ~5-6 week timeline

Most FAANG/tier-1 companies run a fixed loop. Smaller companies compress it but the same phases exist.

| Week | Phase | What happens |
|------|-------|--------------|
| 0 | Recruiter screen | Confirm role level, TC range, timeline. Ask: "What does the loop look like?" |
| 1 | Hiring-manager call | 30-45 min. Culture/scope fit. Gauge the team's pain. This is two-way. |
| 1-2 | Technical phone screen(s) | 1-2 coding/systems rounds. Feedback loop is usually 2-3 business days. |
| 3 | Onsite scheduling | Recruiter books 4-6 rounds in one day (or spread over two for some companies). |
| 4 | Onsite / virtual onsite | The main event. 4-6 hours. See breakdown below. |
| 5 | Debrief & decision | Hiring committee or panel debrief. Decision in 3-7 business days post-onsite. |
| 5-6 | Offer / feedback call | Written offer or verbal, then negotiation. |

> Reference: [The Pragmatic Engineer — How Big Tech Runs Interviews](https://newsletter.pragmaticengineer.com/p/the-software-engineering-interview) covers internal hiring-committee mechanics in detail.

---

## Typical Staff onsite loop structure

Most Staff onsites have 5-6 rounds of 45-60 min each. Common composition:

| Round | Focus | What the interviewer scores |
|-------|-------|-----------------------------|
| Coding / algorithms | Usually 1-2 rounds, even at Staff | Efficiency, edge-case handling, communication |
| System design | 1-2 rounds — often the heaviest weight at Staff | Scope, trade-offs, operability, cost awareness |
| Behavioral / leadership | 1-2 rounds (sometimes merged) | Influence, conflict, ambiguity tolerance, delivery at scale |
| Cross-functional / hiring manager | 1 round | Team fit, strategic thinking, "would I work with this person" |
| Bar-raiser or skip-level | 1 round at Amazon, Meta, Google | Independent vote; looks for company-wide bar, not team fit |

> [IGotAnOffer's Staff-level system design guide](https://igotanoffer.com/blogs/tech/system-design-interview-guide) breaks down the scoring rubric interviewers actually use.

---

## Virtual-onsite setup (non-negotiable checklist)

Do this 48 hours before, not the morning of.

- **Network**: Ethernet, not Wi-Fi. Run a speed test (aim for >50 Mbps up/down). Have a hotspot ready as backup.
- **Audio**: Headset with a boom mic, or a dedicated USB mic. Built-in laptop audio introduces echo and costs you credibility.
- **Camera**: Eye-level. Background: plain wall or a clean virtual background. No windows behind you.
- **Screen sharing**: Know exactly which monitor or application you will share. Test with a friend or in a free Zoom room.
- **Whiteboard tool**: Most companies use CoderPad, Miro, or their own tool. Create an account and do one practice session on their specific tool beforehand. Know how to draw boxes, arrows, and add text in under 10 seconds.
- **Second device**: Keep a phone or tablet with the video call open as a fallback if your main machine dies.
- **Do Not Disturb**: Enable OS-level DND, silence phone, close Slack/email. Notifications during a system design round read as unprepared.
- **Water**: Have it. Interviews are 4-6 hours. Dry throat mid-explanation is a real problem.
- **Time zone**: Confirm the calendar invite timezone explicitly with the recruiter. Reschedule if you have any doubt.

---

## Thinking out loud in a virtual onsite

In-person you can read body language. Virtually you cannot, so narrating your thinking is more important, not less.

**System design — example opening script you can adapt verbatim:**

> "Before I start drawing, let me spend two minutes clarifying scope so we're solving the same problem. Are we designing for global traffic, or one region to start? What's the expected read/write ratio? And is consistency or availability the harder constraint here?"

**Coding — when you hit a wall:**

> "I see a naive O(n²) approach immediately. Let me think about whether a sorted order or a hash map buys me something before I start typing..."

**Behavioral — when a question is vague:**

> "Can I ask — are you more interested in a situation where I influenced without authority, or one where I had to push back on a decision I disagreed with?"

These phrases do two things: they buy you thinking time and they demonstrate the collaborative communication Staff engineers need on ambiguous problems.

---

## Per-round energy management

A six-hour onsite is physically demanding. Staff candidates who burn out after round three lose points in behavioral and cross-functional rounds where presence matters most.

- **Eat before, not during.** A 15-minute lunch break mid-loop is rarely enough recovery time. Eat a real meal 90 minutes before the loop starts.
- **Breaks between rounds.** Ask the recruiter if there are scheduled gaps. Even 5 minutes off-camera to stand up, breathe, and reset your mental state helps.
- **Order of difficulty.** You usually do not control round order. Accept that you might hit system design first and coding last. Treat each round as fresh — do not let a rough round three poison your round four affect.
- **The last round is not throwaway.** Bar-raisers and hiring managers often appear last. They remember your energy level. Finish strong.

---

## Follow-ups and thank-you notes

Most Staff candidates skip this. That is a mistake at companies with distributed hiring committees, where an interviewer's written note can tip a borderline decision.

**Timing:** Send within 24 hours of the interview. After 48 hours it reads as an afterthought.

**Who to thank:** Every interviewer whose name you have. Ask the recruiter for names if you did not catch them all.

**What to write (email — not LinkedIn):**

> Subject: Thank you — [Company] onsite, [date]
>
> Hi [Name],
>
> Thank you for taking the time to interview me today. I especially enjoyed the discussion about [specific topic from that round — e.g., "the consistency trade-off in the notification fan-out design"]. It reinforced why I'm excited about this role.
>
> Looking forward to hearing next steps.
>
> [Your name]

One short paragraph. Specific to that round. No asks, no negotiation, no restating your resume. Specificity is the signal — it shows you were fully present.

---

## Handling the decision window

Recruiters often quote 3-5 business days post-onsite. Reality is 5-10. Build in buffer.

- **Follow up on day 6** if you have heard nothing. Email the recruiter: "Hi [Name], I wanted to follow up on the [Company] onsite on [date]. Happy to provide any additional information. What's the current timeline?"
- **Do not follow up daily.** Once per week is appropriate. Daily follow-ups create an impression that hurts you if the committee is still deliberating.
- **Keep other loops warm.** Do not pause your pipeline for one outstanding offer. A competing offer is your best negotiation leverage and your best insurance.

---

## Handling rejection and re-applying

Rejection at Staff level is common even for strong candidates. The bar is deliberately narrow and hiring committee votes can go 4-3.

- **Request feedback immediately.** Call or email the recruiter the same day. Say: "I understand, and I appreciate the process. Is there any specific feedback you're able to share that would help me improve?" Many recruiters can say more verbally than they can put in writing.
- **Cooldown periods.** Most companies have a 6-12 month rehire cooldown for the same role. Some waive it if the feedback was "strong but wrong level" — ask explicitly.
- **Keep notes.** Write down every question you were asked and your rough answer within 12 hours of the onsite. This is your prep log for the re-apply cycle. Over 2-3 cycles these notes become invaluable.
- **Re-apply strategically.** Apply to the same company at the same level after completing the targeted gaps in their feedback. Not after 6 months of generic practice — after closing the specific gap they named.

> The Pragmatic Engineer's post [Passing the Google/Meta/Amazon system design bar as a senior+](https://newsletter.pragmaticengineer.com/p/system-design-interview-guide) is worth re-reading between cycles — the scoring criteria shift as you internalize the basics.

---

## Pre-interview checklist (day before and day of)

### Day before

- [ ] Confirm interview time, format, and links with recruiter
- [ ] Test video/audio setup with a real call
- [ ] Log in to the whiteboard/coding tool the company uses; verify your account works
- [ ] Review your top 5 STAR stories (one each for: delivery under pressure, technical direction, conflict, scope expansion, failure/learning)
- [ ] Review the company's recent engineering blog posts or system design case studies
- [ ] Set out water, charger, headset
- [ ] Sleep target: 7+ hours

### Day of

- [ ] Ethernet plugged in; hotspot data on
- [ ] DND enabled on all devices
- [ ] Camera clean, background clear, mic tested
- [ ] All browser tabs except the interview tool closed
- [ ] Whiteboard tool open and ready in a separate tab
- [ ] Notepad (physical or digital) for jotting constraints during system design
- [ ] Join the call 2-3 minutes early — not 10, not on time

---

## Quick reference: recruiter questions worth asking at every stage

| Stage | Question |
|-------|----------|
| Recruiter screen | "Who are the interviewers, and what are their roles?" |
| Before onsite | "Is there a bar-raiser or independent evaluator in the loop?" |
| Before onsite | "What tool will be used for coding and diagramming?" |
| After onsite | "When should I expect to hear back, and who should I follow up with?" |
| Offer call | "Is the level firm, or is there flexibility based on interview performance?" |

Asking these questions marks you as organized and thorough — qualities that reinforce the Staff signal before any technical round begins.
