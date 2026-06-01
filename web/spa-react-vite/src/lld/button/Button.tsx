import React from 'react';

// ---------------------------------------------------------------------------
// Types
// ---------------------------------------------------------------------------

export type ButtonVariant = 'primary' | 'secondary' | 'ghost' | 'danger';
export type ButtonSize = 'sm' | 'md' | 'lg';

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  /**
   * Visual style variant.
   * @default 'primary'
   */
  variant?: ButtonVariant;

  /**
   * Size preset controlling padding and font size.
   * @default 'md'
   */
  size?: ButtonSize;

  /**
   * When true, renders a loading spinner, prevents interaction,
   * and sets aria-busy on the underlying <button>.
   * @default false
   */
  loading?: boolean;

  /**
   * Node rendered to the left of the button label (e.g. an SVG icon).
   * Should be aria-hidden — the button label carries the accessible name.
   */
  leftIcon?: React.ReactNode;

  /**
   * Node rendered to the right of the button label (e.g. an SVG icon).
   */
  rightIcon?: React.ReactNode;

  /**
   * When true, the button stretches to 100% of its container width.
   * @default false
   */
  fullWidth?: boolean;
}

// ---------------------------------------------------------------------------
// Component
// ---------------------------------------------------------------------------

/**
 * Design-System Button
 *
 * A composable, accessible button that supports variants, sizes, icons,
 * loading state, and full-width layout.
 *
 * TODO: Implement the real behaviour.
 */
export const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  function Button(
    {
      variant = 'primary',
      size = 'md',
      loading = false,
      leftIcon,
      rightIcon,
      fullWidth = false,
      children,
      disabled,
      ...rest
    },
    ref,
  ) {
    // TODO: implement Button
    // Hints:
    //   1. Derive isDisabled = disabled || loading.
    //   2. Build a className (or style object) from variant + size maps.
    //   3. Suppress onClick when isDisabled is true.
    //   4. Render leftIcon, children, rightIcon with appropriate spacing.
    //   5. Render a spinner element (aria-hidden) when loading is true.
    //   6. Apply aria-busy={loading} and aria-disabled={isDisabled} to the <button>.
    //   7. Always default type to "button" unless the caller overrides it.

    return (
      <button ref={ref} disabled={disabled} {...rest}>
        {/* TODO: implement Button */}
        <span style={{ fontFamily: 'monospace', opacity: 0.5 }}>
          TODO: implement Button
        </span>
      </button>
    );
  },
);

Button.displayName = 'Button';

export default Button;
