# 🚀 Project Manager – Full DevOps CI/CD + Kubernetes + Ingress

## 📘 Overview

This project demonstrates a **complete DevOps pipeline and full-stack system**:

* Spring Boot backend (Java)
* React frontend
* Docker containerization
* Kubernetes (Minikube)
* ArgoCD (GitOps deployment)
* GitHub Actions (CI/CD)
* Trivy (security scanning)
* Ingress (custom domain routing)

---

## 🧱 Architecture

```
React UI → Ingress → Backend API → Kubernetes → CI/CD → Docker Hub
```

---

## ⚙️ Prerequisites

Install:

* Java 17
* Node.js (LTS)
* Docker
* Minikube
* kubectl
* Git

---

## 🚀 1. Backend Setup (Spring Boot)

### Create project

Use Spring Initializr with:

* Java 17
* Spring Boot 3.x
* Dependency: Spring Web

---

### Build project

```
./mvnw clean package
```

---

## 🧠 Backend Code

### Task model

```java
package ron.com.projectmanager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {

    private int id;
    private String name;
    private String assignedTo;
    private boolean completed;

    public Task() {}

    public Task(int id, String name) {
        this.id = id;
        this.name = name;
        this.completed = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
```

---

### Controller

```java
@CrossOrigin(origins = "*")
@RestController
public class TestController {

    private List<Task> tasks = new ArrayList<>();
    private int currentId = 1;

    @GetMapping("/")
    public String home() {
        return "Project Manager API is running 🚀";
    }

    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return tasks;
    }

    @PostMapping("/tasks")
    public Task addTask(@RequestBody Task task) {
        task.setId(currentId++);
        tasks.add(task);
        return task;
    }

    @DeleteMapping("/tasks/{id}")
    public String deleteTask(@PathVariable int id) {
        tasks.removeIf(t -> t.getId() == id);
        return "Task deleted";
    }

    @PostMapping("/tasks/{id}/complete")
    public Task completeTask(@PathVariable int id) {
        Task task = tasks.stream().filter(t -> t.getId() == id).findFirst().orElseThrow();
        task.setCompleted(true);
        return task;
    }

    @PostMapping("/tasks/{id}/assign")
    public Task assignTask(@PathVariable int id, @RequestBody Task updatedTask) {
        Task task = tasks.stream().filter(t -> t.getId() == id).findFirst().orElseThrow();
        task.setAssignedTo(updatedTask.getAssignedTo());
        return task;
    }
}
```

---

## 🐳 2. Docker Setup

### Dockerfile

```dockerfile
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY ../backend/target/projectmanager-0.0.1-SNAPSHOT.jar app.jar

RUN useradd -m appuser
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

### Build and run locally

```
docker build -t your-username/projectmanager .
docker run -p 8080:8080 your-username/projectmanager
```

---

## 🔐 3. Security (Trivy)

Added in GitHub Actions:

* Scans Docker image
* Detects vulnerabilities
* Runs before push

---

## 🔄 4. CI/CD (GitHub Actions)

Pipeline:

```
Build → Scan → Push → Update YAML → ArgoCD deploy
```

---

## ☸️ 5. Kubernetes (Minikube)

Start cluster:

```
minikube start
```

---

## 📦 Deployment

```
kubectl apply -f argocd/deployment.yaml
```

---

## 🌐 Service access

```
minikube service projectmanager
```

---

## 🚀 6. ArgoCD (GitOps)

Install:

```
kubectl create namespace argocd

kubectl apply -n argocd \
-f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```

Access UI:

```
kubectl port-forward svc/argocd-server -n argocd 8081:443
```

---

## 🌐 7. Ingress Setup

Enable ingress:

```
minikube addons enable ingress
```

---

### Ingress YAML

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: projectmanager-ingress
spec:
  rules:
    - host: ron.project.devops
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: projectmanager
                port:
                  number: 80
```

---

### Add local domain

Edit:

```
sudo nano /etc/hosts
```

Add:

```
127.0.0.1 ron.project.devops
```

---

### Start tunnel

```
minikube tunnel
```

---

### Access app

```
http://ron.project.devops
```

---

## 🎨 8. React Frontend

Create app:

```
npx create-react-app projectmanager-ui
cd projectmanager-ui
npm start
```

---

### Update API URL

```javascript
const API = "http://ron.project.devops";
```

---

### Features implemented

* Create task
* Delete task
* Assign user
* Mark complete

---

## 🔁 Full Workflow

```
Code → Git Push → CI/CD → Docker → Kubernetes → ArgoCD → Ingress → Browser
```

---

## 🧠 Key Learnings

* CI/CD pipelines
* Docker containerization
* Kubernetes deployments
* GitOps (ArgoCD)
* Security scanning
* Ingress routing
* Full-stack integration

---

## 🎯 Current Status

✔ Backend API
✔ React frontend
✔ Kubernetes deployment
✔ CI/CD pipeline
✔ Security (Trivy)
✔ Ingress with custom domain

---

## 🚀 Next Steps

* Deploy frontend to Kubernetes
* Add monitoring (Prometheus + Grafana)
* Improve UI design
* Add database (PostgreSQL)

---

## 👨‍💻 Author

Ron Nirzaaev
DevOps Engineer 🚀
