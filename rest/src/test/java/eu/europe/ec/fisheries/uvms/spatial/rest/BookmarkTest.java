/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europe.ec.fisheries.uvms.spatial.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.bookmark.Bookmark;
import org.junit.Test;

public class BookmarkTest {

    @Test
    public void testOrdering() {

        Bookmark bookmark = new Bookmark();
        bookmark.setCreatedBy("2017-05-31 12:19:12 +0200");

        Bookmark bookmark2 = new Bookmark();
        bookmark2.setCreatedBy("2004-03-11 12:23:11 +0200");

        Bookmark bookmark3 = new Bookmark();
        bookmark3.setCreatedBy("2000-03-11 12:23:11 +0200");

        List<Bookmark> objects = new ArrayList<>();
        objects.add(bookmark);
        objects.add(bookmark2);
        objects.add(bookmark3);

        Collections.sort(objects);

        objects.get(0).getCreatedBy().equals("2000-03-11 12:23:11");
        objects.get(1).getCreatedBy().equals("2002-03-11 12:23:11");
        objects.get(2).getCreatedBy().equals("2004-03-11 12:23:11");

    }

}
