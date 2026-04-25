provider "kubernetes" {
  config_path = "~/.kube/config"
}

provider "helm" {
  kubernetes = {
    config_path = "~/.kube/config"
  }
}

# 🔐 Variables
variable "postgres_user" {}
variable "postgres_password" {}
variable "repo_url" {}

# Namespace
resource "kubernetes_namespace_v1" "frontend" {
  metadata {
    name = "frontend"
  }
}

# Secret
resource "kubernetes_secret_v1" "postgres" {
  metadata {
    name      = "postgres-secret"
    namespace = "frontend"
  }

  data = {
    POSTGRES_USER     = base64encode(var.postgres_user)
    POSTGRES_PASSWORD = base64encode(var.postgres_password)
  }

  type = "Opaque"
}

# 🚀 Install ArgoCD via Terraform
resource "helm_release" "argocd" {
  name       = "argocd"
  repository = "https://argoproj.github.io/argo-helm"
  chart      = "argo-cd"
  namespace  = "argocd"

  create_namespace = true

  values = [
    <<EOF
server:
  service:
    type: ClusterIP
EOF
  ]
}

resource "kubernetes_service_account_v1" "backend" {
  metadata {
    name      = "backend-sa"
    namespace = "frontend"
  }
}

resource "kubernetes_role_v1" "backend_role" {
  metadata {
    name      = "backend-role"
    namespace = "frontend"
  }

  rule {
    api_groups = [""]
    resources  = ["pods"]
    verbs      = ["get", "list"]
  }
}

resource "kubernetes_role_binding_v1" "backend_binding" {
  metadata {
    name      = "backend-binding"
    namespace = "frontend"
  }

  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind      = "Role"
    name      = kubernetes_role_v1.backend_role.metadata[0].name
  }

  subject {
    kind      = "ServiceAccount"
    name      = kubernetes_service_account_v1.backend.metadata[0].name
    namespace = "frontend"
  }
}

# 🚀 ArgoCD Application
resource "kubernetes_manifest" "projectmanager_app" {
  depends_on = [helm_release.argocd]

  manifest = {
    apiVersion = "argoproj.io/v1alpha1"
    kind       = "Application"

    metadata = {
      name      = "projectmanager"
      namespace = "argocd"
    }

    spec = {
      project = "default"

      source = {
        repoURL        = var.repo_url
        targetRevision = "main"
        path           = "projectmanager"

        helm = {
          valueFiles = ["values-prod.yaml"]   # ✅ MUST be a list
        }
      }

      destination = {
        server    = "https://kubernetes.default.svc"
        namespace = "frontend"
      }

      syncPolicy = {
        automated = {
          prune    = true
          selfHeal = true
        }
      }
    }
  }
}