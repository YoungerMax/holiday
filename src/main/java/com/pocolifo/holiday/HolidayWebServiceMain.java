package com.pocolifo.holiday;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.staticfiles.Location;
import org.jetbrains.annotations.NotNull;

public class HolidayWebServiceMain {
    public static void main(String[] args) {
        Javalin javalin = Javalin.create();

        javalin.updateConfig(config -> {
            config.staticFiles.add("/static", Location.CLASSPATH);
        });

        javalin.start("0.0.0.0", 10000);
        javalin.get("/", new SearchHandler());

        while (true);
    }

    public static class SearchHandler implements Handler {
        @Override
        public void handle(@NotNull Context ctx) throws Exception {
            Holiday holiday = new Holiday();

            StringBuilder b = new StringBuilder("<link href=\"/style.css\" rel=\"stylesheet\" />" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n");

            holiday.lookup(ctx.queryParam("q"), (card, o) -> {
                System.out.println("LOADED: " + card.getName());

                if (o instanceof Exception) {
                    ((Exception) o).printStackTrace();
                    b.append(String.format("<div class=\"card\"><h3>%s</h3>An error occurred</div>", card.getName()));
                } else {
                    b.append(String.format("<div class=\"card\"><h3>%s</h3>%s</div>", card.getName(), o));
                }
            });

            System.out.println("finished");

            ctx.html(b.toString());
        }
    }
}
