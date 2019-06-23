package generator;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class App {

	static {

		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

	}

	static WebDriver driver = new ChromeDriver();

	public static void main(String[] args) {

		String baseUrl = "https://clarity.design";

		driver.get(baseUrl);

		List<WebElement> eList = driver.findElements(By.cssSelector("*"));

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
		
		for (WebElement e : eList) {
			System.out.print(".");
			xMap.put(e.getTagName(), generateXpath(e));

		}

		xMap.forEach((k, v) -> System.out.println(k + "   " + v));

		driver.close();

	}

	@SuppressWarnings("unchecked")
	public static String generateXpath(WebElement e) {

		if (e.getAttribute("id") != null && !e.getAttribute("id").equals(""))
			return "//" + e.getTagName() + "[id='" + e.getAttribute("id") + "]'";
		if (e.getTagName().equals("html"))
			return "/html[1]";

		int ctr = 0;

		List<WebElement> siblings = (List<WebElement>) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].parentNode.childNodes;", e);

		WebElement parent = (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].parentNode;",
				e);

		for (int i = 0; i < siblings.size(); i++) {

			WebElement sibling = null;

			if ( siblings.get(i) instanceof WebElement) {
				sibling = siblings.get(i);

				Long nodeType = (Long) ((JavascriptExecutor) driver).executeScript("return arguments[0].nodeType;",
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
