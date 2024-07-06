Feature: Company Rest Api

  Scenario: Verify Company Application Health
    Given the system up and running

  Scenario: Verify CRUD Company operation
    Given a new Company
      | Groupon | test@mail | www.groupon.example | HEADQUARTER | My Street, Kingston, New York | 12401 | US | USD | XX-XXXXXXX | (555) 555-1234 | test@mail |
    When create that Company
    Then should be able to get that Company
    And the Company from POST should be the same as from GET
    When update the Company
      | Groupon | test@mail | www.groupon.example |
    And get all Companies
    Then the Company from PUT should be the same as from GET_ALL
    When delete the Company
    Then that Company should be not present
