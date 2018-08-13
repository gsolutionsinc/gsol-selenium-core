/*
 * Copyright <2018> <G-Solutions, Inc.>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.gsol.selenium.pageobject;

import com.gsol.utils.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is to encapsulate all Page related common actions.
 * Every Page class of the application will extend this abstract class to inherit all these common actions.
 *
 * @param <T>
 */

//@Component
public abstract class Page<T extends Page<T>> extends LoadableComponent<T> {

    // TODO add logger
    //private PageActions pageAction;
    private static final int DEFAULT_ELEMENT_WAIT = 60;
    private static String mainWindow;
    //
    private static List<String> windowList = new ArrayList<String>();
    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;

    protected Page(WebDriver webDriver) {

        this.webDriver = webDriver;
        this.load(); // this is to support lazy binding for annotation driven page elements
        this.webDriverWait = new WebDriverWait(webDriver, DEFAULT_ELEMENT_WAIT);
        this.webDriver.manage().window().setSize(new Dimension(1280, 1024));
    }

    @Override
    protected void load() {
        PageFactory.initElements(webDriver, this);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Wait for the presence of a web element on the page before taking any action on it.
     *
     * @param locator the locator of the web element
     * @return the presence of the element
     */
    protected WebElement waitForPresenceOfElement(By locator) {
        return webDriverWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for the visibility of a web element on the page before taking any action on it.
     *
     * @param webElement the web element to check
     * @return the visible element
     */
    protected WebElement waitForVisibilityOfElement(WebElement webElement) {
        return webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
    }

    /**
     * Wait for the visibility of a web element based on a locator on the page before taking any action on it.
     *
     * @param locator the locator of the web element
     * @return the visible element
     */
    protected WebElement waitForVisibilityOfElementByLocator(By locator) {
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Click the web element, if click-able.
     *
     * @param webElement the web element to click
     */
    protected void webElementClick(WebElement webElement) {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement)).click();
    }

    /**
     * Clear and set text in the web element which are of input types.
     *
     * @param webElement the web element to enter the input
     * @param value      the value to be entered
     */
    protected void clearAndSetText(WebElement webElement, String value) {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
        webElement.clear();
        webElement.sendKeys(value.replace("\\s", " "));
    }

    /**
     * Get value inserted in the web element which are of input types.
     *
     * @param webElement the web element to read the value from
     * @return the value read from the web element
     */
    protected String getInputElementValue(WebElement webElement) {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
        return webElement.getAttribute("value");
    }

    /**
     * Navigate to a new url
     *
     * @param url the URL to navigate to
     */
    protected void navigateToUrl(String url) {
        mainWindow = webDriver.getWindowHandle();
        windowList.add(mainWindow);
        webDriver.get(url);
    }

    /**
     * Gets current page title.
     *
     * @return current page title.
     */
    protected String getPageTitle() {
        return webDriver.getTitle();
    }

    /**
     * Gets page source.
     *
     * @return page source.
     */
    protected String getPageSource() {
        return webDriver.getPageSource();
    }

    /**
     * Get the main window
     *
     * @return the main window
     */
    protected String getMainWindowHandle() {
        return mainWindow;
    }

    /**
     * Get the current window
     *
     * @return the current window
     */
    protected String getCurrentWindowHandle() {
        return webDriver.getWindowHandle();
    }

    /**
     * Close the current active window
     */
    protected void closeCurrentWindow() {
        windowList.remove(webDriver.getWindowHandle()); // remove from the windows list before closing
        webDriver.close();
    }

    /**
     * Swtich to newly opened window
     */
    protected void switchToNewWindow() {
        DateUtils.waitUntilReady(3);
        for (String newWindowHandle : webDriver.getWindowHandles()) {
            if (!windowList.contains(newWindowHandle)) {
                windowList.add(newWindowHandle);
                webDriver.switchTo().window(newWindowHandle).manage().window().maximize();
            }
        }
    }

    /**
     * Switch to the main window
     */
    protected void switchToMainWindow() {
        webDriver.switchTo().window(mainWindow);
    }

    /**
     * Closes the web driver.
     */
    protected void close() {
        webDriver.close();
    }

    /**
     * Quits the web driver.
     */
    protected void quit() {
        webDriver.quit();
    }
}
