./mvnw -DskipTests=true clean package
eval $(minikube -p minikube docker-env)
docker compose -f docker-compose_kompose.yml build
