# Week {{N}} Reflection

**Name: Mai Nhia Vang                
**Date: 06/25/2026

---

## Commits This Week

<!-- Paste a link to your commits for this week. The easiest way: go to your repo on GitHub,
     click "commits", and copy the URL after filtering by your name or branch. -->

**Link: https://github.com/Maii2025/media-tracker-android/pull/6
---

## Code Review

<!-- Every week you leave a review on a pod mate's pull request. Fill in both parts below.
     Part 1 is the link — I will verify the review exists on GitHub.
     Part 2 is your written assessment — what you actually looked at and what you found. -->

**Reviewed: Dylan Browne *(pod mate's name)*
**Link to my review: https://github.com/DylanBrowneMetrostate/media-tracker-android/pull/14

### What I Looked At

<!-- Walk through the code you reviewed. What was the PR trying to do? Which files or
     functions did you focus on? -->

The SearchApiService is a networking layer interface that makes GET requests.
These requests are sent to the media endpoint. I also looked at the query parameters such as query,
type, genre, limit, and after. I look at how these query parameters affect the requests
that are sent to the media endpoint.

### What I Noticed

<!-- Be specific. Did you spot a potential bug? A pattern that could cause problems? Something
     done well that you want to call out? "I looked at the ViewModel and everything seemed fine"
     is not specific enough. Name the thing you noticed and explain why it matters. -->

I noticed that most of the query parameters are nullable. This means they can be left out of
request if they are not needed. This matters because the Retrofit omits null values, so 
the API receives the parameters that are provided. As a result, the search results and 
pagination can change depending on which filters are used.

### Comments I Left

<!-- Briefly summarize the comments you left on the PR. If you left a positive comment,
     say what it was. If you left a suggestion, say what you suggested and why. -->
---

The below paragraph are what I comment for Dylan's week-06 link. First comment, I left a good PR for him that most
of his searchMedia function are use as a nullable query parameters. This null values can make the API
a flexible search, which can search other parameters. Second comment, I mentioned the Media class use
to store information in one place, and it can be helpful because we don't need many different class. 
Third comment, I am asking a question instead of a positive comment because I am curious of the 
response headers of the "X-Next-Cursor" and how it impacts the API design. 

Reference to my comments:

First comment: "The searchMedia function uses nullable query parameters, which makes the API flexible for different 
kinds of search. Most of the values in here are nullable, because the request can make it 
flexible to use for the options and can be left empty if they are not needed. For example, 
you can search for movies, songs, and action by providing only those filters in a code. 
If you write code like  " searchMedia " (type= “movie”)", the search will only return movies because
the type filter is applied. If you leave  type as null, that filter is not used, 
so the search can include different types of media based on the other parameters you provide."

Second comment: "Nice. You used the Media class to store information such as books, movies, and shows
in one location. It is helpful, because the app can use one model instead of many different classes."

Third comment: "Why does the app use headers like X-Next-Cursor for loading results instead of 
putting that information in the main response? I wonder how that impact the API design."


## One Thing I Understood More Deeply

<!-- Be specific. Don't write "I learned about ViewModels." Write what specifically clicked —
     what was confusing before, what made it make sense, and how you'd explain it to someone else.
     There are no wrong answers here. -->

---
I understand how Retrofit uses @Query parameters to build a network request. I learned that when
a parameter has a null value, Retrofit does not send that parameter in the request. This helped 
me understand that null values can change what the API returns because the search depends on 
which parameters are included.

## One Thing I'm Still Confused About

<!-- Be honest. This is the most useful part of the reflection for me — it tells me where to
     spend more time in class. You will not lose points for being confused. -->

---
I am still confused how the backend knows what to decide to return many filters when they are used 
together like combining “type, genre, and query.

## Anything Else *(optional)*

<!-- Did you help a pod mate work through something? Did you discover something cool or frustrating?
     Did something from a previous week finally click? This is a good place to put it. -->

---
None.

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
