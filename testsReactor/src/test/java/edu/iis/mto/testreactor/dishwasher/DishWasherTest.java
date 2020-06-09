package edu.iis.mto.testreactor.dishwasher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import edu.iis.mto.testreactor.dishwasher.engine.Engine;
import edu.iis.mto.testreactor.dishwasher.pump.WaterPump;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DishWasherTest {

    // MOCKS
    @Mock
    private WaterPump waterPump;
    @Mock
    private Engine engine;
    @Mock
    private DirtFilter dirtFilter;
    @Mock
    private Door door;

    // DATA
    private DishWasher dishWasher;
    private ProgramConfiguration programConfiguration;
    private WashingProgram washingProgram;
    private FillLevel fillLevel;

    @BeforeEach
    public void setup() {
        dishWasher = new DishWasher(waterPump, engine, dirtFilter, door);
        washingProgram = WashingProgram.ECO;
        fillLevel = FillLevel.FULL;
    }

    @Test
    public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

    // STATE TESTS
    @Test
    public void shouldWashDishesSuccessfullyWithAnyProgram() {
        programConfiguration = ProgramConfiguration.builder()
                                                   .withProgram(washingProgram)
                                                   .withTabletsUsed(false)
                                                   .withFillLevel(fillLevel)
                                                   .build();
        when(door.closed()).thenReturn(true);
        RunResult result = dishWasher.start(programConfiguration);
        assertSame(result.getStatus(), Status.SUCCESS);
    }

}
