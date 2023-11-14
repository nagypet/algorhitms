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

package updatableconcurrentmap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.StringJoiner;

@RequiredArgsConstructor
@Getter
public class Token implements UpdatableEntity
{
    private final Long id;
    private final long expiresAt;
    @Setter
    private String info;


    public Token(Long id)
    {
        this.id = id;
        if (id == 5)
        {
            // Valid for 100 ms
            this.expiresAt = System.currentTimeMillis() + 100;
        }
        else
        {
            // Valid for 10 seconds
            this.expiresAt = System.currentTimeMillis() + 10_000;
        }
    }


    public boolean isValid()
    {
        return expiresAt > System.currentTimeMillis();
    }


    @Override
    public String toString()
    {
        long currentTimeMillis = System.currentTimeMillis();
        return new StringJoiner(", ", Token.class.getSimpleName() + "[", "]")
            .add("id='" + id + "'")
            .add("expiresIn=" + (expiresAt - currentTimeMillis))
            .add("valid=" + isValid())
            .toString();
    }
}
