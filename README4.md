# 🚀 ProjectManager – Full Stack DevOps Application

## 📌 Overview

ProjectManager is a full-stack task management application built with a modern DevOps workflow.

It includes:

* React frontend (UI + UX)
* Spring Boot backend (REST API + JWT auth)
* PostgreSQL database
* Kubernetes deployment
* GitHub Actions CI/CD pipeline
* ArgoCD GitOps deployment
* Monitoring integration

---

## 🏗️ Architecture

```
Browser
   ↓
Ingress (ron.project.devops)
   ↓
Frontend (React + Nginx)
   ↓
/api
   ↓
Backend (Spring Boot)
   ↓
PostgreSQL
```

---

## 🧰 Tech Stack

### Frontend

* React
* TailwindCSS
* React Router
* Nginx (production build)

### Backend

* Java 17
* Spring Boot
* JWT Authentication

### DevOps

* Docker
* Kubernetes (Minikube)
* NGINX Ingress
* GitHub Actions
* ArgoCD (GitOps)

### Database

* PostgreSQL (with Kubernetes Secrets)

---

## 🔐 Features

* User authentication (JWT)
* Role-based access (ADMIN / USER)
* Task management:

    * Create task
    * Delete task (ADMIN only)
    * Complete task
    * Assign task
* Persistent data (PostgreSQL)
* Responsive modern UI (TailwindCSS)

---

## ⚙️ CI/CD Pipeline

```
git push →
GitHub Actions:
  - build backend
  - build frontend
  - push Docker images
  - update Kubernetes manifests
ArgoCD:
  - detects changes
  - deploys automatically to cluster
```

---

## 📦 Docker

### Backend

* Built using Maven + OpenJDK 17

### Frontend

* Multi-stage build (Node → Nginx)
* SPA routing fix with custom nginx.conf

---

## ☸️ Kubernetes

### Components

* Deployments:

    * frontend
    * backend
* Services:

    * ClusterIP services for both
* Ingress:

    * `/` → frontend
    * `/api` → backend

---

## 🌐 Access

```
http://ron.project.devops
```

---

## 🔍 Monitoring

* Metrics collection enabled
* Integrated with monitoring stack (Prometheus / Grafana)

---

## 🧪 Key Challenges Solved

* Fixing React SPA routing (404 on refresh)
* Handling JWT authentication across frontend/backend
* Proper Ingress routing (`/api` path)
* CI/CD Git conflicts in GitHub Actions
* Synchronizing frontend + backend deployments with ArgoCD

---

## 🚀 Status

✔ Fully working in Kubernetes
✔ Auto-deploy with GitOps
✔ Production-ready architecture

---

## 📈 Future Improvements

See roadmap in separate README.

---

## 👨‍💻 Author
