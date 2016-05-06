package tutorial.example2b;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tutorial.example2.service.DictionaryService;

public class Activator implements BundleActivator {
	public void start(BundleContext context) {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("Language", "French");
		String serviceName = DictionaryService.class.getName();
		context.registerService(serviceName, new DictionaryImpl(), props);
		System.out.println("French dictionary bundle has been started");
	}
	
	public void stop(BundleContext context) {
		System.out.println("French dictionary bundle has been stopped");
	}
	
	public static class DictionaryImpl implements DictionaryService {
		String[] m_dictionary = {
				"bienvenue",
				"au",
				"tutoriel",
				"osgi"
		};
		
		public boolean checkWord(String word) {
			word = word.toLowerCase();
			for (int i = 0; i < m_dictionary.length; i++) {
				if (m_dictionary[i].equals(word)) {
					return true;
				}
			}
			return false;
		}
	}
}