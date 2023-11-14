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
        return this.length.equals(this.width) && this.length.equals(this.height);
    }
}
