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

import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import lombok.Data;
import lombok.Getter;
import org.junit.platform.commons.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TestCase
{
    @Data
    public static class Pair
    {
        private final int a;
        private final int b;
    }

    private List<Integer> numArray;
    private List<Pair> pairs = new ArrayList<>();

    public void setNumArray(String numArray)
    {
        String[] split = numArray.split(",");
        this.numArray = Arrays.stream(split).map(i -> Integer.parseInt(i.strip())).collect(Collectors.toList());
    }

    public int[] getNumArray()
    {
        return this.numArray.stream().mapToInt(integer -> integer).toArray();
    }

    public void appendPair(String s)
    {
        String[] split = s.split(",");
        this.pairs.add(new Pair(toInt(split[0]), toInt(split[1])));
    }

    private static int toInt(String s)
    {
        return Integer.parseInt(s.strip());
    }

    public static TestCase fromInput()
    {
        List<String> lines = readInput("rangesumquery/input_2.txt");

        List<String> tokens = getTokens(lines.get(1));

        TestCase testCase = new TestCase();
        testCase.setNumArray(tokens.get(0));

        for (int i = 1; i < tokens.size(); i++)
        {
            testCase.appendPair(tokens.get(i));
        }

        return testCase;
    }


    private static List<String> readInput(String resourceName)
    {
        URL url = Resources.getResource(resourceName);
        try
        {
            CharSource charSource = Resources.asCharSource(url, Charsets.UTF_8);
            BufferedReader br = charSource.openBufferedStream();
            return br.lines().collect(Collectors.toList());
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }


    // [[[1, 3, 5]], [0, 2], [1, 2], [0, 2]] => "[[1, 3, 5]]", "[0, 2]", "[1, 2]", "[0, 2]"
    private static List<String> getTokens(String s)
    {
        List<String> tokens = new ArrayList<>();
        Position position = new Position().increment();

        while (true)
        {
            String nextToken = getNextToken(s, position);
            if (StringUtils.isBlank(nextToken))
            {
                break;
            }
            tokens.add(nextToken);
            skipCommaAndWhitespace(s, position);
        }

        return tokens;
    }

    private static void skipCommaAndWhitespace(String s, Position position)
    {
        while (isCommaOrWhitespace(getChar(s, position.index)))
        {
            position.index++;
        }
    }

    private static boolean isCommaOrWhitespace(String aChar)
    {
        return ",".equals(aChar) || " ".equals(aChar);
    }


    private static class Position
    {
        int index = 0;

        public Position increment()
        {
            this.index++;
            return this;
        }
    }

    private static String getNextToken(String s, Position position)
    {
        if ("[".equals(getChar(s, position.index)))
        {
            String nextToken = getNextToken(s, position.increment());
            position.increment();
            return nextToken;
        }

        StringBuilder sb = new StringBuilder();
        while (true)
        {
            String aChar = getChar(s, position.index++);
            if (isTerminalChar(aChar))
            {
                break;
            }
            sb.append(aChar);
        }

        return sb.toString();
    }

    private static boolean isTerminalChar(String aChar)
    {
        return aChar == null || aChar.equals("]");
    }


    private static String getChar(String s, int index)
    {
        if (index < s.length())
        {
            return s.substring(index, index + 1);
        }
        return null;
    }
}
