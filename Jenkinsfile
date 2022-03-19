pipeline{
    agent any
   tools {
       maven 'maven'
       jdk 'Java'
   }
   environment {
           dockerhub=credentials('batibol-dockerhub')
       }

    stages{
        stage('clean')
        {
            steps{
                sh 'mvn clean'
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
        stage('pushing to dockerhub')
        {
            when{
                branch "master"
                }
            steps{
                sh 'docker tag employee-svc:1.01 batibol/employee-svc:1.01 '
                sh 'echo $dockerhub_PSW | docker login -u $dockerhub_USR --password-stdin'

                sh 'docker push batibol/employee-svc:1.01 '
            }
        }

    }
}