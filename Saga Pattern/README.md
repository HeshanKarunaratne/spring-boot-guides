# Saga Pattern

- Saga Pattern proposes implementing distributed transactions in the form of Sagas
- A Saga is nothing but a sequence of local transactions. These local transactions are occurring at the service level. Whenever a local transaction occurs, it publishes a message or an event. Such an event is responsible for triggering the next transaction in the Saga
- If any single transaction fails Saga executes a series of compensating transactions. These transactions basically undo the changes made by the preceding transactions

There are basically two types of Saga
- Choreography-Based Saga

![Diagram](resources/saga2.PNG "Diagram")

- Order Service is responsible for creating an Order. It also publishes an event for the same
- The Payment Service listens to that event and creates an Invoice
- When the Invoice is created, the Shipping Service creates the shipment
- When the Order is shipped, the Order Service updates the status of the Order
- Each service plays its part in the choreography
- Each service is basically dependent on the events coming out from other services

- Orchestration-Based Saga

![Diagram](resources/saga3.PNG "Diagram")

- Order Service creates an Order. Then, it also creates the Order Management Saga
- The Order Management Saga sends a Create Invoice Command to the Payment Service
- The Payment Service creates the Invoice and responds back to the Order Management Saga. Note that these responses can be totally asynchronous and message-driven as well
- In the next step, the Order Management Saga issues the Create Shipping Command to the Shipping Service
- The Shipping Service does the needful and creates the Shipping. It also replies back to the Order Management Saga
- The Order Management Saga changes the status of the Order and ends the Saga’s life-cycle

- 5 Major High Level Components
    - Order Service: 
        1. Exposes APIs to create an Order
        2. Manages the Order Aggregate(Maintains order related information)
        3. Handles Order Management Saga implementation
    - Payment Service: The Payment Service acts upon the Create Invoice Command issued by the Order Management Saga. Once it finishes its job, it publishes an event. This event pushes the Saga forward onto the next step.
    - Shipping Service: This service takes care of creating a shipment in the system corresponding to the Order. It acts upon a command issued by the Saga Manager. Once it does it’s job, it also publishes an event that pushes the Saga forward.
    - Core-APIs: This is not a service as such. However, Core-APIs acts as the integration-glue between various services that form a part of the Saga. In our case, the Core-APIs will consist of the various commands and event definitions required for our Saga implementation to function
    - Axon Server: Axon Server is part of the Axon Platform. We will be using Axon Framework to manage our Aggregates such as Order, Payment, Shipping. Also, we will be using Axon Server to handle the communication between the three services. You can check out my post on in-depth view of Axon Server if you are looking for more details about it.