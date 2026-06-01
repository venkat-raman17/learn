# Bit Manipulation

## When to reach for it

- You see "find the one element that doesn't have a pair / appears once."
- The problem asks for bit counts, parity, or powers of two without using division or modulo.
- Constraints are `0 <= n < 2^31` or the word "integer" appears alongside "no extra space."
- A range sum / XOR trick would collapse O(n) into O(1) — classic for "find missing number in [0..n]."
- Two integers must be added/subtracted **without arithmetic operators**.
- The problem says "reverse the bits of a 32-bit unsigned integer."
- You notice any mask-and-shift pattern would let you process k bits at a time.

---

## The idea

XOR is the workhorse: `a ^ a == 0` and `a ^ 0 == a`, so XORing a full array cancels duplicates and
leaves survivors. Isolate the lowest set bit with `n & (-n)` or clear it with `n & (n - 1)`.
Brian Kernighan's trick — `n = n & (n - 1)` in a loop — counts set bits in O(k) where k is the
popcount, not O(32). Carry-propagation simulation lets you add integers using only `&` and `^`.

**Typical complexity:** O(1) or O(log n) time; O(1) extra space unless you cache results (Counting Bits).

---

## Template

```java
// ── Bit-manipulation skeleton ──────────────────────────────────────────────

public class BitPatterns {

    // 1. XOR fold — cancels pairs, leaves the lone survivor
    public int xorFold(int[] nums) {
        int result = 0;
        for (int n : nums) result ^= n;
        return result;
    }

    // 2. Count set bits — Brian Kernighan
    public int popcount(int n) {
        int count = 0;
        while (n != 0) {
            n &= (n - 1);   // clear lowest set bit
            count++;
        }
        return count;
    }

    // 3. Reverse 32 bits
    public int reverseBits(int n) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            result = (result << 1) | (n & 1);
            n >>>= 1;       // unsigned right shift — critical for Java
        }
        return result;
    }

    // 4. Add without + (carry simulation)
    public int addWithoutPlus(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1;   // carry bits
            a = a ^ b;                  // partial sum (no carry)
            b = carry;
        }
        return a;
    }

    // 5. Check power of two
    public boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    // 6. Get / set / clear / toggle bit i
    public int getBit   (int n, int i) { return (n >> i) & 1; }
    public int setBit   (int n, int i) { return n | (1 << i); }
    public int clearBit (int n, int i) { return n & ~(1 << i); }
    public int toggleBit(int n, int i) { return n ^ (1 << i); }

    // 7. Isolate lowest set bit
    public int lowestSetBit(int n) { return n & (-n); }
}
```

---

## Variations

**DP + bits (Counting Bits)**
Use the recurrence `dp[i] = dp[i >> 1] + (i & 1)` to fill a popcount table in O(n).

**XOR with index trick (Missing Number)**
XOR every index `0..n` with every value in the array; duplicates cancel and the missing value remains.
`result = n; for (int i = 0; i < n; i++) result ^= i ^ nums[i];`

**Mask iteration**
Enumerate all subsets of a bitmask with `for (int sub = mask; sub > 0; sub = (sub - 1) & mask)`.
Used in subset-sum DP and bitmask DP on small n.

**Sign-extension hazard**
Java's `>>` is arithmetic (sign-extends); `>>>` is logical (zero-fills). For bit reversal or
unsigned interpretations, always use `>>>`.

**Two distinct singles**
If two numbers appear once and the rest appear twice, XOR all → `xor = a ^ b`. Find any set bit
(`xor & -xor`), partition the array on that bit, XOR each partition separately.

---

## Pitfalls

- **Forgetting `>>>` in Java.** `int x = -1; x >> 1` gives `-1`, not `0x7FFFFFFF`. Use `>>>` for
  unsigned shifts, especially in `reverseBits`.
- **Overflow on `1 << 31`.** In Java `int`, `1 << 31` is `Integer.MIN_VALUE` (negative). Cast to
  `long` or use `1L << 31` when you need the full 32nd bit.
- **`n & (n - 1)` on n = 0.** Returns 0 safely, but your loop guard should be `n != 0`, not
  `n > 0`, or you silently skip negative inputs with set bits.
- **Assuming input is non-negative.** Problems like Reverse Integer pass negative values; handle the
  sign separately or work with `long`.
- **Off-by-one in bit position.** Bits are 0-indexed from the right. Bit 31 is the sign bit for
  `int`. Draw it out before coding a mask.
- **Missing the modulo / overflow guard in Reverse Integer.** After reversal, clamp to
  `[Integer.MIN_VALUE, Integer.MAX_VALUE]` before casting back to `int`.

---

## Problems in this pattern

| # | Problem | Key trick |
|---|---------|-----------|
| 1 | **Single Number** | XOR fold — pairs cancel |
| 2 | **Number of 1 Bits** | Brian Kernighan (`n & (n-1)`) or `Integer.bitCount` |
| 3 | **Counting Bits** | DP: `dp[i] = dp[i >> 1] + (i & 1)` |
| 4 | **Reverse Bits** | 32-iteration shift loop with `>>>` |
| 5 | **Missing Number** | XOR index with value, or Gauss sum |
| 6 | **Sum of Two Integers** | Carry simulation with `&` and `^` |
| 7 | **Reverse Integer** | Work in `long`, rebuild digit-by-digit, clamp |
