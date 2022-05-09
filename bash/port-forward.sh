kubectl port-forward service/frontend-web 4200:4200 &
kubectl port-forward service/api-gateway 8079:8079 &
kubectl port-forward service/pgadmin 5050:5050