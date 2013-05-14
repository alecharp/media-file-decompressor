#!/bin/sh
#
# Copyright 2013 Adrien Lecharpentier
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

MEDIA_HOME=`pwd`
JAVA=/usr/bin/java

FRIENDLY_NAME="Media Decompressor"
MAIN_CLASS="org.lecharpentier.media.Bootstrap"

mkdir -p "${MEDIA_HOME}"/{data,logs}

PID_FILE="${MEDIA_HOME}"/data/service.pid

MEDIA_LOG="${MEDIA_HOME}/logs"
LIB_DIR="${MEDIA_HOME}/lib"

JARS=""
for i in ${MEDIA_HOME}/lib/*.jar; do
  if [ -e $i ]; then
    JARS="${JARS}":"$i"
  fi
done

CLASSPATH="${JARS}"
CONSOLE_LOG="${MEDIA_LOG}"/console.log

MEDIA_OPTS="-Dlog4j.configuration=file:conf/log4j.xml -Dorg.lecharpentier.media.config.file=${MEDIA_HOME}/conf/media.properties"

getpid() {
  if [ -f "${PID_FILE}" ]; then
    pid=`cat "${PID_FILE}"`
  else
    pid="`ps -ef | grep ${MAIN_CLASS} | grep -v grep | awk '{print $2}'`"
  fi
}

stop() {
  getpid
  if [ "_" != "_${pid}" ]; then
    echo "Stopping ${FRIENDLY_NAME}.."
    kill $pid
    rm ${PID_FILE} 2>/dev/null
  else
    echo "${FRIENDLY_NAME} is not running"
  fi
}

start() {
  getpid
  if [ "_" != "_${pid}" ]; then
    echo "Already running..."
    exit 1
  fi
  echo "Starting ${FRIENDLY_NAME}.."
  ${JAVA} ${MEDIA_OPTS} -cp ${CLASSPATH} ${MAIN_CLASS} >> ${CONSOLE_LOG} 2>&1 & echo $! > "${PID_FILE}"
}

case "$1" in
  start)
    start
    ;;
  stop)
    stop
    exit 0
    ;;
  restart)
    stop
    sleep 1
    start
    ;;
  *)
    echo "Usage: $0 {start | stop | restart}"
    exit 0;;
esac

exit $?
