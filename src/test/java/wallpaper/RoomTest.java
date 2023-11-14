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
