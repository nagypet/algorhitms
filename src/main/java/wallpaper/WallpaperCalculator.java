package wallpaper;

import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class WallpaperCalculator
{
    public void processInput() throws IOException
    {
        List<Room> rooms = getRoomsFromInput();

        // calculate number of total square feet of wallpaper the company should order for all rooms from input
        BigDecimal totalAmount = rooms.stream().map(Room::getNeededWallpaper).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        log.info("Total amount: {}", totalAmount);

        // list all rooms from input that have a cubic shape (order by total needed wallpaper descending)
        rooms.stream()
                .filter(Room::isCubic)
                .sorted(Comparator.comparing(Room::getNeededWallpaper).reversed())
                .forEach(r -> log.info("This room has cubic shape: {}", r));

        // list all rooms from input that are appearing more than once (order is irrelevant)
        Map<Room, Long> countOfOccurences = rooms.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        countOfOccurences.entrySet().stream()
                .filter(i -> i.getValue() > 1)
                .forEach(r -> log.info(MessageFormat.format("This room {0} appears {1} times", r.getKey(), r.getValue())));
    }

    private List<Room> getRoomsFromInput() throws IOException
    {
        List<Room> rooms = new ArrayList<>();
        URL url = Resources.getResource("wallpaper-input.txt");
        List<String> lines = Resources.readLines(url, StandardCharsets.UTF_8);
        for (String line : lines)
        {
            String[] dimensions = line.split("x");
            rooms.add(new Room(new BigDecimal(dimensions[0]), new BigDecimal(dimensions[1]), new BigDecimal(dimensions[2])));
        }
        return rooms;
    }
}
