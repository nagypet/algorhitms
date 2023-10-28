package enums;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ActionTest
{
    @Test
    void test()
    {
        assertThat(Action.fromValue("convert")).isEqualTo(Optional.of(Action.CONVERT));
        assertThat(Action.fromValue("apple")).isNotPresent();
    }
}