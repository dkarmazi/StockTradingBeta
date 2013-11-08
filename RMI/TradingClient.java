package RMI;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TradingClient {

    private static final int PORT = 2019;

    public static void main(String args[]) {
	try {
	    // Make reference to SSL-based registry
		String host = (args.length < 1) ? "127.0.0.1" : args[0];
		
	    Registry registry = LocateRegistry.getRegistry(
		InetAddress.getByName(host).getHostName(), PORT,
		new RMISSLClientSocketFactory());

	    // "serverInterface" is the identifier that we'll use to refer
	    // to the remote object that implements the "serverInterface"
	    // interface
	    ServerInterface serverInterface = (ServerInterface) registry.lookup("TradingServer");

	    String message = "blank";
	    message = serverInterface.sayHello();
	    System.out.println(message+"\n");
	} catch (Exception e) {
	    System.out.println("TradingClient exception: " + e.getMessage());
	    e.printStackTrace();
	}
    }
}
