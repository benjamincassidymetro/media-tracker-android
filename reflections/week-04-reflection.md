# Week 04 Reflection

**Name:Samba Kamara**
**Date:11 June 2026**

---

## Commits This Week


**Link:https://github.com/benjamincassidymetro/media-tracker-android/pull/35**

---

## Code Review

<!-- Every week you leave a review on a pod mate's pull request. Fill in both parts below.
     Part 1 is the link — I will verify the review exists on GitHub.
     Part 2 is your written assessment — what you actually looked at and what you found. -->

**Reviewed:** *Kenan Port*
**Link to my review:**https://github.com/Zabzar22/media-tracker-android/pull/4#pullrequestreview-4481971220**
### What I Looked At

I reviewed the pull request that implemented backend support for authentication and improved the bottom navigation behavior. 
I focused on the new data models in `Authmodels.kt`, 
the Retrofit API definitions in `ApiService.kt`, the `UserRepository.kt` implementation,
and the changes made to `BottomNavBar.kt`.


### What I Noticed

I noticed that the authentication-related classes were organized cleanly and separated into models, API definitions, and repository logic, which improves maintainability.
I also noticed the change in `BottomNavBar.kt` that replaced direct route comparison with:
```kotlin
currentDestination?.hierarchy?.any { it.route == item.route } == true
```
This approach improves the reliability of active tab selection when navigating between screens.

### Comments I Left

I left positive feedback regarding the separation of concerns between the data models, API layer, and repository. 
I also noted that using hierarchy.any {} in the bottom navigation is a good solution because it makes the selected tab behavior more reliable when navigating between screens.

---

## One Thing I Understood More Deeply

Working on the RegisterScreen helped me better understand state management in Jetpack Compose. 
I learned how `remember` and `mutableStateOf()` preserve values entered by the user and automatically update the UI when state changes. 
I also gained experience using keyboard actions and focus management to improve the user experience.

---

## One Thing I'm Still Confused About

I am still learning how ViewModels should be integrated with composable screens. 
Although I understand local state using `remember`, I would like to gain a deeper understanding of when state should remain inside a composable and when it should be moved into a ViewModel for better separation of concerns.

---

## Anything Else *(optional)*

This week I implemented a Register Screen for the MediaTracker application. 
I added fields for display name, email, username, password, and confirm password, along with validation for empty fields and password mismatches. 
I also updated the string resources and tested the application successfully on the Pixel 9a emulator. 
Troubleshooting emulator startup issues and verifying the UI helped strengthen my understanding of Jetpack Compose and Android development workflows.

---

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
