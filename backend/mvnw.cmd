@echo off
rem Maven Wrapper script for Windows

setlocal

set MAVEN_PROJECTBASEDIR=%~dp0
set MAVEN_WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar
set MAVEN_WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties

rem Download maven-wrapper.jar if it doesn't exist
if not exist "%MAVEN_WRAPPER_JAR%" (
    if exist "%MAVEN_WRAPPER_PROPERTIES%" (
        for /f "tokens=2 delims==" %%a in ('findstr "wrapperUrl" "%MAVEN_WRAPPER_PROPERTIES%"') do set WRAPPER_URL=%%a
        powershell -Command "Invoke-WebRequest -Uri '%WRAPPER_URL%' -OutFile '%MAVEN_WRAPPER_JAR%'"
    )
)

rem Run Maven
if exist "%MAVEN_WRAPPER_JAR%" (
    "%JAVA_HOME%\bin\java" -jar "%MAVEN_WRAPPER_JAR%" %*
) else (
    mvn %*
)

endlocal
