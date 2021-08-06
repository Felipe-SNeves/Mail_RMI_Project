# IMPORTANT

This project was done one year ago and I used a bunch of legacy technologies. I'm just uploading it again. By the way, this project code is a mess, that's a point that I need to improve. I believe I'll try to refactor it on C++ to improve my skills and knowlegde on the language, and try to make a more simple code. And implement some aspects in which this project has problems, like race conditions.

## The project 

Electronic Mail Project

This project was made by Felipe Neves to the Objects Oriented Programming (2020/2), taught by Menezes professor.

This project is a simple email service, it was inspired by an exercise done in class.

The system does has a GUI (Interfaces package) which makes a communication with servers of remote objects (DAO package), using RMI (done by the use of interfaces instead of heritance). At the end, the object servers exchange data with the database. To the implementation of the database it was used a script.

The servers RMI can be inicialized with the rmiregistry command after the stubs creation. Then the DAO files can be invocated.

The database used it was an MariaDB, and the correct JDBC Driver must be used (I used the MySQL Connector/J 5.1.49 provided by Oracle).

### How to run

First the database must be created, and some fictional "SMTP servers" must be added into it. Then, it's important to build the project: build the class files, build the stubs (rmic command) of the DAO package files, at the end the rmiregistry command must be executed and every DAO file must be executed too. Then the project can be used on the hosts.
