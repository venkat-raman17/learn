# LLD Problem: Design-System Button

## Problem Statement

Design and implement a composable, accessible `Button` component for a design system. The component must support multiple visual variants, sizes, loading states, icon slots, and full-width layout — all with a clean, strongly-typed TypeScript API that extends native button semantics.

---

## Component API

```ts
type ButtonVariant = 'primary' | 'secondary' | 'ghost' | 'danger';
type ButtonSize    = 'sm' | 'md' | 'lg';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  /** Visual style variant. Default: 'primary' */
  variant?: ButtonVariant;

  /** Size preset. Default: 'md' */
  size?: ButtonSize;

  /** Shows a spinner and sets aria-busy; disables interaction. Default: false */
  loading?: boolean;

  /** Icon rendered before the label (any React node, typically an SVG). */
  leftIcon?: React.ReactNode;

  /** Icon rendered after the label. */
  rightIcon?: React.ReactNode;

  /** Stretches the button to 100% of its container width. Default: false */
  fullWidth?: boolean;
}
```

The component must forward an `HTMLButtonElement` ref so callers can integrate with focus management, form libraries, and animations.

---

## Requirements

### Functional

1. Render a `<button>` element with the correct `type` attribute (default `"button"`, not `"submit"`).
2. Accept and spread all native `React.ButtonHTMLAttributes<HTMLButtonElement>` props (e.g. `onClick`, `form`, `name`, `value`, `aria-*`, `data-*`).
3. Apply distinct visual styles for each `variant` (primary, secondary, ghost, danger).
4. Apply distinct sizing (padding, font-size, icon size) for each `size`.
5. When `loading` is `true`:
   - Render a spinner (or loading indicator) in place of or alongside the label.
   - Prevent user interaction (the button must behave as disabled).
6. Render `leftIcon` before children and `rightIcon` after children, with appropriate spacing.
7. When `fullWidth` is `true`, the button's block size fills its containing element.

### Disabled State

- Honour the native `disabled` prop passed by the caller.
- When either `disabled` or `loading` is truthy, the button must not fire `onClick` (use `pointer-events: none` or conditional handler removal).
- Visual opacity/cursor must reflect the disabled state.

---

## States to Handle

| State    | Trigger                              | Visual Expectation                              |
|----------|--------------------------------------|-------------------------------------------------|
| Default  | No special state                     | Variant base styles                             |
| Hover    | Mouse over (not disabled/loading)    | Slightly darker/lighter background or border    |
| Focus    | Keyboard focus or click+focus        | Visible focus ring (at least 2px outline)       |
| Active   | Mouse/touch pressed                  | Pressed-down visual (scale or darker shade)     |
| Disabled | `disabled` prop is `true`            | Reduced opacity, `not-allowed` cursor           |
| Loading  | `loading` prop is `true`             | Spinner visible, opacity reduced, `aria-busy`   |

---

## Accessibility Requirements

1. **Focus ring** — Never remove the focus outline via `outline: none` without providing an equivalent visible replacement. Use `:focus-visible` for keyboard-only ring.
2. **`aria-busy`** — Set `aria-busy="true"` on the `<button>` when `loading` is `true`.
3. **`aria-disabled`** — When the button is effectively disabled (either `disabled` or `loading`), set `aria-disabled="true"`. Note: `aria-disabled` does NOT remove the element from the tab order, whereas the native `disabled` attribute does. Choose the approach intentionally.
4. **Spinner label** — The loading spinner must have `aria-hidden="true"` and the button must carry a descriptive accessible label (via `children` or `aria-label`) so screen readers announce the action, not the spinner markup.
5. **Role** — No explicit `role` override needed; `<button>` already carries `role="button"`.
6. **Keyboard** — Enter and Space must trigger the button's action (native behaviour for `<button>`, but verify it is not broken by any `onKeyDown` override).

---

## Variant Styling Approach

Design the styling so that variant and size are orthogonal concerns that can be applied independently. Two common strategies:

- **CSS class composition** — maintain a map from variant/size to CSS class names; concatenate them.
- **CSS-in-JS / inline styles** — maintain a map from variant/size to style objects.

Either approach is valid. What matters is that the mapping is centralised, easy to extend, and does not scatter conditional logic throughout the JSX.

---

## Edge Cases

1. **No children** — A button used as an icon-only button (no text, only `leftIcon`). Must still be accessible via `aria-label`.
2. **Both `disabled` and `loading`** — Both props may be truthy simultaneously; the component must handle this without double-applying styles incorrectly.
3. **`type` override** — A caller may pass `type="submit"` inside a form; the component must not hard-code the type.
4. **`onClick` while loading** — If the caller still passes `onClick` and `loading` is `true`, the handler must NOT execute.
5. **Icon without label** — If only `leftIcon` or `rightIcon` is provided, spacing logic must not add a gap on the empty side.
6. **Ref forwarding** — A caller that passes `ref` must receive an `HTMLButtonElement` ref, not a component ref.

---

## Performance Notes

- The variant/size class or style lookup should be a constant map defined outside the component body so it is not recreated on every render.
- Avoid anonymous inline functions in the JSX (`onClick={() => ...}`) inside the component itself — any handler wrapping should use `useCallback` or be factored out.
- The spinner element should only be mounted when `loading` is `true`, not hidden via CSS, to reduce DOM size in the common non-loading path.

---

## Follow-up Questions

1. **Polymorphic button** — How would you extend the API to support rendering as an `<a>` element (link-styled button) while preserving full type safety? What TypeScript pattern (`as` prop, overloaded signatures, or a discriminated union) would you choose and why?

2. **Token-based theming** — If the design system switches to CSS custom properties (design tokens) for colours, how would you refactor the variant map so that dark-mode and brand theming work without shipping multiple CSS class maps?

3. **Loading accessibility** — The current spec silently disables the button while loading. An alternative is to keep it interactive and announce progress with a live region. What are the tradeoffs, and when would you choose the live-region approach over `aria-busy` + disabled?
