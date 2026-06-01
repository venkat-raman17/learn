# Math & Geometry

## When to reach for it

- You see a 2-D grid and the operation is a rotation, spiral traversal, or reflection.
- The problem involves **in-place matrix mutation** where overwriting would clobber unread values.
- You need to simulate **integer arithmetic without built-in big-integer** (multiply strings, pow).
- The problem asks "is this number eventually X?" — cyclic-detection smell (Floyd's tortoise or a set).
- Coordinates, slopes, or collinearity appear (`ax + by = c`, counting points on a line).
- You have to mark rows/columns without a second matrix.

---

## The idea

Most Math & Geometry problems reduce to one of three moves: **simulate faithfully** (spiral, rotate), **detect a cycle** (happy number, pow with negative exponent), or **track state without extra space** using the matrix itself as scratch (set zeroes). Rotations and spirals live entirely in index arithmetic — derive the mapping once symbolically, then trust it. For big-integer arithmetic, treat digits as arrays and implement grade-school algorithms (long multiply, long add).

Typical complexity: O(n) or O(n x m) time; O(1) extra space is usually achievable for in-place problems.

---

## Template

```java
// ── 1. In-place 90-degree clockwise rotation ──────────────────────────────
//    Step 1: transpose  (matrix[i][j] <-> matrix[j][i])
//    Step 2: reverse each row
void rotate(int[][] m) {
    int n = m.length;
    // transpose
    for (int i = 0; i < n; i++)
        for (int j = i + 1; j < n; j++) {
            int tmp = m[i][j]; m[i][j] = m[j][i]; m[j][i] = tmp;
        }
    // reverse rows
    for (int[] row : m) {
        int lo = 0, hi = n - 1;
        while (lo < hi) { int tmp = row[lo]; row[lo++] = row[hi]; row[hi--] = tmp; }
    }
}

// ── 2. Spiral order traversal ─────────────────────────────────────────────
List<Integer> spiralOrder(int[][] m) {
    List<Integer> out = new ArrayList<>();
    int top = 0, bot = m.length - 1, left = 0, right = m[0].length - 1;
    while (top <= bot && left <= right) {
        for (int c = left;  c <= right; c++) out.add(m[top][c]); top++;
        for (int r = top;   r <= bot;   r++) out.add(m[r][right]); right--;
        if (top <= bot)
            for (int c = right; c >= left; c--) out.add(m[bot][c]); bot--;
        if (left <= right)
            for (int r = bot;   r >= top;  r--) out.add(m[r][left]); left++;
    }
    return out;
}

// ── 3. Set Matrix Zeroes (O(1) extra space) ───────────────────────────────
void setZeroes(int[][] m) {
    int R = m.length, C = m[0].length;
    boolean firstRowZero = false, firstColZero = false;
    for (int c = 0; c < C; c++) if (m[0][c] == 0) firstRowZero = true;
    for (int r = 0; r < R; r++) if (m[r][0] == 0) firstColZero = true;
    for (int r = 1; r < R; r++)
        for (int c = 1; c < C; c++)
            if (m[r][c] == 0) { m[r][0] = 0; m[0][c] = 0; }
    for (int r = 1; r < R; r++)
        for (int c = 1; c < C; c++)
            if (m[r][0] == 0 || m[0][c] == 0) m[r][c] = 0;
    if (firstRowZero) Arrays.fill(m[0], 0);
    if (firstColZero) for (int r = 0; r < R; r++) m[r][0] = 0;
}

// ── 4. Cycle detection with digit-sum (Happy Number) ─────────────────────
boolean isHappy(int n) {
    int slow = n, fast = digitSqSum(n);
    while (fast != 1 && slow != fast) {
        slow = digitSqSum(slow);
        fast = digitSqSum(digitSqSum(fast));
    }
    return fast == 1;
}
int digitSqSum(int n) {
    int s = 0;
    while (n > 0) { int d = n % 10; s += d * d; n /= 10; }
    return s;
}

// ── 5. Fast exponentiation (Pow) ─────────────────────────────────────────
double myPow(double x, int n) {
    long exp = n;                          // guard Integer.MIN_VALUE negation
    if (exp < 0) { x = 1 / x; exp = -exp; }
    double result = 1;
    while (exp > 0) {
        if ((exp & 1) == 1) result *= x;
        x *= x;
        exp >>= 1;
    }
    return result;
}
```

---

## Variations

- **Counter-clockwise rotation**: transpose then reverse *columns* (or reverse rows then transpose).
- **Multiply Strings**: grade-school O(m*n) — `result[i+j+1] += d1 * d2`, then carry left; strip leading zeros.
- **Plus One**: propagate carry right-to-left; if carry exits, prepend 1 (`new int[n+1]` with `[0]=1`).
- **Detect Squares**: store point counts in a `Map<Integer, Map<Integer, Integer>>`; for each query point enumerate axis-aligned squares by iterating one axis pair.
- **Collinearity / slope**: use `dy * dx2 == dx * dy2` (cross-product) to avoid floating-point; group by `(dy/gcd, dx/gcd)` with a canonical sign.

---

## Pitfalls

- **Row-first vs column-first index order**: always write `m[row][col]`; swapping them is the single most common WA on matrix problems.
- **Overwriting in place too early**: in spiral/zeroes, read a boundary before moving the pointer past it, or use the first row/column as flags *after* recording their own zero-status.
- **Integer overflow in Pow**: `n = Integer.MIN_VALUE`; negating it overflows. Cast to `long` before negating.
- **Leading zeros in Multiply Strings**: trim with `while (sb.length() > 1 && sb.charAt(0) == '0') sb.deleteCharAt(0)` before returning.
- **Floyd's fast pointer skipping 1**: check `fast != 1` in the loop condition, not just `slow != fast`, or you loop forever on happy numbers.
- **Off-by-one in spiral**: guard `top <= bot` and `left <= right` before the bottom and left sweeps to avoid duplicate elements in non-square matrices.

---

## Problems in this pattern

| Problem | Key move |
|---|---|
| Happy Number | Floyd cycle detection on digit-square sequence |
| Plus One | Right-to-left carry propagation on digit array |
| Rotate Image | Transpose + reverse rows (no extra matrix) |
| Spiral Matrix | Four-pointer shrink; guard non-square edges |
| Set Matrix Zeroes | Use row 0 / col 0 as flag arrays; snapshot their own state first |
| Pow(x, n) | Binary exponentiation; `long` cast for MIN_VALUE |
| Multiply Strings | Grade-school digit multiply into `int[]`, then carry pass |
| Detect Squares | Count-map per x-column; enumerate rectangle pairs in O(n) per query |
