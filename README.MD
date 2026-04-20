# 🚀 ProjectManager — DevOps Learning Project

## 📌 Overview

This project is a **backend + DevOps pipeline** built step-by-step using modern tools.

It demonstrates:

* Building a Java API
* Containerizing with Docker
* Deploying to Kubernetes
* Managing deployments with ArgoCD (GitOps)

---

## 🧱 Architecture

```
Client (Browser / Mobile)
        ↓
Spring Boot API (Java)
        ↓
Docker Container
        ↓
Kubernetes (Minikube)
        ↓
ArgoCD (GitOps)
```

---

## 🧰 Technologies

* Java + Spring Boot
* Docker
* Kubernetes (Minikube)
* ArgoCD
* Maven

---

# 🧪 Phase 1 — Backend (Spring Boot)

## ▶️ Run the app

```
./mvnw spring-boot:run
```

---

## 🌐 Test

```
http://localhost:8080
```

---

## 📡 API Endpoints

### Get all tasks

```
GET /tasks
```

### Add task

```
POST /tasks
Content-Type: text/plain

Body:
Learn DevOps
```

### Delete task

```
DELETE /tasks/{id}
```

---

# 🐳 Phase 2 — Docker

## 📦 Build JAR

```
./mvnw clean package
```

---

## 📄 Dockerfile

```
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/projectmanager-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🏗️ Build image

```
docker build -t projectmanager .
```

---

## ▶️ Run container

```
docker run -p 8080:8080 projectmanager
```

---

## 🌐 Test

```
http://localhost:8080/tasks
```

---

# ☸️ Phase 3 — Kubernetes (Minikube)

## ▶️ Start Minikube

```
minikube start
```

---

## 🔧 Use Docker inside Minikube

```
eval $(minikube docker-env)
```

---

## 🏗️ Build image

```
docker build -t projectmanager .
```

---

## 📄 deployment.yaml

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: projectmanager
spec:
  replicas: 1
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
          image: projectmanager
          imagePullPolicy: Never
          ports:
            - containerPort: 8080
```

---

## 🚀 Deploy

```
kubectl apply -f deployment.yaml
```

---

## 🔍 Check pods

```
kubectl get pods
```

---

## 🌐 Expose service

```
kubectl expose deployment projectmanager --type=NodePort --port=8080
```

---

## 🔗 Open service

```
minikube service projectmanager
```

---

# 🔁 Phase 4 — ArgoCD (GitOps)

## 📦 Install ArgoCD

```
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/v2.11.3/manifests/install.yaml
```

---

## 🌐 Access UI

```
kubectl port-forward svc/argocd-server -n argocd 8081:443
```

Open:

```
https://localhost:8081
```

---

## 🔑 Get admin password

```
kubectl -n argocd get secret argocd-initial-admin-secret \
-o jsonpath="{.data.password}" | base64 --decode
```

---

## 🔁 GitOps Flow

```
GitHub Repo → ArgoCD → Kubernetes
```

---

# 🧠 Key Concepts Learned

* REST API development
* Docker containerization
* Kubernetes deployments
* Service exposure
* GitOps workflow
* Debugging real-world issues

---

# 📈 Future Improvements

* Use real Task model (JSON instead of string)
* Add database (PostgreSQL)
* Add CI/CD (GitHub Actions)
* Add Helm charts
* Deploy to cloud (Azure / AWS)
* Build mobile app (iOS / Android)

---

# 💬 Notes

This project was built step-by-step to learn:

* backend development
* DevOps tools
* production-style architecture

---

# 🚀 Next Steps

* Connect ArgoCD to GitHub repo
* Enable auto-sync
* Push Docker image to registry
* Add security scanning
* Deploy to cloud

---
