# PC-Pilot-V11
Version 11, elimnates all legacy packages and classes, uses standard JSON, and XML for serialization
This version goes to 11 !
Changes:

* Elimates all legacy code previously needed to support PDA cross functionlaity, such as CatalogInterface, avscience Integer class, 
and avscience.util package.

* Proprietary serialization scheme is now replaced with standard JSON compliant serialization using org.json library.

* XML is supported and used for all client server and peer to peer data exchanges.

* All of the data objects in packages avscience.ppc and avscience.wba are now moved to the Snowscience-DataObjects library and repository.

* Removed all redundant, obsolete code and classes.

* Has all the features of version 10, conforms to latest data model.

Building / Running Instructions: 

- compile with standard Java JDK v 1.5 -> 1.7 .

- Include the three .jar files the /lib folder in the build classpath.

- updated the 'SnowScienceDataObjects' .jar file, if there have been changes to these classes .

- Make sure the snow symbols font file 'SnowSymbolsIACS.ttf' is included in the runtime path for the application.

- The main executable class is avscience.pc.MainFrame
