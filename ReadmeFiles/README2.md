# 🚀 ProjectManager — Full DevOps Platform (CI/CD + Kubernetes + Monitoring)

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen)
![React](https://img.shields.io/badge/React-Frontend-blue)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue)
![Kubernetes](https://img.shields.io/badge/Kubernetes-Minukube-blueviolet)
![ArgoCD](https://img.shields.io/badge/ArgoCD-GitOps-orange)
![Prometheus](https://img.shields.io/badge/Prometheus-Monitoring-red)
![Grafana](https://img.shields.io/badge/Grafana-Dashboard-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-DB-blue)
![CI/CD](https://img.shields.io/badge/GitHubActions-CI%2FCD-black)

---

## 📌 Overview

**ProjectManager** is a full DevOps platform demonstrating a real-world system:

* Spring Boot backend (Java)
* React frontend
* PostgreSQL database (with Flyway migrations)
* Docker containerization
* Kubernetes (Minikube)
* ArgoCD (GitOps deployment)
* GitHub Actions (CI/CD)
* Prometheus + Grafana (monitoring)
* JWT authentication
* Trivy (security scanning)

---

## 🧱 Architecture

```text
React UI → Ingress → Backend API → PostgreSQL
                      ↓
                Prometheus
                      ↓
                   Grafana

CI/CD → GitHub Actions → Docker Hub → ArgoCD → Kubernetes
```

---

## 🔄 End-to-End Flow

1. Developer pushes code to GitHub
2. GitHub Actions builds & scans Docker image (Trivy)
3. Image pushed to Docker Hub
4. ArgoCD detects change and deploys to Kubernetes
5. Backend connects to PostgreSQL
6. Prometheus scrapes `/actuator/prometheus`
7. Grafana visualizes system metrics

---

## ⚙️ Tech Stack

* **Backend:** Spring Boot (Java 17)
* **Frontend:** React
* **Database:** PostgreSQL
* **Migrations:** Flyway
* **Containerization:** Docker
* **Orchestration:** Kubernetes (Minikube)
* **CI/CD:** GitHub Actions + ArgoCD
* **Monitoring:** Prometheus + Grafana
* **Security:** Spring Security + JWT
* **Security Scanning:** Trivy

---

## 🏗️ What Was Implemented

---

### 🟢 Backend (Spring Boot)

* REST API (`/api/tasks`)
* CRUD operations
* JWT authentication
* Spring Security + custom filter
* Actuator endpoints (`/actuator/prometheus`)

---

### 🟢 Frontend (React)

* Task management UI
* Create / delete / assign / complete tasks
* Connected to backend API via Ingress

---

### 🟢 Database (PostgreSQL)

* Runs inside Kubernetes
* Persistent storage (PVC)
* Flyway migrations:

  * `V1` → initial schema
  * `V2` → added `priority`

---

### 🟢 Docker

```bash
docker build -t ronn4/projectmanager .
docker push ronn4/projectmanager
```

---

### 🟢 Kubernetes

* Deployments + Services
* Namespaces separation
* Internal service communication

---

### 🟢 Ingress + Domain

* Custom domain:

```
ron.project.devops
```

* Routing:

```text
Frontend → Backend via Ingress
```

---

### 🟢 CI/CD Pipeline

```text
Build → Scan (Trivy) → Push → Deploy (ArgoCD)
```

* GitHub Actions:

  * Build JAR
  * Build Docker image
  * Security scan
  * Push image

* ArgoCD:

  * Watches Git repo
  * Auto-sync to Kubernetes

---

### 🟢 Monitoring

#### Prometheus

* Installed via Helm
* Scrapes backend metrics:

```yaml
extraScrapeConfigs:
  - job_name: 'backend'
    metrics_path: /actuator/prometheus
    static_configs:
      - targets: ['backend.frontend.svc.cluster.local:8080']
```

---

#### Grafana Dashboards

* Requests per second
* Latency (P95)
* Error rate
* Requests per endpoint

---

### 🟢 Security

* JWT authentication
* Custom filter (`OncePerRequestFilter`)
* Protected endpoints
* Allowed:

```
/api/auth/login
/actuator/**
```

---

## 🧪 Testing

### Get token

```bash
curl -X POST http://ron.project.devops/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"admin","password":"admin"}'
```

---

### Call API

```bash
curl http://ron.project.devops/api/tasks \
-H "Authorization: Bearer <TOKEN>"
```

---

### Metrics

```bash
curl http://localhost:8080/actuator/prometheus
```

---

## 📊 Monitoring Queries

### Requests/sec

```promql
rate(http_server_requests_seconds_count{uri!~"/actuator.*"}[5m])
```

### Latency (P95)

```promql
histogram_quantile(0.95, sum(rate(http_server_requests_seconds_bucket[5m])) by (le))
```

### Errors

```promql
rate(http_server_requests_seconds_count{status=~"5.."}[5m])
```

---

## 🧠 Key Challenges & Solutions

* Fixed JWT filter blocking `/actuator` endpoints
* Debugged empty Prometheus metrics
* Fixed histogram queries using `le` aggregation
* Solved Kubernetes service discovery for scraping
* Integrated Flyway with schema validation
* Connected monitoring stack end-to-end

---

## 📁 Project Structure

```text
ProjectManager/
├── backend/
├── frontend/
├── k8s/
├── Monitoring/
│   ├── prometheus/
│   └── grafana/
├── .github/workflows/
```

---

## 📈 Current Status

```text
✅ Backend API
✅ React Frontend
✅ Kubernetes Deployment
✅ CI/CD (GitHub Actions + ArgoCD)
✅ Monitoring (Prometheus + Grafana)
✅ JWT Authentication (basic)
✅ Flyway DB migrations
```

---

## 🚀 Next Steps

### 🔥 High Priority

* Users table + DB authentication
* BCrypt password hashing
* Roles (USER / ADMIN)
* Input validation

---

### ⚙️ Medium

* Kubernetes Secrets
* Versioned Docker tags
* Deploy frontend to Kubernetes

---

### 💡 Advanced

* Alerts (latency, errors)
* Logging (ELK / Loki)
* Tracing (Jaeger)
* Redis caching

---

## 👨‍💻 Author

DevOps 🚀

---

## ⭐ Final Note

This project evolved into a full system covering:

```text
Build → Secure → Deploy → Monitor → Observe
```

---
