# JWKS Merger

A Spring Boot proxy that fetches multiple upstream JWKS endpoints and serves them as a single merged, deduplicated key set. Designed for environments where an IdP signs ID tokens and access tokens with keys from separate JWKS URIs.

---

## Requirements

- Java 21
- Maven 3.9+
- Docker (for image build)

---

## Build

```bash
mvn clean package -DskipTests
```

The JAR is produced at `target/jwks-merger-*.jar`.

To include tests:

```bash
mvn clean package
```

---

## Run locally

```bash
mvn spring-boot:run
```

Or run the JAR directly:

```bash
java -jar target/jwks-merger-*.jar
```

Verify:

```bash
curl http://localhost:8080/jwks | jq '.keys | length'
curl http://localhost:8080/actuator/health
```

---

## Docker image

Build for the local platform:

```bash
docker build -t jwks-merger:latest .
```

Build for AMD64 (required for AKS / Linux deployments from Apple Silicon):

```bash
docker build --platform linux/amd64 -t jwks-merger:latest .
```

Tag and push to a registry:

```bash
docker tag jwks-merger:latest <DOCKERHUB_USER>/poc-oidc-jwks:jwks-merger
docker push <DOCKERHUB_USER>/poc-oidc-jwks:jwks-merger
```

---

## Placeholders

Before deploying, replace all placeholders in `k8s/deployment.yaml` and `deploy.sh`:

| Placeholder                          | Where                              | What to set                                                     |
|--------------------------------------|------------------------------------|-----------------------------------------------------------------|
| `<DOCKERHUB_USER>`                   | `deploy.sh`                        | Your Docker Hub username                                        |
| `<YOUR_DOCKER_IMAGE>`                | `k8s/deployment.yaml`              | Full image reference, e.g. `youruser/poc-oidc-jwks:jwks-merger` |
| `<YOUR_JWKS_UPSTREAM_URL_0>`         | `k8s/deployment.yaml`              | First upstream JWKS URL                                         |
| `<YOUR_JWKS_UPSTREAM_URL_1>`         | `k8s/deployment.yaml`              | Second upstream JWKS URL                                        |
| `<YOUR_DOMAIN>`                      | `k8s/deployment.yaml`, `deploy.sh` | Your cluster domain, e.g. `example.com`                         |
| `<YOUR_NAMESPACE>`                   | `k8s/deployment.yaml`, `deploy.sh` | Kubernetes namespace to deploy into                             |
| `<YOUR_IMAGE_PULL_SECRET>`           | `k8s/deployment.yaml`              | Name of the K8s secret for pulling private images               |
| `<YOUR_CERT_MANAGER_CLUSTER_ISSUER>` | `k8s/deployment.yaml`              | cert-manager ClusterIssuer name for TLS                         |

---

## Configuration

| Env var                    | Default | Description                   |
|----------------------------|---------|-------------------------------|
| `JWKS_UPSTREAM_URLS_0`     | —       | First upstream JWKS URL       |
| `JWKS_UPSTREAM_URLS_1`     | —       | Second upstream JWKS URL      |
| `JWKS_UPSTREAM_URLS_2`     | —       | Third upstream JWKS URL |
| `JWKS_REFRESH_INTERVAL_MS` | `60000` | Key refresh interval in ms    |

Add more upstreams by incrementing the index (`JWKS_UPSTREAM_URLS_3`, etc.).

---

## Deploy to Kubernetes

```bash
./deploy.sh
```

Builds the AMD64 image, pushes to Docker Hub, applies `k8s/deployment.yaml`, and waits for rollout.


The jwks is available under `jwks`

Example, using
```shell
kubectl port-forward svc/jwks-merger 8091:80 -n camunda
```

Access with a browser

```
http://localhost:8091/jwks
```
