Feature: Price calculation

  Scenario: Validate price calculation
    Given home page is displayed
    And user ensures he is using the correct zip code
      | zip code |
      | 64105    |
    When user searches for a product
      | item     |
      | Skittles |
    And user adds the cheapest one in the cart
    And user searches for a product
      | item     |
      | Snickers |
    And user adds the cheapest one in the cart
    And user goes to checkout page
    Then user should see his final price calculated correctly
    And if user is not signed in redirect him to registration page