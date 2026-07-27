# Week {10} Reflection

**Name:** Samba Kamara
**Date:** 07/23/2026

---

## Commits This Week



**Link:** https://github.com/fascineh1/media-tracker-android/pull/10

---

## Code Review

**Reviewed:** * Kenan Port *
**Link to my review: https://github.com/Zabzar22/media-tracker-android/pull/11#pullrequestreview-4792345097**

### What I Looked At

<!-- Walk through the code you reviewed. What was the PR trying to do? Which files or
     functions did you focus on? 

--> I reviewed Kenan Port's Week 10 pull request, which focused on integrating the MediaTracker application with the backend API. 
I examined changes to the data models, including `ActivityEvent`, `Favorite`, `LibraryItem`, and `Media`, along with the new repository classes and API services. 
I also looked at the serialization updates using `@Serializable` and `@SerialName` to verify that the client models correctly matched the JSON responses returned by the backend.


### What I Noticed

<!-- Be specific. Did you spot a potential bug? A pattern that could cause problems? Something
     done well that you want to call out? "I looked at the ViewModel and everything seemed fine"
     is not specific enough. Name the thing you noticed and explain why it matters. 

--> One implementation that stood out was the use of `@SerialName("want_to")`, `@SerialName("in_progress")`, and `@SerialName("finished")` in the `LibraryStatus` enum. 
These annotations ensure that the JSON values returned by the server are correctly mapped to the application's enum values, preventing serialization issues. I also noticed improvements to `ActivityEvent.kt`, where helper methods and support for relative timestamps were added to simplify formatting activity feed messages and make the UI code easier to maintain. 
I suggested continuing to use consistent error handling for authentication failures, such as HTTP 401 responses, so users receive clear feedback when their session expires.


### Comments I Left

<!-- Briefly summarize the comments you left on the PR. If you left a positive comment,
     say what it was. If you left a suggestion, say what you suggested and why. 

--> I complimented the overall organization of the implementation and the improvements made to the data models and repository layer. 
I highlighted the use of serialization annotations and helper methods as strengths because they improve maintainability and compatibility with the backend API. I also recommended keeping authentication error handling consistent across all repository methods to provide a better user experience when API requests fail. 
Overall, I thought the pull request showed solid progress toward replacing the fake data layer with real backend integration.

---

## One Thing I Understood More Deeply

<!-- Be specific. Don't write "I learned about ViewModels." Write what specifically clicked —
     what was confusing before, what made it make sense, and how you'd explain it to someone else.
     There are no wrong answers here. 

--> This week I gained a much better understanding of how the Repository pattern fits into the overall architecture of the application. 
While implementing `DefaultMediaRepository`, `MediaDetailViewModel`, and `LibraryViewModel`, I learned that the repository should be the only layer responsible for communicating with Retrofit and handling API responses. Instead of placing networking logic inside the ViewModels, the repository exposes methods such as `getMediaDetail()`, `getLibrary()`, and `updateLibraryStatus()`, allowing the ViewModels to focus only on managing UI state. 
Working through this implementation made it much clearer why separating networking from the UI makes the application easier to maintain, test, and extend.

---

## One Thing I'm Still Confused About

<!-- Be honest. This is the most useful part of the reflection for me — it tells me where to
     spend more time in class. You will not lose points for being confused. 

--> I still have questions about the best way to handle authentication when a user's access token expires. 
I understand how my repository detects HTTP 401 responses and returns an error to the ViewModel, but I am not yet sure what the best production approach is after that. 
For example, should the application automatically refresh the access token, redirect the user back to the login screen, or prompt them to sign in again? I would like to better understand how production Android applications typically manage expired authentication sessions.


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
