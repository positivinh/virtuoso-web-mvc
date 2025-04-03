# virtuoso-web-mvc

Software factory components related to Spring Web MVC usage

## Commands

### Install

```shell
mvn clean install
```

### Run sonarqube analysis

Set environment variable `SONAR_TOKEN`

```shell
export SONAR_TOKEN=[...]
```

Run sonarqube analysis

```shell
mvn clean verify sonar:sonar -P sonarqube
```