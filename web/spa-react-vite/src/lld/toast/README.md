# LLD Problem: Toast / Notification System

## Problem Statement

Implement a `ToastProvider` component (React context) and a `useToast()` hook that lets any descendant component imperatively show transient notification messages (toasts). Toasts support multiple variants, auto-dismiss, manual dismiss, stacking with a cap, configurable screen position, and accessibility via ARIA live regions.

---

## Requirements

### Functional

1. `ToastProvider` wraps the app (or a subtree) and renders all active toasts.
2. `useToast()` returns `{ show }` where `show(message, opts?)` adds a new toast.
3. Each toast has:
   - A `message` (string).
   - A `variant`: `"success" | "error" | "info"` (default: `"info"`).
   - An `autoDismissMs` timeout in milliseconds after which the toast removes itself (default: `4000`; pass `0` to disable auto-dismiss).
   - A `dismiss` button the user can click to remove it immediately.
4. Toasts stack visually; only `maxVisible` toasts are shown at once (default: `3`). Older toasts beyond the cap are discarded or queued (your choice — document it).
5. `ToastProvider` accepts a `position` prop controlling where toasts render on screen: `"top-right" | "top-left" | "bottom-right" | "bottom-left" | "top-center" | "bottom-center"`.
6. `ToastProvider` accepts optional `defaultAutoDismissMs` and `defaultMaxVisible` props that serve as defaults when `show()` is called without those options.

### Non-functional

- No external libraries — only React (hooks + context).
- Strict TypeScript: all props, state, and context values must be typed.
- Each toast must have a unique `id` (use `crypto.randomUUID()` or a counter).

---

## Component / Hook API (TypeScript interfaces)

```ts
type ToastVariant = "success" | "error" | "info";

type ToastPosition =
  | "top-right"
  | "top-left"
  | "bottom-right"
  | "bottom-left"
  | "top-center"
  | "bottom-center";

interface ToastOptions {
  variant?: ToastVariant;           // default: "info"
  autoDismissMs?: number;           // default: 4000; 0 = no auto-dismiss
}

interface Toast {
  id: string;
  message: string;
  variant: ToastVariant;
  autoDismissMs: number;
}

interface ToastContextValue {
  show: (message: string, opts?: ToastOptions) => void;
}

interface ToastProviderProps {
  children: React.ReactNode;
  position?: ToastPosition;          // default: "top-right"
  defaultAutoDismissMs?: number;     // default: 4000
  maxVisible?: number;               // default: 3
}
```

`useToast()` must throw (or warn) if called outside a `ToastProvider`.

---

## States to Handle

| State | Description |
|---|---|
| Empty | No active toasts — the live region container still exists in the DOM (for screen readers) but is visually hidden or empty. |
| Single toast | One toast visible; auto-dismiss timer running. |
| Multiple toasts | Up to `maxVisible` toasts stacked; each has its own independent timer. |
| Cap reached | A new `show()` call arrives when `maxVisible` toasts are already visible. Decide: drop oldest, queue newest, or drop newest — document the choice. |
| Auto-dismiss | A timer fires and removes the toast without user action. |
| Manual dismiss | User clicks the dismiss button; toast is removed immediately and its pending timer is cancelled. |
| Provider missing | `useToast()` is called outside the provider; must not silently fail. |

---

## Accessibility Requirements

- The toast container must have `role="status"` for `"info"` / `"success"` toasts (polite announcement) and `role="alert"` for `"error"` toasts (assertive announcement).
- Because role cannot change at runtime on the same element, maintain **two** live-region containers: one with `role="status" aria-live="polite"` and one with `role="alert" aria-live="assertive"`. Route each toast to the correct container.
- Each toast item should have `aria-label` or visible text that fully describes it (variant + message).
- The dismiss button must have an accessible label: `aria-label="Dismiss notification"` (or similar).
- Focus must not move to a toast automatically; toasts are non-interactive notifications.
- Keyboard: the dismiss button must be reachable via Tab and activatable via Enter/Space (standard `<button>` behavior).
- Do not remove a toast from the DOM while it has focus; move focus first or defer removal.

---

## Edge Cases

1. `show()` called multiple times in the same synchronous tick (e.g., inside `Promise.all` callbacks) — all toasts should appear; state updates must be batched correctly (use functional state updater).
2. `autoDismissMs = 0` — no timer is set; the toast persists until manually dismissed.
3. Component unmount while a timer is pending — clear all `setTimeout` handles in the cleanup of `useEffect` to avoid setState-on-unmounted-component warnings.
4. Very long messages — the toast container must not overflow the viewport; apply `max-width` and word-wrap.
5. Rapid dismiss + re-show — stale closures over the toast list can cause lost updates; always use the functional form of the state setter.
6. `maxVisible` prop changes at runtime — decide whether the cap applies immediately (truncate current list) or only on next `show()` call; document it.
7. Duplicate messages — two identical messages should still create two separate toasts with distinct `id`s.

---

## Performance Notes

- Each toast's auto-dismiss `setTimeout` should be set inside a `useEffect` that depends only on that toast's `id` and `autoDismissMs`. Avoid restarting timers for unrelated state changes.
- Memoize the context value (`useMemo`) so consumers do not re-render on every provider render.
- The dismiss callback passed into each toast item should be stable (`useCallback`) to prevent unnecessary child re-renders.
- If you use `useReducer` instead of `useState`, the reducer must be a pure function defined outside the component to avoid re-creation on each render.

---

## Follow-up Questions

1. **Queuing vs. dropping**: When `maxVisible` is exceeded, should excess toasts be queued and shown after the current ones dismiss, or dropped immediately? What are the UX trade-offs? How would you implement a queue without losing the ordering guarantee when multiple timers fire close together?

2. **Animation**: How would you add enter/exit animations (CSS transitions or a library like `@react-spring/web`) without breaking the ARIA live-region announcements? What timing issues arise if you remove the DOM node before the exit animation completes?

3. **Testing**: How would you unit-test the auto-dismiss behavior with Jest + React Testing Library? Which parts would you use `jest.useFakeTimers()` for, and what would you assert to confirm a toast disappears after the timeout?
