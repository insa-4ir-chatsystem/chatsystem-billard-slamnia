# Tech stack
## UDP
We had to use broadcast traffic on the network in order to inform the other users that we are connected and that we are changing our pseudonym.
## TCP
We had to assure that the messages between users are well distributed, which TCP offers on the network.
## Swing
Simple API to create GUIs. We used it mainly because IntelliJ offers the possibility to create the UI quickly with a form file. This feature from IntelliJ also helped us to understand how the UI is reacting to the user input.
## SQLite
Simple API to create a Database on a local computer. Once the database file is created, we just had to create our queries.

# Testing policy
## Unitary tests
We created unitary tests for each of the key feature of the project. You can see that the controllers have their own test file where you can find this key features. It regroups :
- Connection and Disconnection of an agent
- Pseudonym change
- Message exchange
- Update of the contact list
- Update of the DB

We also tested the code in unusual situations but where it should work properly. 
## In-situation tests
We then tested the application on different computers (and with SSH) on the INSA network. It was important, especially for the UI.

# Highlights
