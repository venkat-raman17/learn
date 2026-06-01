import { useState } from 'react'
import './App.css'

import { Button } from './lld/button/Button'
import { Autocomplete, type Item } from './lld/autocomplete/Autocomplete'
import DataTable, { type ColumnDef } from './lld/data-table/DataTable'
import { ToastProvider, useToast } from './lld/toast/ToastProvider'

// ---------------------------------------------------------------------------
// Demo data
// ---------------------------------------------------------------------------

const FRUIT_DB: Item[] = [
  { id: '1', label: 'Apple' },
  { id: '2', label: 'Apricot' },
  { id: '3', label: 'Banana' },
  { id: '4', label: 'Blueberry' },
  { id: '5', label: 'Cherry' },
  { id: '6', label: 'Coconut' },
  { id: '7', label: 'Grape' },
  { id: '8', label: 'Guava' },
  { id: '9', label: 'Kiwi' },
  { id: '10', label: 'Lemon' },
  { id: '11', label: 'Lime' },
  { id: '12', label: 'Mango' },
  { id: '13', label: 'Orange' },
  { id: '14', label: 'Papaya' },
  { id: '15', label: 'Peach' },
  { id: '16', label: 'Pear' },
  { id: '17', label: 'Pineapple' },
  { id: '18', label: 'Plum' },
  { id: '19', label: 'Strawberry' },
  { id: '20', label: 'Watermelon' },
]

interface Employee {
  id: number
  name: string
  department: string
  role: string
  salary: number
}

const EMPLOYEES: Employee[] = [
  { id: 1, name: 'Alice Chen',    department: 'Engineering', role: 'Staff SWE',    salary: 240000 },
  { id: 2, name: 'Bob Patel',     department: 'Engineering', role: 'Senior SWE',   salary: 195000 },
  { id: 3, name: 'Carol Smith',   department: 'Design',      role: 'Lead Designer', salary: 160000 },
  { id: 4, name: 'David Kim',     department: 'Engineering', role: 'SWE II',       salary: 155000 },
  { id: 5, name: 'Eva Martinez',  department: 'Product',     role: 'Sr PM',        salary: 185000 },
  { id: 6, name: 'Frank Nguyen',  department: 'Engineering', role: 'SWE I',        salary: 130000 },
  { id: 7, name: 'Grace Lee',     department: 'Design',      role: 'UX Designer',  salary: 140000 },
  { id: 8, name: 'Henry Brown',   department: 'Product',     role: 'PM',           salary: 160000 },
  { id: 9, name: 'Ivy Johnson',   department: 'Engineering', role: 'SWE II',       salary: 165000 },
  { id: 10, name: 'Jack Wilson',  department: 'Engineering', role: 'Staff SWE',    salary: 245000 },
  { id: 11, name: 'Kate Davis',   department: 'Design',      role: 'Design Lead',  salary: 175000 },
  { id: 12, name: 'Leo Garcia',   department: 'Product',     role: 'Sr PM',        salary: 190000 },
]

const EMPLOYEE_COLS: ColumnDef<Employee>[] = [
  { key: 'id',         header: '#',          sortable: true },
  { key: 'name',       header: 'Name',       sortable: true },
  { key: 'department', header: 'Department', sortable: true },
  { key: 'role',       header: 'Role',       sortable: true },
  {
    key: 'salary',
    header: 'Salary',
    sortable: true,
    render: (row) =>
      new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD', maximumFractionDigits: 0 }).format(row.salary),
  },
]

// ---------------------------------------------------------------------------
// Tab definitions
// ---------------------------------------------------------------------------

type TabId = 'button' | 'autocomplete' | 'datatable' | 'toast'

const TABS: { id: TabId; label: string }[] = [
  { id: 'button',       label: 'Button' },
  { id: 'autocomplete', label: 'Autocomplete' },
  { id: 'datatable',    label: 'DataTable' },
  { id: 'toast',        label: 'Toast' },
]

// ---------------------------------------------------------------------------
// Demo panels
// ---------------------------------------------------------------------------

function ButtonDemo() {
  const [loading, setLoading] = useState(false)

  const triggerLoad = () => {
    setLoading(true)
    setTimeout(() => setLoading(false), 2000)
  }

  return (
    <div className="tab-panel">
      <div className="demo-section">
        <h2>Variants</h2>
        <div className="demo-row">
          <Button variant="primary">Primary</Button>
          <Button variant="secondary">Secondary</Button>
          <Button variant="ghost">Ghost</Button>
          <Button variant="danger">Danger</Button>
        </div>
      </div>

      <div className="demo-section">
        <h2>Sizes</h2>
        <div className="demo-row">
          <Button size="sm">Small</Button>
          <Button size="md">Medium</Button>
          <Button size="lg">Large</Button>
        </div>
      </div>

      <div className="demo-section">
        <h2>Loading state</h2>
        <div className="demo-row">
          <Button loading={loading} onClick={triggerLoad}>
            {loading ? 'Saving…' : 'Save (click me)'}
          </Button>
          <Button loading variant="secondary">Loading</Button>
        </div>
      </div>

      <div className="demo-section">
        <h2>Disabled</h2>
        <div className="demo-row">
          <Button disabled>Disabled Primary</Button>
          <Button disabled variant="secondary">Disabled Secondary</Button>
          <Button disabled variant="danger">Disabled Danger</Button>
        </div>
      </div>

      <div className="demo-section">
        <h2>With icons</h2>
        <div className="demo-row">
          <Button leftIcon="🚀">Deploy</Button>
          <Button rightIcon="→" variant="secondary">Next</Button>
          <Button leftIcon="🗑" variant="danger">Delete</Button>
        </div>
      </div>

      <div className="demo-section">
        <h2>Full width</h2>
        <Button fullWidth>Full-Width Button</Button>
      </div>
    </div>
  )
}

function AutocompleteDemo() {
  const [selected, setSelected] = useState<Item | null>(null)

  const fetchFruits = async (query: string): Promise<Item[]> => {
    await new Promise((r) => setTimeout(r, 200)) // simulate network
    return FRUIT_DB.filter((f) =>
      f.label.toLowerCase().includes(query.toLowerCase()),
    )
  }

  return (
    <div className="tab-panel">
      <div className="demo-section">
        <h2>Basic usage</h2>
        <p style={{ marginBottom: '1rem', fontSize: '0.875rem', color: 'var(--text)' }}>
          Type a fruit name. Results are debounced 300 ms. Use Arrow keys + Enter to
          select, Escape to close.
        </p>
        <Autocomplete
          fetchSuggestions={fetchFruits}
          onSelect={(item) => setSelected(item)}
          placeholder="Search fruits…"
        />
        {selected && (
          <div className="selected-output">
            Selected: {selected.label} (id: {selected.id})
          </div>
        )}
      </div>

      <div className="demo-section">
        <h2>Error state</h2>
        <p style={{ marginBottom: '1rem', fontSize: '0.875rem', color: 'var(--text)' }}>
          This instance always rejects to show the error state.
        </p>
        <Autocomplete
          fetchSuggestions={() => Promise.reject(new Error('Network error'))}
          onSelect={() => {}}
          placeholder="Always errors…"
        />
      </div>
    </div>
  )
}

function DataTableDemo() {
  const [selected, setSelected] = useState<Set<number>>(new Set())

  return (
    <div className="tab-panel">
      <div className="demo-section">
        <h2>Sortable + paginated + selectable</h2>
        <DataTable
          columns={EMPLOYEE_COLS}
          data={EMPLOYEES}
          pageSize={5}
          selectable
          selectedRows={selected}
          onSelectionChange={setSelected}
          caption="Employee directory"
        />
        {selected.size > 0 && (
          <div className="selected-output">
            Selected rows: {[...selected].sort((a, b) => a - b).map((i) => EMPLOYEES[i]?.name).join(', ')}
          </div>
        )}
      </div>

      <div className="demo-section">
        <h2>Loading skeleton</h2>
        <DataTable
          columns={EMPLOYEE_COLS}
          data={[]}
          pageSize={4}
          loading
        />
      </div>

      <div className="demo-section">
        <h2>Empty state</h2>
        <DataTable
          columns={EMPLOYEE_COLS}
          data={[]}
          emptyMessage="No employees match your search."
        />
      </div>
    </div>
  )
}

function ToastDemoInner() {
  const { show } = useToast()

  return (
    <div className="tab-panel">
      <div className="demo-section">
        <h2>Variants</h2>
        <div className="demo-row">
          <Button onClick={() => show('Changes saved successfully!', { variant: 'success' })}>
            Success
          </Button>
          <Button variant="danger" onClick={() => show('Something went wrong.', { variant: 'error' })}>
            Error
          </Button>
          <Button variant="ghost" onClick={() => show('New comment on your PR.', { variant: 'info' })}>
            Info
          </Button>
        </div>
      </div>

      <div className="demo-section">
        <h2>Persistent (no auto-dismiss)</h2>
        <div className="demo-row">
          <Button variant="secondary" onClick={() => show('This toast stays until dismissed.', { autoDismissMs: 0, variant: 'info' })}>
            Show persistent
          </Button>
        </div>
      </div>

      <div className="demo-section">
        <h2>Rapid fire (maxVisible = 3)</h2>
        <div className="demo-row">
          <Button variant="secondary" onClick={() => {
            show('Toast 1', { variant: 'info' })
            show('Toast 2', { variant: 'success' })
            show('Toast 3', { variant: 'info' })
            show('Toast 4 — will queue', { variant: 'info' })
            show('Toast 5 — will queue', { variant: 'error' })
          }}>
            Fire 5 toasts
          </Button>
        </div>
      </div>
    </div>
  )
}

// ---------------------------------------------------------------------------
// Root app
// ---------------------------------------------------------------------------

function App() {
  const [activeTab, setActiveTab] = useState<TabId>('button')

  return (
    <ToastProvider position="top-right" maxVisible={3}>
      <div className="app">
        <header className="app-header">
          <h1>LLD Component Showcase</h1>
          <span className="app-badge">spa-react-vite</span>
        </header>

        <nav className="tabs" role="tablist" aria-label="LLD components">
          {TABS.map((tab) => (
            <button
              key={tab.id}
              type="button"
              role="tab"
              aria-selected={activeTab === tab.id}
              aria-controls={`panel-${tab.id}`}
              id={`tab-${tab.id}`}
              className={`tab-btn${activeTab === tab.id ? ' tab-btn--active' : ''}`}
              onClick={() => setActiveTab(tab.id)}
            >
              {tab.label}
            </button>
          ))}
        </nav>

        <main
          id={`panel-${activeTab}`}
          role="tabpanel"
          aria-labelledby={`tab-${activeTab}`}
        >
          {activeTab === 'button'       && <ButtonDemo />}
          {activeTab === 'autocomplete' && <AutocompleteDemo />}
          {activeTab === 'datatable'    && <DataTableDemo />}
          {activeTab === 'toast'        && <ToastDemoInner />}
        </main>
      </div>
    </ToastProvider>
  )
}

export default App
