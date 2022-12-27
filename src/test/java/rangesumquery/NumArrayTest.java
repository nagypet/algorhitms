package rangesumquery;

import hu.perit.spvitamin.core.took.Took;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class NumArrayTest
{

    @Test
    void sumRange()
    {
        NumArray numArray = new NumArray(new int[]{1, 3, 5});
        assertEquals(9, numArray.sumRange(0, 2));
        numArray.update(1, 2);
        assertEquals(8, numArray.sumRange(0, 2));
    }


    private enum Operation
    {
        SUM_RANGE,
        UPDATE
    }

    @Test
    void performanceTest()
    {
        TestCase testCase = TestCase.fromInput();

        NumArray numArray = new NumArray(testCase.getNumArray());

        try (Took took = new Took())
        {
            int i = 0;
            Operation operation = Operation.SUM_RANGE;
            while (i < testCase.getPairs().size())
            {
                TestCase.Pair pair = testCase.getPairs().get(i);
                if (operation == Operation.SUM_RANGE)
                {
                    numArray.sumRange(pair.getA(), pair.getB());
                }
                else if (operation == Operation.UPDATE)
                {
                    numArray.update(pair.getA(), pair.getB());
                }
                operation = flipOperation(operation);
                i++;
            }

            log.debug("Executed {} operations on numArray of {}", i, testCase.getNumArray().length);
        }
    }

    private Operation flipOperation(Operation operation)
    {
        return operation == Operation.SUM_RANGE ? Operation.UPDATE : Operation.SUM_RANGE;
    }
}
