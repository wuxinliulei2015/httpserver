import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.ssl.HttpsURLConnection;

public class HttpServerHandler implements HttpHandler
{
	public void onError(HttpExchange exchange) throws IOException
	{
		exchange.sendResponseHeaders(HttpsURLConnection.HTTP_BAD_REQUEST, 0);
		exchange.getResponseBody().close();
	}

	public static String getQueryString(HttpExchange exchange) throws IOException
	{

		if (exchange.getRequestMethod().equalsIgnoreCase("GET"))
		{
			return exchange.getRequestURI().getQuery();
		}

		String requestBodyString = getRequestBodyString(exchange);

		if (requestBodyString.length() == 0)
		{
			return exchange.getRequestURI().getQuery();
		}
		return requestBodyString;
	}

	public static String getRequestBodyString(HttpExchange exchange) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null)
		{
			builder.append(line);
		}

		return builder.toString();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void handle(HttpExchange exchange) throws IOException
	{
		OutputStream os = null;
		final URI uri = exchange.getRequestURI();
		// http://127.0.0.1:8080/test/helloWorld
		final String path = uri.getPath();
		final String query = getQueryString(exchange);
		final Headers responseHeaders = exchange.getResponseHeaders();
		// ·µ»Ø½á¹û
		final byte[] result = new byte[]
		{ ' ', ' ' };

		responseHeaders.set("Content-Type", "text/plain");
		responseHeaders.set("Content-length", String.valueOf(result.length));

		exchange.sendResponseHeaders(HttpsURLConnection.HTTP_OK, 0);
		os = exchange.getResponseBody();
		os.write(result);

		os.close();
	}
}