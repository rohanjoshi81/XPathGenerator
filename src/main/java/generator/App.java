package generator;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class App {
	
	static {
	System.setProperty("webdriver.chrome.driver","D:\\chromedriver.exe");
	}
	
	static WebDriver wd = new ChromeDriver();
	
	public static void waitForPageLoaded(WebDriver webDriver) {
	    ExpectedCondition<Boolean> expectation = new
	            ExpectedCondition<Boolean>() {
	                public Boolean apply(WebDriver driver) {
	                    return ((JavascriptExecutor) driver).executeAsyncScript(
	                        "var callback = arguments[arguments.length - 1];" +
	                        "if (document.readyState !== 'complete') {" +
	                        "  callback('document not ready');" +
	                        "} else {" +
	                        "  try {" +
	                        "    var testabilities = window.getAllAngularTestabilities();" +
	                        "    var count = testabilities.length;" +
	                        "    var decrement = function() {" +
	                        "      count--;" +
	                        "      if (count === 0) {" +
	                        "        callback('complete');" +
	                        "      }" +
	                        "    };" +
	                        "    testabilities.forEach(function(testability) {" +
	                        "      testability.whenStable(decrement);" +
	                        "    });" +
	                        "  } catch (err) {" +
	                        "    callback(err.message);" +
	                        "  }" +
	                        "}"
	                    ).toString().equals("complete");
	                }
	            };
	    try {
	        WebDriverWait wait = new WebDriverWait(webDriver, 10);
	        wait.until(expectation);
	    } catch (Throwable error) {
	        new Exception("Timeout waiting for Page Load Request to complete.");
	    }
	} 
	
	
	public static void main(String [] args)
	{
		String URL = "http://192.168.172.20/main";  // "http://www.google.com"
		
		wd.manage().window().maximize();
		
		wd.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);  // IMplicit Wait by using timeouts() on webdriver
		
		wd.get(URL);
		
		waitForPageLoaded(wd);
		
		
		// 2 CLICKS TO REACH A MENU 
		
		wd.findElement(By.xpath("//*[@id=\'first-level\']/div[2]/div[2]/a")).click();
		
		wd.findElement(By.xpath("//*[@id=\'second-level\']/div[2]/div[1]/a")).click(); // THIS IS THE MENU TO BE AUTMOATED
		 
		waitForPageLoaded(wd);
			
		List<WebElement> eList = wd.findElements(By.cssSelector("*"));

		HashMap<String, String> xMap = new HashMap<String, String>();

		System.out.println("__   _______     _______ _    _ \r\n" + 
				"\\ \\ / /  __ \\ /\\|__   __| |  | |\r\n" + 
				" \\ V /| |__) /  \\  | |  | |__| |\r\n" + 
				"  > < |  ___/ /\\ \\ | |  |  __  |\r\n" + 
				" / . \\| |  / ____ \\| |  | |  | |\r\n" + 
				"/_/ \\_\\_| /_/    \\_\\_|  |_|  |_|\r\n" + 
				"                                \r\n" + 
				"                                \r\n" + 
				"  _____ ______ _   _ ______ _____         _______ ____  _____  \r\n" + 
				" / ____|  ____| \\ | |  ____|  __ \\     /\\|__   __/ __ \\|  __ \\ \r\n" + 
				"| |  __| |__  |  \\| | |__  | |__) |   /  \\  | | | |  | | |__) |\r\n" + 
				"| | |_ |  __| | . ` |  __| |  _  /   / /\\ \\ | | | |  | |  _  / \r\n" + 
				"| |__| | |____| |\\  | |____| | \\ \\  / ____ \\| | | |__| | | \\ \\ \r\n" + 
				" \\_____|______|_| \\_|______|_|  \\_\\/_/    \\_\\_|  \\____/|_|  \\_\\\r\n" + 
				"                                                               ");
		
		int i=0;
		
		
		// Limiting the generation for selected tags - 'INPUT', 'BUTTON', 'SELECT', 'TEXTAREA' , 'A' 
		
		for (WebElement e : eList) {
			System.out.print(".");
			if(Stream.of("INPUT", "BUTTON", "SELECT", "TEXTAREA", "A").anyMatch(e.getTagName()::equalsIgnoreCase))
				xMap.put("Element#" + i +" " + e.getTagName() + " "+ (!e.getAttribute("name").equals("")?e.getAttribute("name"):e.getAttribute("id")), generateXpath(e));
			i++;
		}
		
		System.out.println("\n\n");
		
		xMap.forEach((k, v) -> System.out.println(k + "     " + v));
		// Map not allowing duplicate values as keys so skipping tags
		
		System.out.println("\n\nTotal XPath generated : "+xMap.keySet().size());
		
		
		wd.close();
			
	}

	@SuppressWarnings("unchecked")
	public static String generateXpath(WebElement e) {

		if (e.getAttribute("id") != null && !e.getAttribute("id").equals(""))
			return "//" + e.getTagName() + "[@id='" + e.getAttribute("id") + "']";
		if (e.getTagName().equals("html"))
			return "/html[1]";

		int ctr = 0;

		List<WebElement> siblings = (List<WebElement>) ((JavascriptExecutor) wd)
				.executeScript("return arguments[0].parentNode.childNodes;", e);

		WebElement parent = (WebElement) ((JavascriptExecutor) wd).executeScript("return arguments[0].parentNode;",
				e);

		for (int i = 0; i < siblings.size(); i++) {

			WebElement sibling = null;

			if ( siblings.get(i) instanceof WebElement) {
				sibling = siblings.get(i);

				Long nodeType = (Long) ((JavascriptExecutor) wd).executeScript("return arguments[0].nodeType;",
						sibling);

				if (sibling.equals(e))
					return generateXpath(parent) + '/' + e.getTagName().toLowerCase() + '[' + (ctr + 1) + ']';

				if (nodeType == 1 && sibling.getTagName().equalsIgnoreCase(e.getTagName().toLowerCase()))
					ctr++;
			}

		}

		return null;

	}
}


/*
 * 
 * 	 This piece of code will print the entire DOM to console

String javascript = "return arguments[0].innerHTML";
String pageSource=(String)((JavascriptExecutor)wd)
    .executeScript(javascript, wd.findElement(By.tagName("html")));
pageSource = "<html>"+pageSource +"</html>";
System.out.println(pageSource);



 */
	