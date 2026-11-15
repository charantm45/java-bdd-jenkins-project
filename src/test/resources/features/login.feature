Feature: SauceDemo login

  Background:
    Given the user opens the SauceDemo login page

  @smoke @positive
  Scenario: Login with valid credentials
    When the user logs in with username "standard_user" and password "secret_sauce"
    Then the products page should be displayed

  @negative
  Scenario: Login with invalid credentials
    When the user logs in with username "invalid_user" and password "wrong_password"
    Then the login error should contain "Username and password do not match"
