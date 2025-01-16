pipeline {

    agent any

    triggers {

        githubPush()

    }

    environment {

        SONAR_TOKEN = credentials('sonar-token')

        GITHUB_TOKEN = credentials('github-token')

    }

    stages {

        stage('Checkout') {

            steps {

                script {

                    echo "Realizando checkout y obteniendo el tag"

                    // Realiza el checkout del repositorio

                    git branch: 'main',

                    credentialsId: 'github-token',

                    url: 'https://github.com/Muti9289/WEBLEZGUARD.git'

                    // Obtenemos el nombre del tag

                    def gitTag = sh(script: "git describe --tags --exact-match", returnStdout: true).trim()

                    

                    // Comprobamos si se ha encontrado un tag

                    if (gitTag) {

                        echo "Tag detectado: ${gitTag}"

                        env.GIT_TAG_NAME = gitTag  // Asignamos el nombre del tag a la variable de entorno

                    } else {

                        echo "No se encontró un tag"

                    }

                }

            }

        }

        stage('Run Tests') {
            steps {
                script {
                    echo "Ejecutando tests con Maven..."
                    sh """
                        mvn clean test
                    """
                }
            }
        }

        stage('SonarQube Analysis') {

            when {

                expression {

                    // Solo ejecutar esta etapa si se detecta un tag que empieza con "v"

                    return env.GIT_TAG_NAME != null && env.GIT_TAG_NAME =~ /^v.*/
                }

            }

            steps {
                withSonarQubeEnv('SonarQube') {
                    script {
                        def sonarScannerPath = '/opt/sonar-scanner/bin/sonar-scanner'
                        
                        sh """
                            if [ ! -f "${sonarScannerPath}" ]; then
                                echo "Installing sonar-scanner..."
                                wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.8.0.2856-linux.zip
                                unzip sonar-scanner-cli-4.8.0.2856-linux.zip
                                sudo mv sonar-scanner-4.8.0.2856-linux /opt/sonar-scanner
                            fi
                            
                            ${sonarScannerPath} \
                                -Dsonar.projectKey=StaticAnalisis_LEZGuard_WEB \
                                -Dsonar.sources=. \
                                -Dsonar.host.url=http://localhost:9000 \
                                -Dsonar.java.binaries=* \
                                -Dsonar.login=${SONAR_TOKEN}
                                -Dsonar.junit.reportPaths=target/test-classes/TEST-*.xml \
                                -Dsonar.python.coverage.reportPaths=coverage.xml
                        """
                    }
                }
            }
        }

    }

}