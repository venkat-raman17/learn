import React, { useEffect, useMemo, useState } from 'react';
import './DataTable.css';

// ---------------------------------------------------------------------------
// Sub-types
// ---------------------------------------------------------------------------

export type SortDirection = 'asc' | 'desc' | 'none';

export interface SortState {
  key: string;
  direction: Exclude<SortDirection, 'none'>;
}

export interface ColumnDef<T> {
  key: string;
  header: string;
  render?: (row: T, rowIndex: number) => React.ReactNode;
  sortable?: boolean;
}

export interface DataTableProps<T> {
  columns: ColumnDef<T>[];
  data: T[];
  pageSize?: number;
  onSort?: (key: string, direction: SortDirection) => void;
  sortState?: SortState;
  selectable?: boolean;
  selectedRows?: Set<number>;
  onSelectionChange?: (selected: Set<number>) => void;
  loading?: boolean;
  emptyMessage?: string;
  caption?: string;
  className?: string;
}

// ---------------------------------------------------------------------------
// Sort icon helper
// ---------------------------------------------------------------------------

function SortIcon({ direction }: { direction: SortDirection }) {
  const label =
    direction === 'asc' ? '▲' : direction === 'desc' ? '▼' : '⇅';
  return (
    <em
      className={`dt-sort-icon${direction !== 'none' ? ' dt-sort-icon--active' : ''}`}
      aria-hidden="true"
    >
      {label}
    </em>
  );
}

// ---------------------------------------------------------------------------
// Component
// ---------------------------------------------------------------------------

function DataTable<T>(props: DataTableProps<T>): React.ReactElement {
  const {
    columns,
    data,
    pageSize,
    onSort,
    sortState: controlledSortState,
    selectable,
    selectedRows: controlledSelectedRows,
    onSelectionChange,
    loading = false,
    emptyMessage = 'No data available.',
    caption,
    className,
  } = props;

  // Internal state — used when the parent doesn't supply controlled props
  const [currentPage, setCurrentPage] = useState(1);
  const [internalSortState, setInternalSortState] = useState<SortState | null>(null);
  const [internalSelectedRows, setInternalSelectedRows] = useState<Set<number>>(
    new Set(),
  );

  const isControlledSort = onSort !== undefined;
  const isControlledSelection =
    onSelectionChange !== undefined && controlledSelectedRows !== undefined;

  const effectiveSortState = isControlledSort ? controlledSortState ?? null : internalSortState;
  const effectiveSelectedRows = isControlledSelection
    ? controlledSelectedRows
    : internalSelectedRows;

  // Reset to page 1 when data or sort changes
  useEffect(() => {
    setCurrentPage(1);
  }, [data, effectiveSortState]);

  // Sorted rows
  const sortedData = useMemo(() => {
    if (!effectiveSortState) return data;
    const { key, direction } = effectiveSortState;
    return [...data].sort((a, b) => {
      const aVal = (a as Record<string, unknown>)[key];
      const bVal = (b as Record<string, unknown>)[key];
      if (aVal === bVal) return 0;
      const cmp = String(aVal).localeCompare(String(bVal), undefined, { numeric: true });
      return direction === 'asc' ? cmp : -cmp;
    });
  }, [data, effectiveSortState]);

  // Paginated slice
  const totalPages = pageSize ? Math.max(1, Math.ceil(sortedData.length / pageSize)) : 1;
  const pagedData = useMemo(() => {
    if (!pageSize) return sortedData;
    const start = (currentPage - 1) * pageSize;
    return sortedData.slice(start, start + pageSize);
  }, [sortedData, currentPage, pageSize]);

  // Page announcements for screen readers
  const [pageAnnounce, setPageAnnounce] = useState('');

  const handleSortClick = (key: string) => {
    let next: SortDirection;
    if (effectiveSortState?.key === key) {
      if (effectiveSortState.direction === 'asc') next = 'desc';
      else if (effectiveSortState.direction === 'desc') next = 'none';
      else next = 'asc';
    } else {
      next = 'asc';
    }

    if (isControlledSort) {
      onSort!(key, next);
    } else {
      setInternalSortState(
        next === 'none' ? null : { key, direction: next },
      );
    }
  };

  const getSortDirection = (key: string): SortDirection =>
    effectiveSortState?.key === key ? effectiveSortState.direction : 'none';

  // Selection helpers
  const updateSelection = (next: Set<number>) => {
    if (isControlledSelection) onSelectionChange!(next);
    else setInternalSelectedRows(next);
  };

  const handleRowSelect = (dataIndex: number) => {
    const next = new Set(effectiveSelectedRows);
    if (next.has(dataIndex)) next.delete(dataIndex);
    else next.add(dataIndex);
    updateSelection(next);
  };

  // Compute the absolute data indices for the current page
  const pageDataIndices = pagedData.map((_, i) =>
    pageSize ? (currentPage - 1) * pageSize + i : i,
  );

  const allPageSelected =
    pageDataIndices.length > 0 &&
    pageDataIndices.every((i) => effectiveSelectedRows.has(i));
  const somePageSelected = pageDataIndices.some((i) =>
    effectiveSelectedRows.has(i),
  );

  const handleSelectAll = () => {
    const next = new Set(effectiveSelectedRows);
    if (allPageSelected) {
      pageDataIndices.forEach((i) => next.delete(i));
    } else {
      pageDataIndices.forEach((i) => next.add(i));
    }
    updateSelection(next);
  };

  const goToPage = (page: number) => {
    setCurrentPage(page);
    setPageAnnounce(`Page ${page} of ${totalPages}`);
  };

  // Skeleton col count includes the selection column if enabled
  const colCount = columns.length + (selectable ? 1 : 0);
  const skeletonWidths = ['60%', '80%', '50%', '70%', '40%'];

  return (
    <div className="dt-wrapper">
      {/* Visually hidden live region for page change announcements */}
      <div aria-live="polite" className="dt-page-announce">
        {pageAnnounce}
      </div>

      <table
        role="grid"
        className={`dt${className ? ` ${className}` : ''}`}
      >
        {caption && <caption>{caption}</caption>}

        <thead>
          <tr>
            {selectable && (
              <th style={{ width: '2.5rem' }}>
                <input
                  type="checkbox"
                  className="dt-checkbox"
                  checked={allPageSelected}
                  ref={(el) => {
                    if (el) el.indeterminate = somePageSelected && !allPageSelected;
                  }}
                  onChange={handleSelectAll}
                  aria-label="Select all rows on this page"
                />
              </th>
            )}
            {columns.map((col) => {
              const dir = getSortDirection(col.key);
              return (
                <th
                  key={col.key}
                  aria-sort={
                    col.sortable
                      ? dir === 'none'
                        ? 'none'
                        : dir === 'asc'
                        ? 'ascending'
                        : 'descending'
                      : undefined
                  }
                >
                  {col.sortable ? (
                    <button
                      type="button"
                      className="dt-sort-btn"
                      onClick={() => handleSortClick(col.key)}
                    >
                      {col.header}
                      <SortIcon direction={dir} />
                    </button>
                  ) : (
                    col.header
                  )}
                </th>
              );
            })}
          </tr>
        </thead>

        <tbody>
          {loading ? (
            // Skeleton rows
            Array.from({ length: pageSize ?? 5 }).map((_, rowIdx) => (
              <tr key={rowIdx} aria-hidden="true">
                {selectable && (
                  <td>
                    <span
                      className="dt-skeleton"
                      style={{ width: '1rem' }}
                    />
                  </td>
                )}
                {columns.map((col, colIdx) => (
                  <td key={col.key}>
                    <span
                      className="dt-skeleton"
                      style={{
                        width: skeletonWidths[(rowIdx + colIdx) % skeletonWidths.length],
                      }}
                    />
                  </td>
                ))}
              </tr>
            ))
          ) : pagedData.length === 0 ? (
            <tr>
              <td className="dt-empty" colSpan={colCount}>
                {emptyMessage}
              </td>
            </tr>
          ) : (
            pagedData.map((row, pageRowIdx) => {
              const dataIndex = pageDataIndices[pageRowIdx];
              const isSelected = effectiveSelectedRows.has(dataIndex);
              return (
                <tr
                  key={dataIndex}
                  className={isSelected ? 'dt-row--selected' : undefined}
                  aria-selected={selectable ? isSelected : undefined}
                >
                  {selectable && (
                    <td>
                      <input
                        type="checkbox"
                        className="dt-checkbox"
                        checked={isSelected}
                        onChange={() => handleRowSelect(dataIndex)}
                        aria-label={`Select row ${dataIndex + 1}`}
                      />
                    </td>
                  )}
                  {columns.map((col) => (
                    <td key={col.key}>
                      {col.render
                        ? col.render(row, dataIndex)
                        : String(
                            (row as Record<string, unknown>)[col.key] ?? '',
                          )}
                    </td>
                  ))}
                </tr>
              );
            })
          )}
        </tbody>

        {pageSize && !loading && sortedData.length > 0 && (
          <tfoot>
            <tr>
              <td colSpan={colCount}>
                <div className="dt-pagination">
                  <span>
                    {Math.min((currentPage - 1) * pageSize + 1, sortedData.length)}–
                    {Math.min(currentPage * pageSize, sortedData.length)} of{' '}
                    {sortedData.length}
                  </span>
                  <button
                    type="button"
                    onClick={() => goToPage(currentPage - 1)}
                    disabled={currentPage === 1}
                    aria-label="Previous page"
                  >
                    ‹
                  </button>
                  <span aria-current="page">
                    {currentPage} / {totalPages}
                  </span>
                  <button
                    type="button"
                    onClick={() => goToPage(currentPage + 1)}
                    disabled={currentPage === totalPages}
                    aria-label="Next page"
                  >
                    ›
                  </button>
                </div>
              </td>
            </tr>
          </tfoot>
        )}
      </table>
    </div>
  );
}

export default DataTable;
