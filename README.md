
# HIT337 assignment 1

Copyright © 2017 Samuel Walladge

## About

This is a simple online book catalogue system with no authentication, built using J2EE and Tomcat 7.

There is a single admin user that can edit/add any book for any user, and there are an unlimited number of normal users
who can view, edit, and add their own books (up to a customizable maximum limit).

The details you can store about each book are: the title, author, and your rating on a scale of 1 to 10.

It does do data validation/sanitization on untrusted input, and escapes html special characters for displayed data to
prevent XSS.


## Configuring

### Maximum number of books per user.

This is configured by an init param of the `book` servlet (the one for adding/editing a book) as `maxRecords`.
The default is 10 books.

### Admin user

The admin user is configured as a context parameter named `adminUsername`. By default this is `admin`.

### Database

The server expects to connect to a locally running Derby server. Creating tables is performed from the `init` method of
one of the servlets, so you don't have to do that manually.
