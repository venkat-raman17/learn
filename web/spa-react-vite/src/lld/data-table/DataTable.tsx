import React from 'react';

// ---------------------------------------------------------------------------
// Sub-types
// ---------------------------------------------------------------------------

/** Direction a column can be sorted. 'none' means unsorted. */
export type SortDirection = 'asc' | 'desc' | 'none';

/** Active sort state: which column and which direction (never 'none'). */
export interface SortState {
  key: string;
  direction: Exclude<SortDirection, 'none'>;
}

/**
 * Definition for a single column in the table.
 * @template T - The row data type.
 */
export interface ColumnDef<T> {
  /**
   * Unique identifier for this column.
   * May be a key of T or any string for computed/synthetic columns.
   */
  key: string;
  /** Text rendered in the <th> header cell. */
  header: string;
  /**
   * Optional custom cell renderer.
   * Receives the full row object and its zero-based index in `data`.
   * When omitted, the component falls back to String(row[key as keyof T] ?? '').
   */
  render?: (row: T, rowIndex: number) => React.ReactNode;
  /** When true, the column header becomes a sort trigger. Default: false. */
  sortable?: boolean;
}

// ---------------------------------------------------------------------------
// Main props interface
// ---------------------------------------------------------------------------

export interface DataTableProps<T> {
  /** Column definitions controlling headers, cell rendering, and sort eligibility. */
  columns: ColumnDef<T>[];
  /** Array of row data objects. */
  data: T[];
  /**
   * Number of rows per page. When omitted, all rows are shown and pagination
   * controls are hidden.
   */
  pageSize?: number;
  /**
   * Callback fired when the user clicks a sortable column header.
   * Providing this prop makes sort controlled (the parent owns `sortState`).
   * When omitted, the component manages sort state internally.
   */
  onSort?: (key: string, direction: SortDirection) => void;
  /**
   * Controlled sort state. Pair with `onSort` for server-side sorting.
   * Ignored when `onSort` is not provided.
   */
  sortState?: SortState;
  /**
   * When true, each row renders a leading checkbox for selection.
   * Default: false.
   */
  selectable?: boolean;
  /**
   * Controlled set of selected row indices (indices into `data`).
   * Must be paired with `onSelectionChange`.
   */
  selectedRows?: Set<number>;
  /**
   * Called whenever the selection changes.
   * Receives the new complete set of selected row indices.
   */
  onSelectionChange?: (selected: Set<number>) => void;
  /**
   * When true, renders a loading skeleton instead of data rows.
   * Default: false.
   */
  loading?: boolean;
  /**
   * Message displayed inside the table when `data` is empty.
   * Default: "No data available."
   */
  emptyMessage?: string;
  /**
   * Accessible label rendered as a <caption> element.
   * Strongly recommended for screen-reader context.
   */
  caption?: string;
  /** Additional CSS class applied to the root <table> element. */
  className?: string;
}

// ---------------------------------------------------------------------------
// Component stub — implement the body below
// ---------------------------------------------------------------------------

/**
 * DataTable<T>
 *
 * A generic, accessible data table with client-side sort, pagination, and
 * optional row selection.
 *
 * TODO: implement the following inside the function body:
 *  1. Internal state: currentPage, internalSortState (uncontrolled mode),
 *     internalSelectedRows (uncontrolled mode).
 *  2. Derived data (useMemo):
 *     - sorted rows (respect controlled sortState vs internal state)
 *     - paginated slice
 *  3. Render:
 *     - <table> with real <thead>, <tbody>, <tfoot> (pagination)
 *     - Sortable <th> buttons with aria-sort attribute
 *     - Select-all checkbox in header (aria-checked="mixed" when partial)
 *     - Per-row checkboxes when selectable=true
 *     - Loading skeleton rows when loading=true
 *     - Empty-state row spanning all columns when data is empty
 *     - Pagination controls: prev/next buttons, page indicator
 *     - aria-live region for page-change announcements
 */
function DataTable<T>(props: DataTableProps<T>): React.ReactElement {
  // Destructure props so TypeScript confirms the full interface is used.
  const {
    columns,
    data,
    pageSize,
    onSort,
    sortState,
    selectable,
    selectedRows,
    onSelectionChange,
    loading,
    emptyMessage,
    caption,
    className,
  } = props;

  // Silence "unused variable" warnings in the stub — remove these when implementing.
  void columns;
  void data;
  void pageSize;
  void onSort;
  void sortState;
  void selectable;
  void selectedRows;
  void onSelectionChange;
  void loading;
  void emptyMessage;
  void caption;
  void className;

  // TODO: implement DataTable
  return (
    <div style={{ fontFamily: 'sans-serif', padding: '1rem', border: '1px dashed #888', borderRadius: 4 }}>
      <strong>TODO: implement DataTable</strong>
      <p style={{ margin: '0.5rem 0 0', color: '#555', fontSize: '0.875rem' }}>
        See README.md for the full spec, props interface, accessibility requirements, and follow-up questions.
      </p>
    </div>
  );
}

export default DataTable;
