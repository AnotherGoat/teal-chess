# Building the project

As with many Gradle projects, a Gradle wrapper is included. Replace `./gradlew`
with `.\gradlew.bat` if you're using Windows.

## Using the program

### Running the program

The game can be easily started like this:

```shell
/.gradlew run
```

### Compiling a JAR (platform-independent, but requires JRE)

To generate a JAR with every dependency included, the
[Gradle Shadow Plugin](https://imperceptiblethoughts.com/shadow/) can be used:

```shell
./gradlew shadowJar
```

The generated JAR can be found in `desktop/build/libs/teal-chess.jar`. No other
file is required to run the program.

### Running the JAR

The JAR requires JRE (or JDK) version 17 or higher. To start the program, run:

```shell
java -jar desktop/build/libs/teal-chess.jar
```

To check other command line options, run:

```shell
java -jar desktop/build/libs/teal-chess.jar --help
```

### Packaging the application (platform-specific)

The example below uses Linux and generates an RPM installer. Depending on your
platform, you may need to reconfigure the plugin in `build.gradle` to produce a
different output. Also, make sure that you've installed the proper packaging
tools for your platform.

To package the application with JRE for your platform, run:

```shell
./gradlew jpackage
```

The platform-specific installer can be found in
`desktop/build/jpackage/teal-chess-0.0-1.x86_64.rpm`.

To install the RPM, you can run:

```shell
sudo rpm -i desktop/build/jpackage/teal-chess-0.0-1.x86_64.rpm
```

Then the installed program can be run like:

```shell
/opt/teal-chess/bin/teal-chess
```

And that's it! You can add it to your PATH if you want to run it more easily.

To reinstall the RPM, run:

```shell
sudo rpm -e teal-chess-0.0-1.x86_64
sudo rpm -i desktop/build/jpackage/teal-chess-0.0-1.x86_64.rpm
```

This will first uninstall and then reinstall the RPM, which shouldn't cause
problems due to it not having dependencies. The next sections aren't
platform-specific.

## Development tools

### Cleaning the build directory

To clean all `build` directories, run:

```shell
./gradlew clean
```

### Formatting the code

The code is automatically formatted every time the project is compiled. To apply
it manually, run:

```shell
./gradlew spotlessApply
```

### Running unit tests

To run every unit test, run:

```shell
./gradlew test
```

A [JaCoCo](https://www.jacoco.org/jacoco/) test coverage report is generated
after running the tests, which can be found at
`engine/build/reports/jacoco/test`. This generates HTML, XML and CSV reports
containing the results.

### Generating Javadocs

To generate Javadocs for the project's code and see what files are missing
documentation, run:

```shell
./gradlew javadoc
```

The generated Javadocs can be found at `engine/build/docs/javadoc`,
`core/build/docs/javadoc` and `desktop/build/docs/javadoc`.

### Using SonarQube

For checking code quality and security, the Gradle SonarQube Plugin is included.
I suggest reading
[the documentation](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-gradle/)
to learn how to use it.
