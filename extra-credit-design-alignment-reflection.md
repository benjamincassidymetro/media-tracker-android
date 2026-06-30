# Extra Credit Reflection — Design Alignment

*See `extra-credit-design-alignment.md` for submission requirements and the full assignment description.*

**Name: Samba Kamara**
**Date: 06-30-2026**

---

## The Audit

*Before touching any code, compare your running app to the wireframes screen by screen. List what you found — be specific about which screen, which component, and what was different. "The colors were off" is not specific. "The active chip on the Search screen was using amber instead of primary container (#E0E0FF)" is specific.*

*List at least five concrete differences you found:*

1. On the Login screen, the Sign In button used the default Material button shape instead of the 20dp rounded pill shown in the wireframe.

2. The Login screen OutlinedTextFields used the default Material shape instead of an 8dp RoundedCornerShape with the primary-colored focused border.

3. The Search screen search bar was not using the required 28dp pill shape shown in the wireframe.

4. The Search screen FilterChips were using the default Material appearance instead of an 8dp rounded shape with the primary container color (#E0E0FF) for the selected state.

5. The media cards on the Search screen were missing the consistent 12dp rounded corners and 2dp elevation specified by the design system.

6. The Bottom Navigation active tab was not displaying the primary container indicator pill or using the correct primary color for the selected icon and label.


---

## What You Changed

*Walk through the changes you made. For each area of the design system, describe what the code looked like before and what you changed it to. Reference specific files and Composables.*


### Color System

<!-- What did your Color.kt look like before? What did you add or change? How did you wire colors into MaterialTheme? 

--> I updated Color.kt with the design system color tokens and wired them into Theme.kt through the application's colorScheme. This replaced the default Material colors so buttons, navigation, filter chips, and other UI components consistently used the colors defined in the wireframes.

### Typography

<!-- Were weights hardcoded? Did you update Type.kt? What specifically changed? 

--> I reviewed Type.kt and verified that the app used MaterialTheme.typography instead of hardcoded font sizes and weights. Headlines, body text, and labels now consistently use the typography definitions provided by the theme.

### Buttons

<!-- Which button variants needed work? What was wrong and how did you fix it? 

--> I updated the buttons to use ButtonDefaults.buttonColors() together with RoundedCornerShape(20.dp) so they match the filled button style shown in the wireframes.

### Text Fields

<!-- What shape and color changes did you make? 

--> I updated the Login text fields to use OutlinedTextFieldDefaults.colors() so the focused border uses the application's primary color. I also applied an 8dp corner radius for standard fields and a 28dp rounded shape for the Search bar.

### Other Components

<!-- Chips, cards, bottom nav, status badges — what changed? 

--> I updated the Search screen cards, filter chips, and bottom navigation to better match the wireframes. I also reviewed the status badge implementation to ensure it follows the design system colors where used.



---

## What Was Hard

*Describe the most technically challenging part of this work. Don't write "it was confusing." Explain specifically what confused you, what you tried, and what helped you figure it out. If something in the Jetpack Compose theming system surprised you, describe it.*

--> The hardest part was understanding where styling should live. At first, I was changing individual Composables directly, but the assignment made me realize that colors and typography should mostly come from `Color.kt`, `Theme.kt`, and `Type.kt`. The confusing part was knowing when to use `MaterialTheme.colorScheme.primary`, when to use component defaults like `ButtonDefaults`, and when a custom shape or color needed to be applied directly to a component.

Another challenge was comparing the running emulator against the wireframes. Some differences were small, like corner radius, chip spacing, or text weight, but those details matter because they make the app look intentional instead of just functional.

---

## What You Understand Now

*What do you understand about Jetpack Compose theming — `MaterialTheme`, `colorScheme`, `typography`, component defaults — that you didn't fully grasp before this assignment? Be specific enough that you could explain it to a pod mate who hasn't done this yet.*

--> I understand that `MaterialTheme` is the central place where Compose gets its design system. If the colors and typography are wired correctly there, the rest of the app becomes easier to keep consistent. I also understand that component defaults can be overridden in a controlled way using `ButtonDefaults`, `OutlinedTextFieldDefaults`, `CardDefaults`, and shape values like `RoundedCornerShape`.

Before this assignment, I mostly focused on whether the screen worked. Now I understand that matching the design spec means checking the exact colors, typography, spacing, shapes, and component states.

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

--> I improved the app from looking like default Material components to looking closer to the provided wireframes, especially the Login and Search screens.

*One thing I know I left incomplete or could have done better:*

--> I could do a deeper final pass to make sure there are no hardcoded colors or inconsistent shapes left in any Composable.
