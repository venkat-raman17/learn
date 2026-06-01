import React, {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useReducer,
  useRef,
} from "react";
import ReactDOM from "react-dom";
import "./Toast.css";

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
  variant?: ToastVariant;
  autoDismissMs?: number;
}

export interface Toast {
  id: string;
  message: string;
  variant: ToastVariant;
  autoDismissMs: number;
}

export interface ToastContextValue {
  show: (message: string, opts?: ToastOptions) => void;
}

export interface ToastProviderProps {
  children: React.ReactNode;
  position?: ToastPosition;
  defaultAutoDismissMs?: number;
  maxVisible?: number;
}

// ---------------------------------------------------------------------------
// Reducer
// ---------------------------------------------------------------------------

type ToastAction =
  | { type: "ADD"; toast: Toast }
  | { type: "REMOVE"; id: string };

function toastReducer(state: Toast[], action: ToastAction): Toast[] {
  switch (action.type) {
    case "ADD":
      return [...state, action.toast];
    case "REMOVE":
      return state.filter((t) => t.id !== action.id);
    default:
      return state;
  }
}

// ---------------------------------------------------------------------------
// Context
// ---------------------------------------------------------------------------

const ToastContext = createContext<ToastContextValue | null>(null);

// ---------------------------------------------------------------------------
// Hook
// ---------------------------------------------------------------------------

export function useToast(): ToastContextValue {
  const ctx = useContext(ToastContext);
  if (ctx === null) {
    throw new Error("useToast must be called inside a <ToastProvider>.");
  }
  return ctx;
}

// ---------------------------------------------------------------------------
// Icons
// ---------------------------------------------------------------------------

const ICONS: Record<ToastVariant, string> = {
  success: "✓",
  error: "✕",
  info: "ℹ",
};

// ---------------------------------------------------------------------------
// Provider
// ---------------------------------------------------------------------------

export function ToastProvider({
  children,
  position = "top-right",
  defaultAutoDismissMs = 4000,
  maxVisible = 3,
}: ToastProviderProps): React.ReactElement {
  const [allToasts, dispatch] = useReducer(toastReducer, []);

  // Map of toast id → timer id so we can clear on manual dismiss
  const timersRef = useRef<Map<string, ReturnType<typeof setTimeout>>>(
    new Map(),
  );

  const remove = useCallback((id: string) => {
    const timer = timersRef.current.get(id);
    if (timer !== undefined) {
      clearTimeout(timer);
      timersRef.current.delete(id);
    }
    dispatch({ type: "REMOVE", id });
  }, []);

  const show = useCallback(
    (message: string, opts?: ToastOptions) => {
      const toast: Toast = {
        id: crypto.randomUUID(),
        message,
        variant: opts?.variant ?? "info",
        autoDismissMs:
          opts?.autoDismissMs !== undefined
            ? opts.autoDismissMs
            : defaultAutoDismissMs,
      };

      dispatch({ type: "ADD", toast });

      if (toast.autoDismissMs > 0) {
        const timer = setTimeout(() => remove(toast.id), toast.autoDismissMs);
        timersRef.current.set(toast.id, timer);
      }
    },
    [defaultAutoDismissMs, remove],
  );

  const contextValue = useMemo(() => ({ show }), [show]);

  // Cap visible toasts at maxVisible
  const visibleToasts = allToasts.slice(-maxVisible);

  // Separate info/success vs error for ARIA live regions
  const politeToasts = visibleToasts.filter(
    (t) => t.variant === "success" || t.variant === "info",
  );
  const assertiveToasts = visibleToasts.filter((t) => t.variant === "error");

  const containerCls = `toast-container toast-pos-${position}`;

  const toastEl = (
    <>
      {/* Polite live region (info + success) */}
      <div role="status" aria-live="polite" aria-atomic="false">
        {politeToasts.map((t) => (
          <ToastItem key={t.id} toast={t} onDismiss={remove} />
        ))}
      </div>

      {/* Assertive live region (errors) */}
      <div role="alert" aria-live="assertive" aria-atomic="false">
        {assertiveToasts.map((t) => (
          <ToastItem key={t.id} toast={t} onDismiss={remove} />
        ))}
      </div>

      {/* Visual container — mirrors both regions in one positioned div */}
      <div className={containerCls} aria-hidden="true">
        {visibleToasts.map((t) => (
          <ToastItem key={t.id} toast={t} onDismiss={remove} visual />
        ))}
      </div>
    </>
  );

  return (
    <ToastContext.Provider value={contextValue}>
      {children}
      {ReactDOM.createPortal(toastEl, document.body)}
    </ToastContext.Provider>
  );
}

// ---------------------------------------------------------------------------
// Individual toast item
// ---------------------------------------------------------------------------

interface ToastItemProps {
  toast: Toast;
  onDismiss: (id: string) => void;
  visual?: boolean;
}

function ToastItem({ toast, onDismiss, visual }: ToastItemProps) {
  return (
    <div
      className={`toast toast-${toast.variant}`}
      style={visual ? undefined : { position: "absolute", width: 1, height: 1, overflow: "hidden", clip: "rect(0,0,0,0)", whiteSpace: "nowrap" }}
    >
      <span className="toast-icon" aria-hidden="true">
        {ICONS[toast.variant]}
      </span>
      <span className="toast-message">{toast.message}</span>
      <button
        type="button"
        className="toast-dismiss"
        aria-label="Dismiss notification"
        onClick={() => onDismiss(toast.id)}
      >
        ×
      </button>
    </div>
  );
}
