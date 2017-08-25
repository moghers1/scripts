#!/bin/bash
#
# run on cmd line:
# bash service_runner.sh -h <hadoop_version> -i <infa_version> -s <start/stop>
# 
#
# CHANGE LOG:
# 
# DATE		DESCRIPTION
# 8/25/17	add h,i,s parms
#
#


while getopts h:i:s: opt
do
 case "${opt}"
 in
 h) vHADOOP=${OPTARG};;
 i) vINFA=${OPTARG};;
 s) STATUS=${OPTARG};;
 esac
done


# This will START Hadoop multi-node services.
if [ -n "h" ] && [ "s"=="start" ]; then

	cd ~/hadoop-"$HADOOP"/sbin

	# start HDFS & MapReduce daemons
	./start-dfs.sh
	./start-yarn.sh;

	# leave safemode
	hdfs dfsadmin -safemode leave;
	hdfs namenode -safemode leave;

	# display jobs
	jps;
fi


# This will STOP Hadoop multi-node services.
if [ -n "h" ] && [ "s"=="stop" ]; then

	cd ~/hadoop-"$vHADOOP"/sbin

	# stop HDFS & MapReduce daemons
	./stop-yarn.sh;
	./stop-dfs.sh

	# display jobs
	jps;

fi


# This will START Informatica services.
if [ -n "i" ] && [ "s"=="startup" ]; then

	## TO DO: need to add a validation here; check if oracle DB is up.
	## If not, don't run this.
	## DB needs to be running prior to starting Informatica

	cd ~/Informatica/"$vINFA"/tomcat

	# start Informatica service
	./bin/infaservice.sh "$STATUS";

	# display jobs
	jps;
fi
