Feature: feature to test google search functionality

  Scenario: Validate google search is working
    Given browser is open
    And user is oon google search page
    When user enters a text in search box
    And hit enter
    Then user is navigated to search result