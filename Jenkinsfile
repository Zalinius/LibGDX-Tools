@Library('zalinius-shared-library') _

pipeline {
    agent any
    tools {
        maven 'maven3'
    }
    stages {
        // Note that the agent automatically checks out the source code from Github	
        stage('Build') { 
            steps {
                buildLibrary()
            }}
        stage('Deploy') {
            when { branch 'main'}
            environment {
                SONAR_CREDS = credentials('sonar')
            }
            steps {
                sonarScan()
                deployLibrary()
    }}}
    post {
        always  { testReport() }    
        success { githubSuccess() }    
        failure { githubFailure() }    
    }
}