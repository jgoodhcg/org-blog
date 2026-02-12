---
title: "Resume Update"
status: done
description: "Full content revision and visual update for the resume."
tags: [area/content, type/update]
priority: high
created: 2026-02-01
updated: 2026-02-01
---

# Resume Update

## Problem / Intent

Resume needs a full content revision and visual update.

## Constraints

`pages/resume.edn` (data) and `src/org_blog/pages/resume.clj` (template).

## Proposed Approach

- **Content:** Revise all job descriptions, summaries, and skills.
- ~~**Visuals:** Replace grid-based skills with a word cloud.~~ (Skipped: content layout is sufficient for now).
- ~~**Additions:** Add AI/CLI tools section.~~ (Skipped: integrated into technology lists instead).

## Open Questions

- ~~Word cloud implementation details?~~

## Notes

**2026-02-01:** Content update complete. Decided to skip the visual "word cloud" and dedicated "AI/CLI" section in favor of keeping the simple, readable list format. The content itself now reflects the AI/Agentic focus.

On 2026-01-31, capture the AI-output critique and the follow-up clarifying questions needed to reposition the resume around agentic instruction standards, evaluation harnesses (browser emulation + screenshots), and org-wide adoption/enablement work. Questions to answer for resume positioning: what problem the AGENTS.md instruction-seed work solved, who used it, and what changed as a result; what specific evaluation workflows were built and the scale they run at; how those evals increased autonomy or reduced human review; the adoption audience size, cadence, and measurable outcomes; any compliance or auditability improvements tied to this work; concrete impact metrics (time saved, QA reduction, incident reduction, reliability); the tooling stack and CI/logging integration; ownership model and cross-team collaboration; which personal projects best demonstrate this work and whether they are public; and the target role titles to optimize for.
