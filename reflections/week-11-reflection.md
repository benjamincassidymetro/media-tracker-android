# Week 11 Reflection — Bonus Feature Sprint (Week 1 of 2)

*This week's reflection is different from the standard template. We're not doing Profile this week — instead, this is the first of two weeks building your assigned bonus feature (Write Review, Quotes, or Priorities). See `reflection-instructions.md` for naming/submission rules, which are unchanged; only the content below differs.*

**Name:*Samba Kamara*
**Date:*08/06/2026*
**My assigned bonus feature:** *Priorities*

---

## Commits This Week

<!-- Paste a link to your commits for this week. -->

**Link:**

---

## Code Review

<!-- Code review continues as normal — same pod rotation, regardless of which bonus feature you or your pod mate are building. -->

**Reviewed:** *Issa Ali*
**Link to my review:*https://github.com/Issa-Ismail-Ali/media-tracker-android/commit/ab945e17d52adb4ce0f4fa93baa92cf76c08f21d#r195295347*

### What I Looked At
--> I reviewed Issa Ali's implementation of the Quotes bonus feature. 
I focused on how the networking layer was wired to the backend, how the repository handled API calls, 
and how the new feature fit into the existing project architecture.

### What I Noticed
--> The implementation follows the existing project architecture well. 
I noticed that Issa created separate model and request classes (Quote.kt and AddQuoteRequest.kt) instead of placing everything into one file, which keeps the code organized.

I also noticed that the repository includes dedicated functions such as addQuote() and getQuotes(), along with error handling that parses API responses before returning meaningful error messages. 
This makes the networking layer easier to maintain and debug.

### Comments I Left
--> Nice job keeping the Quotes feature organized. I like that you separated the Quote model, 
request object, and repository methods instead of combining them together. 
The dedicated addQuote() and getQuotes() functions make the repository easy to read, and the error parsing should make debugging API issues much easier.
---

## Bonus Feature Progress

<!-- This is the most important section this week. Be concrete: which endpoint(s) did you wire?
     What's actually showing on screen with real data? What's still stubbed or fake?
     "I worked on my bonus feature" is not an answer. "I got POST /quotes working from Media Detail
     and quotes show up in a list on my profile, but I haven't wired edit or delete yet" is. -->

**What's working:**
--> What's working:
-Implemented the Priorities feature using the real backend API.
-Connected the GET /priorities endpoint to load existing priorities.
-Connected the PUT /priorities endpoint to save priority changes.
-Added the Priorities section to the Library screen.
-Added the Set Priority dialog.
-Users can:
     Add items to priorities.
     Edit priority level (High, Medium, Low).
     Save estimated hours.
     Save notes.
     Remove priorities.
-Priority cards display:
     Priority level
     Estimated time
     Notes
-Priority count updates correctly (for example, 3/5).
-Fixed crashes caused by duplicate LazyColumn keys.
-Fixed missing mediaId request errors by correcting the request mapping.
-Added unit tests for the Priorities ViewModel.

**What's still stubbed, fake, or not started:**
-Drag-and-drop reordering still needs additional testing and polishing.
-More validation is needed for edge cases when editing priorities.
-Additional UI polish is planned for Week 2.

**What I'm blocked on, if anything:**
--> I am not currently blocked. Earlier in development I encountered API request errors ("Missing required field: mediaId"), duplicate key crashes in Compose, and issues saving priorities. 
After debugging the request objects and repository logic, those issues were resolved.
---

## One Thing I Understood More Deeply

--> This week helped me better understand how data flows through an Android MVVM application. I learned how the Repository, ViewModel, Retrofit API interface, and Compose UI all work together. 
I also gained more experience debugging backend API requests by reading server error messages and tracing problems through the networking layer.

---

## One Thing I'm Still Confused About

-->  I would like to better understand the best practices for implementing drag-and-drop reordering in Jetpack Compose while keeping the UI synchronized with the backend order indexes. 
I also want to learn more about handling optimistic updates when network requests fail.

---

## Anything Else *(optional)*

--> This project required significantly more debugging than previous assignments because it involved integrating new API endpoints with existing Library functionality. 
Although I encountered several crashes and API errors during development, working through them helped me gain confidence debugging Compose UI, Retrofit requests, and ViewModel state management.
---

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Concrete progress report (what's wired, what's not) plus specific, honest "Understood More Deeply" and "Still Confused" sections. | Present but vague — "I worked on my feature" with no specifics on what's actually working. | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match.
