
void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: env.GIT_URL],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}


//ZJE Jenkinsfile
pipeline {
    agent any
    tools {
        maven 'maven3'
    }
    environment{
        SONAR_CREDS=credentials('sonar')
    }
    
    stages {
   		// Note that the agent automatically checks out the source code from Github	
        stage('Build') {
            steps {
                sh 'mvn --batch-mode clean verify'
            }
        }
        stage('Deploy QuestGiver version') {
            when {
                branch 'main-questgiver'
            }
            steps {
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
        }
    }
}
