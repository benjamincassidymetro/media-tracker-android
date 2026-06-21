# Week 6 Reflection

**Name:** Samba Kamara 
**Date:** 06/25/2026 

---

## Commits This Week

**Link:** ht

---

## Code Review

**Reviewed:** Kenan Port
**Link to my review:** https://github.com/Zabzar22/media-tracker-android/pull/5

### What I Looked At

Kenan's Week 5 pull request added support for authentication and networking using Retrofit. 
I focused primarily on the files in the data/network package, including DefaultUserRepository.kt, UserApiService.kt, 
RetrofitInstance.kt, RegisterRequest.kt, TokenRequest.kt, TokenResponse.kt, and TokenStore.kt.
I paid particular attention to the register() and login() functions in DefaultUserRepository.kt 
to understand how API requests are sent and how HTTP response codes are converted into RegisterResult and LoginResult objects

---

### What I Noticed

One thing I noticed in DefaultUserRepository.kt was how the repository separates network operations from the UI layer.
The register() function creates a RegisterRequest, sends it through UserApiService, and then converts the HTTP response codes into RegisterResult objects.
Similarly, the login() function stores tokens inside TokenStore after receiving a successful response.
I thought this organization was useful because it keeps API details out of ViewModels and makes the code easier to maintain and test.
I also noticed that RetrofitInstance.kt uses an HttpLoggingInterceptor configured with BODY logging.
This should make debugging API requests easier during development because the request and response bodies can be inspected.

### Comments 

I left a positive review noting that the separation between UserApiService, RetrofitInstance,
and DefaultUserRepository made the authentication flow easy to follow. 
I specifically pointed out that converting HTTP status codes into LoginResult and RegisterResult 
objects creates a clean interface for the rest of the application and avoids exposing networking details to higher layers

---

## One Thing I Understood More Deeply

This week I gained a better understanding of the Repository pattern and how it fits between the ViewModel and external APIs. 
Previously, I understood that ViewModels should contain state and business logic, but I was not completely clear about where network requests should live. 
Seeing the authentication code helped me understand that repositories act as an abstraction layer that handles communication with remote services and converts raw responses into application-specific results. 
I would explain it to someone else as the "middleman" between the UI and the backend.

---

## One Thing I'm Still Confused About

I am still somewhat confused about token management and long-term authentication. 
I understand that TokenStore temporarily holds access and refresh tokens, but I am not yet sure how tokens should be refreshed automatically when they expire or how persistent storage solutions such as DataStore or encrypted storage should be integrated. 
I would like to better understand how authentication state is maintained across application restarts.

---

## Anything Else *(optional)*

Kenan reminded me to create my pull request against my own repository instead of the professor's repository. 
This helped me avoid repeating a mistake that had been mentioned in previous 
feedback and reinforced the importance of maintaining my own fork and commit history.

---

