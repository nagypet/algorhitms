package romantoint;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RomanConverterTest
{

    @Test
    void romanToInt()
    {
        RomanConverter converter = new RomanConverter();
        assertEquals(3, converter.romanToInt("III"));
        assertEquals(58, converter.romanToInt("LVIII"));
        assertEquals(1994, converter.romanToInt("MCMXCIV"));
        assertThrows(IllegalArgumentException.class, () -> converter.romanToInt("alma"));
    }
}
