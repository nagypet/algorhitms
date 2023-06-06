package wallpaper;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomSide implements Comparable<RoomSide>
{
    private final BigDecimal x;
    private final BigDecimal y;

    public BigDecimal getArea()
    {
        if (x == null)
        {
            return BigDecimal.ZERO;
        }

        return x.multiply(y);
    }

    @Override
    public int compareTo(RoomSide other)
    {
        return this.getArea().compareTo(other.getArea());
    }
}
