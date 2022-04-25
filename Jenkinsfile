pipeline {
   agent any

   environment {
     // You must set the following environment variables
     // ORGANIZATION_NAME
     // YOUR_DOCKERHUB_USERNAME (it doesn't matter if you don't have one)

     SERVICE_NAME = "employee-svc"
     REPOSITORY_TAG="batibol/employee-svc:latest"
   }

   stages {
      stage('Preparation') {
         steps {
            cleanWs()
            git credentialsId: 'GitHub', url: "https://github.com/batibol/employee-service"
         }
      }
      stage('Build') {
         steps {
            sh 'echo building'
         }
      }
      stage ('Tests') {
         steps {
            sh './mvnw test'
         }
      }
      stage('Sonnarq scan') {
         steps {
            withSonarQubeEnv(installationName: 'sq1') {
             sh './mvnw clean install org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar -Dsonar.sources=src/main/java/ -Dsonar.java.binaries=./target/classes'
            }
         }
      }

      stage('Build and Push Image') {
         steps {
           sh 'docker image build -t employee-svc .'
         }
      }

      stage('Deploy to Cluster') {
          steps {
            sh 'envsubst < ${WORKSPACE}/deploy.yaml | kubectl apply -f -'
          }
      }
   }
}
