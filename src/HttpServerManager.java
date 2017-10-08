import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class HttpServerManager
{
	private final static HttpServerManager instance = new HttpServerManager();

	private HttpServer httpServer = null;

	/**
	 * 
	 */
	private ExecutorService executor = null;

	private HttpServerManager()
	{

	}

	public final static HttpServerManager getInstance()
	{
		return instance;
	}

	public final void init() throws IOException
	{
		this.executor = Executors.newCachedThreadPool();
		final InetSocketAddress sa = new InetSocketAddress("0.0.0.0", 8080);
		this.httpServer = HttpServer.create(sa, 0);
		this.httpServer.setExecutor(this.executor);
		this.httpServer.createContext("/", new HttpServerHandler());
		this.httpServer.start();
	}

	public final void exit()
	{
		this.executor.shutdown();
		this.httpServer.stop(0);
	}

	public static void main(String[] args) throws IOException
	{
		HttpServerManager.getInstance().init();
	}
}