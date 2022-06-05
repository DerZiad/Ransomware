#!/bin/sh
mvn clean compile assembly:single
cd target
java -jar ALUKAclient-0.0.1-SNAPSHOT-jar-with-dependencies.jar
