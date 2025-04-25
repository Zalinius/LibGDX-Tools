
void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: env.GIT_URL],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}

pipeline {
    agent any
    tools {
        maven 'maven3'
    }
    stages {
   		// Note that the agent automatically checks out the source code from Github	
        stage('Build') {
            steps {
                sh 'mvn --batch-mode clean verify'
            }
        }
        stage('Deploy') {
            when {
                branch 'main'
            }
            environment {
                SONAR_CREDS = credentials('sonar')
            }
            steps {
                sh 'mvn sonar:sonar -Dsonar.host.url=$SONARQUBE_HOST -Dsonar.login=$SONAR_CREDS' //Send test coverage to Sonarqube, and let it know there is a new version of main to cover
                sh 'mvn --batch-mode clean install'  //Install publishes to the local jenkins Maven repo
	        }
	    }
    }
    
    post {

        always {
            junit '**/target/*-reports/TEST-*.xml'
        }
    
        success {
            setBuildStatus("Build succeeded", "SUCCESS");
        }
        failure {
            setBuildStatus("Build failed", "FAILURE");
            script {
                if (env.BRANCH_NAME == 'main') {
                    discordSend description: "main branch build failed", footer: "😬 😬 😬", link: env.BUILD_URL, result: currentBuild.currentResult, title: env.JOB_NAME, webhookURL: "${DISCORD_WEBHOOK}"
                }
            }
        }
    }
}
