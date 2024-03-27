Feature: Rest Api

  Scenario: Verify Application Health
    Given the system up and running
    When GET the Application Health
    Then the GET status should be OK  
    When GET the Application Info
    Then the GET status should be OK  

  Scenario: Verify CRUD Category operation
    Given a new Category
    | CAT1 | Category example |
    When create that Category
    Then should be able to get that Category
    And the GET status should be OK
    And the Category from POST should the same as from GET
    When update the Category
    | CAT2 | Category updated |
    Then the PUT status should be OK
    And should be able to find that Category
    And the Category from PUT should the same as from GET_ALL
    When delete the Category
    Then the DELETE status should be NO_CONTENT
    And should be not able to get that Category

  Scenario: Verify CRUD Product operation
    Given a new Category
    | CAT1 | Category example |
    And a new Product
    | PROD1 | Product description | image.jpg | JPG | 10 | L |
    When create that Product
    Then should be able to get that Product
    And the GET status should be OK
    And the Product from POST should the same as from GET
    When update the Product
    | PROD2 | Description updated | image.gif | GIF | 11 | M |
    Then the PUT status should be OK
    And should be able to find that Product
    And the Product from PUT should the same as from GET_ALL
    When delete the Product
    Then the DELETE status should be NO_CONTENT
    And should be not able to get that Product

  Scenario: Verify CRUD Order operation
    Given a new Category
    | CAT1 | Category example |
    And a new Product
    | PROD1 | Product description | image.jpg | JPG | 10 | L |
    And a new OrderItem
    | 100 |
    And a new Order
    | ORD1 | Customer name |
    When create that Order
    Then should be able to get that Order
    And the GET status should be OK
    And that Order should have 1 items
    And the Order from POST should the same as from GET
    When update the Order
    | ORD2 | Customer name updated |
    Then the PUT status should be OK
    And should be able to find that Order
    And the Order from PUT should the same as from GET_ALL
    When delete the Order
    Then the DELETE status should be NO_CONTENT
    And should be not able to get that Order
    When delete the Product
    Then the DELETE status should be NO_CONTENT
    When delete the Category
    Then the DELETE status should be NO_CONTENT

  Scenario: Verify CRUD Order Item operation
    Given a new Category
    | CAT1 | Category example |
    And a new Product
    | PROD1 | First Product description | image.jpg | JPG | 10 | L |
    And a new OrderItem
    | 50 |
    And a new Order
    | ORD1 | Customer name |
    And a new Product
    | PROD2 | Second Product description | image.gif | GIF | 12 | L |
    And a new OrderItem
    | 100 |
    When create that Order
    And create that OrderItem
    Then the POST status should be CREATED
    And should be able to get that Order
    And that Order should have 2 items
    When delete the OrderItem
    Then the DELETE status should be NO_CONTENT
    And should be able to get that Order
    And that Order should have 1 items
