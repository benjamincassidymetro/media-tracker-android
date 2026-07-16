# Week 08 Reflection

**Name:** Samba Kamara
**Date:** 07/09/2026

---

## Commits This Week


**Link:** https://github.com/fascineh1/media-tracker-android/pull/9

---

## Code Review

**Reviewed:** Kenan Port
**Link to my review:** https://github.com/Zabzar22/media-tracker-android/pull/10

### What I Looked At

<!-- Walk through the code you reviewed. What was the PR trying to do? Which files or
     functions did you focus on? 
     
     --> I reviewed Kenan's Week 8 PR, focusing on the API and model changes. I spent most of my time reading `Media.kt`, `MediaType.kt`, `LibraryItem.kt`, and the new repository and network classes. I wanted to understand how the API responses were mapped into strongly typed Kotlin models and how the detail screen receives different data than the search screen.

### What I Noticed

<!-- Be specific. Did you spot a potential bug? A pattern that could cause problems? Something
     done well that you want to call out? "I looked at the ViewModel and everything seemed fine"
     is not specific enough. Name the thing you noticed and explain why it matters. 
     
     --> One thing that stood out was replacing the raw `mediaType` string with a `MediaType` enum using `@SerialName("book")`, `@SerialName("movie")`, and `@SerialName("show")`. This prevents invalid string comparisons throughout the app and allows `when (mediaType)` to be checked by the compiler.

I also noticed the new `typeStat()` helper in `Media.kt`. Instead of the UI deciding whether to display page count, runtime, or seasons and episodes, the model returns the correct label and value based on the media type. Keeping that logic in one place makes the detail screen simpler and reduces duplicate branching throughout the UI.

One small thing I questioned was the fallback in `MediaType?.iconRes()`. When the media type is null it currently returns the TV icon. While this prevents crashes, unknown data could accidentally appear as a TV show, so a neutral placeholder icon might better represent missing information.

### Comments I Left

<!-- Briefly summarize the comments you left on the PR. If you left a positive comment,
     say what it was. If you left a suggestion, say what you suggested and why. 
     
     --> I complimented the move from raw strings to a serializable enum because it improves type safety and makes invalid values impossible to ignore. 
I also pointed out that the `typeStat()` helper centralizes media-specific display logic instead of repeating conditional statements throughout the UI. Finally, I suggested reviewing the default icon returned by `iconRes()` so unknown media types are displayed more accurately.


---

## One Thing I Understood More Deeply

<!-- Be specific. Don't write "I learned about ViewModels." Write what specifically clicked —
     what was confusing before, what made it make sense, and how you'd explain it to someone else.
     There are no wrong answers here. 
     
     --> This week I finally understood why the project separates the search results from the detail endpoint instead of trying to reuse the same object everywhere. 
Earlier I thought adding nullable fields like `description`, `pageCount`, `runtimeMinutes`, `seasonCount`, and `episodeCount` directly to `Media` was enough.

After working through my own implementation and comparing it with the reference design, I realized that `GET /media` intentionally returns a smaller object while `GET /media/{id}` returns the complete details. 
The detail screen therefore has to request the item again by ID because the search response simply doesn't contain all the information. Before this week I was following that pattern because it was in the starter code. Now I understand the architectural reason behind it instead of just copying it.

---

## One Thing I'm Still Confused About

<!-- Be honest. This is the most useful part of the reflection for me — it tells me where to
     spend more time in class. You will not lose points for being confused. 
     
     --> I'm still trying to build a better intuition for coroutine exception handling when multiple asynchronous requests are running at the same time. 
My detail screen loads the media item, library status, and reviews together, and I understand that `runCatching` and `async/await` can prevent one request from crashing the others.

What I'm still not completely confident about is knowing when each coroutine should fail independently versus when failures should cancel the entire operation. 
I understand the syntax, but I still need more practice understanding how structured concurrency behaves in larger applications.


---

## Anything Else *(optional)*

<!-- Did you help a pod mate work through something? Did you discover something cool or frustrating?
     Did something from a previous week finally click? This is a good place to put it. 
     
     --> This week reminded me how much easier it is to understand architecture after reading someone else's implementation. 
Reviewing Kenan's API models helped me understand why several design decisions were made in the starter project, 
and I was able to apply some of those ideas while working on my own detail screen implementation.

---

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.