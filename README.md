## Bearchoke Tempest ##

Welcome to Bearchoke Tempest. This platform aims to be a best practices framework using the latest Java based frameworks, Enterprise Integration Patterns (EIP) and specifically focusing on using an asynchronous, message based, event driven approach to creating web-based software. The current push in the user interface space is SPA/SPI (Single Page Applications / Single Page Interface). The samples will leverage AngularJS to showcase this. Bearchoke Tempest was created to show how an SPA can interface with the Spring server world as well as leverage other SPA modular goodies. 

If you just want to check out the demo, you can check out the Cloud Foundry hosted instance here: [https://www.bearchoke.com](https://www.bearchoke.com).

## Credits
* Thank you to [Pivotal](http://www.pivotal.com) for the free space on Cloud Foundry to be able to show case the apps the way they should be show cased.
* Thank you to [JetBrains](http://www.jetbrains.com) for providing me with an open source license to their flagship product IntelliJ, the best IDE on the market.

Key technologies that I've leveraged to make this work are:

* Spring
* Redis
* CQRS
* Rabbit MQ
* Web Socket
* Apache CXF
* MongoDb

Another key focus has been on the separation of front-end and back-end technologies. This was born out of the requirement to "build for mobile first". To the layman, it means that we want to avoid building more than one way of accessing our service endpoints. It should be the same calls being made in the browser as in a native mobile application. 

Below, I will cover what is required to get up and running using all the goodies available on the platform. But first, here is the list of technologies and frameworks the platform used to get the job done.

## Back-end Technologies
* [Spring Framework](http://projects.spring.io/spring-framework/) (Inversion of Control IoC)
* Spring Web MVC (Part of Spring Framework: REST mappings)
* Spring Web Socket (Part of Spring Framework: Web Socket mappings)
* [Spring Security](http://projects.spring.io/spring-security/) (security + authentication)
* [Spring Integration](http://projects.spring.io/spring-integration/) (Enterprise Integration Patterns EIP and implementations)
* [Spring Data MongoDb](http://projects.spring.io/spring-data-mongodb) (Persistence layer to MongoDb)
* [Spring Data Jpa](http://projects.spring.io/spring-data-jpa) (Persistence layer to Jpa)
* [Spring Data Redis](http://projects.spring.io/spring-data-redis) (Persistence layer to Redis)
* [Spring Session](https://github.com/spring-projects/spring-session) (for easier persisting session for REST / Web Socket based clients)
* [Axon Framework](http://www.axonframework.org) (Command Query Responsibility Segmentation CQRS)
* [Jasypt](http://jasypt.org/) (Encryption)
* [Jackson](https://github.com/FasterXML/jackson) (JSON transformation)
* [Apache CXF](http://cxf.apache.org) (JAXWS Web Service) 

## Databases and Queues
* [MongoDb](http://www.mongodb.org)
* [MySQL](http://dev.mysql.com) / [PostgreSQL](http://www.postgresql.org) / [DerbyDb](http://db.apache.org/derby/) or any other RDBMS with a Java driver
* [Redis](http://www.redis.io)
* [RabbitMQ](http://www.rabbitmq.com)

## Front-end Technologies
* [AngularJS](http://www.angularjs.org) (MVC and IoC framework in javascript)
* [LinemanJS](http://www.linemanjs.com) (Build and test tool)
* [Grunt](http://www.gruntjs.com) (Dependency management for Javascript)
* Web Socket using [SockJS](https://github.com/sockjs) + [Stomp](http://stomp.github.io/) sub-protocol
* [Bootstrap](http://getbootstrap.com/) (Responsive web framework)

## Version Control
* [Github](https://www.github.com) [https://github.com/bjornharvold/bearchoke](https://github.com/bjornharvold/bearchoke)

## IDE, Build & Deployment Tools
* [JetBrains IntelliJ IDEA](http://www.jetbrains.com/idea/) (Integrated Development Environment IDE)
* [Maven](http://maven.apache.org) (Dependency management)
* [Pivotal Cloud Foundry](http://www.pivotal.io/platform-as-a-service/pivotal-cf) (Scalable Platform as a Service PaaS)

## Project Management Tools
* [Github](https://github.com/bjornharvold/bearchoke/issues) (Issue tracking)
* [Github](https://github.com/bjornharvold/bearchoke/wiki) (Wiki)

## Requirements
* Brew (latest)
* Maven v3.2.3+
* Redis v3.0.4+
* RabbitMQ v3.5.6+
* MongoDb v3.0.0+
* Apache Tomcat v8.0.28+
* JetBrains IntelliJ v15+
* LinemanJS (latest)
* Grunt (latest)

## Project Directory Structure
* **bearchoke-spa** (AngularJS SPA parent directory)
   * bearchoke-spa-frontend (Demo application)
   * bearchoke-spa-shared (Reusable shared assets directory for other SPAs)
* **bearchoke-backend** (Java module parent directory)
   * bearchoke-api (Parent directory with sub-modules containing API specific classes such as: commands, events, dtos, identifiers for the domain layer)
   * bearchoke-domain (Parent directory for sub-modules containing domain specific functionality. E.g. User management)
   * bearchoke-integration-tests (Tests that initialize the entire code base or separate sub-modules, such as connecting to a database and testing queries. These tests will be ignored during Maven's regular test cycle)
   * bearchoke-persistence (Parent directory for all sub-modules that connect to databases or queues)
   * bearchoke-platform-base (Code that is re-used by most Bearchoke modules but does not fit anywhere else... but more advanced than just putting it in a "utils" module)
   * bearchoke-server (Parent directory for sub-modules that contain everything necessary to run the Bearchoke web application)
   * bearchoke-web (Parent directory for sub-modules of type "war" that pack code from bearchoke-server sub-modules. No actual code in here. Only POM dependencies.)
   * bearchoke-webservices (Module to deal with functionality exposed through Apache CXF)


**Importing project**
If you are using IntelliJ, you can just import the project by clicking File - Import Project and selecting pom.xml located in the root directory. In case you encounter any issues with third-party dependencies, you get more information about the error if you run the initial Maven project compilation from your Terminal / Command Prompt. Go into bearchoke-backend and type:

	$ cd bearchoke-tempest
	$ mvn clean install

This will download all dependencies and install all Bearchoke libraries in the Maven repository. If you do this before you import the project into IntelliJ, project creation will also be faster as all dependencies have already been downloaded.

When you are set up with your project successfully, install the Project Lombok plugin for IntelliJ.

## Setting up Databases and Queues
Install [Brew](http://brew.sh/) before you do anything else.

## Redis (required for every type of active spring profile)
[Redis](http://www.redis.io) is used by [Spring Session](https://github.com/spring-projects/spring-session) to keep a user session over several protocols and devices without needing a cookie (aka JSESSION_ID). Once a user is authenticated, the user can make calls over REST and Web Socket seamlessly.

Install Redis using Brew: 

	$ brew install redis
	$ redis-server database/redis/redis.conf
	
You can start it later as well by using the start script located in the root of bearchoke-tempest called: startServices.sh.
	
## RabbitMQ (required for every type of spring profile)
[RabbitMQ](http://www.rabbitmq.com) is the AMQP implementation used by Spring Integration to broker messages. RabbitMQ is a message broker. The principal idea is pretty simple: it accepts and forwards messages. You can think about it as a post office: when you send mail to the post box you're pretty sure that Mr. Postman will eventually deliver the mail to your recipient. Using this metaphor RabbitMQ is a post box, a post office and a postman. The major difference between RabbitMQ and the post office is the fact that it doesn't deal with paper, instead it accepts, stores and forwards binary blobs of data ‒ messages. RabbitMQ helps in making the architecture event driven.

Install RabbitMQ using Brew:

	$ brew install rabbitmq

Once you have it installed, you want to first enable the STOMP protocol for RabbitMQ.

	$ rabbitmq-plugins enable rabbitmq_stomp

Then you can start RabbitMQ by typing:

	$ rabbitmq-server start	

You can start it later as well by using the start script located in the root of bearchoke-tempest called: startServices.sh.

## MongoDb (required for every type of spring profile)
[MongoDb](http://www.mongodb.org) is a NoSQL database, more specifically known as a "document store". 

Install MongoDb using Brew:

	$ brew install mongodb

Create a 'database/mongodb' directory in the root directory of bearchoke-tempest and then starting MongoDb by typing:

	$ mongod --dbpath database/mongodb &

You can start it later as well by using the start script located in the root of bearchoke-tempest called: startServices.sh.

## Jpa (required for 'jpa' spring profile)
Jpa is the standard Java Persistence API. To showcase the framework's Jpa support, we're using an in-memory version of [DerbyDb](http://db.apache.org/derby/). There is no installation necessary. Jpa is currently not leveraged in the showcase but the configuration to connect to a local or cloud based db is available to you.

## Lineman
In order to install Lineman, you also need to install the latest version of [Node.JS](http://www.nodejs.org/). 

Install Node.JS using Brew:

	$ brew install nodejs
	
Once Node.JS is installed, you can install Lineman by typing this in your Terminal / Command Prompt:

	$ sudo npm install -g lineman	

The last requirement for Lineman is that you have [PhantomJS](http://phantomjs.org/download.html) installed and on your PATH.

## Grunt
Once you have installed Lineman, Grunt is next. Type this in your Terminal / Command Prompt:

	$ npm install -g grunt-cli	

## Getting Up and Running
To see a basic web application that showcases the technologies mentioned above, you need to start the bearchoke-web-frontend web app and start the bearchoke-spa-frontend app. 

The server part was configured for Tomcat 8+. There is no embedded Maven plugin for Tomcat 8 so this app cannot be run from the command line unfortuantely. You will have to create an IntelliJ 'run configuration' where you deploy the bearchoke-web-frontend webapp. Set the active profiles for Spring in your environment variables section of your run configuration.	

Local Spring config:

	$ -Dspring.profiles.active=local		
	
To deploy the server on Cloud Foundry, the active profiles for the default configuration would be.

Cloud Foundry:

	$ -Dspring.profiles.active=cloud		
	
Finally, the app has been configured to use a custom domain. Please edit your /etc/hosts file and add dev.bearchoke.com. You can verify that the bearchoke-web-frontend web application is running by going to http://dev.bearchoke.com:8080 in your browser.

Next, you have to start the bearchoke-spa-frontend application. Type the following in a new Terminal / Command Prompt window:

	$ cd bearchoke-spa/bearchoke-spa-frontend
	$ npm install (will install all dependencies located in package.json)
	$ lineman run	

You can verify that it's running by going to http://dev.bearchoke.com:8000 in your browser. This is the sample app. Enjoy!

## What the Framework Gives You
There are a few things the Bearchoke Tempest gives you out of the box. 

**On the backend, the framework gives you:**

* Spring Security [pre-]authentication implementations with a device agnostic session manager to help keep state across devices and protocols.
* Beans to help you quickly get set up with using Command Query Responsibility Segregation (CQRS) and Enterprise Integration Patterns (EIP).
* Examples on how to create versioned, REST based, controller request mappings using Spring Web MVC.
* Examples on how to create Web Socket URL mappings using Spring Web Socket with StompJS.
* Example on how to leverage Elasticsearch in your SPA.
* Spring Profiles for custom deployment configurations.
* Apache CXF integration

**On the frontend, the framework gives you:**

* Model-View-Controller and Dependency Injection framework [and much MUCH more] with AngularJS.
* A fully featured build tool with LinemanJS. Much like Maven, it offers code separation. Controllers, views, css, services and third party libraries are all separated into different directories. Lineman concatenates, minifies, uglyfies etc all the javascript and css into 2 files that are then being used by the application. Files you are working on are updated in real-time and you just have to refresh your browser. It supports LESS, SCSS and CSS for styling. Both LinemanJS configuration files and AngularJS code can be writting in either Coffeescript or Javascript. 
* Remember me functionality using local storage
* Bootstrap for building responsive web applications
* Client-side translation using Angular-Translate
* Restangular for use with REST-based server-side calls
* UI-Router to handle your page setup and navigation
* StompJS client for communication via websocket
* Lodash and underscore javascript utility libraries
* Template based views with AngularJS
* Facebook login and registration
* MixPanel integration
* MailChimp integration
* Apache CXF Web Services with Javascript sample
* [Less CSS](http://lesscss.org/) is a CSS pre-processor that adds new features that allow variables, mixins, functions and many other techniques that allow you to make CSS that is more maintainable, themable and extendable.

## Current release

* 2015-12-26 - 2.0.0 Added Elasticsearch example
* 2015-1-12  - 0.0.4 Apache CXF integration 
* 2015-1-7   - 0.0.3 MixPanel integration and more unit tests
* 2015-1-1   - 0.0.2 The Happy New Year release
* 2014-10-16 - 0.0.1 Initial version

## New features in 2.0.0
This was a major version bump! A lot of updated code and better way of organizing things, from my commercial product, was added to Bearchoke Tempest. This will be presented at our Bangkok DevCon in March.

## New features in 0.0.4

* Added a simple HelloWorldService with Apache CXF and generated Javascript from the wsdl to call service from AngularJS. Extended CXF's js library to make it work with CORS. Added an AngularJS wrapper around the Web Service.

## New features in 0.0.3

* MixPanel integration. If you have a MixPanel account and you set your MixPanel api token in development.json, you will see registration and login events going through.
* More test on the Java side.

## New features in 0.0.2

* Custom MongoDb user provider / authentication implementation using CQRS. 
* Removed in-memory option
* Registration using CQRS
* MailChimp integration
* Facebook integration with [angular-easyfb](https://github.com/pc035860/angular-easyfb)

## Limitations

* Cloud Foundry does not yet support HTML5 push. You cannot go straight to a url unless it's the root url. E.g. https://www.bearchoke.com is ok. https://www.bearchoke.com/ui/chat is not ok. We developed a custom buildpack for this reason which you can find here: [Custom Static buildpack to support SPAs](https://github.com/bjornharvold/staticfile-buildpack.git)
* Cloud Foundry has a limitation when using your custom domain while using SSL over Cloudflare. If you enable CSRF, it will fail because you will have to use the cfapps.io domain name to reach your web socket services.
* If you're working on a Mac, you might run into an EMFILE warning when running LinemanJS. You need to increase the ulimit size. Please see the FAQ here for more information: [grunt-contrib-watch](http://cnpmjs.org/package/grunt-contrib-watch)


## Next release features
This framework is for me a way to prove that the technologies can co-exist together [peacefully] and that they work and are testable. As my other projects require new features, I will add these here first as a test bed. Upcoming features I already want to add are:

* File upload example
* Charting and reporting
* Tests, tests and tests

If you would like to commit to this project you are more than welcome to do so. You can message me on Twitter at @bjornharvold or on Github.