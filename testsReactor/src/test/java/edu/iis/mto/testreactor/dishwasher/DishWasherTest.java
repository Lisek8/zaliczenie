package edu.iis.mto.testreactor.dishwasher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.iis.mto.testreactor.dishwasher.engine.Engine;
import edu.iis.mto.testreactor.dishwasher.pump.WaterPump;

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
    private WashingProgram irreleventWashingProgram;
    private FillLevel irreleventFillLevel;
    private boolean irreleventTabletsUsed;

    @BeforeEach
    public void setup() {
        dishWasher = new DishWasher(waterPump, engine, dirtFilter, door);
        irreleventWashingProgram = WashingProgram.ECO;
        irreleventFillLevel = FillLevel.FULL;
        irreleventTabletsUsed = true;
    }

    @Test
    public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

    // STATE TESTS
    @Test
    public void shouldWashDishesSuccessfullyWithAnyProgram() {
        programConfiguration = ProgramConfiguration.builder()
                                                   .withProgram(irreleventWashingProgram)
                                                   .withTabletsUsed(irreleventTabletsUsed)
                                                   .withFillLevel(irreleventFillLevel)
                                                   .build();
        when(door.closed()).thenReturn(true);
        when(dirtFilter.capacity()).thenReturn(DishWasher.MAXIMAL_FILTER_CAPACITY + 1d);
        RunResult result = dishWasher.start(programConfiguration);
        assertSame(result.getStatus(), Status.SUCCESS);
    }

    @Test
    public void shouldReturnOpenDoorErrorOnOpenDoor() {
        programConfiguration = ProgramConfiguration.builder()
                                                   .withProgram(irreleventWashingProgram)
                                                   .withTabletsUsed(irreleventTabletsUsed)
                                                   .withFillLevel(irreleventFillLevel)
                                                   .build();
        when(door.closed()).thenReturn(false);
        RunResult result = dishWasher.start(programConfiguration);
        assertSame(result.getStatus(), Status.DOOR_OPEN);
    }

    @Test
    public void shouldReturnErrorFilterErrorOnDirtyFilter() {
        programConfiguration = ProgramConfiguration.builder()
                                                   .withProgram(irreleventWashingProgram)
                                                   .withTabletsUsed(irreleventTabletsUsed)
                                                   .withFillLevel(irreleventFillLevel)
                                                   .build();
        when(door.closed()).thenReturn(true);
        when(dirtFilter.capacity()).thenReturn(DishWasher.MAXIMAL_FILTER_CAPACITY - 1d);
        RunResult result = dishWasher.start(programConfiguration);
        assertSame(result.getStatus(), Status.ERROR_FILTER);
    }

    // BEHAVIORAL TESTS
    @Test
    public void shouldCheckIfDoorIsClosedAndIfFilterIsClean() {
        programConfiguration = ProgramConfiguration.builder()
                                                   .withProgram(irreleventWashingProgram)
                                                   .withTabletsUsed(true)
                                                   .withFillLevel(irreleventFillLevel)
                                                   .build();
        when(door.closed()).thenReturn(true);
        dishWasher.start(programConfiguration);
        InOrder order = inOrder(door, dirtFilter);
        order.verify(door).closed();
        order.verify(dirtFilter).capacity();
    }

}
