#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

IMAGE="<DOCKERHUB_USER>/poc-oidc-jwks:jwks-merger"

echo "==> Building AMD64 image: $IMAGE"
docker build --platform linux/amd64 -t "$IMAGE" .

echo "==> Pushing to Docker Hub"
docker push "$IMAGE"

echo "==> Applying K8s manifests"
kubectl apply -f k8s/deployment.yaml

echo "==> Waiting for rollout"
kubectl rollout status deployment/jwks-merger -n <YOUR_NAMESPACE>

echo ""
echo "Done. Test with:"
echo "  curl https://jwks-merger.<YOUR_DOMAIN>/jwks | jq '.keys | length'"
