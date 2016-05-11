package tutorial.example3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import tutorial.example2.service.DictionaryService;

public class Activator implements BundleActivator {
	public void start(BundleContext context) throws Exception {
		String serviceName = DictionaryService.class.getName();
		String filter = "(Language=*)";
		ServiceReference[] refs = context.getServiceReferences(
				serviceName, filter);
		if (refs != null) {
			try {
				System.out.println("Enter a blank line to exit.");
				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(isr);
				String word = "";
				
				while (true) {
					System.out.print("Enter word: ");
					word = br.readLine();
					if (word.length() == 0) {
						break;
					}
					printWordCheckingResult(context, refs, word);
				}
			} catch (IOException e) {
			}
		} else {
			System.out.println("Couldn't find any dictionary service ...");
		}
		
		System.out.println("Dictionary Client Bundle has been started");
	}
	
	public void stop(BundleContext context) {
		System.out.println("Dictionary Client Bundle has been stopped");
	}
	
	private void printWordCheckingResult(
			BundleContext context, ServiceReference[] refs, String word
	) {
		DictionaryService dictionary = getDictionary(context, refs);
		if (dictionary.checkWord(word)) {
			System.out.println("Correct.");
		} else {
			System.out.println("Incorrect.");
		}
		context.ungetService(refs[0]);
	}
	
	private DictionaryService getDictionary(
			BundleContext context, ServiceReference[] refs
	) {
		return (DictionaryService) context.getService(refs[0]);
	}
}