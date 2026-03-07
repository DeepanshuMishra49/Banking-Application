@Library("shared") _
pipeline {
    agent { 
        label 'james' 
    }

    stages {

        stage('Code') {
            steps {
                script{
                clone("https://github.com/DeepanshuMishra49/Banking-Application.git" ,"main")
                }
            }
        }

stage('Build Maven') {
    steps {
        sh 'mvn clean package -DskipTests'
    }
}

        stage('Build') {
            steps {
                script{
docker_build("banking-app-image", "latest", "deep270804")
}
            }
        }

        stage('Image Push') {
            steps {
                script{
                   docker_push("banking-app-image", "latest", "deep270804")
                   }
                }
            }

        stage('Deploy') {
            steps {
                echo '📦 This is the Deployment Phase'
                sh 'sudo docker compose up -d'
            }
        }
    }
}
