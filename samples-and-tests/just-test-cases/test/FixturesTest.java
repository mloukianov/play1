import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.management.RuntimeErrorException;

import models.Bloc;
import models.vendor.Vendor;
import models.vendor.tag.AreaTag;
import models.vendor.tag.FunctionTag;
import models.vendor.tag.Tag;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class FixturesTest extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteAll();
    }

    @Test
    public void loadData() {
        Fixtures.load("vendor-data.yml");
        assertEquals(2, Vendor.findAll().size());
        assertEquals(4, Tag.findAll().size());
        assertEquals(2, AreaTag.findAll().size());
        assertEquals(2, FunctionTag.findAll().size());

        assertEquals(2, Vendor.find(
                "SELECT DISTINCT v.id " +
                "FROM Vendor v " +
                "JOIN v.tags as t " +
                "WHERE t.label IN ('China', 'Wedding') " +
                "GROUP BY v.id HAVING count(t.id) = 2 ").fetch().size());

        assertEquals(1, Bloc.count());

        Bloc b = Bloc.<Bloc>findAll().get(0);
        assertEquals("Yop", b.name);
        assertEquals(2, b.criterias.size());
        assertEquals("value1", b.criterias.get("key1"));
        assertEquals("value2", b.criterias.get("key2"));

        try {
            assertEquals(new SimpleDateFormat("yyyy/MM/dd").parse("2005/06/18"), b.created);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

}
