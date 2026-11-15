
pipeline {

    agent any

    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
        skipDefaultCheckout(true)
    }

    parameters {

        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox'],
            description: 'Browser to execute tests'
        )

        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browser in headless mode'
        )

        string(
            name: 'CUCUMBER_TAGS',
            defaultValue: '@smoke or @negative',
            description: 'Cucumber tag expression'
        )
    }

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Environment') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
            }
        }

        stage('Clean and Test') {
            steps {
                bat '''
                    mvn -B clean test ^
                    -Dbrowser=%BROWSER% ^
                    -Dheadless=%HEADLESS% ^
                    -Dcucumber.filter.tags="%CUCUMBER_TAGS%"
                '''
            }
        }
    }

    post {

        always {

            junit(
                testResults: 'target/cucumber-reports/cucumber.xml',
                allowEmptyResults: true
            )

            archiveArtifacts(
                artifacts: 'target/cucumber-reports/**',
                allowEmptyArchive: true
            )

            publishHTML(target: [
                reportDir: 'target/cucumber-reports',
                reportFiles: 'cucumber.html',
                reportName: 'Cucumber HTML Report',
                keepAll: true,
                alwaysLinkToLastBuild: true,
                allowMissing: true
            ])
        }

        success {
            echo 'BDD automation pipeline completed successfully.'
        }

        failure {
            echo 'BDD automation pipeline failed. Check reports.'
        }
    }
}
