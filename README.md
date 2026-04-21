# 🚀 Project Manager – Full CI/CD + GitOps Pipeline

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-4.0.5-brightgreen)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue)
![Kubernetes](https://img.shields.io/badge/Kubernetes-Minukube-orange)
![ArgoCD](https://img.shields.io/badge/ArgoCD-GitOps-red)
![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-black)

---

## 📘 Overview

This project demonstrates a **complete end-to-end DevOps pipeline**:

- ⚙️ Spring Boot backend
- 🐳 Docker containerization
- 🔄 GitHub Actions CI/CD
- 📦 Docker Hub image registry
- ☸️ Kubernetes (Minikube)
- 🚀 ArgoCD (GitOps deployment)

---

## 🧱 Architecture


Developer → GitHub → CI (GitHub Actions)
↓
Docker Image
↓
Docker Hub
↓
deployment.yaml updated
↓
ArgoCD detects
↓
Kubernetes deploys 🚀


---

## 📁 Project Structure

projectmanager/
├── src/main/java/ron/com/projectmanager
│ ├── ProjectmanagerApplication.java
│ └── TestController.java
├── argocd/
│ └── deployment.yaml
├── .github/workflows/
│ └── docker.yml
├── Dockerfile
├── pom.xml
└── README.md


---

## 🧠 Application Code

### Main Class

```java
package ron.com.projectmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectmanagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectmanagerApplication.class, args);
    }
}

Controller

package ron.com.projectmanager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "App is working!";
    }

    @GetMapping("/tasks")
    public String tasks() {
        return "Tasks endpoint working!";
    }
}

⚙️ Build the Project
./mvnw clean package

🐳 Docker Setup
Dockerfile
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/projectmanager-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
Build & Run Locally
docker build -t your-username/projectmanager:latest .
docker run -p 8080:8080 your-username/projectmanager

Test:

http://localhost:8080/tasks
☸️ Kubernetes Deployment
argocd/deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: projectmanager
spec:
  replicas: 2
  selector:
    matchLabels:
      app: projectmanager
  template:
    metadata:
      labels:
        app: projectmanager
    spec:
      containers:
        - name: projectmanager
          image: your-username/projectmanager:latest
          imagePullPolicy: Always
          ports:
            - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: projectmanager
spec:
  type: NodePort
  selector:
    app: projectmanager
  ports:
    - port: 80
      targetPort: 8080
      nodePort: 30007
🐳 Start Kubernetes
minikube start
🚀 Install ArgoCD
kubectl create namespace argocd

kubectl apply -n argocd \
-f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
🌐 Access ArgoCD
kubectl port-forward svc/argocd-server -n argocd 8081:443

Open:

http://localhost:8081
🔐 Get ArgoCD Password
kubectl get secret argocd-initial-admin-secret -n argocd \
-o jsonpath="{.data.password}" | base64 -d
🔗 Connect ArgoCD
Repo: your GitHub repository
Branch: main
Path: argocd
Namespace: default
🔄 CI/CD Pipeline
.github/workflows/docker.yml
name: Build and Deploy

on:
  push:
    branches:
      - main

permissions:
  contents: write

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - run: ./mvnw clean package

      - run: docker build -t ${{ secrets.DOCKER_USERNAME }}/projectmanager:${{ github.sha }} .

      - run: echo "${{ secrets.DOCKER_PASSWORD }}" | docker login -u "${{ secrets.DOCKER_USERNAME }}" --password-stdin

      - run: docker push ${{ secrets.DOCKER_USERNAME }}/projectmanager:${{ github.sha }}

      - name: Update deployment.yaml
        run: |
          sed -i "s|image: .*|image: ${{ secrets.DOCKER_USERNAME }}/projectmanager:${{ github.sha }}|g" argocd/deployment.yaml

      - name: Commit and push
        run: |
          git config user.name "github-actions"
          git config user.email "actions@github.com"
          git add argocd/deployment.yaml
          git diff --cached --quiet || git commit -m "update image"
          git push
🔐 GitHub Secrets
DOCKER_USERNAME
DOCKER_PASSWORD (Docker Hub token)
🌐 Access Application
minikube service projectmanager

Open:

http://127.0.0.1:<PORT>/tasks
🔁 CI/CD Flow
Code change
   ↓
git push
   ↓
GitHub Actions builds image
   ↓
Push to Docker Hub
   ↓
Update deployment.yaml
   ↓
Push to GitHub
   ↓
ArgoCD detects change
   ↓
Kubernetes deploys 🚀
🧠 Key Learnings
Java package must match folder structure
Avoid using latest image tag
Kubernetes does not auto-update images
ArgoCD reacts to Git, not Docker
GitOps requires manifest updates
🎉 Result

✔ Fully automated CI/CD
✔ GitOps deployment
✔ Zero manual steps
✔ Production-style DevOps pipeline

🚀 Future Improvements
Ingress (custom domain)
Azure AKS deployment
Monitoring (Prometheus + Grafana)
Frontend integration
👨‍💻 Author

Ron Nirzaaev
DevOps Engineer 🚀


---

This one you can **copy once → paste → done** ✅  

If you want next level after this:
👉 I can add **diagram image + screenshots + “wow” GitHub look (like top repos)** 💪