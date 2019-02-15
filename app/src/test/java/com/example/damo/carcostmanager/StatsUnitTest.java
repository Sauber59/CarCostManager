package com.example.damo.carcostmanager;

import com.example.damo.carcostmanager.classes.Cost;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class StatsUnitTest {

    StatsActivity statsActivity = new StatsActivity();
    List<Cost> costList = new ArrayList<>();

    @Test
    public void calculateDistance_isCorrect() throws Exception{
        fillListFuel(costList);
        float result = statsActivity.calculateDistance(costList);
        float expected = 700;
        assertEquals(expected, result, 0);
    }

    @Test
    public void calculateKilometerCost_isCorrect() throws Exception{
        fillListFuel(costList);
        float result = statsActivity.calculateKilometerCost(costList, 700);
        float expected = 1;
        assertEquals(expected, result, 0);
    }

    @Test
    public void calculateAverageFuelConsumption_isCorrect() throws Exception{
        fillListFuel(costList);
        float result = statsActivity.calculateAverageFuelConsumption(costList, 400);
        float expected = 10;
        assertEquals(expected, result, 0);
    }

    @Test
    public void calculateAllCostSUM_isCorrect() throws Exception{
        float result = statsActivity.calculateAllCostSUM(1, 2 ,3, 4);
        float expected = 10;
        assertEquals(expected, result, 0);
    }

    @Test
    public void roundFloatTo2_isCorrect() throws Exception{
        float result = statsActivity.roundFloatTo2(1.5999F);
        float expected = 1.60F;
        assertEquals(expected, result, 0);
    }

    public void fillListFuel(List<Cost> costList){
        costList.clear();
        costList.add(new Cost("1", "01-02-2019", 60 ,12, 200, "tankowanie"));
        costList.add(new Cost("1", "02-02-2019", 50 ,10, 400, "tankowanie"));
        costList.add(new Cost("1", "02-02-2019", 60 ,12, 600, "tankowanie"));
        costList.add(new Cost("1", "02-02-2019", 90 ,18, 900, "tankowanie"));

    }
}