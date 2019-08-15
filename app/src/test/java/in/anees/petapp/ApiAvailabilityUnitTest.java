package in.anees.petapp;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import in.anees.petapp.constant.Constants;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ApiAvailabilityUnitTest {

    @Test public void testAvailabilityPetListURL() throws Exception {
        StringBuffer bufferResult = checkUrl(Constants.PET_LIST_URL);
        assert bufferResult.length() > 0;

    }

    @Test public void testAvailabilityConfigurationURL() throws Exception {
        StringBuffer bufferResult = checkUrl(Constants.CONFIG_URL);
        assert bufferResult.length() > 0;

    }

    private StringBuffer checkUrl(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        InputStream response = connection.getInputStream();

        StringBuffer buffer = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response, Charset.defaultCharset()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                buffer.append(line);
            }
        }
        return buffer;
    }
}