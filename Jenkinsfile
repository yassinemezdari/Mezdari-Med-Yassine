pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('Checkout Git') {
            steps {
                git branch: 'main',
                    credentialsId: 'github-credentials',
                    url: 'https://github.com/yassinemezdari/Mezdari-Med-Yassine.git'
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn compile'
            }
        }
    }
}