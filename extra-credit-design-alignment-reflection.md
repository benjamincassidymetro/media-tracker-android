# Extra Credit Reflection — Design Alignment

*See `extra-credit-design-alignment.md` for submission requirements and the full assignment description.*

**Name: Samba Kamara**
**Date: 06-30-2026**

---

## The Audit

*Before touching any code, compare your running app to the wireframes screen by screen. List what you found — be specific about which screen, which component, and what was different. "The colors were off" is not specific. "The active chip on the Search screen was using amber instead of primary container (#E0E0FF)" is specific.*

*List at least five concrete differences you found:*

1. The Login screen's Sign In button used the default Material button styling instead of the required 20dp rounded pill shape.

2. The Login screen OutlinedTextFields used the default Material styling instead of an 8dp corner radius with the primary-colored focused border.

3. The Search screen search field was not using the required 28dp pill-shaped design from the wireframe.

4. The Search screen FilterChips were using the default Material appearance instead of 8dp rounded corners with the primary container color (#E0E0FF) when selected.

5. The media cards on the Search screen were missing the required 12dp rounded corners and 2dp elevation.

6. The Bottom Navigation active tab did not display the primary container indicator pill and was not using the proper primary color for the selected icon and label.


---

## What You Changed

*Walk through the changes you made. For each area of the design system, describe what the code looked like before and what you changed it to. Reference specific files and Composables.*


### Color System

<!-- What did your Color.kt look like before? What did you add or change? How did you wire colors into MaterialTheme? 

--> I updated Color.kt with all of the design system color tokens specified in the assignment and connected them through Theme.kt using the application's colorScheme. 
This removed the remaining hardcoded colors and allowed components throughout the app to consistently use the shared theme colors.

### Typography

<!-- Were weights hardcoded? Did you update Type.kt? What specifically changed? 

--> I reviewed Type.kt and verified that screens use MaterialTheme.typography instead of hardcoded font sizes and font weights. 
Headings, body text, and labels now consistently use the typography defined by the theme.

### Buttons

<!-- Which button variants needed work? What was wrong and how did you fix it? 

--> I updated the application's buttons to follow the design specification by using the proper Material button colors together with RoundedCornerShape(20.dp). 
I also updated the outlined Sign Out button in SettingsScreen.kt so it matches the required styling.

### Text Fields

<!-- What shape and color changes did you make? 

--> I updated the Login and Search screens to use the proper Material text field styling. 
Standard text fields now use an 8dp corner radius while the Search field uses a 28dp pill shape with the primary-colored focused border.

### Other Components

<!-- Chips, cards, bottom nav, status badges — what changed? 

--> I updated the Search screen to better match the wireframes by:

>Styling the FilterChips with 8dp rounded corners and the correct selected and unselected colors.
>Updating media cards to use 12dp rounded corners and 2dp elevation.
>Expanding the search results to include media type, release year, and rating.
>Updating the Bottom Navigation to use the proper selected indicator styling defined in the design system.


---

## What Was Hard

*Describe the most technically challenging part of this work. Don't write "it was confusing." Explain specifically what confused you, what you tried, and what helped you figure it out. If something in the Jetpack Compose theming system surprised you, describe it.*

--> The most difficult part was understanding how Jetpack Compose's theming system works. 
Initially, I was modifying individual Composables whenever I noticed something that looked different. 
As I worked through the assignment, I realized that most styling should be centralized in Color.kt, Theme.kt, and Type.kt, allowing the UI to inherit those styles automatically.

Another challenge was comparing the emulator against the wireframes. Small differences such as chip colors, border radius, typography, spacing, and card elevation were easy to overlook,
but correcting those details made the application feel much closer to a professionally designed product.
---

## What You Understand Now

*What do you understand about Jetpack Compose theming — `MaterialTheme`, `colorScheme`, `typography`, component defaults — that you didn't fully grasp before this assignment? Be specific enough that you could explain it to a pod mate who hasn't done this yet.*

--> This assignment helped me understand that MaterialTheme acts as the application's design system. 
By defining colors, typography, and shapes in one place, every screen can remain visually consistent without repeating styling throughout the project.

I also learned how Material components can be customized using classes such as:

ButtonDefaults
OutlinedTextFieldDefaults
FilterChipDefaults
CardDefaults

Instead of hardcoding values repeatedly, these APIs allow components to follow the application's overall design language.

Before this assignment, I mainly focused on making features functional. 
Now I understand that implementing a design specification requires paying attention to colors, typography, spacing, elevation,
corner radius, and component states so the final application matches the designer's intent.
---

## Self-Assessment

*Look at the rubric (`extra-credit-design-alignment-rubric.md`) and estimate your own score for each section. Be honest — this does not affect your grade, but it shows me whether you read the rubric carefully.*

| Section | Possible | My Estimate |
|:---|:---:|:---:|
| Color System | 13 | 12 |
| Typography | 5 | 5 |
| Component Styling | 15 | 13 |
| Navigation & Cards | 5 | 5 |
| Reflection | 12 | 12 |
| **Total** | **50** | 47 |

*One thing I think I did well:*

--> I carefully compared my application against the wireframes and updated multiple screens so they consistently use the same design system. 
The Login, Search, Settings, cards, chips, text fields, and navigation now closely match the provided specification.

*One thing I know I left incomplete or could have done better:*

--> If I had additional time, I would perform one more complete audit of every screen to ensure every remaining component, spacing value, 
and typography style exactly matches the wireframes and that no hardcoded styling remains anywhere in the project.
