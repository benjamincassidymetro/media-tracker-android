week-02-reflection.md

**Name:Samba Kamara**
**Date:05-28-2026**

---

## Commits This Week



**Link:https://github.com/benjamincassidymetro/media-tracker-android/pull/27**

---

## Code Review

<!-- Every week you leave a review on a pod mate's pull request. Fill in both parts below.
     Part 1 is the link — I will verify the review exists on GitHub.
     Part 2 is your written assessment — what you actually looked at and what you found. -->

**Reviewed:** *(Kenan Port)*
**Link to my review:https://github.com/Zabzar22/media-tracker-android/pull/2#issuecomment-4570388248**

### What I Looked At

I reviewed Kenan's Week 02 pull request, focusing on the navigation-related changes and overall application structure. I examined the screen navigation implementation, route definitions, and how navigation actions were connected between screens.
I also reviewed the organization of the code to ensure it followed the project structure discussed in class.

### What I Noticed

One thing I noticed was that the navigation flow was implemented consistently across the application, making it easier to follow and maintain. The route definitions were clearly organized, which reduces the likelihood of navigation errors as the application grows.
I also noticed that the code was readable and well-structured, which would make future debugging and feature additions easier.

### Comments I Left

I commented that the implementation looked good overall and thanked Kenan for helping me earlier in class.
I specifically noted that the navigation structure was easy to follow and appeared to be functioning correctly.

---

## One Thing I Understood More Deeply

This week I gained a better understanding of how Jetpack Navigation works together with the NavController and route definitions.
Before this week, I understood navigation conceptually, but I was not fully comfortable troubleshooting navigation issues. While fixing the register button behavior and updating navigation routes, I learned how route matching affects screen transitions and how incorrect route definitions can cause crashes or unexpected behavior.
I now have a much better understanding of how screens are connected and how navigation state is managed throughout the application.

---

## One Thing I'm Still Confused About

I am still somewhat confused about the more advanced aspects of Jetpack Compose state management, particularly how state should be shared between multiple screens while maintaining a clean MVVM architecture.
I understand the basic use of ViewModels, but I would like more practice understanding when state should be stored locally within a composable versus within a ViewModel that survives navigation events.

---

## Anything Else *(optional)*

This week reinforced the importance of testing navigation after every change. Small modifications to routes or navigation actions can have unintended effects on multiple screens.
I also gained more confidence using GitHub pull requests and reviewing other students' code.

---

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
