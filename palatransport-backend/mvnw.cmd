@REM ----------------------------------------------------------------------------
@REM Maven Wrapper Script per Windows
@REM
@REM Questo script permette di eseguire Maven anche senza averlo installato.
@REM Al primo avvio scarica automaticamente Maven nella cartella .m2 dell'utente.
@REM Uso: .\mvnw.cmd <goal-maven>  es: .\mvnw.cmd spring-boot:run
@REM ----------------------------------------------------------------------------
@echo off

set MAVEN_PROJECTBASEDIR=%~dp0

set JAVA_EXE=java
if defined JAVA_HOME (
    set JAVA_EXE="%JAVA_HOME%\bin\java.exe"
)

set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"

%JAVA_EXE% "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR:~0,-1%" -cp %WRAPPER_JAR% org.apache.maven.wrapper.MavenWrapperMain %*
