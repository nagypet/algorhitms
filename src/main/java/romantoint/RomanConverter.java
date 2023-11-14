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

package romantoint;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Digits:
 * I: 1
 * IV: 4
 * V: 5
 * IX: 9
 * X: 10
 * XL: 40
 * L: 50
 * XC: 90
 * C: 100
 * CD: 400
 * D: 500
 * CM: 900
 * M: 1000
 */
public class RomanConverter
{
    @Getter
    private enum Token
    {
        M(1000),
        CM(900),
        D(500),
        CD(400),
        C(100),
        XC(90),
        L(50),
        XL(40),
        X(10),
        IX(9),
        V(5),
        IV(4),
        I(1);

        private int value;
        Token(int value)
        {
            this.value = value;
        }
    }

    public int romanToInt(final String s)
    {
        String input = s;
        // Iterating through tokens
        int value = 0;
        for (Token token : Token.values())
        {
            // Finding tokens in input
            while (tokenAvailable(token, input))
            {
                value += token.getValue();
                input = substractToken(token, input);
                if (input.isEmpty())
                {
                    return value;
                }
            }
        }
        throw new IllegalArgumentException(String.format("'%s' contains invalid roman numbers!", s));
    }

    private String substractToken(Token token, String input)
    {
        return input.substring(token.name().length());
    }

    private boolean tokenAvailable(Token token, String input)
    {
        return input.startsWith(token.name());
    }
}
