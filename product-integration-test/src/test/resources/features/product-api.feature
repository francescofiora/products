Feature: Product Rest Api

  Scenario: Verify Product Application Health
    Given the system up and running

  Scenario: Verify CRUD Category operation
    Given a new Category
    | CAT1 | Category example |
    When create that Category
    Then should be able to get that Category
    And the Category from POST should be the same as from GET
    When update the Category
    | CAT2 | Category updated |
    And get all Categories
    Then the Category from PUT should be the same as from GET_ALL
    When delete the Category
    Then that Category should be not present

  Scenario: Verify CRUD Product operation
    Given a new Category
    | CAT1 | Category example |
    And a new Product
    | PROD1 | Product description | image.jpg | JPG | 10 | L |
    When create that Product
    Then should be able to get that Product
    And the Product from POST should be the same as from GET
    When update the Product
    | PROD2 | Description updated | image.gif | GIF | 11 | M |
    And get all Products
    Then the Product from PUT should be the same as from GET_ALL
    When delete the Product
    Then that Product should be not present

  Scenario: Verify CRUD Order operation
    Given a new Category
    | CAT1 | Category example |
    And a new Product
    | PROD1 | Product description | image.jpg | JPG | 10 | L |
    And a new OrderItem
    | 100 |
    And a new Order
    | ORD1 | 1 |
    When create that Order
    Then should be able to get that Order
    And that Order should have 1 items
    And the Order from POST should be the same as from GET
    When update the Order
    | ORD2 | 2 |
    And get all Orders
    Then the Order from PUT should be the same as from GET_ALL
    When delete the Order
    Then that Order should be not present
    When delete the Product
    Then that Product should be not present
    When delete the Category
    Then that Category should be not present

  Scenario: Verify CRUD Order Item operation
    Given a new Category
    | CAT1 | Category example |
    And a new Product
    | PROD1 | First Product description | image.jpg | JPG | 10 | L |
    And a new OrderItem
    | 50 |
    And a new Order
    | ORD1 | 3 |
    And a new Product
    | PROD2 | Second Product description | image.gif | GIF | 12 | L |
    And a new OrderItem
    | 100 |
    When create that Order
    And create that OrderItem
    Then should be able to get that Order
    And that Order should have 2 items
    When delete the OrderItem
    Then should be able to get that Order
    And that Order should have 1 items
