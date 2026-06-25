# Week 06 Reflection

**Name: Samba Kamara**
**Date: 06-25-2026**

---

## Commits This Week

**Link: https://github.com/fascineh1/media-tracker-android/pull/2/commits **

---

## Code Review

<!-- Every week you leave a review on a pod mate's pull request. Fill in both parts below.
     Part 1 is the link — I will verify the review exists on GitHub.
     Part 2 is your written assessment — what you actually looked at and what you found. -->

**Reviewed:** *Kenan Port*
**Link to my review:**

### What I Looked At

I reviewed Kenan's implementation of the authentication and networking models. 
I focused on how the API request and response classes were structured because I was implementing similar functionality in my own project this week.

### What I Noticed

One thing I noticed was that every API model class used for Retrofit requests and responses was annotated with @Serializable. 
This included classes such as TokenRequest, TokenResponse, and AuthResponse. 
Seeing the annotations consistently applied made me realize that Kotlin Serialization requires every request and response model to be explicitly marked for serialization. 
Without those annotations, Retrofit cannot properly convert objects to and from JSON at runtime.

### Comments I Left

<!-- Briefly summarize the comments you left on the PR. If you left a positive comment,
     say what it was. If you left a suggestion, say what you suggested and why. -->

---

## One Thing I Understood More Deeply

While implementing SearchScreen.kt, I initially filtered the media list directly inside the Composable using the current search text. 
As I continued working, I realized that the UI became easier to understand when the SearchViewModel owned the search results and the Composable simply displayed the current state. 
Observing the ViewModel with collectAsState() made the screen cleaner because the Composable was responsible only for rendering the interface instead of managing application logic.

Another issue I encountered involved my API model classes. After submitting my pull request, I received feedback from Kenan Port pointing out that my request and response models were missing the @Serializable annotation. 
I originally thought importing the Kotlin Serialization library was enough, but after comparing my code with the instructor's authentication example, 
I learned that every model class—including Media, LibraryItem, Review, User, ActivityEvent, TokenRequest, TokenResponse, AuthResponse, and CreateUserRequest—must be explicitly annotated. 
Updating each file helped me understand how Retrofit and Kotlin Serialization work together to convert Kotlin objects into JSON.
---

## One Thing I'm Still Confused About

While building the Search feature, I sometimes wasn't sure where filtering and data transformation should occur. 
Some filtering logic can be written directly inside SearchScreen.kt, while other logic could be moved into SearchViewModel or even the Repository.
I would like a deeper understanding of how experienced Android developers decide which layer should own different types of logic as projects become larger and more complex.
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
