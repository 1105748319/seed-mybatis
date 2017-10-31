#!/bin/sh
APP_NAME=seed-web
APP_VERSION=1.0.1
LOGS_FILE=logs/charge.log

SERVER_NAME=$APP_NAME\-$APP_VERSION
JAR_NAME=$SERVER_NAME\.jar
PID=$SERVER_NAME\.pid

JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "
JAVA_HOME=/jre

cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`

#P_ID=`ps -f | grep java | grep "$DEPLOY_DIR" |awk '{print $2}'`

P_ID=""
if [ -f $DEPLOY_DIR/$PID ] ; then
	echo "记录PID："$P_ID
	P_ID=`cat $DEPLOY_DIR/$PID`
fi

status() {
	if [ -n "$P_ID" ]; then
		echo "The $SERVER_NAME already running! PID: $P_ID"

	else
		echo "The $SERVER_NAME already stopped!"

	fi
}

start() {
	if [ -n "$P_ID" ]; then
		echo "ERROR: The $SERVER_NAME already started! PID: $P_ID"
    		exit 1
	else
		LOGS_DIR=""
		if [ -n "$LOGS_FILE" ]; then
    			LOGS_DIR=`dirname $LOGS_FILE`
		else
    			LOGS_DIR=$DEPLOY_DIR/logs
		fi

		if [ ! -d $LOGS_DIR ]; then
    			mkdir $LOGS_DIR
		fi

		if [ "$2" = "debug" ]; then
    			JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n "
		fi

		JAVA_MEM_OPTS=""
		BITS=`java -version 2>&1 | grep -i 64-bit`

		if [ -n "$BITS" ]; then
    			JAVA_MEM_OPTS=" -server -Xms100m -Xmx512m -Xmn64m -XX:PermSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
		else
    			JAVA_MEM_OPTS=" -server -Xms100m -Xmx512m -XX:PermSize=128m -XX:SurvivorRatio=2 -XX:+UseParallelGC "
		fi

		echo -e "Starting the $SERVER_NAME"
		nohup java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS -jar $DEPLOY_DIR/$JAR_NAME > $DEPLOY_DIR/logs/charge.log 2>&1 &

		echo $! > $DEPLOY_DIR/$PID
		tail -f $DEPLOY_DIR/logs/charge.log
	fi

}

stop() {
	if [ -z "$P_ID" ]; then
    		echo "ERROR: The $SERVER_NAME does not started!"
    		exit 1
	fi

	echo -e "Stopping the $SERVER_NAME ..."

	kill `cat $DEPLOY_DIR/$PID`

	sleep 5
	P_ID=`ps -f | grep java | grep "$DEPLOY_DIR" |awk '{print $2}'`
	if [ -n "$P_ID" ]; then
		echo "WARN:time out while normal stop, begin kill $SERVER_NAME process, pid is:$P_ID"
        	kill $P_ID
    fi

	rm -rf $DEPLOY_DIR/$PID
	echo -e "The $SERVER_NAME stoped successfully! "

}

case "$1" in

start)
	start
	;;
stop)
	stop
	;;
restart)
	stop
	sleep 1
	start
	;;
status)
	status
	;;
*)
	echo "Usage: $0 {start|stop|restart|status}"
	exit 1
	esac
	exit 0
