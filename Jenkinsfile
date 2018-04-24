pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'sh \'mvn -Dmaven.test.failure.ignore=true install\''
      }
    }
  }
}