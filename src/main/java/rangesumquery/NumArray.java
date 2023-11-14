/*
 * Copyright 2020-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
