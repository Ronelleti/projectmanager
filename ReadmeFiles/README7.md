🚀 ProjectManager — Production-Style DevOps Platform
<p align="center"> <b>End-to-end DevOps system using Kubernetes, GitOps, CI/CD, Terraform, and Monitoring</b> </p>
🧱 Tech Stack






















📌 Overview

This project demonstrates a real-world DevOps architecture, not just deployment:

Full-stack application (React + Spring Boot)
GitOps deployment using ArgoCD
CI/CD pipeline with GitHub Actions
Infrastructure as Code using Terraform + Helm
Kubernetes orchestration (Minikube)
Monitoring with Prometheus & Grafana
Secure backend with JWT authentication
PostgreSQL with automated backups

📂 Built and evolved through multiple iterations and debugging cycles

🧱 Architecture
🔄 End-to-End Flow
git push →
GitHub Actions:
- build backend
- build frontend
- security scan (Trivy)
- push Docker images
- update Helm values
  ArgoCD:
- detects changes
- syncs cluster
  Kubernetes:
- deploys updated containers
  Ingress:
- routes traffic
  ⚙️ Infrastructure (Terraform)

Provisioned using Terraform:

Kubernetes namespace (frontend)
PostgreSQL secrets
ArgoCD installation (Helm)
RBAC:
ServiceAccount
Role
RoleBinding
ArgoCD Application (GitOps)
📦 Kubernetes Setup

Deployed components:

Backend (Spring Boot)
Frontend (React + Nginx)
PostgreSQL (Stateful)
Services (ClusterIP)
Ingress (custom domain)
CronJob (database backups)
RBAC permissions
Secrets (DB credentials)
🔐 Security
JWT authentication (Spring Security)
Custom filter (JwtFilter)
Stateless backend
/actuator/** excluded from auth (health checks)
Kubernetes Secrets
RBAC enforcement
💾 Database & Backups
PostgreSQL
Runs inside Kubernetes
Connected via internal service
CronJob Backup
pg_dump -h postgres -U $POSTGRES_USER projectmanager > /backup/backup.sql
Retention
find /backup -type f -mtime +1 -delete
🔄 CI/CD Pipeline
Build → Scan → Push → GitOps Deploy
GitHub Actions:
Build backend (Maven)
Build frontend (Docker)
Scan images (Trivy)
Push to Docker Hub
Update Helm values
Commit back to repo
🏷️ Tagging Strategy
TAG=${GITHUB_SHA}

✔ Same commit = same version across services

⚠️ Critical CI Fix

❌ Problem:

sed -i "s|tag: .*|tag: ${TAG}|g"

👉 Broke YAML and replaced wrong sections

✅ Fix:

sed -i '/backend:/,/tag:/ s/tag:.*/tag: '"${TAG}"'/'
sed -i '/frontend:/,/tag:/ s/tag:.*/tag: '"${TAG}"'/'
🔄 GitOps (ArgoCD)
Watches Git repository
Automatically deploys changes
Maintains desired state

Access:

kubectl port-forward svc/argocd-server -n argocd 8081:443
📦 Helm (IaC)
projectmanager/
Chart.yaml
values-prod.yaml
templates/

Example:

frontend:
image:
repository: ronn4/projectmanager-frontend
🐛 Real Issues & Solutions
1. Database Authentication Failure

Error:

password authentication failed for user "cm9u"

Cause:

Double base64 encoding in Terraform

Fix:

Removed base64encode()
2. Frontend CrashLoopBackOff

Cause:

Wrong Docker image used

Fix:

kubectl delete deployment frontend -n frontend
3. ArgoCD Not Updating

Cause:

Stale deployment state

Fix:

kubectl delete application projectmanager -n argocd
terraform apply
4. Helm Not Applying Changes

Cause:

Existing deployment not replaced

Fix:

kubectl delete deployment frontend -n frontend
5. Backup Job Failures

Cause:

Wrong DB user
Missing directory

Fix:

Use secret values
Ensure /backup exists
6. Minikube NotReady

Fix:

minikube start --memory=7800 --cpus=4
📊 Monitoring
Prometheus

Scrapes:

/actuator/prometheus
Grafana

Dashboards:

Request rate
Latency (P95)
Error rate
🎯 Current State

✔ Full-stack system running in Kubernetes
✔ GitOps deployment (ArgoCD)
✔ CI/CD fully automated
✔ Helm + Terraform IaC
✔ Monitoring integrated
✔ Backup system working
✔ Real production issues solved

🚀 Roadmap
Next Steps
Multi-environment (dev / prod)
Separate database per environment
Cloud deployment (AKS)
DevOps Improvements
Autoscaling (HPA)
Secrets management (Vault)
Advanced Helm configs
Observability
Alerts
Logging (Loki / ELK)
Tracing (Jaeger)
🧠 Key Learnings
Debugging Kubernetes in real scenarios
GitOps workflows with ArgoCD
CI/CD pitfalls and fixes
Helm templating best practices
Terraform + Kubernetes integration
Observability and monitoring
👨‍💻 Author

Ron
DevOps Engineer 🚀

⭐ Final Note
Build → Break → Debug → Fix → Improve → Repeat

👉 This project reflects real DevOps work, not just deployment.