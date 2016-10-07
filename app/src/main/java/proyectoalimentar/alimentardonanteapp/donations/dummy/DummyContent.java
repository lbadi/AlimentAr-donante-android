package proyectoalimentar.alimentardonanteapp.donations.dummy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import proyectoalimentar.alimentardonanteapp.model.Donation;
import proyectoalimentar.alimentardonanteapp.model.State;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Donation> ITEMS = new ArrayList<Donation>();


    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Donation item) {
        ITEMS.add(item);
    }

    private static Donation createDummyItem(int position) {
        return new Donation(State.ACTIVE, "Descripción" + position, Calendar.getInstance(), Calendar.getInstance());
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
