# Week {07} Reflection

**Name: Samba Kamara **
**Date: 07-02-2026 **

---

## Commits This Week

<!-- Paste a link to your commits for this week. The easiest way: go to your repo on GitHub,
     click "commits", and copy the URL after filtering by your name or branch. -->

**Link: https://github.com/fascineh1/media-tracker-android/pull/8 **

---

## Code Review

<!-- Every week you leave a review on a pod mate's pull request. Fill in both parts below.
     Part 1 is the link — I will verify the review exists on GitHub.
     Part 2 is your written assessment — what you actually looked at and what you found. -->

**Reviewed:** * Kenan Port *
**Link to my review: https://github.com/Zabzar22/media-tracker-android/pull/8#pullrequestreview-4622303297 **

### What I Looked At

<!-- Walk through the code you reviewed. What was the PR trying to do? Which files or
     functions did you focus on? 

--> I reviewed Kenan Port's implementation of the Week 7 Media Detail screen. 
I focused primarily on MediaDetailScreen.kt, MediaDetail.kt, FakeMediaRepository.kt, and the new string resources in strings.xml. 
I compared the implementation against the Screen 07 wireframe to verify that all required UI elements were present, 
including the top app bar, cover image, rating section, action buttons, stat grid, and review cards. 
I also examined how the hardcoded MediaDetail object and sample reviews were used to populate the screen before API integration.




### What I Noticed

<!-- Be specific. Did you spot a potential bug? A pattern that could cause problems? Something
     done well that you want to call out? "I looked at the ViewModel and everything seemed fine"
     is not specific enough. Name the thing you noticed and explain why it matters.

--> One thing I noticed was that the screen was organized into reusable composable functions, which made the layout much easier to read and maintain. 
The stat grid also correctly displayed different values depending on whether the media item was a book, movie, or TV show. 
I also checked that the Media Detail screen loaded the selected media item properly and that the review section followed the wireframe layout

### Comments I Left

<!-- Briefly summarize the comments you left on the PR. If you left a positive comment,
     say what it was. If you left a suggestion, say what you suggested and why. 

--> I complimented the organization of the Media Detail screen and pointed out that the reusable composable functions made the implementation easy to follow. 
I also noted that the dynamic stat grid correctly adapts to different media types and that the ViewModel's fallback to the fake repository 
makes UI testing much easier while the API integration is still in progress.

---

## One Thing I Understood More Deeply

<!-- Be specific. Don't write "I learned about ViewModels." Write what specifically clicked —
     what was confusing before, what made it make sense, and how you'd explain it to someone else.
     There are no wrong answers here. 

--> This week helped me better understand how to build larger Compose screens by combining smaller reusable composable functions. 
Before this assignment I tended to place everything inside one large composable, but building the Media Detail screen showed me how separating components like the cover image, 
rating row, stat boxes, and review cards makes the code much cleaner and easier to modify. 
I also gained a better understanding of how a ViewModel manages UI state while the composables are only responsible for displaying that state.

---

## One Thing I'm Still Confused About

<!-- Be honest. This is the most useful part of the reflection for me — it tells me where to
     spend more time in class. You will not lose points for being confused. 

--> I'm still becoming comfortable with deciding what logic belongs inside the ViewModel versus what should stay inside composable helper functions. 
Sometimes it's obvious that data transformation belongs in the ViewModel, but for smaller formatting functions like rating displays or stat labels 
I'm still learning where the best separation of responsibilities should be.

---

## Anything Else *(optional)*

<!-- Did you help a pod mate work through something? Did you discover something cool or frustrating?
     Did something from a previous week finally click? This is a good place to put it. -->

---

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
