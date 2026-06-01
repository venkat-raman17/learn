import React, { useEffect, useReducer, useRef } from "react";
import "./Autocomplete.css";

// ---------------------------------------------------------------------------
// Types
// ---------------------------------------------------------------------------

export interface Item {
  id: string;
  label: string;
}

export interface AutocompleteProps {
  fetchSuggestions: (query: string) => Promise<Item[]>;
  onSelect: (item: Item) => void;
  debounceMs?: number;
  placeholder?: string;
}

type FetchStatus = "idle" | "loading" | "success" | "error" | "empty";

interface AutocompleteState {
  query: string;
  items: Item[];
  status: FetchStatus;
  activeIndex: number;
  isOpen: boolean;
}

// ---------------------------------------------------------------------------
// Reducer
// ---------------------------------------------------------------------------

type Action =
  | { type: "SET_QUERY"; query: string }
  | { type: "SET_LOADING" }
  | { type: "SET_ITEMS"; items: Item[] }
  | { type: "SET_ERROR" }
  | { type: "SET_ACTIVE_INDEX"; index: number }
  | { type: "SELECT_ITEM"; label: string }
  | { type: "CLOSE" }
  | { type: "RESET" };

function reducer(state: AutocompleteState, action: Action): AutocompleteState {
  switch (action.type) {
    case "SET_QUERY":
      return { ...state, query: action.query };
    case "SET_LOADING":
      return { ...state, status: "loading", isOpen: true };
    case "SET_ITEMS":
      return {
        ...state,
        items: action.items,
        status: action.items.length === 0 ? "empty" : "success",
        isOpen: true,
        activeIndex: -1,
      };
    case "SET_ERROR":
      return { ...state, status: "error", isOpen: true };
    case "SET_ACTIVE_INDEX":
      return { ...state, activeIndex: action.index };
    case "SELECT_ITEM":
      return { ...state, query: action.label, isOpen: false, activeIndex: -1 };
    case "CLOSE":
      return { ...state, isOpen: false, activeIndex: -1 };
    case "RESET":
      return { query: "", items: [], status: "idle", activeIndex: -1, isOpen: false };
    default:
      return state;
  }
}

// ---------------------------------------------------------------------------
// Highlight helper — wraps the matched substring in <strong>
// ---------------------------------------------------------------------------

function highlightMatch(label: string, query: string): React.ReactNode {
  if (!query) return label;
  const idx = label.toLowerCase().indexOf(query.toLowerCase());
  if (idx === -1) return label;
  return (
    <>
      {label.slice(0, idx)}
      <strong>{label.slice(idx, idx + query.length)}</strong>
      {label.slice(idx + query.length)}
    </>
  );
}

// ---------------------------------------------------------------------------
// Component
// ---------------------------------------------------------------------------

export const Autocomplete: React.FC<AutocompleteProps> = ({
  fetchSuggestions,
  onSelect,
  debounceMs = 300,
  placeholder,
}) => {
  const [state, dispatch] = useReducer(reducer, {
    query: "",
    items: [],
    status: "idle",
    activeIndex: -1,
    isOpen: false,
  });

  const debounceTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const requestIdRef = useRef<number>(0);
  const containerRef = useRef<HTMLDivElement>(null);
  const listboxId = "autocomplete-listbox";

  // Click-outside closes the dropdown
  useEffect(() => {
    const handleMouseDown = (e: MouseEvent) => {
      if (
        containerRef.current &&
        !containerRef.current.contains(e.target as Node)
      ) {
        dispatch({ type: "CLOSE" });
      }
    };
    document.addEventListener("mousedown", handleMouseDown);
    return () => document.removeEventListener("mousedown", handleMouseDown);
  }, []);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
    const q = e.target.value;
    dispatch({ type: "SET_QUERY", query: q });

    if (debounceTimerRef.current) clearTimeout(debounceTimerRef.current);

    if (!q.trim()) {
      dispatch({ type: "RESET" });
      return;
    }

    // Signal that a new request is in flight; discard stale responses
    const reqId = ++requestIdRef.current;
    dispatch({ type: "SET_LOADING" });

    debounceTimerRef.current = setTimeout(async () => {
      try {
        const items = await fetchSuggestions(q);
        if (reqId === requestIdRef.current) {
          dispatch({ type: "SET_ITEMS", items });
        }
      } catch {
        if (reqId === requestIdRef.current) {
          dispatch({ type: "SET_ERROR" });
        }
      }
    }, debounceMs);
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>): void => {
    if (!state.isOpen) return;

    switch (e.key) {
      case "ArrowDown": {
        e.preventDefault();
        dispatch({
          type: "SET_ACTIVE_INDEX",
          index: Math.min(state.activeIndex + 1, state.items.length - 1),
        });
        break;
      }
      case "ArrowUp": {
        e.preventDefault();
        dispatch({
          type: "SET_ACTIVE_INDEX",
          index: Math.max(state.activeIndex - 1, 0),
        });
        break;
      }
      case "Enter": {
        const item = state.items[state.activeIndex];
        if (item) {
          dispatch({ type: "SELECT_ITEM", label: item.label });
          onSelect(item);
        }
        break;
      }
      case "Escape":
        dispatch({ type: "CLOSE" });
        break;
    }
  };

  const handleItemClick = (item: Item): void => {
    dispatch({ type: "SELECT_ITEM", label: item.label });
    onSelect(item);
  };

  const activeDescendant =
    state.activeIndex >= 0
      ? `autocomplete-option-${state.activeIndex}`
      : undefined;

  const showDropdown =
    state.isOpen &&
    (state.status === "loading" ||
      state.status === "error" ||
      state.status === "empty" ||
      state.status === "success");

  return (
    <div ref={containerRef} className="autocomplete">
      <input
        className="autocomplete-input"
        role="combobox"
        aria-autocomplete="list"
        aria-expanded={state.isOpen && state.status === "success"}
        aria-controls={listboxId}
        aria-activedescendant={activeDescendant}
        placeholder={placeholder}
        value={state.query}
        onChange={handleInputChange}
        onKeyDown={handleKeyDown}
        autoComplete="off"
      />

      {showDropdown && (
        <div className="autocomplete-dropdown">
          {state.status === "loading" && (
            <div className="autocomplete-status">Loading…</div>
          )}
          {state.status === "error" && (
            <div className="autocomplete-status autocomplete-error">
              Error fetching suggestions.
            </div>
          )}
          {state.status === "empty" && (
            <div className="autocomplete-status">No results found.</div>
          )}
          {state.status === "success" && (
            <ul id={listboxId} role="listbox" className="autocomplete-list">
              {state.items.map((item, index) => (
                <li
                  key={item.id}
                  id={`autocomplete-option-${index}`}
                  role="option"
                  aria-selected={index === state.activeIndex}
                  className={`autocomplete-option${
                    index === state.activeIndex
                      ? " autocomplete-option--active"
                      : ""
                  }`}
                  onMouseDown={(e) => {
                    e.preventDefault(); // keep focus on input
                    handleItemClick(item);
                  }}
                >
                  {highlightMatch(item.label, state.query)}
                </li>
              ))}
            </ul>
          )}
        </div>
      )}
    </div>
  );
};

export default Autocomplete;
