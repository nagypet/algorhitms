package rangesumquery;

import java.util.stream.IntStream;

/**
 * https://leetcode.com/problems/range-sum-query-mutable/description/
 *
 * The first approach. Since also this version is very fast, executes in less than 30 ms, there is no reason for further
 * optimization.
 */

public class NumArray
{
    private final int[] nums;

    public NumArray(int[] nums)
    {
        this.nums = nums;
    }

    public void update(int index, int val)
    {
        this.nums[index] = val;
    }

    public int sumRange(int left, int right)
    {
        return IntStream.rangeClosed(left, right).map(i -> this.nums[i]).reduce(0, Integer::sum);
    }
}
