package tutorial.example5;


import java.io.BufferedReader;
import java.io.InputStreamReader;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;

import org.osgi.util.tracker.ServiceTracker;


import tutorial.example2.service.DictionaryService;


public class Activator implements BundleActivator {

	private BundleContext m_context = null;
	private ServiceTracker m_tracker = null;

	public void start(BundleContext context) throws Exception {
		m_context = context;
		String serviceName = DictionaryService.class.getName();
		String filterPattern = "(&(objectClass=" + serviceName 
				+ ")(Language=*))";
		Filter filter = m_context.createFilter(filterPattern);
		m_tracker = new ServiceTracker(m_context, filter, null);
		m_tracker.open();
		System.out.println("Enter a blank line to exit.");
		String word = "";
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		while (true) {
			System.out.print("Enter word: ");
			word = br.readLine();
			DictionaryService dictionary = 
					(DictionaryService) m_tracker.getService();
			if (word.length() == 0) {
				break;
			} else if (dictionary == null) {
				System.out.println("No dictionary available.");
			} else if (dictionary.checkWord(word)) {
				System.out.println("Correct.");
			} else {
				System.out.println("Incorrect.");
			}
		}
		System.out.println(
				"Dictionary Client with Service Tracker has been started");
	}
	
	public void stop(BundleContext context) {
		System.out.println(
				"Dictionary Client with Service Tracker has been stopped");
	}
}