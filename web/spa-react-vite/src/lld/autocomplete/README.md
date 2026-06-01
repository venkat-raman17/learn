# LLD Practice: Autocomplete / Typeahead

## Problem Statement

Build a reusable `Autocomplete` component that lets a user type a query, fetches
matching suggestions asynchronously, and allows selection via mouse or keyboard.

---

## Component API

```ts
/** A single suggestion item returned by the fetch function. */
export interface Item {
  id: string;
  label: string;
}

export interface AutocompleteProps {
  /**
   * Async function that returns suggestions for a given query string.
   * Called after debouncing the user's input.
   */
  fetchSuggestions: (query: string) => Promise<Item[]>;

  /**
   * Called when the user selects an item (keyboard Enter or mouse click).
   */
  onSelect: (item: Item) => void;

  /**
   * Debounce delay in milliseconds before triggering fetchSuggestions.
   * @default 300
   */
  debounceMs?: number;

  /** Placeholder text shown in the input when empty. */
  placeholder?: string;
}
```

---

## Behaviors to Implement

### Core flow
1. Render a text `<input>` and a dropdown listbox below it.
2. On every keystroke, debounce by `debounceMs` ms, then call `fetchSuggestions(query)`.
3. While waiting for the response, show a **loading** indicator.
4. On success, render the returned `Item[]` as a list.
5. On error, show an **error** message.
6. If the returned array is empty (and there was no error), show an **empty** state ("No results").
7. Clear / close the dropdown when the input is cleared.

### Stale-response handling
- Requests can return out of order (network jitter). Only apply the response
  that corresponds to the **latest** request. Ignore all earlier in-flight
  responses. Common patterns: an incrementing request ID / token, or
  `AbortController`.

### Keyboard navigation
| Key | Behavior |
|-----|----------|
| `ArrowDown` | Move highlight to next item (wraps from last to first) |
| `ArrowUp` | Move highlight to previous item (wraps from first to last) |
| `Enter` | Select the currently highlighted item |
| `Escape` | Close the dropdown and return focus to the input |

### Match highlighting
- Within each suggestion label, **bold** (or otherwise visually highlight) the
  substring that matches the current query (case-insensitive).

### Click-outside to close
- Clicking anywhere outside the component (input + dropdown) should close the
  dropdown without selecting an item.

---

## States to Handle

| State | Description |
|-------|-------------|
| `idle` | Input is empty or dropdown is closed |
| `loading` | Fetch is in progress |
| `success` | Items are available to display |
| `error` | The fetch threw or rejected |
| `empty` | Fetch succeeded but returned `[]` |

---

## Accessibility Requirements

- The `<input>` must have `role="combobox"`, `aria-expanded`, `aria-autocomplete="list"`,
  and `aria-controls` pointing to the listbox element's `id`.
- The dropdown list must have `role="listbox"`.
- Each option must have `role="option"` and a unique `id` attribute.
- The `<input>` must carry `aria-activedescendant` set to the `id` of the
  currently highlighted option (or removed when nothing is highlighted).
- Use `aria-busy="true"` on the listbox while loading.
- Announce errors via `role="alert"` or `aria-live="assertive"`.

---

## Edge Cases

1. **Rapid typing** — many debounced calls fire; only the last should take effect.
2. **Unmounted component** — if the component unmounts while a fetch is pending,
   the response must be discarded (no `setState` on unmounted component).
3. **Network error / rejected promise** — must transition to the `error` state
   rather than crashing.
4. **Very long labels** — the dropdown must not overflow the viewport; apply CSS
   `max-height` + `overflow-y: auto`.
5. **Single character query** — decide whether to fetch immediately or require a
   minimum length (e.g. `>= 2` chars). Document your decision.
6. **Re-selecting the same item** — `onSelect` should still fire.
7. **Controlled vs. uncontrolled input value** — keep it simple: manage the input
   value in local state (uncontrolled pattern relative to the parent).

---

## Performance Notes

- **Debounce** avoids hammering the network on every keystroke.
- **AbortController** (or a request-ID counter) prevents stale-response bugs and
  also allows cancelling in-flight HTTP requests when a newer query supersedes them.
- Avoid re-rendering the entire list on every keystroke: the list only changes
  when the fetch resolves, not while the user is typing.
- `useCallback` / `useMemo` are useful but not required for correctness; focus on
  correctness first.

---

## Follow-up Questions

1. **Caching** — How would you cache results so that typing the same prefix twice
   does not fire a second network request? What eviction policy would you use?

2. **Virtualization** — If `fetchSuggestions` can return thousands of items, how
   would you render only the visible rows? Which library or technique would you
   reach for, and why?

3. **Controlled component** — How would you refactor `Autocomplete` to be a
   *controlled* component where the parent owns the input value and the selected
   item, similar to how `<input value={v} onChange={…} />` works in React?
