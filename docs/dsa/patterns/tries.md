# Tries

## When to reach for it

- The problem involves a **dictionary of words** and queries against that dictionary.
- You need **prefix matching** — "does any stored word start with X?"
- The problem asks you to search with **wildcards** (`.` matches any char, `*` suffix, etc.).
- Words share long common prefixes and you are burning time on repeated string hashing.
- The input is a **set of strings** and you must retrieve or validate them character by character.
- Key phrase triggers: "add and search words", "autocomplete", "prefix", "word board search", "starts with".

---

## The idea

A Trie (prefix tree) stores strings as paths from a shared root node. Each node holds one character of a key; children represent the next possible characters. A boolean flag at a node marks the end of a valid word. Lookup and insert both run in **O(L)** time where L is the word length, independent of the number of stored words. Space is **O(A * N * L)** in the worst case (A = alphabet size, N = words, L = avg length), though shared prefixes compress this in practice.

---

## Template

```java
class TrieNode {
    TrieNode[] children = new TrieNode[26]; // extend to 128 for full ASCII
    boolean isEnd = false;
}

class Trie {
    private final TrieNode root = new TrieNode();

    // --- insert ---
    public void insert(String word) {
        TrieNode cur = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (cur.children[idx] == null) {
                cur.children[idx] = new TrieNode();
            }
            cur = cur.children[idx];
        }
        cur.isEnd = true;
    }

    // --- exact search ---
    public boolean search(String word) {
        TrieNode node = traverse(word);
        return node != null && node.isEnd;
    }

    // --- prefix check ---
    public boolean startsWith(String prefix) {
        return traverse(prefix) != null;
    }

    // --- internal helper ---
    private TrieNode traverse(String s) {
        TrieNode cur = root;
        for (char c : s.toCharArray()) {
            int idx = c - 'a';
            if (cur.children[idx] == null) return null;
            cur = cur.children[idx];
        }
        return cur;
    }
}
```

---

## Variations

**Wildcard / regex search (`Design Add and Search Words`)**
Replace the iterative traverse with a recursive DFS. When you hit a `.`, branch into every non-null child and OR the results.

```java
private boolean dfs(TrieNode node, String word, int i) {
    if (i == word.length()) return node.isEnd;
    char c = word.charAt(i);
    if (c == '.') {
        for (TrieNode child : node.children) {
            if (child != null && dfs(child, word, i + 1)) return true;
        }
        return false;
    }
    TrieNode next = node.children[c - 'a'];
    return next != null && dfs(next, word, i + 1);
}
```

**Board search with Trie (`Word Search II`)**
Build a Trie from the word list. Run DFS over the board; at each cell descend into the Trie instead of checking a visited-string set. When `node.isEnd` is true, record the word, then **set `node.isEnd = false`** and optionally prune leaf nodes to avoid revisiting.

**HashMap children instead of fixed array**
Use `Map<Character, TrieNode>` when the alphabet is large or sparse. Slightly slower constant factor, less wasted space.

**Trie with count / frequency**
Add an `int count` field to each node. Useful for autocomplete ranked by frequency or for counting how many words share a prefix.

---

## Pitfalls

- **Forgetting `isEnd`** — traversal reaching the last character is not enough; a word may be a prefix of another stored word.
- **Off-by-one in board DFS** — marking a cell visited in the board (via a `visited[][]` array or in-place char swap) is separate from descending the Trie; conflating them causes incorrect backtracking.
- **Not pruning in Word Search II** — without clearing `isEnd` (and optionally removing dead branches) after finding a word, you will emit duplicates.
- **Fixed array size mismatch** — using `new TrieNode[26]` then indexing with uppercase letters or digits will throw `ArrayIndexOutOfBoundsException`. Decide your alphabet before writing a single line.
- **Reusing the same Trie across test cases** — instantiate a fresh root per test; stale nodes from prior inserts corrupt results.
- **Returning `node != null` for `search`** — must also check `node.isEnd`; "apple" stored does not mean "app" is a valid word.

---

## Problems in this pattern

| Problem | Key insight |
|---|---|
| **Implement Trie** | Baseline insert / search / startsWith; nail the `isEnd` flag. |
| **Design Add and Search Words** | Wildcard `.` forces recursive DFS over children instead of linear traversal. |
| **Word Search II** | Build Trie from word list; DFS the board guided by Trie descent; prune found words to avoid duplicates. |
