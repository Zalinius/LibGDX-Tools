
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
    stages {
   		// Note that the agent automatically checks out the source code from Github	
        stage('Compile') { 
            steps {
            	sh 'mvn --batch-mode compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn --batch-mode test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Package Jar') {
            steps {
                sh 'mvn --batch-mode -DskipTests clean package'
            }
        }
        stage('Deploy') {
        	when {
 				branch 'main'
        	}
			steps {
                sh 'mvn --batch-mode -DskipTests clean install'  //Install publishes to the local jenkins Maven repo
	        }
	    }
    }
    
    post {
    success {
        setBuildStatus("Build succeeded", "SUCCESS");
    }
    failure {
        setBuildStatus("Build failed", "FAILURE");
    }
  }
}