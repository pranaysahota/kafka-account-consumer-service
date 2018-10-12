# account-creation-monitor-service


##Pre-requisites:

1. Java 1.8 is present
2. Maven is installed
3. Kafka and ZK are running on Ports 9092, 2181 respectively.


##Steps to run:

1. Import the folder with (src and pom.xml)
2. Run mvn clean command (Cleans the old target classes that were compiled)
3. Run mvn install command (Creates the executable jar in target folder of project)
4. Run the program with command (java -jar target/account-creation-monitor-service-1.0.jar)
