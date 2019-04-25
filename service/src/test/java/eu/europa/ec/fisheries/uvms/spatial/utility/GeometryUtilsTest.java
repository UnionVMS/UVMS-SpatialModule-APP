/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.utility;

import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import eu.europa.ec.fisheries.uvms.commons.geometry.utils.GeometryUtils;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.SneakyThrows;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junitparams.JUnitParamsRunner.$;

@RunWith(JUnitParamsRunner.class)
@Ignore

public class GeometryUtilsTest extends BaseUnitilsTest {

    @Test(expected = IllegalArgumentException.class)
    @Parameters(method = "parameterValues")
    @SneakyThrows
    public void shouldThrowIllegalArgumentException(double latitude, double longitude, int crs) {
        GeometryUtils.toGeographic(latitude, longitude, crs);
    }

    @Test
    @SneakyThrows
    public void test2() {

        System.out.println(GeometryUtils.toGeographic(-21D, 50D, 3216));
    }

    protected Object[] parameterValues(){

        return $(
                $(-150, 3, 4326), $(-90.01, 3, 4326), $(90.01, 3, 4326), $(-180, 3, 4326),
                $(3000, 3, 4326), $(-91, 3d, 4326), $(91, 3d, 4326), $(0, 188, 4326), $(90, -1288, 4326)
        );
    }
}
