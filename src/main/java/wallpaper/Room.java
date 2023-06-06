package wallpaper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
public class Room
{
    private final BigDecimal length;
    private final BigDecimal width;
    private final BigDecimal height;

    public Room(BigDecimal length, BigDecimal width, BigDecimal height)
    {
        this.length = getNullSafeBigDecimal(length);
        this.width = getNullSafeBigDecimal(width);
        this.height = getNullSafeBigDecimal(height);
    }

    private static BigDecimal getNullSafeBigDecimal(BigDecimal value)
    {
        return value != null ? value : BigDecimal.ZERO;
    }

    /**
     * Every room is a rectangular prism, which makes calculations easier: the surface area of room is 2*l*w + 2*w*h + 2*h*l.
     * The company also need a little extra wallpaper for each room: the area of the smallest side.
     *
     * @return Needed amount of wallpaper
     */
    public BigDecimal getNeededWallpaper()
    {
        List<RoomSide> sides = List.of(
                new RoomSide(this.length, this.width),
                new RoomSide(this.width, this.height),
                new RoomSide(this.height, this.length));

        // Calculating area
        BigDecimal halfAmount = sides.stream().map(RoomSide::getArea).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        // Calculating extra
        BigDecimal extra = sides.stream().min(RoomSide::compareTo).map(RoomSide::getArea).orElse(BigDecimal.ZERO);

        return halfAmount.multiply(BigDecimal.valueOf(2)).add(extra);
    }

    public boolean isCubic()
    {
        return this.length.equals(this.width);
    }
}
