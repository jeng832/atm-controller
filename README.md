# ATM controller
## abstract
- Developing simple ATM controller
### Requirements
- [x] Should be implemented following flow
    - [x] Insert Card
    - [x] PIN number
    - [x] Select Account
    - [x] See Balance/Deposit/Withdraw
- [x] Testing ATM controller
## How to build and run
```$bash
$ chmod +x gradlew
$ ./gradlew bootRun
```
## Environment
- gradle
- spring-boot
- spring-statemachine
- jpa
- h2database
## Interface
```java
public interface AtmService {

    Long insertCard(String cardNum);
    boolean checkPin(Long id, String pin);
    List<String> getAccountNumbers(Long id);
    Optional<MoneyInfo> selectAccount(String number);
    void cancelJob();
    void done();

}
```
## Block Diagram
## ER Diagram
## State Machine Diagram
## Package architecture the project
```
atm-controller
  └─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─bear
    │  │          └─atm
    │  │              └─controller
    │  │                  ├─domain
    │  │                  ├─exception
    │  │                  ├─manager
    │  │                  ├─model
    │  │                  ├─repository
    │  │                  ├─service
    │  │                  │  └─impl
    │  │                  └─statemachine
    │  └─resources
    └─test
        └─java
            └─com
                └─bear
                    └─atm
                        └─controller
                            ├─manager
                            ├─service
                            └─statemachine

```
## Future development
- 
## Lessons & Learned
- spring-statemachine
