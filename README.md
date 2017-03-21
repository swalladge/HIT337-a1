
# HIT337 assignment 1

Copyright © 2017 Samuel Walladge

## About

This is a simple online book catalogue system with no authentication, built usingg J2EE and Tomcat 7.



## Configuring

### Maximum number of books per user.

This is configured by an init param of the `book` servlet (the one for adding/editing a book) as `maxRecords`.
The default is 10 books.

### Admin user

The admin user is configured as a context parameter named `adminUsername`. By default this is `admin`.

### Database

The server expects to connect to a locally running Derby server. Creating tables is performed from the `init` method of
one of the servlets, so you don't have to do that manually.
