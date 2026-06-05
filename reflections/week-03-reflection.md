# week-03-Reflection

**Name:Samba Kamara**
**Date:06-04-2026**

---

## Commits This Week

**Link:https://github.com/benjamincassidymetro/media-tracker-android/pull/31**

---

## Code Review

**Reviewed:** *Issa Ali*
**Link to my review:https://github.com/Issa-Ismail-Ali/media-tracker-android/pull/3**

### What I Looked At

I reviewed the implementation of the registration screen and associated ViewModel classes. 
I focused on how state was managed and how the UI components were organized. 
I examined the composables, ViewModel interactions, and overall file structure to understand how the feature was implemented.


### What I Noticed

I noticed that separating UI state from composables using a ViewModel makes the code easier to maintain and improves readability. 
I also observed that organizing files by feature helps keep the project structure clean and easier to navigate.


### Comments I Left
I left positive feedback regarding the organization of the registration feature. 
I noted that the separation between the composables and ViewModel improved maintenance and that the feature-based file organization made the code easier to understand.


---

## One Thing I Understood More Deeply

One concept that clicked for me this week was the separation of responsibilities between composables and ViewModels. 
Before, I understood that ViewModels stored state, but I did not fully appreciate why keeping business logic out of composables was important. After implementing and reviewing the registration feature, 
I saw how composables are responsible for displaying the UI while ViewModels manage state and user interactions.

---

## One Thing I'm Still Confused About

One area I'm still trying to understand better is how repositories and API service classes work together with ViewModels. 
I understand that the ViewModel should not directly handle network requests and that the repository acts as an intermediary, 
but I'm still not completely clear on the flow of data between the UI, ViewModel, repository, and Retrofit API calls.

---

## Anything Else *(optional)*
