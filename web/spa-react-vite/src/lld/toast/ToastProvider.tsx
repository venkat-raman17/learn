import React, { createContext, useContext } from "react";

// ---------------------------------------------------------------------------
// Types
// ---------------------------------------------------------------------------

export type ToastVariant = "success" | "error" | "info";

export type ToastPosition =
  | "top-right"
  | "top-left"
  | "bottom-right"
  | "bottom-left"
  | "top-center"
  | "bottom-center";

export interface ToastOptions {
  /** Visual style of the notification. Defaults to "info". */
  variant?: ToastVariant;
  /**
   * Milliseconds before the toast auto-dismisses.
   * Pass 0 to disable auto-dismiss.
   * Falls back to the provider's defaultAutoDismissMs (default 4000).
   */
  autoDismissMs?: number;
}

export interface Toast {
  /** Unique identifier for this toast instance. */
  id: string;
  /** The text content displayed inside the toast. */
  message: string;
  /** Resolved variant (never undefined at runtime). */
  variant: ToastVariant;
  /** Resolved auto-dismiss timeout in ms (0 = no auto-dismiss). */
  autoDismissMs: number;
}

export interface ToastContextValue {
  /**
   * Imperatively show a toast notification.
   * @param message - The text to display.
   * @param opts    - Optional variant and dismiss timeout.
   */
  show: (message: string, opts?: ToastOptions) => void;
}

export interface ToastProviderProps {
  children: React.ReactNode;
  /** Where on screen toasts are rendered. Defaults to "top-right". */
  position?: ToastPosition;
  /** Fallback auto-dismiss timeout used when show() omits autoDismissMs. Defaults to 4000. */
  defaultAutoDismissMs?: number;
  /** Maximum number of toasts visible at one time. Defaults to 3. */
  maxVisible?: number;
}

// ---------------------------------------------------------------------------
// Context
// ---------------------------------------------------------------------------

const ToastContext = createContext<ToastContextValue | null>(null);

// ---------------------------------------------------------------------------
// Hook
// ---------------------------------------------------------------------------

/**
 * Returns { show } for displaying toast notifications.
 * Must be called inside a <ToastProvider>.
 */
export function useToast(): ToastContextValue {
  const ctx = useContext(ToastContext);
  if (ctx === null) {
    throw new Error("useToast must be called inside a <ToastProvider>.");
  }
  return ctx;
}

// ---------------------------------------------------------------------------
// Provider (stub — implement the real behavior)
// ---------------------------------------------------------------------------

/**
 * Wrap your app (or a subtree) with ToastProvider to enable toast notifications.
 *
 * TODO: implement the real behavior:
 *  - Maintain a list of active Toast objects in state.
 *  - Expose show() via context, generating a unique id per toast.
 *  - Start a setTimeout for each toast whose autoDismissMs > 0; clear it on
 *    manual dismiss or unmount (useEffect cleanup).
 *  - Enforce maxVisible: decide whether excess toasts are queued or dropped.
 *  - Render two ARIA live-region containers:
 *      <div role="status"  aria-live="polite">   for info + success toasts
 *      <div role="alert"   aria-live="assertive"> for error toasts
 *  - Position the container via inline styles or CSS classes derived from
 *    the position prop.
 *  - Memoize the context value with useMemo to avoid unnecessary re-renders.
 */
export function ToastProvider({
  children,
  position = "top-right",
  defaultAutoDismissMs = 4000,
  maxVisible = 3,
}: ToastProviderProps): React.ReactElement {
  // Silence "unused variable" warnings on the stub props so tsc --strict passes.
  void position;
  void defaultAutoDismissMs;
  void maxVisible;

  // TODO: replace this stub context value with the real implementation.
  const stubContextValue: ToastContextValue = {
    show: (_message: string, _opts?: ToastOptions): void => {
      // TODO: implement
    },
  };

  return (
    <ToastContext.Provider value={stubContextValue}>
      {children}
      {/* TODO: render the positioned toast container + ARIA live regions here */}
      <div
        style={{
          position: "fixed",
          bottom: 16,
          right: 16,
          background: "#f3f4f6",
          border: "1px dashed #9ca3af",
          borderRadius: 6,
          padding: "8px 12px",
          fontSize: 12,
          color: "#6b7280",
          pointerEvents: "none",
        }}
        aria-hidden="true"
      >
        TODO: implement ToastProvider
      </div>
    </ToastContext.Provider>
  );
}
