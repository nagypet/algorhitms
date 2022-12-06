package romantoint;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RomanConverterTest
{

    @Test
    void romanToInt()
    {
        RomanConverter converter = new RomanConverter();
        Assertions.assertEquals(3, converter.romanToInt("III"));
        Assertions.assertEquals(58, converter.romanToInt("LVIII"));
        Assertions.assertEquals(1994, converter.romanToInt("MCMXCIV"));
    }
}
