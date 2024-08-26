import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

// This test class inherits BasicSetupTest class, where the browser is initialized
// browser variable is available here as it's inherited, so you'll have it available at any place
public class SeleniumTestngTest extends BasicSetupTest {

    private static final Logger logger = LogManager.getLogger(SeleniumTestngTest.class);

    @BeforeMethod
    public void openDefaultBrowserPage() {
        logger.info("Opening the 'Herokuapp' homepage");
        browser.get("https://the-internet.herokuapp.com/");
    }

    @Test
    public void abTestingPageHasSpecificTextTest() throws InterruptedException {
        // Відкрийте сторінку "A/B Testing" та перевірте наявність тексту "A/B Test Control".
        logger.info("Redirecting to A/B Testing page");
        WebElement abTestingTaskLink = browser.findElement(By.linkText("A/B Testing"));
        abTestingTaskLink.click();

        WebElement abHeader = browser.findElement(By.cssSelector("h3"));
        Assert.assertTrue(abHeader.getText().contains("A/B Test"));
    }

    @Test
    public void addRemoveElementsTest() throws InterruptedException {
        // На сторінці "Add/Remove Elements" додайте 3 кнопки "Delete", в тесті переконайтеся що вони
        // дійсно з*явилися і є видимими, потім видаліть їх всі і переконайтеся, що вони видалились.
        logger.info("Redirecting to the 'Add/Remove Elements' page");
        WebElement addRemoveElementsLink = browser.findElement(By.linkText("Add/Remove Elements"));
        addRemoveElementsLink.click();

        By deleteButtonsDiv = By.cssSelector("#elements>*");
        WebElement addElementBtn = browser.findElement(By.cssSelector("button[onclick='addElement()']"));

        logger.info("Adding new elements and check if they appeared");
        addElementBtn.click();
        addElementBtn.click();
        addElementBtn.click();

        Thread.sleep(100);

        List<WebElement> addedBtns = browser.findElements(deleteButtonsDiv);
        Assert.assertEquals(addedBtns.size(), 3);
        addedBtns.forEach(btn -> {
            Assert.assertTrue(btn.isDisplayed(), "Delete button should be displayed");
            Assert.assertEquals(btn.getText(), "Delete", "Delete button text mismatch");
        });

        logger.info("Deleting added elements");
        addedBtns.forEach(WebElement::click);
        addedBtns = browser.findElements(deleteButtonsDiv);
        Assert.assertTrue(addedBtns.isEmpty(), "Delete buttons should not be visible");
    }


    @Test
    public void selectCheckboxesTest() throws InterruptedException {
        // Перейдіть на сторінку "Checkboxes", поставте всі чекбокси у вибране положення
        // (переконайтеся, що усі чекбокси дійсно стали у положення "checked"/"вибрано", тобто в них стоїть "пташечка")
        logger.info("Redirecting to the 'Checkboxes' page");
        WebElement checkboxesLink = browser.findElement(By.linkText("Checkboxes"));
        checkboxesLink.click();

        By checkboxesElement = By.cssSelector("input[type=checkbox]");
        List<WebElement> checkboxes = browser.findElements(checkboxesElement);

        checkboxes.forEach(checkbox -> {
            if (!checkbox.isSelected()) {
                checkbox.click();
                logger.info("Selecting not checked checkbox");
            }
        });
        checkboxes = browser.findElements(checkboxesElement);
        checkboxes.forEach(checkbox -> {
            Assert.assertTrue(checkbox.isSelected());
        });
    }

    @Test
    public void selectDropdownOptionTest() throws InterruptedException {
//    На сторінці "Dropdown" оберіть опцію "Option 2", переконайтеся, що опція стала активною.
        logger.info("Redirecting to the 'Dropdown' page");
        browser.findElement(By.linkText("Dropdown")).click();

        Thread.sleep(1000);
        browser.findElement(By.id("dropdown")).click();

        By option2Selector = By.cssSelector("option[value='2']");

        logger.info("Selecting 'Option 2' from the 'Dropdown'");
        browser.findElement(option2Selector).click();
        Assert.assertTrue(browser.findElement(option2Selector).isSelected(), "Option 2 should be selected.");
    }


    @Test
    public void authenticationWithValidCredentialsTest() throws InterruptedException {
//    На сторінці "Form Authentication" введіть валідні і коректні юзернейм і пароль і переконайтеся,
//    що ви зайшли в систему, після цього натисніть кнопку "Logout"
//    і переконайтесь, що вас вилогінило із системи
        logger.info("Redirecting to the 'Form Authentication' page");
        WebElement formAuthenticationLink = browser.findElement(By.linkText("Form Authentication"));
        formAuthenticationLink.click();
        WebElement usernameField = browser.findElement(By.id("username"));
        WebElement passwordField = browser.findElement(By.id("password"));

        String username = "tomsmith";
        String password = "SuperSecretPassword!";

        logger.info("Entering valid username and password");
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        Thread.sleep(1000);
        logger.info("Clicking 'Login' button and redirection to secure page");
        WebElement loginBtn = browser.findElement(By.xpath("//button"));
        loginBtn.click();

        WebElement secureHeader = browser.findElement(By.xpath("//h2"));
        Assert.assertTrue(secureHeader.getText().contains("Secure Area"));
        Assert.assertEquals(browser.getCurrentUrl(), "https://the-internet.herokuapp.com/secure");

        logger.info("Clicking 'Logout' button");
        WebElement logoutBtn = browser.findElement(By.xpath("//i[contains(text(), 'Logout')]"));
        logoutBtn.click();

        WebElement loginHeader = browser.findElement(By.xpath("//h2"));
        Assert.assertEquals(loginHeader.getText(), "Login Page");
        Assert.assertEquals(browser.getCurrentUrl(), "https://the-internet.herokuapp.com/login");
    }

    @Test
    public void dragAndDropTest() throws InterruptedException {
//    На сторінці "Drag and Drop" перетягніть eлемент A на місце елемента B,
//    та переконайтеся що вони помінялися місцями
        logger.info("Redirecting to the 'Drag and Drop' page");
        WebElement dragAndDropLink = browser.findElement(By.linkText("Drag and Drop"));
        dragAndDropLink.click();
        WebElement elementA = browser.findElement(By.id("column-a"));
        WebElement elementB = browser.findElement(By.id("column-b"));

        logger.info("Moving element A to the position of element B");
        Actions actions = new Actions(browser);
        actions.dragAndDrop(elementA, elementB).build().perform();

        Thread.sleep(1000);

        WebElement firstElementHeader = browser.findElement(By.xpath("//*[@id=\"column-a\"]/header"));
        WebElement secondElementHeader = browser.findElement(By.xpath("//*[@id=\"column-b\"]/header"));

        Assert.assertEquals(firstElementHeader.getText(), "B");
        Assert.assertEquals(secondElementHeader.getText(), "A");
    }

    @Test
    public void verifyHorizontalSliderTest() throws InterruptedException {
//    На сторінці "Horizontal Slider" перетягніть слайдер в положення, що відрізняється від 0 і перевірте,
//    що необхідне значення дійсно було встановлене на слайдері
        logger.info("Redirecting to the 'Horizontal Slider' page");
        WebElement horizontalSliderLink = browser.findElement(By.linkText("Horizontal Slider"));
        horizontalSliderLink.click();

        WebElement slider = browser.findElement(By.xpath("//input"));
        int shift = new Random().nextInt(50);

        logger.info("Moving slider to {} pixel(s) right", shift);
        Actions actions = new Actions(browser);
        actions.clickAndHold(slider)
                .moveByOffset(shift, 0)
                .release()
                .perform();

        Thread.sleep(100);
        WebElement sliderRangeElement = browser.findElement(By.id("range"));
        double sliderValue = Double.parseDouble(sliderRangeElement.getText());

        logger.info("Slider value after moving: {}", sliderValue);
        Assert.assertTrue(sliderValue > 0.0d, "Slider value should be greater than 0");
    }
}
