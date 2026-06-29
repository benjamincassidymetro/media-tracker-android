# Week 06 Reflection

**Name: Samba Kamara**
**Date: 06-25-2026**

---

## Commits This Week

Link: https://github.com/fascineh1/media-tracker-android/pull/2/commits 

---

## Code Review

**Reviewed:** Kenan Port
Link to my review: https://github.com/Zabzar22/media-tracker-android/pull/6 

### What I Looked At

I reviewed Kenan Port's implementation of `FakeSearchResults.kt`, which adds a larger fake dataset for the search feature. 
I focused on how the sample data was organized and how it supports testing the search screen, media filtering, and 
pagination before the application is connected to the real API.


### What I Noticed

I noticed that the fake dataset includes a good variety of books, movies, and TV shows with consistent `Media` objects. 
Using a larger dataset makes it much easier to verify that filtering works correctly and that pagination or infinite scrolling behaves as expected. 
The file is easy to follow, although it could become harder to maintain if the dataset continues to grow.


### Comments I Left

I complimented Kenan on expanding the fake dataset because it improves testing for filtering, pagination, and infinite scrolling. 
I also suggested that if the fake dataset continues to grow, it could be organized into separate collections or files to improve 
readability and make future maintenance easier.

---

## One Thing I Understood More Deeply

While implementing SearchScreen.kt, I initially filtered the media list directly inside the Composable using the current search text. 
As I continued working, I realized that the UI became easier to understand when the SearchViewModel owned the search results and the Composable simply displayed the current state. 
Observing the ViewModel with collectAsState() made the screen cleaner because the Composable was responsible only for rendering the interface instead of managing application logic.

After submitting my pull request, Kenan pointed out that my SearchViewModel is still returning four hard-coded search results instead of supporting paginated API data, and that I created a local MediaItem class instead of using the shared Media model. 
I had been focused on getting the Search screen working visually, so I hadn't thought as much about keeping my implementation aligned with the overall application architecture. 
His feedback helped me realize that using the shared models and designing the ViewModel around the API from the beginning will make the search feature easier to maintain and integrate once the live API is connected.
---

## One Thing I'm Still Confused About

While building the Search feature, I sometimes wasn't sure where filtering and data transformation should occur. 
Some filtering logic can be written directly inside SearchScreen.kt, while other logic could be moved into SearchViewModel or even the Repository.
I would like a deeper understanding of how experienced Android developers decide which layer should own different types of logic as projects become larger and more complex.
---

## Anything Else *(optional)*

This week helped reinforce how the ViewModel, repository, and API models work together. 
Comparing my implementation with my pod mate's code and incorporating feedback made me more confident in 
organizing Android applications using the MVVM architecture.
---

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
