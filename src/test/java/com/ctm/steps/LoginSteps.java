package com.ctm.steps;

import com.ctm.driver.ConfigReader;
import com.ctm.driver.DriverFactory;
import com.ctm.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps {
    private LoginPage loginPage;

    @Given("the user opens the SauceDemo login page")
    public void openLoginPage() {
        loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.open(System.getProperty("baseUrl", ConfigReader.get("baseUrl")));
    }

    @When("the user logs in with username {string} and password {string}")
    public void login(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("the products page should be displayed")
    public void verifyProductsPage() {
        Assert.assertTrue(loginPage.productsPageIsDisplayed(), "Products page was not displayed");
    }

    @Then("the login error should contain {string}")
    public void verifyLoginError(String expected) {
        Assert.assertTrue(loginPage.getErrorMessage().contains(expected),
            "Expected error text: " + expected);
    }
}
