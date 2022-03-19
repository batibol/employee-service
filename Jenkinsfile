pipeline{
    agent any
   tools {
       maven 'maven'
       jdk 'Java'
   }

    stages{
        stage('clean')
        {
            steps{
                sh 'mvn clean'
            }
        }
        stage('package')
        {
         tools {
                maven 'maven'
                jdk 'Java'
         }
            steps {
                sh 'mvn package -DskipTests'
            }
        }
        stage('build docker image')
        {
            when{
                branch "master"
                }
            steps{
                sh 'docker build -t employee-svc:1.01 .'
            }
        }

    }
}