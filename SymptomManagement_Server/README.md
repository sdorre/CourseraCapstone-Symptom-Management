## Running the Video Service Application

__Read this First__
To run this application, you will need to download, install, and launch MongoDB
on your local machine: http://www.mongodb.org/

To run the application:

1. Right-click on the Application class in the org.symptom.management
package, Run As->Java Application 

To stop the application:

Open the Eclipse Debug Perspective (Window->Open Perspective->Debug), right-click on
the application in the "Debug" view (if it isn't open, Window->Show View->Debug) and
select Terminate

## First launch 

You need to initialize the database if you want to use this server. 
After running the application, you have to start the JUnit test named
ServerInitialization. It will filled Patients,Doctor and UserRights necessary for
the application.

## Accessing the Service

The sevice has been securised with OAuth2 anthentication. So you can't access directly
the server with your browser. You can use The package org.magnum.mobilecloud.integration
in test folder to see how to access to server with passing a credentials.
URL:

http://localhost:8443/

## What to Pay Attention to

In this version of the VideoSvc application, we are using MongoDB to store data.
See the Video and VideoRepository classes for the annotation changes that this 
requires. See the src/main/resources/application.properties file for configuration
options if you want to connect to a remote MongoDB instance.

