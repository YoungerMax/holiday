package com.pocolifo.holiday;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

class HolidayTest {
    @Test
    void testHoliday() throws IOException {
        Holiday h = new Holiday();

        StringBuilder b = new StringBuilder("<link href=\"style.css\" rel=\"stylesheet\" />\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");

        h.lookup("Moms for Liberty", (card, o) -> {
            System.out.println("LOADED: " + card.getName());

            if (o instanceof Exception) {
                b.append(String.format("<div class=\"card\"><h3>%s</h3>An error occurred</div>", card.getName()));
            } else {
                b.append(String.format("<div class=\"card\"><h3>%s</h3>%s</div>", card.getName(), o));
            }
        });

        Files.write(new File("out.html").toPath(), b.toString().getBytes(StandardCharsets.UTF_8));
    }
}