package com.venkat.dsa.coding.medium.solutions;

/**
 * Multiply Strings (LeetCode 43)
 *
 * <p>Multiplies two non-negative integers represented as strings without converting directly to
 * a built-in big-integer type. Uses grade-school long multiplication: the product of digits
 * num1[i] and num2[j] contributes to positions (i+j) and (i+j+1) in a result buffer of length
 * m+n. After filling the buffer, carry is resolved and leading zeros are stripped.
 *
 * <p><b>Key insight:</b> num1[i] * num2[j] affects exactly positions i+j (tens) and i+j+1 (units)
 * in the result array — no intermediate string concatenation needed.
 *
 * <p><b>Time complexity:</b> O(m * n). <b>Space complexity:</b> O(m + n).
 */
public class MultiplyStrings {

    /**
     * Returns the product of {@code num1} and {@code num2} as a string.
     *
     * @param num1 non-negative integer string, no leading zeros except "0"
     * @param num2 non-negative integer string, no leading zeros except "0"
     * @return string representation of num1 * num2
     */
    public String multiply(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) return "0";

        int m = num1.length(), n = num2.length();
        int[] pos = new int[m + n]; // pos[i+j] = tens digit, pos[i+j+1] = units digit

        // Multiply each pair of digits
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int mul = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                int p1 = i + j, p2 = i + j + 1;
                int sum = mul + pos[p2]; // add existing value at units position

                pos[p2] = sum % 10;       // units digit
                pos[p1] += sum / 10;      // carry to tens position
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int digit : pos) {
            if (!(sb.length() == 0 && digit == 0)) { // skip leading zeros
                sb.append(digit);
            }
        }
        return sb.toString();
    }
}
