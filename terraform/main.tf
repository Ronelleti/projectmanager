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

# Secret (✅ FIXED - no base64encode, no string_data)
resource "kubernetes_secret_v1" "postgres" {
  metadata {
    name      = "postgres-secret"
    namespace = "frontend"
  }

  data = {
    POSTGRES_USER     = var.postgres_user
    POSTGRES_PASSWORD = var.postgres_password
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

# RBAC - Service Account
resource "kubernetes_service_account_v1" "backend" {
  metadata {
    name      = "backend-sa"
    namespace = "frontend"
  }
}

# RBAC - Role
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

# RBAC - Role Binding
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

resource "kubernetes_namespace_v1" "frontend_dev" {
  metadata {
    name = "frontend-dev"
  }
}

resource "kubernetes_manifest" "projectmanager_dev_app" {
  depends_on = [
    helm_release.argocd,
    kubernetes_secret_v1.postgres,
    kubernetes_namespace_v1.frontend_dev
  ]

  manifest = {
    apiVersion = "argoproj.io/v1alpha1"
    kind       = "Application"

    metadata = {
      name      = "projectmanager-dev"
      namespace = "argocd"
    }

    spec = {
      project = "default"

      source = {
        repoURL        = var.repo_url
        targetRevision = "main"
        path           = "projectmanager"

        helm = {
          valueFiles = ["values-dev.yaml"]
        }
      }

      destination = {
        server    = "https://kubernetes.default.svc"
        namespace = "frontend-dev"
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


# 🚀 ArgoCD Application
resource "kubernetes_manifest" "projectmanager_app" {
  depends_on = [
    helm_release.argocd,
    kubernetes_secret_v1.postgres
  ]

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
          valueFiles = ["values-prod.yaml"]
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