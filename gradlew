#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS=""

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

# OS specific support.
cygwin=false
darwin=false
linux=false
case "`uname`" in
  CYGWIN*)
    cygwin=true
    ;;
  Darwin*)
    darwin=true
    ;;
  Linux)
    linux=true
    ;;
esac

# For Cygwin, ensure paths are in UNIX format before anything is touched.
if ${cygwin} ; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$GRADLE_HOME" ] && GRADLE_HOME=`cygpath --unix "$GRADLE_HOME"`
fi

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`"/$link"
  fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

# Attempt to set JAVA_HOME if it is not set
if [ -z "$JAVA_HOME" ]; then
    # If a JDK is installed with Homebrew on macOS, use it
    if ${darwin} && [ -x "/usr/libexec/java_home" ]; then
        export JAVA_HOME=($(/usr/libexec/java_home))
    # Check for a JDK in a standard location
    elif [ -d "/usr/java/latest" ]; then
        export JAVA_HOME="/usr/java/latest"
    fi
fi

# Check for a valid Java executable
if [ ! -x "$JAVA_HOME/bin/java" ]; then
    # Fallback to searching for a 'java' executable on the path
    if ! command -v java >/dev/null 2>&1; then
        echo "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH." >&2
        echo "" >&2
        echo "Please set the JAVA_HOME variable in your environment to match the" >&2
        echo "location of your Java installation." >&2
        exit 1
    fi
    # Use the found 'java' executable
    JAVA_CMD="java"
else
    JAVA_CMD="$JAVA_HOME/bin/java"
fi

# Add on extra jar files to CLASSPATH
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_CMD" ] ; then
  JAVACMD="$JAVA_CMD"
else
  JAVACMD="java"
fi

# Increase the maximum number of open files if necessary.
if ! ${cygwin} && ! ${darwin} ; then
  if [ "${MAX_FD}" = "maximum" -o "${MAX_FD}" = "max" ] ; then
    # Use the maximum available.
    MAX_FD="`ulimit -H -n`"
  fi
  if [ -n "${MAX_FD}" ] && [ -n "`ulimit -n`" ]; then
    if [ `ulimit -n` -lt "${MAX_FD}" ] ; then
      ulimit -n "${MAX_FD}"
    fi
  fi
fi

# For Cygwin, switch paths to Windows format before running java
if ${cygwin} ; then
  APP_HOME=`cygpath --path --windows "$APP_HOME"`
  CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi

# Split up the JVM options string into an array, following the shell quoting and substitution rules
function splitJvmOpts() {
  JVM_OPTS=("$@")
}
eval splitJvmOpts $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS

# Execute Gradle
exec "$JAVACMD" "${JVM_OPTS[@]}" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
