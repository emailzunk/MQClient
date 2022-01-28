# MQClient
https://developer.ibm.com/tutorials/mq-connect-app-queue-manager-containers/

https://developer.ibm.com/learningpaths/ibm-mq-badge/

1. docker pull ibmcom/mq:latest
2. docker images
3. docker volume create qm1data
4. docker run --env LICENSE=accept --env MQ_QMGR_NAME=QM1 --volume qm1data:/mnt/mqm --publish 1414:1414 --publish 9443:9443 --detach --env MQ_APP_PASSWORD=passw0rd ibmcom/mq:latest
5. docker ps
6. docker exec -ti <your container id> /bin/bash
7. dspmqver
          Name:        IBM MQ
          Version:     9.2.2.0
          Level:       p922-L210310.DE
          BuildType:   IKAP - (Production)
          Platform:    IBM MQ for Linux (x86-64 platform)
          Mode:        64-bit
          O/S:         Linux 4.19.121-linuxkit
          O/S Details: Red Hat Enterprise Linux 8.3 (Ootpa)
          InstName:    Installation1
          InstDesc:    IBM MQ V9.2.2.0 (Unzipped)
          Primary:     N/A
          InstPath:    /opt/mqm
          DataPath:    /mnt/mqm/data
          MaxCmdLevel: 922
          LicenseType: Developer
8. dspmq

Queue manager QM1
Queue DEV.QUEUE.1
Channel: DEV.APP.SVRCONN
Listener: DEV.LISTENER.TCP on port 1414

https://localhost:9443/ibmmq/console
Platform	          MQ Version	Credentials
Containers/Docker	  Any	        Username: admin Password: passw0rd

9.  mkdir MQClient
10. curl -o com.ibm.mq.allclient-9.1.4.0.jar https://repo1.maven.org/maven2/com/ibm/mq/com.ibm.mq.allclient/9.1.4.0/com.ibm.mq.allclient-9.1.4.0.jar
11. curl -o javax.jms-api-2.0.1.jar https://repo1.maven.org/maven2/javax/jms/javax.jms-api/2.0.1/javax.jms-api-2.0.1.jar
12. mkdir -p com/ibm/mq/samples/jms
13. cd com/ibm/mq/samples/jms
14. curl -o JmsPutGet.java https://raw.githubusercontent.com/ibm-messaging/mq-dev-samples/master/gettingStarted/jms/JmsPutGet.java
15. code .
16. cd /MQClient
17. javac -cp ./com.ibm.mq.allclient-9.1.4.0.jar:./javax.jms-api-2.0.1.jar com/ibm/mq/samples/jms/JmsPutGetV2.java
18. ls -l com/ibm/mq/samples/jms/*.class
19. java -cp ./com.ibm.mq.allclient-9.1.4.0.jar:./javax.jms-api-2.0.1.jar:. com.ibm.mq.samples.jms.JmsPutGetV2


java -jar .\lib\JRecordCodeGen.jar -Template standard -package com.rbc.cobol -Schema filelayout.cobcopy -FileOrganisation FixedWidth -DropCopybookName true -outputDirectory Gen_Output
