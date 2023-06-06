package wallpaper;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomTest
{
    @Test
    void test1()
    {
        Room room = new Room(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3"));
        assertEquals(new BigDecimal("24"), room.getNeededWallpaper());
    }

    @Test
    void test2()
    {
        Room room = new Room(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("5"));
        assertEquals(new BigDecimal("23"), room.getNeededWallpaper());
    }
}
