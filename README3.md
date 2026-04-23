# 🚀 Tomorrow Plan — ProjectManager Improvements

## 🎯 Goal

Upgrade the project from a working system → **production-ready DevOps application**

---

# 🔥 PHASE 1 — AUTHENTICATION (HIGH PRIORITY)

## ✅ 1. Create Users in DB

* Add `User` entity
* Add `UserRepository`
* Create table via Flyway

Fields:

* id
* username
* password (hashed)
* role (USER / ADMIN)

---

## ✅ 2. Password Hashing

Use:

```java
BCryptPasswordEncoder
```

✔ Never store plain passwords

---

## ✅ 3. Login from Database

Replace hardcoded login with:

* Fetch user from DB
* Validate password
* Generate JWT

---

## ✅ 4. Add Roles

Example:

* USER → read tasks
* ADMIN → create/delete tasks

Secure endpoints:

```java
.hasRole("ADMIN")
```

---

# 🧱 PHASE 2 — DATA MODEL

## ✅ 5. Add Priority Field

* Add to `Task` entity
* Use enum:

```java
public enum Priority {
    LOW, MEDIUM, HIGH
}
```

* Create Flyway migration `V3`

---

## ✅ 6. Add Validation

Use:

```java
@NotBlank
@NotNull
```

✔ Prevent invalid data

---

# 🧪 PHASE 3 — TESTING

## ✅ 7. Unit Tests

* Test service layer logic

---

## ✅ 8. Integration Tests

* Test API endpoints (`/api/tasks`)
* Validate responses

---

## ✅ 9. Add Tests to CI

In GitHub Actions:

```bash
mvn test
```

✔ Prevent broken deployments

---

# 🔒 PHASE 4 — SECURITY

## ✅ 10. Kubernetes Secrets

Move sensitive data:

* DB username/password → Secret

---

## ✅ 11. Remove Hardcoded Config

Use:

* Environment variables
* ConfigMaps

---

# ⚙️ PHASE 5 — CI/CD IMPROVEMENTS

## ✅ 12. Version Docker Images

Instead of:

```
latest ❌
```

Use:

```
v1, v2, commit SHA ✅
```

---

## ✅ 13. Add Rollback Ability

* Keep previous versions in deployment

---

# 🚀 BONUS TASKS (IF TIME)

## 🔥 Logging

* Add basic logs or Loki

## 🔥 Grafana Improvements

* Better dashboard layout

## 🔥 Kubernetes Health Checks

```yaml
livenessProbe
readinessProbe
```

---

# 🧠 PRIORITY ORDER

If limited time:

1. JWT with DB + roles 🔐
2. Tests in CI 🧪
3. Kubernetes Secrets 🔒

---

# 🎯 EXPECTED RESULT

After completing this:

✔ Secure authentication
✔ Reliable deployments
✔ Production-ready pipeline
✔ Real DevOps-level project

---

# 💬 Next Step

Pick one to start:

* “Start JWT with users + roles”
* “Add tests”
* “Move secrets to Kubernetes”

---
