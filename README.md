Project Setup Guide
-------------------

1.Prerequisites
=================

1.1 Install Oracle JDK 7 

    Download it from Oracle website(http://www.oracle.com) and install it into 
    your local system.

    NetBeans 7.1 works well with Java 7. And Eclipse 3.7.2 also support Java 7.

1.2 Install Apache Maven 3. 

    Download from http://maven.apache.org, and unzip it into your system. Set a
    M2_HOME system variable, the value is the maven folder, and add  
    <maven folder>/bin to your path.

1.3 Install Tomcat 7.0.26

    Download the latest version of tomcat from http://tomcat.apache.org. Extract the
    files into your local disk.


2.Import Project into your IDE
==============================

2.1 Overview of the projects

    This project includes three maven moudles, it is a tree structure in the
    system.

    * pom.xml 					The parent maven pom xml.
    ** bifincan-website 		The work we have done, project was renamed as 
                                                                    bifincan-website.
    ** bifincan-admin 			The admin application.
    ** bifincan-library 		The commone library which can be reused in admin and website.
    ** bifincan-admin-primefaces-theme Custom primefaces theme for the admin application.

    WARNING: DO NOT ADD CODES TO THE ORIGINAL PROJECT(bifincan-spring), it will be 
    discarded after we confirm all work well in the new projects.

2.2 Import into the projects in IDE

    This project use maven as build tool, you can select your favorate IDE.

    For NetBeans user, you can open the project directly, NetBeans 7 provides
    excellent Maven support.

    For Eclipse user, please install m2eclipse plugin firstly, I suggest you use 
    the neweset Eclipse 3.7 and install JBoss Tools which has excellent facelet 
    and JSF support. 

    Open "Import" dialog in the "File" menu, and select "Existing Maven project" from
    "Maven" folder.

3. Configuraton for SSL
=======================
3.1. generate ssl key for tomcat.

   keytool -genkey -alias bifincan.com -keypass	bfpass  -keystore bifincan.com.bin -storepass bfpass
   What is your first and last name?
	  [Unknown]:  Hantsy bai
   What is the name of your organizational unit?
	  [Unknown]:  bifincan.com dev
   What is the name of your organization?
	  [Unknown]:  bifincan.com
   What is the name of your City or Locality?
	  [Unknown]:  Guangzhou
   What is the name of your State or Province?
	  [Unknown]:  Guangdong
   What is the two-letter country code for this unit?
	  [Unknown]:  CN
   Is CN=Hantsy bai, OU=bifincan.com dev, O=bifincan.com, L=Guangzhou, ST=Guangdong
	, C=CN correct?
	  [no]:  yes

3.2. modify the tomcat server.xml and enable the ssl configuration.
   
    <Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
               maxThreads="150" scheme="https" secure="true"
               clientAuth="false" sslProtocol="TLS" keystoreFile="bin/bifincan.com.bin" keystorePass="bfpass"/>

3.3 Run on tomcat and browse https://localhost:8443/fi. 

    NOTE: I think in the production phase, it should apply a ssl certification file from a public security orgzination, 
    The configuration steps should be different from these.


4. Troubleshooting
=====================

1) The kaptcha is not available in the maven repository, try to use the following 
   command to install it into ur local repository manually.
   
   A copy of kaptcha is available in the lib directory under project root folder.

   mvn install:install-file -Dfile=lib\kaptcha-2.3.2.jar -Dversion=2.3.2 
		-Dpackaging=jar -DgroupId=com.google.code -DartifactId=kaptcha  
		-DgeneratePom=true -DcreateChecksum=true
		
2) Follow the same procedure as #1 for the Mobilus.jar which is necessary for the SMS submission. That is execute the:

   mvn install:install-file -Dfile=lib\Mobilus.jar -Dversion=1.0.0 
		-Dpackaging=jar -DgroupId=com.Mobilus.Sms -DartifactId=mobilus  
		-DgeneratePom=true -DcreateChecksum=true	
		
3) Run the project.

    The official tomcat plugin is available now, I replace the configuration with the
    original t7.
    
        mvn package tomcat7:run-war

    You can run project from IDE directly. 
    
    NetBeans IDE can not recognise the dependent modules automatically like 
    Eclipse(m2e). So for NetBeans user, when you run webapp or admin, you must run 
    
        mvn clean install 
        
    in the root folder to get the newest dependencies if you have modified other
    modules.


4) "Exception: java.lang.OutOfMemoryError" exception.

    After upgraded to Hibernate 4.x, OutOfMemoryError was occured frequently. You should add
    jvm parameter MaxPermSize, eg." -XX:MaxPermSize=256m " to start tomcat.

    The more flexible way is creating a setenv script file in the tomcat/bin folder, it is 
    a placeholder for user to execute custom script.

    For Windows user, the content of setenv.bat file should look like the following.

        set CATALINA_OPTS=-Xmx2048m -XX:MaxPermSize=256m -Duser.language=en -Duser.locale=US -Dfile.encoding=UTF8

    For Linux user, create a bash file(setenv.sh) instead.

        export CATALINA_OPTS=-Xmx2048m -XX:MaxPermSize=256m -Duser.language=en -Duser.locale=US -Dfile.encoding=UTF8

    PRODUCTION CONFIG

        -Xms4096m -Xmx4096m -XX:MaxPermSize=512m
        -XX:+UseParallelGC -XX:+UseParallelOldGC
        -Duser.language=en -Duser.locale=US -Duser.timezone=Europe/Istanbul -Dfile.encoding=UTF8




