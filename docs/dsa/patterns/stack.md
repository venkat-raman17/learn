# Stack

## When to reach for it

- You see **matching/nesting**: brackets, tags, open/close pairs of any kind.
- You need the **most-recently-seen** element while scanning left-to-right.
- A problem asks for the **next greater / next smaller** element for each index.
- You need to process items in **LIFO order** or undo the last action.
- The phrase "monotonic" appears, or you need to maintain a sorted running window of candidates.
- Evaluating expressions or simulating a call stack (recursion elimination).
- You spot "histogram", "water trap", or "span" — classic monotonic-stack territory.

## The idea

Push elements onto the stack as you scan; pop when you find the element that "resolves" or "beats" what is on top. For **monotonic stacks**, maintain the invariant that the stack is always sorted (ascending or descending) by popping anything that violates it before pushing. This gives O(n) time because each element is pushed and popped at most once — even though there is a nested loop in the template. Space is O(n) in the worst case.

## Template

```java
import java.util.ArrayDeque;
import java.util.Deque;

// Generic monotonic-stack scan (adapt for your invariant)
public int[] nextGreaterElement(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];
    Arrays.fill(result, -1);                      // default: no answer found

    // Use ArrayDeque as a stack; stores indices (usually more useful than values)
    Deque<Integer> stack = new ArrayDeque<>();     // stack of indices, monotone decreasing values

    for (int i = 0; i < n; i++) {
        // Pop while the invariant is violated (here: nums[i] > nums[stack.peek()])
        while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
            int idx = stack.pop();
            result[idx] = nums[i];               // nums[i] is the "answer" for idx
        }
        stack.push(i);
    }
    // Anything left in the stack has no answer (already defaulted to -1)
    return result;
}

// Matching-brackets skeleton
public boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    for (char c : s.toCharArray()) {
        if (c == '(' || c == '[' || c == '{') {
            stack.push(c);
        } else {
            if (stack.isEmpty()) return false;
            char top = stack.pop();
            if (c == ')' && top != '(') return false;
            if (c == ']' && top != '[') return false;
            if (c == '}' && top != '{') return false;
        }
    }
    return stack.isEmpty();
}
```

## Variations

**Monotone increasing stack** — pop when `nums[i] < stack.peek()`. Used to find the next smaller element or the largest rectangle in a histogram (pop gives the height, current index gives the right boundary).

**Min/Max stack** — maintain a second stack that tracks the running min or max. Push to the auxiliary stack only when the new value is <= (or >=) the current auxiliary top; pop in sync.

**Stack + hashmap** — store paired characters or indices in a map for O(1) lookups during matching (e.g., storing the index of a matching open bracket for jump games).

**Recursive DFS via explicit stack** — replace the call stack with an explicit `Deque<Frame>` when recursion depth risks a `StackOverflowError`. Push children in reverse order so the leftmost child is processed first.

**Two-pass or circular array** — for problems like "daily temperatures" on a circular array, run the scan twice (indices 0..2n-1 mod n) to handle wrap-around without special-casing.

## Pitfalls

- **Forgetting `stack.isEmpty()` before `peek()`/`pop()`** — always guard the while condition; a NullPointerException here is easy to miss under pressure.
- **Storing values instead of indices** — you usually need the original index to write into a result array or compute a width. Store indices and retrieve values via `nums[stack.peek()]`.
- **Off-by-one on width in histogram** — when popping for a bar of height `h`, the right boundary is `i` and the left boundary is `stack.isEmpty() ? 0 : stack.peek() + 1`. Forgetting the `+1` is the classic bug.
- **Not draining the stack after the loop** — for monotonic-stack problems, elements remaining in the stack after the scan still need processing (e.g., extend the rectangle to the array's right edge).
- **Using `Stack<>` instead of `ArrayDeque`** — `java.util.Stack` is synchronized and inherits `Vector`; prefer `ArrayDeque` for all stack use in competitive/interview code.
- **Generating parentheses: confusing count vs. string** — track open/close counts, not the stack size, to decide when to recurse or backtrack.

## Problems in this pattern

| Problem | Key insight |
|---|---|
| **Valid Parentheses** | Push open brackets; pop and match on close. Return `stack.isEmpty()`. |
| **Min Stack** | Parallel auxiliary stack tracks running minimum; push/pop in sync with main stack. |
| **Evaluate Reverse Polish Notation** | Push operands; on operator, pop two, compute, push result. |
| **Generate Parentheses** | Backtracking with open/close counters; not a literal stack problem but stack-of-calls mental model. |
| **Daily Temperatures** | Monotone decreasing stack of indices; pop when a warmer day is found, record the gap. |
| **Car Fleet** | Sort by position descending; push time-to-target, pop if current <= top (caught up, same fleet). |
| **Largest Rectangle in Histogram** | Monotone increasing stack of indices; on pop compute width as `i - stack.peek() - 1` (or full width if stack empty). |
