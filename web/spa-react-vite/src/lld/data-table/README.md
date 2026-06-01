# LLD Practice: DataTable

## Problem Statement

Build a generic, reusable `DataTable<T>` component in React + TypeScript that renders tabular data with client-side sort, filter, pagination, sticky header, and row selection. The component must use real `<table>` semantics and be fully accessible.

---

## Component Props / API

```typescript
// A single column definition for a row type T
interface ColumnDef<T> {
  /** Unique key used to access data on row objects (keyof T or any string for computed cols) */
  key: string;
  /** Text displayed in the column header */
  header: string;
  /** Custom cell renderer. Receives the full row value. Defaults to String(row[key as keyof T]) */
  render?: (row: T, rowIndex: number) => React.ReactNode;
  /** Whether clicking the header triggers onSort for this column */
  sortable?: boolean;
}

type SortDirection = 'asc' | 'desc' | 'none';

interface SortState {
  key: string;
  direction: Exclude<SortDirection, 'none'>;
}

interface DataTableProps<T> {
  /** Column definitions */
  columns: ColumnDef<T>[];
  /** Rows to display */
  data: T[];
  /** Number of rows per page. Omit to disable pagination. */
  pageSize?: number;
  /**
   * Called when the user requests a sort on a column.
   * Receives the column key and the new direction.
   * If provided, the parent owns sort state (controlled).
   * If omitted, the component manages sort internally (uncontrolled).
   */
  onSort?: (key: string, direction: SortDirection) => void;
  /** Controlled sort state (pair with onSort for server-side sort) */
  sortState?: SortState;
  /** Whether rows are selectable via checkboxes */
  selectable?: boolean;
  /** Controlled selected row indices */
  selectedRows?: Set<number>;
  /** Called when selection changes */
  onSelectionChange?: (selected: Set<number>) => void;
  /** Show loading skeleton instead of rows */
  loading?: boolean;
  /** Message to show when data is empty (default: "No data available.") */
  emptyMessage?: string;
  /** Optional accessible label for the table */
  caption?: string;
  /** Additional CSS class on the <table> element */
  className?: string;
}
```

---

## States to Handle

| State | Description |
|---|---|
| Normal | Rows visible, header shows column names |
| Loading | `loading=true` — render a skeleton or spinner, no data rows |
| Empty | `data` is an empty array — render an empty-state message spanning all columns |
| Sorted (asc/desc) | Column header shows active sort direction; `aria-sort` attribute updates |
| Paginated | Footer shows current page indicator; prev/next buttons |
| Row selected | Checkbox checked; row visually highlighted |
| All selected | "Select all" header checkbox checked (or indeterminate if partial) |

---

## Accessibility Requirements (ARIA + Keyboard)

- Use a real `<table>`, `<thead>`, `<tbody>`, `<tr>`, `<th>`, `<td>` — never `<div>` grid.
- Add `role="grid"` on `<table>` if rows are interactive (selectable).
- Sortable column headers: use `<th scope="col" aria-sort="ascending|descending|none">`.
  - Cycle: none → ascending → descending → none.
  - The sort trigger inside `<th>` should be a `<button>` so it is keyboard-focusable.
- Selection checkboxes: label each with `aria-label="Select row N"` (or use `<label>`).
  - Header checkbox: `aria-label="Select all rows"` with `aria-checked="mixed"` when partial.
- Provide a `<caption>` element when the `caption` prop is set (improves screen reader context).
- Keyboard navigation:
  - Tab/Shift-Tab moves focus through interactive elements (sort buttons, checkboxes, pagination).
  - Arrow keys inside the table for grid navigation are a stretch goal (see follow-up #3).
- Pagination buttons must have `aria-label` e.g. "Go to previous page", "Go to next page".
- Announce page changes to screen readers via an `aria-live="polite"` region.

---

## Edge Cases

1. **Empty columns array** — render an empty table with a single empty row or a clear message.
2. **Data changes while on page N > 1** — reset to page 1 if new data has fewer pages.
3. **pageSize larger than data length** — show all rows on a single page; hide pagination controls.
4. **Column key not present on row** — `render` is missing AND `row[key]` is `undefined`; render empty cell gracefully (no crash).
5. **Controlled vs uncontrolled sort** — if `onSort` is provided without `sortState`, warn in dev; if `sortState` is provided without `onSort`, sort arrows display but clicking does nothing (or fire `onSort` if provided).
6. **Selection with pagination** — decide: does `selectedRows` hold indices into `data` (global) or the current page? Document your choice.
7. **Very long cell content** — overflow/truncation strategy (CSS, `title` attribute).
8. **React key collisions** — rows must have stable keys; use index only as fallback.

---

## Performance Notes

- **Memoize derived data**: wrap sorted/filtered/paginated slices in `useMemo` to avoid recomputation on unrelated re-renders.
- **Stable column references**: if `columns` is defined inline in the parent, wrap it in `useMemo` to prevent re-renders inside DataTable.
- **Row virtualization follow-up**: for large datasets (1000+ rows), rendering all `<tr>` elements causes layout thrash. Consider a windowing approach (see follow-up #1).
- **Avoid anonymous render fns in JSX**: inline arrow functions in the `render` prop of `ColumnDef` create new references every render — document this gotcha for users.

---

## Follow-Up Questions

1. **Row Virtualization**: The current implementation renders every row in the DOM. How would you add row virtualization (render only visible rows + a buffer) without an external library? Describe the scroll container, sentinel approach, and how sticky header interacts with the virtual scroll offset.

2. **Server-Side Sort & Filter**: Suppose the dataset is too large for the client to hold entirely. How do you refactor `DataTable` so that `onSort`, `onFilter`, and `onPageChange` all become controlled callbacks that trigger API calls in the parent? What internal state disappears and what new props are needed?

3. **Full Keyboard Grid Navigation**: WCAG 2.1 pattern `role="grid"` requires arrow-key navigation between cells. Describe how you would implement `ArrowUp/Down/Left/Right` focus management using `useRef` + a 2D focus map, and how Tab vs Arrow key semantics differ in a grid widget.
