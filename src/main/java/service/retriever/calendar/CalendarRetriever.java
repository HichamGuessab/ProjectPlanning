package service.retriever.calendar;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class CalendarRetriever {
    public static Calendar retrieve(URL url) {
        try {
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            inputStream = replaceLineEndings(inputStream);
            CalendarBuilder builder = new CalendarBuilder();
            return builder.build(inputStream);
        } catch (IOException | ParserException e) {
            throw new RuntimeException(e);
        }
    }

    private static InputStream replaceLineEndings(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int previousChar = -1;
        int currentChar;

        while ((currentChar = inputStream.read()) != -1) {
            if (currentChar == '\n' && previousChar != '\r') {
                outputStream.write('\r');
            }
            outputStream.write(currentChar);
            previousChar = currentChar;
        }

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
