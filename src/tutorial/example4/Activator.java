package tutorial.example4;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import tutorial.example2.service.DictionaryService;

public class Activator implements BundleActivator, ServiceListener {

	private BundleContext m_context = null;
	private ServiceReference m_ref = null;
	private DictionaryService m_dictionary = null;

	public void start(BundleContext context) throws Exception {
		m_context = context;
		synchronized(this) {
			String serviceName = DictionaryService.class.getName();
			String languageFilter = "(Language=*)";
			String filter = "(&(objectClass=" + serviceName + ")" 
					+ languageFilter + ")";

			m_context.addServiceListener(this, filter);
			
			ServiceReference[] refs = m_context.getServiceReferences(
					serviceName, languageFilter);
					
			if (refs != null) {
				m_ref = refs[0];
				m_dictionary = getDictionary();
			}
		}
		System.out.println("Enter a blank line to exit.");
		String word = "";
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		while (true) {
			System.out.print("Enter word: ");
			word = br.readLine();
			if (word.length() == 0) {
				break;
			} else if (m_dictionary == null) {
				System.out.println("No dictionary available.");
			} else if (m_dictionary.checkWord(word)) {
				System.out.println("Correct.");
			} else {
				System.out.println("Incorrect.");
			}
		}
		System.out.println("Robust Dictionary Client Bundle has been started");
	}
	
	public void stop(BundleContext context) {
		System.out.println("Robust Dictionary Client Bundle has been stopped");
	}
	
	public synchronized void serviceChanged(ServiceEvent event) {
		if (event.getType() == ServiceEvent.REGISTERED) {
			if (m_ref == null) {
				m_ref = event.getServiceReference();
				m_dictionary = (DictionaryService) m_context.getService(m_ref);
			}
		} else if (event.getType() == ServiceEvent.UNREGISTERING) {
			if (event.getServiceReference() == m_ref) {
				m_context.ungetService(m_ref);
				m_ref = null;
				m_dictionary = null;
				
				ServiceReference[] refs = null;
				try {
					String serviceName = DictionaryService.class.getName();
					String filter = "(Language=*)";
					refs = m_context.getServiceReferences(serviceName, filter);
				} catch (InvalidSyntaxException e) {}
				if (refs != null) {
					m_ref = refs[0];
					m_dictionary = getDictionary();
				}
			}
		}
	}
	
	private DictionaryService getDictionary() {
		if (m_context == null) {
			return null;
		}
		return (DictionaryService) m_context.getService(m_ref);
	}
}