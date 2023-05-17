# Propagation Level

For each propagation level there will be 2 use cases

- UC1:
~~~text
(t2)
------> Message Service ----->
~~~

- UC2: 
~~~text
(t1)                        (t2)  
------> User Service --------> Message Service --------->
~~~

- REQUIRED(default)
    - UC1: Creates new transaction
    - UC2: Uses the transaction created in user service
- SUPPORTS
    - UC1: Do not create new transaction
    - UC2: Uses the transaction created in user service
- MANDATORY
    - UC1: Exception
    - UC2: Uses the transaction created in user service
- REQUIRES_NEW
    - UC1: Creates new transaction
    - UC2: Creates new transaction in both services
- NOT_SUPPORTED
    - UC1: Do not create new transaction
    - UC2: Uses the transaction created in user service
- NEVER
    - UC1: Do not create new transaction
    - UC2: Exception
- NESTED